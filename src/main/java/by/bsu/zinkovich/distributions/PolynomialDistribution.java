package by.bsu.zinkovich.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.BitsStreamGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PolynomialDistribution extends AbstractRealDistribution {

    private BitsStreamGenerator generator;
    private List<Double> coefficients;

    @Override
    public double density(double v) {
        return 0;
    }

    @Override
    public double cumulativeProbability(double v) {
        return 0;
    }

    @Override
    public double getNumericalMean() {
        return 0;
    }

    @Override
    public double getNumericalVariance() {
        return 0;
    }

    @Override
    public double getSupportLowerBound() {
        return 0;
    }

    @Override
    public double getSupportUpperBound() {
        return 0;
    }

    @Override
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportConnected() {
        return false;
    }

    @Override
    public double[] sample(int sampleSize) {
        int dim = coefficients.size();
        List<Double> generatedValues = Stream.generate(generator::nextDouble).limit(sampleSize * sampleSize).collect(Collectors.toList());
        List<Double> bins = Stream.generate(PolynomialDistribution::zero).limit(dim + 1).collect(Collectors.toList());
        for (int i = 0; i < dim; i++) {
            bins.set(i + 1, bins.get(i) + coefficients.get(i));
        }
        int count = generatedValues.size() / sampleSize;
        List<List<Double>> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            res.add(Stream.generate(PolynomialDistribution::zero).limit(dim).collect(Collectors.toList()));
        }
        for (int c = 0; c < count; c++) {

        }
//        for count in range(count):
//        block = seq[count*n:(count+1)*n]
//        res[count,] = np.histogram(block,bins=bins)[0]
        return super.sample(sampleSize);
    }

    public void setGenerator(BitsStreamGenerator generator) {
        this.generator = generator;
    }

    private static Double zero() {
        return (double) 0;
    }

    public void setCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
    }
}
