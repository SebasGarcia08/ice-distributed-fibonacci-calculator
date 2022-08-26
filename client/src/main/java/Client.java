import java.util.Scanner;

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
                if (msg.equals("exit")){
                    break;
                }
                String res = printer.printString(prefix + msg);
                System.out.println("Server: " + res);
                System.out.println("");
            }
            in.close();
        }
    }
}