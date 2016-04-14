package by.bsu.zinkovich.generator;


import org.apache.commons.math3.random.BitsStreamGenerator;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static by.bsu.zinkovich.util.BitsUtil.getBit;

public class A5Generator extends BitsStreamGenerator {

    private LFSRGenerator firstGenerator = new LFSRGenerator(19, 18, 17, 14, 1);
    private LFSRGenerator secondGenerator = new LFSRGenerator(22, 21, 1);
    private LFSRGenerator thirdGenerator = new LFSRGenerator(23, 22, 21, 8, 1);

    private int firstSyncBit = 9;
    private int secondSyncBit = 11;
    private int thirdSyncBit = 11;


    @Override
    public void setSeed(int seed) {
    }

    @Override
    public void setSeed(int[] seed) {
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    protected int next(int bits) {
        String binary = IntStream.range(0, bits).mapToObj(i -> String.valueOf(nextBit())).collect(Collectors.joining());
        return Integer.parseInt(binary, 2);
    }

    public int nextBit() {
        int first = 0;
        int second = 0;
        int third = 0;
        int f = calculateF();

        if(f == getBit(firstGenerator.getRegister(), firstSyncBit)) {
            first = firstGenerator.nextBit();
        }

        if(f == getBit(secondGenerator.getRegister(), secondSyncBit)) {
            second = secondGenerator.nextBit();
        }

        if(f == getBit(thirdGenerator.getRegister(), thirdSyncBit)) {
            third = thirdGenerator.nextBit();
        }

        return first ^ second ^ third;
    }

    private int calculateF() {
        return (getBit(firstGenerator.getRegister(), firstSyncBit) & getBit(secondGenerator.getRegister(), secondSyncBit)) |
               (getBit(firstGenerator.getRegister(), firstSyncBit) & getBit(thirdGenerator.getRegister(), thirdSyncBit)) |
               (getBit(secondGenerator.getRegister(), secondSyncBit) & getBit(thirdGenerator.getRegister(), thirdSyncBit));
    }
}
