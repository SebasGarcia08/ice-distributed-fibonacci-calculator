import java.util.concurrent.Semaphore;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;

import Demo.CallbackPrx;

public class TaskExecutor {

    private final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    private static TaskExecutor instance = null;

    private ExecutorService pool;
    private HashMap<String, CallbackPrx> clients;
    private Semaphore sem;

    private TaskExecutor() {
        this.sem = new Semaphore(1);
        this.pool = Executors.newFixedThreadPool(MAX_THREADS);

        this.clients = new HashMap<String, CallbackPrx>();
    }

    public static TaskExecutor getInstance() {
        if (instance == null) {
            instance = new TaskExecutor();
        }
        return instance;
    }

    public ExecutorService getPool() {
        return this.pool;
    }

    public HashMap<String, CallbackPrx> getClients() {
        return this.clients;
    }

    public CallbackPrx getClientCallback(String hostname) {
        if (this.clients.containsKey(hostname)) {
            return this.clients.get(hostname);
        }
        return null;
    }

    public void addClient(String message, CallbackPrx callback) {
        String hostname = Utils.parseHostname(message);
        try {
            this.sem.acquire();
            if (!this.clients.containsKey(hostname)) {
                this.clients.put(hostname, callback);
                System.out.println(hostname + " joined. \n");
            }
            this.sem.release();
        } catch (InterruptedException e) {
            System.out.println("[ERROR]" + hostname + " failed to join. \n");
            e.printStackTrace();
        } finally {
            this.sem.release();
        }
    }

    public Semaphore getSemaphore() {
        return this.sem;
    }

    public void execute(Task task) {
        this.pool.execute(task);
    }

}
