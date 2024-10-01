package src.p1;

import java.util.Collections;
import java.util.LinkedList;

import static java.lang.System.currentTimeMillis;

public class P1 extends Thread {
    int start;
    int end;
    public static LinkedList<Integer> primeNumbers = new LinkedList<>();

    public P1(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static void main(String[] args) throws InterruptedException {

        LinkedList<Thread> threadList = new LinkedList<>();

        int startNumber = 0;
        int endNumber = 100000;
        int threads = 8;

        int division = (endNumber-startNumber)/threads;

        for (int i = 0; i < threads; i++) {
            if (i != threads-1) {
                threadList.add(new P1(startNumber+division*i, startNumber+division*(i+1)));
            } else {
                threadList.add(new P1(startNumber+division*i, endNumber));
            }
        }

        double startTime = currentTimeMillis();

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        double endTime = currentTimeMillis();


        Collections.sort(primeNumbers);
        System.out.println("Antall primtall mellom " + startNumber + " og " + endNumber + ": " + primeNumbers.size());
        System.out.println(primeNumbers);

        System.out.println("Tid brukt = " + (endTime-startTime) + " ms");
    }

    public void run() {
        findPrimeNumbers(start, end);
    }

    public static synchronized void addToPrimeList(int i) {
        if (!primeNumbers.contains(i)) {
            primeNumbers.add(i);
        }
    }

    public static void findPrimeNumbers(int start, int slutt) {
        if (start == 0 || start == 1) start = 2;

        for (int i = start; i <= slutt; i++) {
            if (isPrime(i)) {
                addToPrimeList(i);
            }
        }

    }
    public static boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}