package org.learners.java.exercises;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;


/**
 *
 Create a program to calculate factorials

 Main thread should create 1 producer thread & 10 consumer threads

 Producer thread should produce random numbers between 1 to 10 at an interval of 1 sec
 &
 Task threads should pick the numbers and return the factorial for that number

 Main thread should print the number and its respective factorial value.

 */
public class FactorialCalculator {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);                // executor with 10 consumer threads
    private static BlockingQueue<Future<FactNumber>> futuresQueue = new ArrayBlockingQueue<>(100);     // futuresQueue to hold calculation objects
    private static int min = 1;
    private static int max = 10;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Producer producer = new Producer(executor, futuresQueue, min, max);
        Thread thread = new Thread(producer);
        thread.start();

        processTaskResults();

        executor.shutdown();

    }

    private static void processTaskResults() throws InterruptedException, ExecutionException {

        for(int i = 1; i <= futuresQueue.remainingCapacity(); i++){

            Future<FactNumber> future = futuresQueue.take();       // will block main thread, till the future object calculation is done, then only loop will move on to next future object
            FactNumber factNumber = future.get();

            System.out.println(String.format("Number: %s, Factorial: %s", factNumber.getNumber(), factNumber.getFactorial()));

        }

    }

}


class Producer implements Runnable {

    private ExecutorService executor;
    private BlockingQueue<Future<FactNumber>> futuresQueue;
    private int min;
    private int max;

    public Producer(ExecutorService executor, BlockingQueue<Future<FactNumber>> queue, int min, int max) {
        this.executor = executor;
        this.futuresQueue = queue;
        this.min = min;
        this.max = max;
    }

    @Override
    public void run() {

        System.out.println("Producer thread is up and running...");

        int i=1;
        while (i<=15){      // produces 15 tasks

            int number = getRandomNumberInRange(min, max);
            FactNumber factNumber = new FactNumber(number);

            Future<FactNumber> future = createAndSubmitTasksToExecutor(factNumber);
            futuresQueue.add(future);

            sleepForSeconds(1);
            i++;
        }

        System.out.println("Producer thread is done, now tearing down...");

    }

    private Future<FactNumber> createAndSubmitTasksToExecutor(FactNumber factNumber){

        Task task = new Task(factNumber);
        Future<FactNumber> future = executor.submit(task);

        return future;
    }

    private void sleepForSeconds(int seconds){

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private int getRandomNumberInRange(int min, int max){
        Random random = new Random();
        return random.nextInt(max-min + 1) + min;
    }
}

class Task implements Callable<FactNumber> {

    private FactNumber factNumber;

    public Task(FactNumber factNumber) {
        this.factNumber = factNumber;
    }

    @Override
    public FactNumber call() throws Exception {

        int factorial = calculateFactorial(factNumber.getNumber());
        factNumber.setFactorial(factorial);

        return factNumber;
    }

    private int calculateFactorial(int n){
        if(n == 1){
            return 1;
        }
        return n * calculateFactorial(n-1);
    }

}


class FactNumber {

    private int number;
    private int factorial;

    public FactNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getFactorial() {
        return factorial;
    }

    public void setFactorial(int factorial) {
        this.factorial = factorial;
    }
}