package org.learners.java.exercises;

/**
 * Main thread should start a new Thread,
 * this new thread should start another new thread
 * and so on...until thread count reaches 50
 *
 * each thread should print "Hello with its thread number"
 *
 * Note: Hello should get printed in reverse like "Hello 50" then "Hello 49" and so on..
 *
 */
public class ReverseHello {

    public static void main(String[] args) {

        Hello hello = new Hello(1);
        Thread t = new Thread(hello);
        t.start();

    }


}

class Hello implements Runnable {

    private int count;

    public Hello(int count) {
        this.count = count;
    }

    @Override
    public void run() {

        while(count < 51){

            Hello hello = new Hello(count + 1);
            Thread t = new Thread(hello);
            t.start();

            count++;
        }

        System.out.println("Hello " + this.count);
    }
}
