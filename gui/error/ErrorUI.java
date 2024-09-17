package gui.error;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.Window;

public class ErrorUI extends JFrame {
    public static void showError(Exception e) {
        new ErrorUI(e);
    }
    private ErrorUI(Exception e) {
        super(e.getClass().getName());
        this.setBounds(100, 100, 600, 400);
        this.setType(Window.Type.UTILITY);
        this.setResizable(false);

        TextArea area = new TextArea();
        for(StackTraceElement s : e.getStackTrace()) {
            area.append(s.toString()+"\n");
        }
        area.setBackground(Color.WHITE);
        area.setEditable(false);

        this.add(area, BorderLayout.CENTER);

        this.setVisible(true);
    }
}
