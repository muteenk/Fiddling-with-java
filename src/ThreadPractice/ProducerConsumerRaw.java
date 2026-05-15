package ThreadPractice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProducerConsumerRaw {
    private final Object mutex = new Object();
    private final Queue<Task> taskQueue = new LinkedList<>();

    class Producer implements Runnable {
        private final Task task;
        int MAX_CAPACITY = 5;
        Producer(Task task) {
            this.task = task;
        }
        public void run() {
            try {
                synchronized (mutex) {
                    while (taskQueue.size() >= MAX_CAPACITY) mutex.wait();
                    taskQueue.add(task);
                    mutex.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        public void run() {
            try {
                Task t;
                synchronized (mutex) {
                    while (taskQueue.isEmpty()) mutex.wait();
                    t = taskQueue.remove();
                    mutex.notifyAll();
                }
                t.markDone();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void main() {
        ProducerConsumerRaw pc = new ProducerConsumerRaw();
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
