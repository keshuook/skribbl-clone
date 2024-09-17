package gui.client;

import network.client.SkribblClient;
import network.server.Client;

import java.awt.*;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

public class ClientUI extends JFrame {
    private DefaultListModel<String> users;
    private Chat chat;
    private DrawingBoard drawingBoard;
    private Canvas canvas;
    private WordPanel panel;
    private SkribblClient client;
    public ClientUI(SkribblClient client) {
        super("Skribbl");

        this.client = client;

        this.setBounds(220, 80, 1000, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.users = new DefaultListModel<String>();
        JList listOfUsers = new JList<>(users);
        users.addElement("Players Online");
        listOfUsers.setPreferredSize(new Dimension(220, 0));

        this.chat = new Chat(client);

        this.add(chat, BorderLayout.EAST);
        this.add(listOfUsers, BorderLayout.WEST);

        this.setVisible(true);
    }
    public void setUpNonEssential() {
        this.drawingBoard = new DrawingBoard(this);
        this.canvas = drawingBoard.getCanvas();
        this.add(drawingBoard, BorderLayout.CENTER);
        this.panel = new WordPanel(this);
        this.add(panel, BorderLayout.NORTH);
    }
    public void addUser(String user) {
        if(user.equals("null")) return;
        users.addElement(user);
        chat.green(user+" has joined the game.");
    }
    public void removeUser(String user) {
        if(user.equals("null")) return;
        users.removeElement(user);
        chat.green(user+" has left the game.");
    }
    public void updateScore(String username, int score) {
        for(int i = 0;i < users.size();i++) {
            if(users.get(i).split(" ")[0].equals(username)) {
                users.set(i, username+" ("+score+")");
            }
        }
    }
    public void chat(String text) {
        if(text.startsWith("&")) {
            chat.green(text.substring(1));
        } else if(text.startsWith("#")) {
            chat.red(text.substring(1));
        } else {
            chat.black(text);
        }
    }
    public void sendDraw(int x, int y, int x2, int y2) {
        this.client.sendPacket("3\n"+x+"\n"+y+"\n"+x2+"\n"+y2); // Send line packet
    }
    public void line(int x, int y, int x2, int y2) {
        this.canvas.line(x, y, x2, y2);
    }
    public void selectWord(int index) {
        this.client.sendPacket("4\n"+index); // Sends index of selected word
    }
    public void chooseWord(String s1, String s2, String s3) {
        this.panel.choose(s1, s2, s3);
    }
    public void displayWord(String word) {
        this.panel.showWord(word.toUpperCase());
    }
    public void sendPenChange(int penData) {
        this.client.sendPacket("5\n"+penData);
    }
    public void penChanged(int data) {
        this.canvas.penChanged(data);
    }
}
