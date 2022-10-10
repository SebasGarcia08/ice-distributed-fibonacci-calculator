import com.zeroc.Ice.Util;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Communicator;
import java.util.List;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "config.server",
                extraArgs)) {
            String p = communicator.getProperties().getProperty("Key.example");
            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                for (String v : extraArgs) {
                    System.out.println(v);
                }
            }
            // System.out.println("Key.example " + p);
            ObjectAdapter adapter = communicator.createObjectAdapter("Printer");
            Object object = new PrinterI();
            adapter.add(object, Util.stringToIdentity("SimplePrinter"));
            adapter.activate();
            communicator.waitForShutdown();
        }
    }
}