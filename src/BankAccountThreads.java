import java.util.ArrayList;
import java.util.List;

public class BankAccountThreads {
    private float balance = 100000;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    private class ThreadSend implements Runnable {
        public void run() {
            synchronized (lock1) {
                synchronized (lock2) {
                    float curr = balance;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    balance = curr - 1000;
                }
            }
        }
    }

    private class ThreadReceive implements Runnable {
        public void run() {
            synchronized (lock2) {
                synchronized (lock1) {
                    float curr = balance;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    balance = curr + 1000;
                }
            }
        }
    }

    public float getBalance() {return balance;}

    static void main(String[] args) {
        BankAccountThreads bacc = new BankAccountThreads();
        List<Thread> t = new ArrayList<>();

        for (int i = 0; i < 1000; i++){
            Thread sendThread = new Thread(bacc.new ThreadSend());
            Thread receiveThread = new Thread(bacc.new ThreadReceive());
            t.add(sendThread);
            t.add(receiveThread);
        }

        for (int i = 0; i < t.size(); i++){
            t.get(i).start();
        }

        for (int i = 0; i < t.size(); i++){
            try {
                t.get(i).join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(bacc.getBalance());
    }


}
