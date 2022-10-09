import java.math.BigInteger;

public class PrinterI implements Demo.Printer
{

    boolean printNumbers = false;
    BigInteger[] mem; 
    IFibonacci fib = new FastFibonacci();

    public PrinterI() {
    }

    public String printString(String s, com.zeroc.Ice.Current current)
    {
        String[] splittedMsg = s.split(":");
        String clientHostName = splittedMsg[0];
        String msg = splittedMsg[1];
        String response = "0";

        Integer value = Utils.isInteger(msg);

        if (value != null){
            if (value > this.fib.maximum()){
                response = "The number is too big";
            } 
            else if (value < 0){
                response = "Cannot calculate fibonacci from negative numbers";
            } 
            else {
                BigInteger fib = this.fib.calculate(value);
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

}