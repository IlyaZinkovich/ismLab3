package by.bsu.zinkovich.util;


import java.util.List;

import static java.lang.Math.pow;

public class MomentUtils
{
    private MomentUtils(){}

    public static long[] empiricalFrequency(List<Number> numbers) {
        long [] frequencies = new long[numbers.size()];
        for(int i = 0; i < frequencies.length; i++) {
            final int j = i;
            frequencies[i] = numbers.stream().filter(n -> n.equals(j)).count();
        }
        return frequencies;
    }

    public static long[] empiricalFrequency(List<Double> numbers, double length) {
        double step = length / numbers.size();
        long [] frequencies = new long[numbers.size()];
        for(int i = 0; i < frequencies.length; i++) {
            final int j = i;
            frequencies[i] = numbers.stream().filter(n -> n > -length / 2 + step * j && n <= -length/2 + step*(j+1)).count();
        }
        return frequencies;
    }

    public static double empiricalProbability(List<? extends Number> numbers, Number x) {
        return (double) (numbers.stream().mapToDouble(n -> n.doubleValue()).filter(n -> n <= x.doubleValue()).count()) / numbers.size();
    }

    public static double getRawMoment(List<? extends Number> numbers, int power) {
        return numbers.stream().mapToDouble(n -> pow(n.doubleValue(), power)).average().getAsDouble();
    }

    public static double getCentralMoment(List<? extends Number> numbers, int power) {
        double mean = getMean(numbers);
        return numbers.stream().mapToDouble(n -> pow(n.doubleValue() - mean, power)).average().getAsDouble();
    }

    public static double getMean(List<? extends Number> numbers) {
        return numbers.stream().mapToDouble(n -> n.doubleValue()).average().getAsDouble();
    }

    public static void printMoments(List<? extends Number> distribution, int count) {
        for(int i = 0; i < count; i++) {
            System.out.printf("Moment %d: %5.2f / %5.2f \n", (i + 1), getRawMoment(distribution, (i + 1)), getCentralMoment(distribution, (i + 1)));
        }
    }
}
