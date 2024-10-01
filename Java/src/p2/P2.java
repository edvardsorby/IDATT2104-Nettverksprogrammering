package src.p2;

public class P2 {
    public static void main(String[] args) throws InterruptedException {

        Workers worker_threads = new Workers(4);
        Workers event_loop = new Workers(1);

        worker_threads.start();
        event_loop.start();

        worker_threads.post(() -> {
                    System.out.println("Task A");
                }
        );
        worker_threads.post(() -> {
                    System.out.println("Task B");
                }
        );


        event_loop.post(() -> {
                    System.out.println("Task C");
                }
        );
        event_loop.post(() -> {
                    System.out.println("Task D");
                }
        );


        worker_threads.join();
        event_loop.join();

    }
}
