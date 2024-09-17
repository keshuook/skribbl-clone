package gui.server;

import network.server.SkribblServer;

import java.awt.*;

import javax.swing.*;

public class ServerUI extends JFrame {
    private TextArea information;
    private DefaultListModel<String> users;
    private JPanel roundSelection;
    public ServerUI(SkribblServer server, String URL) {
        super("Server on "+URL);

        this.setBounds(100, 100, 800, 550);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.users = new DefaultListModel<String>();
        JList<String> list = new JList<String>(users);

        list.setEnabled(false);
        list.setBackground(Color.WHITE);
        users.addElement("List of Users Online");

        JButton start = new JButton("Play");

        this.roundSelection = new JPanel();
        roundSelection.add(start, BorderLayout.EAST);

        JComboBox roundN = new JComboBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        roundSelection.add(roundN, BorderLayout.WEST);

        this.add(roundSelection, BorderLayout.SOUTH);

        start.addActionListener(l -> {
            roundSelection.setVisible(false);
            server.startGame(Integer.parseInt((String) roundN.getSelectedItem()));
        });

        this.information = new TextArea();
        information.setBackground(Color.WHITE);
        information.setEditable(false);
        information.setSize(300, 0);

        this.add(information, BorderLayout.CENTER);
        this.add(list, BorderLayout.WEST);

        this.setVisible(true);
    }
    public void addInformation(String info) {
        information.append(info+"\n");
    }
    public void addUser(String username) {
        users.addElement(username);
    }
    public void removeUser(String username) {
        users.removeElement(username);
    }
    public void showStartRoundSettings() {
        roundSelection.setVisible(true);
    }
}
