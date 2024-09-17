package gui.client;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Canvas extends JPanel {
    private  Image image;
    private int x = 0,y = 0;
    private Graphics2D g;
    private ClientUI ui;
    public Canvas(ClientUI ui) {
        super();
        this.ui = ui;
        this.setPreferredSize(new Dimension(450, 450));
        this.setBorder(BasicBorders.getTextFieldBorder());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX() > getWidth() || e.getX() < 0 ? x : e.getX();
                y = e.getY() > getHeight() || e.getY() < 0 ? y : e.getY();
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                ui.sendDraw(x, y, e.getX() > getWidth() - 40 || e.getX() < 0 ? x : e.getX(), e.getY() > getHeight() || e.getY() < 0 ? y : e.getY());
                x = e.getX() > getWidth() || e.getX() < 0 ? x : e.getX();
                y = e.getY() > getHeight() || e.getY() < 0 ? y : e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        if(this.g == null) {
            this.image = createImage(500, 550);
            this.g = (Graphics2D) (image.getGraphics());
            this.g.setColor(Color.WHITE);
            this.g.fillRect(0, 0, 500, 550);
            this.g.setStroke(new BasicStroke(5));
            this.g.setColor(Color.BLACK);

        }
        g.drawImage(image, 0, 0, null);
        super.paintComponents(g);
    }
    public void line(int x, int y, int x2, int y2) {
        g.drawLine(x, y, x2, y2);
        repaint();
    }
    public void penChanged(int data) {
        switch (data) {
            case 10:
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                this.repaint();
            case 0:
                g.setStroke(new BasicStroke(5));
                break;
            case 1:
                g.setStroke(new BasicStroke(15));
                break;
            case 2:
                g.setStroke(new BasicStroke(30));
                break;
            case 3:
                g.setColor(Color.BLACK);
                break;
            case 4:
                g.setColor(Color.WHITE);
                break;
            case 5:
                g.setColor(new Color(0xffff1a));
                break;
            case 6:
                g.setColor(new Color(0xff2000));
                break;
            case 7:
                g.setColor(new Color(0x46a049));
                break;
            case 8:
                g.setColor(new Color(0x668cff));
                break;
            case 9:
                g.setColor(new Color(0x800000));
                break;
        }
    }
}
