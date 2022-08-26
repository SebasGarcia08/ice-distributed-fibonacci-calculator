public class PrinterI implements Demo.Printer
{
    public String printString(String s, com.zeroc.Ice.Current current)
    {
        System.out.println(s);
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];

        if (this.isInteger(msg)){
            int value = Integer.parseInt(msg);
            if (value > 0){
                int fib = this.fibonacci(value);
                return String.valueOf(fib);
            }
        }
        System.out.println(msg);
        return "0";
    }

    public boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    public int fibonacci(int input){
        if(input == 1) {
            return 1;
        }
        else if(input == 2) {
            return 1;
        }
        else {
            return fibonacci(input - 1) + fibonacci(input - 2);
        }
    }
}