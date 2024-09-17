package network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import gui.error.ErrorUI;
import gui.server.ServerUI;
import objects.WordGetter;

public class SkribblServer extends ServerSocket implements Runnable {
    private ArrayList<Client> clients = new ArrayList<Client>(10);
    private String word;
    private ServerUI ui;
    private String[] wordChoice;
    private Client drawer;
    private int drawerIndex;
    private String curWord;
    private int numberOfRounds;
    private int completedRounds;
    private TimeManager manager;
    private WordGetter wordGetter;

    public String getCurWord() {
        return curWord;
    }

    public SkribblServer(int port) throws IOException {
        super(port);
        wordGetter = new WordGetter("assets/words.txt");
        this.ui = new ServerUI(this, InetAddress.getLocalHost().getHostAddress()+":"+port);
        ui.addInformation("The server started on "+InetAddress.getLocalHost().getHostAddress()+":"+port+". Send this to people to allow them to join.");
    }
    public void listen() throws IOException {
        Thread thread = new Thread(this, "Skribbl Server");
        thread.start();
    }
    public void connection(Client client) {
        ui.addUser(client.getUsername());
        ui.addInformation(client.getUsername()+" joined the game from "+client.getAddress());
        clients.forEach(clientElement -> {
            if(clientElement != client) client.send("1\n"+clientElement.getUsername()); // It already receives the packet which acknowledges that it has joined
        });
    }
    public void disconnection(Client client) {
        clients.remove(client);
        if(client.getUsername() == null) return; // The client was a socket that checked if the server was online
        ui.addInformation(client.getUsername()+" has left the game.");
        ui.removeUser(client.getUsername());
        this.clients.remove(client);
    }
    public void sendAllClients(String packet) {
        clients.forEach(client -> {
            client.send(packet);
        });
    }
    public void sendGuessedClients(String packet) {
        clients.forEach(client -> {
            if(client.hasGuessedWord()) {
                client.send(packet);
            }
        });
    }
    public void setWord(int index) {
        StringBuffer buf = new StringBuffer();
        for(int i = 0;i < wordChoice[index].length();i++) {
            if(wordChoice[index].charAt(i) == ' ') {
                buf.append(' ');
            } else {
                buf.append('_');
            }
        }
        this.curWord = wordChoice[index];
        this.sendAllClients("6\n"+buf.toString());
        drawer.send("6\n"+wordChoice[index]);
        this.sendAllClients("7\n10"); // Clear board
        this.sendAllClients("3\n&"+drawer.getUsername()+" has selected the word");
        manager.start();
    }
    boolean allGuessed;
    public void guessedWord(Client client) {
        // Score and display logic
        int score = (int)(Math.round(0.2*manager.getTime())*10);
        sendAllClients("3\n&"+client.getUsername()+" guessed the word! (+"+score+")");
        client.addScore(score);
        drawer.addScore(score/(clients.size()-1)); // Drawer will get average score

        // Check if all clients have guessed
        allGuessed = true;
        clients.forEach(clientElement -> {
            if(!clientElement.hasGuessedWord()) {
                allGuessed = false;
            }
        });
        if(allGuessed) {
            manager.interrupt();
            drawerIndex = drawerIndex+1 < clients.size() ? drawerIndex+1 : 0;
            startSkribbling();
        }
    }
    public void startSkribbling() {
        clients.forEach(client -> {
            client.sendScore();
        });
        if(drawerIndex == 0) completedRounds++;
        if(completedRounds > numberOfRounds) {
            ui.showStartRoundSettings();
            return;
        }

        // Clear stats from previous drawing and guessing
        clients.forEach(client -> {
            client.resetRoundStats();
        });

        drawer = clients.get(drawerIndex);
        wordChoice = wordGetter.getWords();
        drawer.send("5\n"+wordChoice[0]+"\n"+wordChoice[1]+"\n"+wordChoice[2]);
        this.sendAllClients("3\n&"+drawer.getUsername()+" is selecting a word.");

        manager = new TimeManager();
        manager.setCallback(() -> {
            if(manager.getTime() > 0) {
                if(manager.getTime() == 1) {
                    this.sendAllClients("3\n#Only 1 second remains.");
                } else {
                    this.sendAllClients("3\n#Only " + manager.getTime() + " seconds remain.");
                }
            } else {
                this.sendAllClients("3\n#Time's Up!");
                drawerIndex = drawerIndex + 1 < clients.size() ? drawerIndex + 1 : 0;
                startSkribbling();
            }
        });
    }
    public void startGame(int nOfRounds) {
        drawerIndex = 0;
        this.completedRounds = 0;
        this.numberOfRounds = nOfRounds;
        startSkribbling();
    }
    public void run() {
        while (true) {
            Socket socket;
            try {
                socket = this.accept();
                if(drawer == null) {
                    clients.add(new Client(socket, this));
                } else {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.print("3\n&You can't join as the game is in progress.");
                    writer.close();
                }
            } catch (IOException e) {
                ErrorUI.showError(e);
            }
        }
    }
}
