import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client
{
    public static void main(String[] args)
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(
                communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            //Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);
            Demo.PrinterPrx printer = twoway.ice_twoway();

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }


            String hostname;
            try {
                hostname = java.net.InetAddress.getLocalHost().getHostName();
            } catch (java.net.UnknownHostException e) {
                System.out.println("Unknown host");
                e.printStackTrace();
                return;
            }
            
            System.out.println("Welcome, " + hostname  + "!");
            
            Scanner in = new Scanner(System.in);
            while(true){
                String prefix = hostname + ":";
                System.out.print("You: ");
                String msg = in.nextLine();
                long startTime = System.nanoTime();
                String res = printer.printString(prefix + msg);
                long elapsed = System.nanoTime() - startTime;
                long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
                long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);

                System.out.println("Server: " + res);
                System.out.println("Time: " + (elapsedMillis) + " ms, " + (elapsedSecs) + " s");
                System.out.println("");
                if (msg.equals("exit")){
                    System.out.println("Connection closed.");
                    break;
                }
            }
            in.close();
        }
    }
}