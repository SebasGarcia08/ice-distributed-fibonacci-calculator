import java.util.HashMap;

public class PrinterI implements Demo.Printer
{
    
    HashMap<Long, Long> mem; 
    
    public PrinterI() {
        this.mem = new HashMap<Long, Long>();
        this.mem.put(1L, 1L);
        this.mem.put(2L, 1L);
    }

    public String printString(String s, com.zeroc.Ice.Current current)
    {
        System.out.println(s);
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];

        if(msg.equals("exit")){
            System.out.println(clientHostName + " left. \n");
            return "Bye bye!";
        }

        if (this.isInteger(msg)){
            long value = Long.parseLong(msg);
            if (value > 0){
                long fib = this.fibonacci(value);
                return String.valueOf(fib);
            }
        }

        System.out.println(msg);
        System.out.println("");
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

    public long fibonacci(long input){
        if(this.mem.containsKey(input)){
            return this.mem.get(input);
        }

        for (long i = this.mem.size() + 1; i <= input; i++){
            long ithFib = this.mem.get(i-1) + this.mem.get(i-2);
            this.mem.put(i, ithFib);
        }

        return this.mem.get(input);
    }
}