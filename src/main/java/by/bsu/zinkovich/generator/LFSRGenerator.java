package by.bsu.zinkovich.generator;

public class LFSRGenerator {

    private long register = 1L;
    private int[] coefficients;

    public LFSRGenerator(int... coefficients) {
        this.coefficients = coefficients;
    }

    public int nextBit() {
        long temp = 0;
        for(int coefficient : coefficients) {
            temp ^= (register >> (coefficient - 1));
        }
        temp &= 1L;
        register = (temp << (coefficients[0] - 1)) | (register >> 1);

        return (int) (register & 1L);
    }


    public long getRegister() {
        return register;
    }
}
