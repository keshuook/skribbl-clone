package network.server;

import gui.error.ErrorUI;

public class TimeManager extends Thread {
    private long millis;
    private int time = 120;
    private Runnable callback;

    public int getTime() {
        return time;
    }
    @Override
    public void run() {
        while (true) {
            try {
                millis = System.currentTimeMillis();
                if (time < 0){
                    return;
                } else if(time == 45 || time == 20 || time == 15 || time <= 10) {
                    callback.run();
                }
                time--;
                Thread.sleep(1000-(System.currentTimeMillis() - millis));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
}
