
import java.math.BigInteger;

class FastFibo {

    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger TWO = new BigInteger("2");
    static BigInteger f[];
    static int MAX = 1000000000;

    public static BigInteger karatsuba(BigInteger x, BigInteger y) {

        // cutoff to brute force
        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= 200000000) return x.multiply(y);                // optimize this parameter

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b,   y = c + 2^N d
        BigInteger b = x.shiftRight(N);
        BigInteger a = x.subtract(b.shiftLeft(N));
        BigInteger d = y.shiftRight(N);
        BigInteger c = y.subtract(d.shiftLeft(N));

        // compute sub-expressions
        BigInteger ac    = karatsuba(a, c);
        BigInteger bd    = karatsuba(b, d);
        BigInteger abcd  = karatsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }

   public static BigInteger mul(BigInteger a, BigInteger b) {
        return karatsuba(a, b);
    }

    public static BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public static BigInteger fib(int n) {
        BigInteger two = new BigInteger("2");


        // Base cases
        if (n == 0)
            return BigInteger.ZERO;
             
        if (n == 1 || n == 2) {
            f[n] = BigInteger.ONE;
            return f[n]; 
        }      

        // If fib(n) is already computed
        if (f[n] != null) {
//            System.out.println("already computed f[" + n + "] = " + f[n]);
            return f[n];
        }      

        int k = (n & 1) == 1? (n + 1) / 2
                            : n / 2;
      
        // Applying above formula [Note value
        // n&1 is 1 if n is odd, else 0.
        f[n] = (n & 1) == 1? add(mul(fib(k), fib(k)), mul(fib(k - 1), fib(k - 1)))
                       : mul(add(mul(two, fib(k-1)), fib(k)), fib(k));
        //System.out.println("f[" + n + "] = " + f[n]);
        return f[n];
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        f = new BigInteger[MAX];
        BigInteger result = fib(N);
    }
}
