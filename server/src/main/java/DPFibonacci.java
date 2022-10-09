import java.math.BigInteger;

public class DPFibonacci implements IFibonacci {

    // Maxmimum size of the array
    final int MAX = 1000000000;
    BigInteger[] dp; 
    int size;

    public DPFibonacci() {
        this.dp = new BigInteger[MAX];
        this.dp[0] = BigInteger.ZERO;
        this.dp[1] = BigInteger.ONE;
        this.dp[2] = BigInteger.ONE;
    }

    public BigInteger calculate(Integer input){
        if(input <= this.size)
            return this.dp[input];

        for (int i = this.size + 1; i <= input; i++) {
            this.dp[i] = this.dp[i - 1].add(this.dp[i - 2]);
        }

        this.size++;

        return this.dp[input];
    }

    public int maximum(){
        return this.MAX;
    }

}