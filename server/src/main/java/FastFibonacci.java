import java.math.BigInteger;

public class FastFibonacci implements IFibonacci {

    private BigInteger f[];
    private int MAX = 100_000_000;
    private final BigInteger TWO = new BigInteger("2");

    public FastFibonacci() {
        this.f = new BigInteger[MAX+1];
    }

    private BigInteger mul(BigInteger a, BigInteger b) {
        return Utils.karatsuba(a, b);
    }

    private BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public BigInteger calculate(Integer n) {

        // Base cases
        if (n == 0)
            return BigInteger.ZERO;
             
        if (n == 1 || n == 2) {
            f[n] = BigInteger.ONE;
            return f[n]; 
        }      

        // If fib(n) is already computed
        if (f[n] != null) {
            return f[n];
        }      

        int k = (n & 1) == 1? (n + 1) / 2
                            : n / 2;

        BigInteger fk = calculate(k);
        BigInteger fk1 = calculate(k - 1);
      
        // Applying above formula [Note value
        // n&1 is 1 if n is odd, else 0.
        f[n] = (n & 1) == 1? add(mul(fk, fk), mul(fk1, fk1))
                       : mul(add(mul(TWO, fk1), fk), fk);
        //System.out.println("f[" + n + "] = " + f[n]);
        return f[n];
    }

    public int maximum(){
        return this.MAX;
    }

    public static void main(String[] args) {
        IFibonacci fib = new FastFibonacci();
        int N = Integer.parseInt(args[0]);
        BigInteger result = fib.calculate(N);
    }

}
