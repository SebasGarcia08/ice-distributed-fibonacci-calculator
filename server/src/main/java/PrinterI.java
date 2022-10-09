import java.math.BigInteger;

public class PrinterI implements Demo.Printer {

    public void printString(String message, Demo.CallbackPrx callback, com.zeroc.Ice.Current current) {
        (new TaskManager()).execute();
    }

}