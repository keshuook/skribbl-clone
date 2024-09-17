import gui.welcome.Welcome;
import gui.welcome.WelcomeCallback;
import network.client.SkribblClient;
import network.server.SkribblServer;

public class Main {
    public static void main(String[] args) {
        Welcome welcomeScreen = new Welcome(new WelcomeCallback() {
            public void callback(String username, String URL) {
                try {
                    if(!SkribblClient.isOnline(URL)) { // If the server doesn't exist, create it
                        SkribblServer server = new SkribblServer(Integer.parseInt(URL.split(":")[1]));
                        server.listen();
                    }
                    SkribblClient client = new SkribblClient(username, URL);
                } catch (Exception e) {}
            }
        });
    }
}