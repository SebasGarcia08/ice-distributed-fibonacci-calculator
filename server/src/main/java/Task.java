import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import Demo.CallbackPrx;

public class Task implements Runnable {

    private CallbackPrx callback;
    private String message;
    private IFibonacci fib;
    private TaskExecutor executor;
    private Semaphore sem;

    public Task(
            String message,
            CallbackPrx callback,
            TaskExecutor executor,
            IFibonacci fib) {
        this.executor = executor;
        this.message = message;
        this.callback = callback;
        this.fib = fib;

        this.sem = executor.getSemaphore();
    }

    public void run() {
        String clientHostName = Utils.parseHostname(message);
        String msg = Utils.parseMessage(message);

        if (msg == null) {
            try {
                sem.acquire();
                callback.response("0");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.sem.release();
            }
            return;
        }
        msg = msg.toLowerCase();

        Integer value = Utils.isInteger(msg);

        try {
            sem.acquire();
            if (value != null) {
                respondFibonacciRequest(value, clientHostName);
            } else if (msg.startsWith("list clients")) {
                respondListClients();
            } else if (msg.startsWith("bc")) {
                broadcast(clientHostName, msg.replace("bc", ""));
            } else if (msg.startsWith("to")) {
                msg = msg.replace("to", "").trim();
                System.out.println(msg);
                String[] splitted = msg.split(" ");
                String toHostname = splitted[0];
                String toMessage = msg.substring(toHostname.length(), msg.length());
                send(clientHostName, toHostname, toMessage);
            } else if (msg.equals("exit")) {
                String leftMessage = clientHostName + " left.";
                System.out.println(leftMessage + "\n");
                broadcast(clientHostName, leftMessage);
            } else {
                System.out.println(this.message);
                callback.response("0");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sem.release();
        }
    }

    public void respondFibonacciRequest(Integer value, String clientHostname) {
        String response = "0";
        long startTimeRes = System.nanoTime();

        if (value > this.fib.maximum()) {
            response = "The number is too big";
        } else if (value < 0) {
            response = "Cannot calculate fibonacci from negative numbers";
        } else {
            NumberFormat numFormat = new DecimalFormat("0E0");
            String formatted = numFormat.format(value);
            long startTime = System.nanoTime();
            System.out.println("Calculating fibonacci for " + formatted);
            BigInteger fib = this.fib.calculate(value);
            long elapsed = System.nanoTime() - startTime;
            long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            System.out.println(
                    "[INFO] Took  " + elapsedSecs + " s (" + elapsedMillis + " ms) to calculate fibonacci of "
                            + formatted);
            response = String.valueOf(fib);
        }
        callback.response(response);
        long elapsed = System.nanoTime() - startTimeRes;
        long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        System.out.println("[INFO] Sent response of " + value + "-th Fibonacci to " + clientHostname + " took: "
                + elapsedSecs + " s (" + elapsedMillis + " ms)");
    }

    public void send(String from, String to, String message) throws InterruptedException {

        System.out.println("Sending message from " + from + " to " + to + " with message " + message);

        sem.acquire();

        CallbackPrx client = executor.getClientCallback(to);
        if (client != null) {
            String response = from + ": " + message;
            client.response(response);
            System.out.println("Success");
        } else {
            System.out.println("Client " + to + " not found");
        }

        sem.release();
    }

    public void broadcast(String from, String message) throws InterruptedException {

        System.out.println("Broadcasting message from " + this.message);
        sem.acquire();

        for (HashMap.Entry<String, CallbackPrx> entry : executor.getClients().entrySet()) {
            String to = entry.getKey();
            CallbackPrx client = entry.getValue();
            if (to.equals(from))
                continue;
            String response = from + ": " + message;
            client.response(response);
        }

        sem.release();
    }

    public ArrayList<String> listClients() throws InterruptedException {
        sem.acquire();

        ArrayList<String> clients = new ArrayList<String>();

        for (HashMap.Entry<String, CallbackPrx> entry : executor.getClients().entrySet()) {
            String to = entry.getKey();
            clients.add(to);
        }

        sem.release();

        return clients;
    }

    public void respondListClients() throws InterruptedException {
        ArrayList<String> clients = listClients();
        String response = "\nConnected clients: \n";
        for (String client : clients) {
            response += "\t" + client + "\n";
        }
        callback.response(response);
    }

}