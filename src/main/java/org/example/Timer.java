package org.example;

public class Timer implements Runnable {
    private int seconds;

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            seconds += 1;
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
