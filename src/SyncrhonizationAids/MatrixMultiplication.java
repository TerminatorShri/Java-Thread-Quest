package SyncrhonizationAids;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MatrixMultiplication {
    private static final int SIZE = 3;
    private static final int[][] A = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };
    private static final int[][] B = {
            {9, 8, 7},
            {6, 5, 4},
            {3, 2, 1}
    };
    private static final int[][] result = new int[SIZE][SIZE];

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(SIZE, () -> {
            System.out.println("All rows computed. Result matrix:");
            printMatrix();
        });

        for (int i = 0; i < SIZE; i++) {
            new Thread(new Worker(i, barrier)).start();
        }
    }

    static class Worker implements Runnable {
        private final int row;
        private final CyclicBarrier barrier;

        public Worker(int row, CyclicBarrier barrier) {
            this.row = row;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = 0;
                for (int k = 0; k < SIZE; k++) {
                    result[row][col] += A[row][k] * B[k][col];
                }
            }
            System.out.println("Thread for row " + row + " finished computation.");

            try {
                barrier.await(); // Wait until all threads complete their row
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.println("Barrier error: " + e.getMessage());
            }
        }
    }

    private static void printMatrix() {
        for (int[] row : MatrixMultiplication.result) {
            for (int col : row) {
                System.out.print(col + "\t");
            }
            System.out.println();
        }
    }
}
