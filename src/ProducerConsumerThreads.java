import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

class Task {
    private final String title;
    private String status;

    public Task(String title) {
        this.title = title;
        this.status = "PENDING";
    }
    public void markDone() {this.status="DONE";}
    public void repr() {
        System.out.println("-----------------------");
        System.out.println("TASK: " + this.title);
        System.out.println("STATUS: " + this.status);
        System.out.println("-----------------------\n");
    }
}

public class ProducerConsumerThreads {
    private final Object mutex = new Object();
    private final Semaphore emptySlots = new Semaphore(5);
    private final Semaphore occupiedSlots = new Semaphore(0);
    private final Queue<Task> taskQueue = new LinkedList<>();

    class Producer implements Runnable {
        private final Task task;
        Producer(Task task) {
            this.task = task;
        }
        public void run() {
            try {
                emptySlots.acquire();
                synchronized (mutex) {
                    taskQueue.add(task);
                }
                occupiedSlots.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        public void run() {
            try {
                occupiedSlots.acquire();
                synchronized (mutex) {
                    Task t = taskQueue.remove();
                    t.markDone();
                }
                emptySlots.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void main() {
        ProducerConsumerThreads pc = new ProducerConsumerThreads();
        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();

        for (int i = 0; i < 40; i++){
            Task newTask = new Task("TASK "+(i+1));
            allTasks.add(newTask);
            producers.add(new Thread(pc.new Producer(newTask)));
            consumers.add(new Thread(pc.new Consumer()));
        }

        for (int i = 0; i < 40; i++){
            producers.get(i).start();
            consumers.get(i).start();
        }

        for (int i = 0; i < 40; i++){
            try {
                producers.get(i).join();
                consumers.get(i).join();
            } catch (Exception e){
                e.printStackTrace();
                producers.get(i).interrupt();
                consumers.get(i).interrupt();
            }
        }

        for (Task task: allTasks){
            task.repr();
        }
    }
}
