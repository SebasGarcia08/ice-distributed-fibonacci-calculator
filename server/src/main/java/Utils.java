import java.math.BigInteger;

public class Utils {

    // If the size of any of the two digits is less than or equal to this
    // amount of bits, then Karatsuba's algorithm is not used. This is because
    // the recursive nature of the algorithm could cause a stack overflow.
    static final int CUTOFF_BITS = 10_000_000; // 1.25 mb, optimize this parameter

    public static BigInteger karatsuba(BigInteger x, BigInteger y) {

        // cutoff to brute force
        int N = Math.max(x.bitLength(), y.bitLength());
        // Use default multiply method when the number of bits is less than the cutoff
        if (N <= CUTOFF_BITS)
            return x.multiply(y); // optimize this parameter
        // System.out.println("karatsuba");

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b, y = c + 2^N d
        BigInteger b = x.shiftRight(N);
        BigInteger a = x.subtract(b.shiftLeft(N));
        BigInteger d = y.shiftRight(N);
        BigInteger c = y.subtract(d.shiftLeft(N));

        // compute sub-expressions
        BigInteger ac = karatsuba(a, c);
        BigInteger bd = karatsuba(b, d);
        BigInteger abcd = karatsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2 * N));
    }

    public static Integer isInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String parseHostname(String message) {
        String[] splittedMsg = message.split(":");
        if (splittedMsg.length > 1) {
            return splittedMsg[0].trim();
        }
        return null;
    }

    public static String parseMessage(String message) {
        String[] splittedMsg = message.split(":");
        if (splittedMsg.length > 1) {
            String parsedMessage = "";
            for (int i = 1; i < splittedMsg.length; i++) {
                parsedMessage += splittedMsg[i];
            }
            return parsedMessage.trim();
        }
        return null;
    }
}