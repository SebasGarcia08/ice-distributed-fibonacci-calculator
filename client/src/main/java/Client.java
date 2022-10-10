import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Demo.CallbackPrx;
import Demo.PrinterPrx;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;

public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "config.client", extraArgs)) {
            // com.zeroc.Ice.ObjectPrx base =
            // communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(
                    communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            // Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);
            PrinterPrx printer = twoway.ice_twoway();

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            // Callback configuration
            ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            Object obj = new Callback();
            ObjectPrx objectPrx = adapter.add(obj, Util.stringToIdentity("callback"));
            adapter.activate();
            CallbackPrx callPrx = CallbackPrx.uncheckedCast(objectPrx);
            String hostname;
            try {
                hostname = java.net.InetAddress.getLocalHost().getHostName();
            } catch (java.net.UnknownHostException e) {
                System.out.println("Unknown host");
                e.printStackTrace();
                return;
            }
            if (args.length > 0) {
                String req = hostname + ":" + args[0];
                System.out.println("Request: " + req);
                try {
                    printer.printString(req, callPrx);
                    Thread.sleep(20000);
                    System.out.println("TIMEOUT");
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }
            } else {
                run(printer, callPrx, hostname);
            }
        }
    }

    public static void run(PrinterPrx printer, CallbackPrx callback, String hostname) {
        System.out.println("Welcome, " + hostname + "!");

        Scanner in = new Scanner(System.in);
        boolean firstPrompt = true;

        while (true) {
            String prefix = hostname + ":";
            if (firstPrompt) {
                System.out.print("You: ");
                firstPrompt = false;
            } else {
                System.out.println("");
            }
            String msg = in.nextLine();
            long startTime = System.nanoTime();

            printer.printString(prefix + msg, callback);

            long elapsed = System.nanoTime() - startTime;
            long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            System.out.print("Time: " + (elapsedMillis) + " ms, " + (elapsedSecs) + " s");
            if (msg.equals("exit")) {
                System.out.println("Connection closed.");
                break;
            }
        }
        in.close();
    }
}