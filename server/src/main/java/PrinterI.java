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
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];
        String response = "0";

        if (this.isInteger(msg)){
            long value = Long.parseLong(msg);
            if (value > 0){
                long fib = this.fibonacci(value);
                response = String.valueOf(fib);
                for (long i = 1; i <= value; i++){
                    System.out.print(this.mem.get(i) + " ");
                }
                System.out.println("");
            }
        } 
        else if(msg.equals("exit")){
            System.out.println(clientHostName + " left. \n");
            response = "Bye bye!";
        }
        else {
            response = "0";
            System.out.println(s);
        }

        return response;
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