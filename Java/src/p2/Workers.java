package src.p2;

import java.util.LinkedList;

public class Workers {
    int numberOfThreads;
    boolean running = false;
    LinkedList<Runnable> tasks = new LinkedList<>();
    LinkedList<Thread> threads = new LinkedList<>();

    public Workers(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void start() {
        running = true;

        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(() -> {
                while (running) {
                    Runnable task = null;
                    synchronized (tasks) {
                        if (!tasks.isEmpty()) {
                            task = tasks.get(0);
                            tasks.remove(0);
                        }
                    }
                    if (task != null) {
                        task.run();
                    }
                }
            }));
            threads.get(i).start();
        }
    }

    public synchronized void post(Runnable task) {
        tasks.add(task);
    }


    public void post_timeout(Runnable task, int millis) throws InterruptedException {
        Thread.sleep(millis);

        synchronized (tasks) {
            tasks.add(task);
        }
    }

    public synchronized void stop() {
        if (tasks.isEmpty()) {
            running = false;
        }
    }

    public void join() throws InterruptedException {
        stop();
        for (Thread thread : threads) {
            thread.join();
        }
    }
}