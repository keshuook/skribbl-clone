package gui.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawingBoard extends JPanel {
    private Canvas canvas;
    public DrawingBoard(ClientUI ui) {
        this.canvas = new Canvas(ui);
        this.add(canvas, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon("assets/colour.png");
        Image image = icon.getImage().getScaledInstance(450, 45, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);

        JLabel bgLabel = new JLabel(icon);
        this.add(bgLabel, BorderLayout.SOUTH);

        bgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ui.sendPenChange(e.getX()/45);
            }
        });
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
