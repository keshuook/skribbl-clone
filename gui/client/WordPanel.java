package gui.client;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class WordPanel extends JPanel {
    private JLabel word;
    private JButton word1Button, word2Button, word3Button;
    public WordPanel(ClientUI ui) {
        super();
        this.setBackground(new Color(0x90D5FF));
        this.setPreferredSize(new Dimension(1000, 40));

        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.TRACKING, 0.5);
        Font wordFont = new Font("", Font.BOLD, 24).deriveFont(attributes);
        this.word = new JLabel("");
        word.setFont(wordFont);

        // Buttons for word selection
        word1Button = new JButton();
        this.add(word1Button, BorderLayout.WEST);

        word2Button = new JButton();
        this.add(word2Button, BorderLayout.CENTER);

        word3Button = new JButton();
        this.add(word3Button, BorderLayout.EAST);

        word1Button.addActionListener(l -> {
            ui.selectWord(0);
        });

        word2Button.addActionListener(l -> {
            ui.selectWord(1);
        });

        word3Button.addActionListener(l -> {
            ui.selectWord(2);
        });
        showWord("");
        this.add(word, BorderLayout.CENTER);
    }
    public void choose(String s1, String s2, String s3) {
        word.setVisible(false);
        word1Button.setVisible(true);
        word1Button.setText(s1);
        word2Button.setVisible(true);
        word2Button.setText(s2);
        word3Button.setVisible(true);
        word3Button.setText(s3);
    }
    public void showWord(String str) {
        word.setVisible(true);
        word1Button.setVisible(false);
        word2Button.setVisible(false);
        word3Button.setVisible(false);
        word.setText(str);
    }
}
