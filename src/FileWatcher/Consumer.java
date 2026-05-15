package FileWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Consumer {
    private final Path buffPath;
    private final Object mutex;
    public Consumer(Path bufferLocation, Object lock){
        buffPath = bufferLocation;
        mutex = lock;
    }

    class RunnableConsumer implements Runnable {
        public void run() {
            try {
                synchronized (mutex) {
                    long bytesize = Files.size(buffPath);
                    if (bytesize <= 0) mutex.wait();
                }
                
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            catch (IOException e){
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to read buffer path : " +e);
            }
        }
    }

    public void watch() {

    }
}
