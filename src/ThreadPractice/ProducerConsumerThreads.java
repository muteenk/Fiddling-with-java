package ThreadPractice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;


/*  IMPLEMENTING PRODUCER CONSUMER BUFFER WITH SEMAPHORES
*
*   In this example, we are utilizing semaphores along with mutex locks
*   to ensure multiple producers and consumers can act together without
*   failure or race conditions.
*
*   The producer consumer problems:
*   - Consumer tries to consume before anything is produced
*   - Producer tries to write to queue, even though it is full
*
*   So, we use semaphores with '5' permits,
*   which allows only 5 queue slots to be filled up
*   by threads, once a slot is occupied the permit goes down by one
*   This happens on emptySlots.acquire(), it does 5-1
*
*   Then, once a slot is occupied by a thread, it calls
*   occupiedSlots.release(), which internally does 0+1
*   to tell the consumer that you can consume 1 item
*
*   BUT, that's not it, we still need mutex locks, semaphores
*   only limit the no. of threads, the critical section still remains
*   unsafe. For that we still use mutex locks.
* */

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
