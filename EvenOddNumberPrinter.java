package org.learners.java.exercises;

/**
 * Program to print number is order
 *
 * Main thread should start two threads
 * one thread should print number numbers &
 * another thread should print number numbers
 *
 * Number should be printed in order like 0, 1, 2, 3, 4, 5, 6 and so on..
 *
 * NOTE: Implement using Thread.sleep() approach
 *
 */

public class EvenOddNumberPrinter {

    static MyNumber number = new MyNumber(-1);
    static Object object = new Object();

    public static void main(String[] args) {

        Thread even = new Thread(new Even(number, object), "EvenThread");
        Thread odd = new Thread(new Odd(number, object), "OddThread");

        even.start();
        odd.start();

    }
}


class Even implements Runnable {

    private MyNumber number;
    private Object object;

    public Even(MyNumber number, Object object) {
        this.number = number;
        this.object = object;
    }

    @Override
    public void run() {

        while(true){

            synchronized (object){

                if(number.getNumber() >= 9){
                    break;
                }

                if(number.getNumber() %2 == 0){
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                number.setNumber(number.getNumber() + 1);
                System.out.println(number);

                object.notify();

            }

        }


    }

}

class Odd implements Runnable {

    private MyNumber number;
    private Object object;

    public Odd(MyNumber number, Object object) {
        this.number = number;
        this.object = object;
    }

    @Override
    public void run() {

        while (true){

            synchronized (object){

                if(number.getNumber() >= 9){
                    break;
                }

                if(number.getNumber() %2 != 0){
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                number.setNumber(number.getNumber() + 1);
                System.out.println(number);

                object.notify();

            }

        }

    }

}

class MyNumber {
    private volatile int number;

    public MyNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return number + "";
    }
}