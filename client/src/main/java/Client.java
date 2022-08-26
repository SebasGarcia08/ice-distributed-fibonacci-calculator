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
            String res = printer.printString("Hello me!");
            try {
                String hostname = java.net.InetAddress.getLocalHost().getHostName();
                System.out.println("Hostname " + hostname);
                System.out.println("Result: " + res);
            }
            catch (java.net.UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}