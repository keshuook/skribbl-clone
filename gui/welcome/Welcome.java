package gui.welcome;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Welcome extends JFrame {
    private WelcomeCallback callback;
    public Welcome(WelcomeCallback callback) {
        super("Server Selector");
        this.callback = callback;

        this.setBounds(120, 40, 800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);
        this.add(getWelcomePanel());
        this.repaint();
    }
    private JPanel getWelcomePanel() {
        final Font INPUTFONT = new Font("Courier New", Font.PLAIN, 24);

        // Create the welcome panel
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, this.getWidth(), this.getHeight());
        panel.setLayout(null);
    
        // Create the form elements for the server joining screen
        JTextField serverIP = new JTextField("localhost:141");
        serverIP.setBounds(250, 150, 300, 50);
        serverIP.setFont(INPUTFONT);
        
        JTextField clientName = new JTextField("Kapes");
        clientName.setBounds(250, 300, 300, 50);
        clientName.setFont(INPUTFONT);
        
        JButton playButton = new JButton("Play!");
        playButton.setBounds(250, 350, 300, 50);
        playButton.setFont(INPUTFONT);

        final JFrame window = this;
        playButton.addActionListener(listener -> {
            callback.callback(clientName.getText(), serverIP.getText());
            window.dispose(); // Close welcome screen after client connects/attempts to connect to game
        });
        
        ImageIcon icon = new ImageIcon("assets/welcome.png");
        Image image = icon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        
        JLabel bgLabel = new JLabel(icon);
        bgLabel.setBounds(0, 0, 800, 600);
        
        // Add components in the correct order
        panel.add(serverIP);
        panel.add(clientName);
        panel.add(playButton);
        panel.add(bgLabel);
    
        return panel;
    }
}
