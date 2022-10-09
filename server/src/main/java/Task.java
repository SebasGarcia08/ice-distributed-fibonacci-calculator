import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.HashMap;

import java.util.concurrent.Semaphore;
import Demo.Callback;
import Demo.CallbackPrx;

public class Task implements Runnable {

    private CallbackPrx callback;
    private HashMap<String, CallbackPrx> clients;
    private Semaphore sem;
    private String message;
    private IFibonacci fib;

    public Task(
            String message,
            CallbackPrx callback,
            HashMap<String, CallbackPrx> clients,
            Semaphore sem) {
        this.message = message;
        this.callback = callback;
        this.clients = clients;
        this.sem = sem;
        this.fib = new FastFibonacci();
    }

    public void run() {
        String[] splittedMsg = this.message.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];
        String response = "0";

        Integer value = Utils.isInteger(msg);

        if (value != null) {
            if (value > this.fib.maximum()) {
                response = "The number is too big";
            } else if (value < 0) {
                response = "Cannot calculate fibonacci from negative numbers";
            } else {
                BigInteger fib = this.fib.calculate(value);
                response = String.valueOf(fib);
            }
        } else if (msg.equals("exit")) {
            System.out.println(clientHostName + " left. \n");
            response = "Bye bye!";
        } else {
            response = "0";
            System.out.println(this.message);
        }
    }

    public void broadcast() {
    }

    public String listClients() {
        return "";
    }

}