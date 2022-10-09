import java.util.HashMap;
import Demo.CallbackPrx;

public class PrinterI implements Demo.Printer {

    public void printString(String message, Demo.CallbackPrx callback, com.zeroc.Ice.Current current) {
        TaskExecutor executor = TaskExecutor.getInstance();
        IFibonacci fib = new FastFibonacci();
        Task task = new Task(message, callback, executor, fib);
        executor.addClient(message, callback);
        executor.execute(task);
    }

}