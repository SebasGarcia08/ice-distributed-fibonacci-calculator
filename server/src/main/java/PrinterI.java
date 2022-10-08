import java.util.HashMap;
import java.math.BigInteger;

public class PrinterI implements Demo.Printer
{
    final int MAX = 100000000;
    boolean printNumbers = false;
    BigInteger[] mem; 
    int size = 2; 

    public PrinterI() {
        this.mem = new BigInteger[MAX];
        this.mem[0] = BigInteger.ZERO;
        this.mem[1] = BigInteger.ONE;
        this.mem[2] = BigInteger.ONE;
    }

    public String printString(String s, com.zeroc.Ice.Current current)
    {
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];
        String response = "0";

        Integer value = this.isInteger(msg);

        if (value != null){
            if (value > 0L){
                BigInteger fib = this.fibonacci(value);
                response = String.valueOf(fib);
                if (this.printNumbers){
                    for (int i = 1; i <= value; i++)
                        System.out.print(this.mem[i] + " ");
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

    public Integer isInteger(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }

    public BigInteger fibonacci(Integer input){
        if(input <= this.size)
            return this.mem[input];

        for (int i = this.size + 1; i <= input; i++) {
            BigInteger fib = this.mem[i - 1].add(this.mem[i - 2]);
            this.mem[i] = fib;
        }

        this.size++;

        return this.mem[input];
    }
}