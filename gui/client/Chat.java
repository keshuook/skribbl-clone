package gui.client;

import network.client.SkribblClient;

import javax.swing.*;
import java.awt.*;

public class Chat extends JPanel {
    private SkribblClient client;
    private JScrollPane chatPane;
    private JPanel chatArea;  // Panel to hold chat messages
    private JTextField msgbox;

    public Chat(SkribblClient client) {
        super();
        this.client = client;

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 550));

        // Chat area to hold chat messages
        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));  // Vertical layout for messages
        chatArea.setBackground(Color.WHITE);

        // JScrollPane to make chatArea scrollable
        chatPane = new JScrollPane(chatArea);
        chatPane.setPreferredSize(new Dimension(300, 450));
        this.add(chatPane, BorderLayout.CENTER);

        // Message input box
        msgbox = new JTextField();
        msgbox.setPreferredSize(new Dimension(300, 50));
        this.add(msgbox, BorderLayout.SOUTH);

        msgbox.addActionListener(l -> {
            if(msgbox.getText().equals("")) return; // Prevent sending blank messages
            client.sendPacket("2\n"+msgbox.getText());
            msgbox.setText(""); // Clear message box
        });
    }

    // Method to add green-colored messages to the chat area
    public void green(String text) {
        this.text(text, new Color(0x4caf50));
    }
    public void red(String text) {
        this.text(text, new Color(0xc50032));
    }
    public void black(String text) {
        this.text(text, new Color(0x070738));
    }
    private void text(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);

        // Add the message to the chat area
        chatArea.add(label);
        chatArea.revalidate();
        chatArea.repaint();


        // Scroll to the bottom after adding the message
        SwingUtilities.invokeLater(() -> { // Ensures the scroll pane scrolls to the bottom after adding the message
            JScrollBar vertical = chatPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());  // Scroll to the bottom
        });
    }
}
