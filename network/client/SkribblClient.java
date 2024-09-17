package network.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import gui.client.ClientUI;
import gui.error.ErrorUI;
import objects.Reader;

public class SkribblClient extends Socket implements Runnable {
    private Reader reader;
    private PrintWriter writer;
    private ClientUI ui;
    private long time;

    public static boolean isOnline(String URL) throws Exception {
        try {
            new Socket(URL.split(":")[0], Integer.parseInt(URL.split(":")[1])).close();
        } catch (ConnectException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) { // For incorrect URLs
            JOptionPane.showMessageDialog(null, "Please enter a valid url.", "Invalid Server URL", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Invalid URL");
        } catch (IOException e) {
            ErrorUI.showError(e);
        }
        return true;
    }
    public SkribblClient(String username, String URL) throws NumberFormatException, UnknownHostException, IOException {
        super(URL.split(":")[0], Integer.parseInt(URL.split(":")[1]));
        reader = new Reader(this.getInputStream());
        writer = new PrintWriter(this.getOutputStream(), true);

        writer.print(1+"\n"+username+"\n");

        this.ui = new ClientUI(this);
        new Thread(this).start();
        ui.setUpNonEssential();
    }
    public void sendPacket(String data) {
        writer.print(data+"\n");
    }
    public void run() {
        time = System.currentTimeMillis();
        while (true) {
            // Ping server so that it knows that the client is still connected
            writer.print(0 + "\n");
            if((System.currentTimeMillis()-time) > 1000) {
                ui.displayWord("Connection Timed Out");
                ErrorUI.showError(new SocketTimeoutException());
                return;
            }
            while (reader.ready()) {
                time = System.currentTimeMillis();
                switch (reader.getInt()) {
                    case 0:
                        writer.print(0 + "\n");
                        break;
                    case 1: // Player Join
                        ui.addUser(reader.getString());
                        break;
                    case 2: // Player Leave
                        ui.removeUser(reader.getString());
                        break;
                    case 3: // Chat message
                        ui.chat(reader.getString());
                        break;
                    case 4: // Draw
                        ui.line(reader.getInt(), reader.getInt(), reader.getInt(), reader.getInt());
                        break;
                    case 5: // Choose word
                        ui.chooseWord(reader.getString(), reader.getString(), reader.getString());
                        break;
                    case 6: // Display word
                        ui.displayWord(reader.getString());
                        break;
                    case 7: // Pen property (10 = clear)
                        ui.penChanged(reader.getInt());
                        break;
                    case 8: // Score packet
                        ui.updateScore(reader.getString(), reader.getInt());
                    default:
                        break;
                }
            }
        }
    }
    
}