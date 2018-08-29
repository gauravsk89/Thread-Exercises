package org.learners.java.exercises;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 *
 * Find sum of array elements, given an array of size n
 *
 * put m number of threads to make this calculation faster
 *
 */
public class SumOfArray {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int arrayLength = 10;
         int threadCount = 4;

        int[] array = new int[arrayLength];

        for(int i=0; i<arrayLength; i++){
            array[i] = getRandomNumberInRange(1, 10);
        }

        System.out.println(Arrays.toString(array));
        System.out.println();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future<Integer>[] futures = new Future[threadCount];

        for(int j=0; j < threadCount; j++){

            int startIndex = (j * arrayLength) / threadCount;
            int endIndex = (j + 1) * arrayLength / threadCount;

            Task task = new Task(array, startIndex, endIndex);

            futures[j] = executor.submit(task);
        }

        int totalSum = 0;
        for(int k=0; k < threadCount; k++){
            totalSum += futures[k].get();
        }
        executor.shutdown();

        System.out.println(String.format("\nTotal sum: %s", totalSum));

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
