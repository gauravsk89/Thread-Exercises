package org.learners.java.exercises;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * Find sum of array elements, given an array of size n
 *
 * put m number of threads to make this calculation faster
 *
 */
public class SumOfArray {

    private static int arrayLength = 10;
    private static int threadCount = 4;

    private static int array[] = new int[arrayLength];

    private static ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    private static Future<Integer>[] futures = new Future[threadCount];


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        populateArrayWithRandomValues();

        printArray();

        createAndSubmitTasksToExecutor();

        int totalSum = aggregateIndividualThreadResults();

        executor.shutdown();

        System.out.println(String.format("\nTotal sum: %s", totalSum));

    }

    private static int aggregateIndividualThreadResults() throws ExecutionException, InterruptedException {

        int totalSum = 0;

        for(int k=0; k < futures.length; k++){
            totalSum += futures[k].get();
        }

        return totalSum;
    }


    private static void populateArrayWithRandomValues(){

        for(int i=0; i<arrayLength; i++){
            array[i] = getRandomNumberInRange(1, 10);
        }

    }

    public static void printArray(){
        System.out.println(Arrays.toString(array));
        System.out.println();
    }

    private static ExecutorService createAndSubmitTasksToExecutor(){

        for(int j=0; j < threadCount; j++){

            int startIndex = (j * array.length) / threadCount;
            int endIndex = (j + 1) * array.length / threadCount;

            Task task = new Task(array, startIndex, endIndex);

            futures[j] = executor.submit(task);
        }

        return executor;
    }

    public static int getRandomNumberInRange(int min, int max){
        Random random = new Random();
        return random.nextInt(max-min + 1) + min;
    }

}

class Task implements Callable<Integer> {

    private int[] array;
    private int startIndex;
    private int endIndex;

    public Task(int[] array, int startIndex, int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Integer call() throws Exception {

        int sum = 0;

        for(int i = startIndex; i < endIndex; i++){
            sum += array[i];
        }

        System.out.println(String.format("Sum = %s for range %s to %s", sum, startIndex, endIndex-1));

        return sum;
    }
}
