import java.util.HashMap;
import java.math.BigInteger;

public class PrinterI implements Demo.Printer
{
    boolean printNumbers = false;
    HashMap<Long, BigInteger> mem; 

    public PrinterI() {
        this.mem = new HashMap<Long, BigInteger>();
        BigInteger one = BigInteger.ONE;
        this.mem.put(1L, one);
        this.mem.put(2L, one);
    }

    public String printString(String s, com.zeroc.Ice.Current current)
    {
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];
        String response = "0";

        Long value = this.isLong(msg);

        if (value != null){
            if (value > 0L){
                BigInteger fib = this.fibonacci(value);
                response = String.valueOf(fib);
                if (this.printNumbers){
                    for (long i = 1; i <= value; i++)
                        System.out.print(this.mem.get(i) + " ");
                    System.out.println("");
                }
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

    public Long isLong(String s)
    {
        try
        {
            return Long.parseLong(s);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }

    public BigInteger fibonacci(long input){
        if(this.mem.containsKey(input))
            return this.mem.get(input);

        for (long i = this.mem.size() + 1; i <= input; i++) {
            BigInteger fib = this.mem.get(i - 1).add(this.mem.get(i - 2));
            this.mem.put(i, fib);
        }

        return this.mem.get(input);
    }
}