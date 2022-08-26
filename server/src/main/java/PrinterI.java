public class PrinterI implements Demo.Printer
{
    public String printString(String s, com.zeroc.Ice.Current current)
    {
        System.out.println(s);
        return "result";
    }
}