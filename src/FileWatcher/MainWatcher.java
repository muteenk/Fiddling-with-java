package FileWatcher;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainWatcher {

    static void main() {
        String BUFFPATH = "./buff.data";
        Path buffLocation = Paths.get(BUFFPATH);
        Object mutex = new Object();

        Consumer fileWatcher = new Consumer(buffLocation, mutex);
        fileWatcher.watch();
    }
}
