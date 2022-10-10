import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

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
            callback.response("0");
            return;
        }
        msg = msg.toLowerCase();

        Integer value = Utils.isInteger(msg);

        try {
            if (value != null) {
                respondFibonacciRequest(value, clientHostName);
            } else if (msg.startsWith("list clients")) {
                respondListClients();
            } else if (msg.startsWith("bc")) {
                broadcast(clientHostName, msg.replace("bc", ""));
            } else if (msg.startsWith("to")) {
                msg = msg.replace("to", "").trim();
                String toHostname = Utils.parseHostname(msg);
                String toMessage = Utils.parseMessage(msg);
                send(clientHostName, toHostname, toMessage);
            } else if (msg.equals("exit")) {
                System.out.println(clientHostName + " left. \n");
            } else {
                System.out.println(this.message);
                callback.response("0");
            }
        } catch (InterruptedException e) {
            sem.release();
            e.printStackTrace();
        }
    }

    public void respondFibonacciRequest(Integer value, String clientHostname) {
        String response = "0";
        if (value > this.fib.maximum()) {
            response = "The number is too big";
        } else if (value < 0) {
            response = "Cannot calculate fibonacci from negative numbers";
        } else {
            BigInteger fib = this.fib.calculate(value);
            response = String.valueOf(fib);
        }
        callback.response(response);
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
        String response = "Clients: ";
        for (String client : clients) {
            response += "\t" + client + "\n";
        }
        callback.response(response);
    }

}