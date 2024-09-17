package network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import gui.error.ErrorUI;
import objects.Reader;

public class Client implements Runnable {
    private Reader reader;
    private PrintWriter writer;
    private long time;
    public SkribblServer server;
    private String userName;
    private String address;
    private boolean guessedWord;
    private int score;

    private boolean isDrawer;
    public boolean hasGuessedWord() {
        return guessedWord;
    }

    public void addScore(int score) {
        this.score += score;
    }
    public String getUsername() {
        return userName;
    }

    public String getAddress() {
        return address;
    }
    
    public Client(Socket socket, SkribblServer server) throws IOException {
        time = System.currentTimeMillis(); // Time when last packet was received
        this.reader = new Reader(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
        this.address = socket.getInetAddress().getHostName()+":"+socket.getPort();
        new Thread(this).start();
    }
    public void send(String packet) {
        writer.print(packet+"\n"); // \n character is used over println for platform compatiblity 
    }
    public void resetRoundStats() {
        // Set's isDrawer and guessedWord booleans to false
        this.isDrawer = false;
        this.guessedWord = false;
    }
    public void sendScore() {
        server.sendAllClients("8\n"+userName+"\n"+score);
    }
    public void run() {
        while (true) {
            if((System.currentTimeMillis() - time) > 1000) {
                server.sendAllClients("2\n"+userName); // Client has left the game
                server.disconnection(this);
                break; // Client has stopped sending pings
            }
            while (reader.ready()) {
                switch (reader.getInt()) {
                    case 0: // Ping packet
                        writer.print("0\n");
                        break;
                    case 1: // Set Name packet
                        this.userName = reader.getString();
                        server.connection(this);
                        server.sendAllClients("1\n"+userName); // Send user joined packet
                        break;
                    case 2: // Chat message packet
                        String msg = reader.getString();
                        if(this.guessedWord) {
                            server.sendGuessedClients("3\n&"+userName+": "+msg);
                        } else {
                            if(msg.equalsIgnoreCase(server.getCurWord())) {
                                send("6\n"+msg);
                                this.guessedWord = true;
                                server.guessedWord(this);
                            } else {
                                server.sendAllClients("3\n" + userName + ": " + msg);
                            }
                        }
                        break;
                    case 3: // Line packet
                        if(!this.isDrawer) {
                            for(int i = 0;i < 4;i++) reader.getInt();
                            break;
                        }
                        server.sendAllClients("4\n"+reader.getInt()+"\n"+reader.getInt()+"\n"+reader.getInt()+"\n"+reader.getInt());
                        break;
                    case 4: // Word selected
                        this.guessedWord = true;
                        this.isDrawer = true;
                        server.setWord(reader.getInt());
                        break;
                    case 5: // Pen property
                        if(!this.isDrawer) {
                            reader.getInt();
                            break;
                        }
                        server.sendAllClients("7\n"+reader.getInt());
                        break;
                    default:
                        break;
                }
                time = System.currentTimeMillis();
            }
            try {
                Thread.sleep(10); // Sleep so that resources are conserved
            } catch (InterruptedException e) {
                ErrorUI.showError(e);
                break;
            }
        }
    }
}
