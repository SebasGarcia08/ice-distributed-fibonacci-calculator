public class Callback implements Demo.Callback {

    public void response(String msg, com.zeroc.Ice.Current current) {
        System.out.println("--> " + msg);
        System.out.println('\n');
    }

}