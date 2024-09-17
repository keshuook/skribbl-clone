package objects;

import gui.error.ErrorUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
    private BufferedReader reader;
    public Reader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
    }
    public boolean ready() {
        try {
            return reader.ready();
        } catch (IOException e) {
            return false;
        }
    }
    public String getString() {
        try {
            StringBuffer buffer = new StringBuffer();
            int currentCharacter;
            do {
                currentCharacter = reader.read();
                buffer.append((char)(currentCharacter));
            } while (reader.ready() && currentCharacter != 10);
            return buffer.toString().trim();
        } catch (IOException e) {
            ErrorUI.showError(e);
            return null;
        }
    }
    public int getInt() {
        String s = getString();
        return s.equals("") ? -1 : Integer.parseInt(s); // Blank packets get sent sometimes which cause errors
    }
    public void waitForInput() {
        while (!ready());
    }
}
