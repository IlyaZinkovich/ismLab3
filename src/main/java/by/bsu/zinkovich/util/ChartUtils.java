package by.bsu.zinkovich.util;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static by.bsu.zinkovich.util.MomentUtils.empiricalFrequency;
import static by.bsu.zinkovich.util.MomentUtils.empiricalProbability;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.toObject;

public class ChartUtils
{
    private ChartUtils() {}

    public static void prepareChart(XYChart<Integer, Double> chart, IntegerDistribution distribution, int n) {
        Series<Integer, Double> cumulativeSeries = new Series<>();
        cumulativeSeries.setName("Cumulative Distribution");
        IntStream.range(0, n).forEach(i -> cumulativeSeries.getData().add(new Data<>(i, distribution.cumulativeProbability(i))));
        chart.getData().add(cumulativeSeries);

        Series<Integer, Double> empiricalSeries = new Series<>();
        empiricalSeries.setName("Empirical Distribution");
        List<Integer> empiricalData = asList(toObject(distribution.sample(n)));
        IntStream.range(0, n).forEach(i -> empiricalSeries.getData().add(new Data<>(i, empiricalProbability(empiricalData, i))));
        chart.getData().add(empiricalSeries);

        ChiSquareTest chiSquareTest = new ChiSquareTest();
        long[] observed = empiricalFrequency(asList(toObject(distribution.sample(n))));
        double[] expected = IntStream.range(0, n).mapToDouble(i -> distribution.probability(i) * n).toArray();
        boolean rejected = chiSquareTest.chiSquareTest(expected, observed, 0.037);
        System.out.println(distribution.getClass().getSimpleName() + ": " + rejected);
    }

    public static void prepareChart(XYChart<Double, Double> chart, RealDistribution distribution, int n, double length) {
        double step = length / n;

        Series<Double, Double> cumulativeSeries = new Series<>();
        cumulativeSeries.setName("Cumulative Distribution");
        DoubleStream.iterate(-length/2, i -> i + step).limit(n).forEach(i -> cumulativeSeries.getData().add(new Data<>(i, distribution.cumulativeProbability(i))));
        chart.getData().add(cumulativeSeries);

        Series<Double, Double> empiricalSeries = new Series<>();
        empiricalSeries.setName("Empirical Distribution");
        List<Double> empiricalData = asList(toObject(distribution.sample(n)));
        DoubleStream.iterate(-length/2, i -> i + step).limit(n).forEach(i -> empiricalSeries.getData().add(new Data<>(i, empiricalProbability(empiricalData, i))));
        chart.getData().add(empiricalSeries);

        Series<Double, Double> densitySeries = new Series<>();
        densitySeries.setName("Probability Density");
        DoubleStream.iterate(-length/2, i -> i + step).limit(n).forEach(i -> densitySeries.getData().add(new Data<>(i, distribution.density(i))));
        chart.getData().add(densitySeries);

        ChiSquareTest chiSquareTest = new ChiSquareTest();
        long[] observed = empiricalFrequency(asList(toObject(distribution.sample(n))), length);
        double[] expected = IntStream.range(0, n).mapToDouble(i -> distribution.cumulativeProbability(-length / 2 + step * i, -length / 2 + step * (i + 1)) * n).toArray();
        boolean chiSquareRejected = chiSquareTest.chiSquareTest(expected, observed, 0.037);
        System.out.println(distribution.getClass().getSimpleName() + ": " + chiSquareRejected);

        KolmogorovSmirnovTest kolmogorovSmirnovTest = new KolmogorovSmirnovTest();
        boolean kolmogorovSmirnovRejected = kolmogorovSmirnovTest.kolmogorovSmirnovTest(distribution, distribution.sample(n), 0.037);
        System.out.println(distribution.getClass().getSimpleName() + ": " + kolmogorovSmirnovRejected);
    }
}
