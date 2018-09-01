package org.learners.java.exercises;

/**
 * Program to gracefully stop a running thread
 */
public class StopARunningThreadGracefully {

    static volatile Flag flag = new Flag();

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(new MyThread(flag), "MyThread");
        t.start();

        Thread.sleep(10);

        flag.setKeepRunning(false);
    }

}

class MyThread implements Runnable {

    private Flag flag;

    public MyThread(Flag flag) {
        this.flag = flag;
    }

    @Override
    public void run() {

        int count = 0;

        System.out.println("Starting with count " + count);

        while (flag.isKeepRunning()){
            System.out.println(count++);
        }

        System.out.println("Ending with count " + count);

    }
}

class Flag {

    private volatile boolean keepRunning = true;

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}