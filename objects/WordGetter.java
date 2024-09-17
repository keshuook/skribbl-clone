package objects;

import gui.error.ErrorUI;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordGetter {
    private ArrayList<String> words = new ArrayList<String>();
    public WordGetter(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            StringBuffer buffer = new StringBuffer();
            while (reader.ready()) {
                char c = (char)(reader.read());
                if(c == '\n') {
                    words.add(buffer.toString().trim());
                    buffer.setLength(0);
                } else {
                    buffer.append(c);
                }
            }
        } catch (IOException e) {
            ErrorUI.showError(e);
        }
    }
    public String[] getWords() {
        String[] words = new String[3];
        for(int i = 0;i < words.length;i++) {
            words[i] = this.words.get((int)(Math.floor(Math.random()*this.words.size())));
        }
        return words;
    }
}
