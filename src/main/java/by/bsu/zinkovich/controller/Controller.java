package by.bsu.zinkovich.controller;


import by.bsu.zinkovich.generator.A5Generator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static by.bsu.zinkovich.util.ChartUtils.prepareChart;
import static org.apache.commons.math3.distribution.CauchyDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY;

public class Controller implements Initializable
{
    @FXML
    XYChart<Integer, Double> pascalChart;
    @FXML
    XYChart<Double, Double> gaussianChart;
    @FXML
    XYChart<Double, Double> studentChart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateValues();
    }

    public void generateValues() {

        RandomGenerator generator = new A5Generator();

        PascalDistribution pascalDistribution = new PascalDistribution(generator, 50, 0.5);
        NormalDistribution gaussianDistribution = new NormalDistribution(generator, 0, 1, DEFAULT_INVERSE_ABSOLUTE_ACCURACY );
        TDistribution studentDistribution = new TDistribution(generator, 10, TDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);

        prepareChart(pascalChart, pascalDistribution, 200);
        prepareChart(gaussianChart, gaussianDistribution, 200, 10);
        prepareChart(studentChart, studentDistribution, 200, 50);

        twoSeriesModelling(gaussianDistribution, studentDistribution);
    }

    private void twoSeriesModelling(NormalDistribution gaussianDistribution, TDistribution studentDistribution) {
        int U = 30000;
        int samplesCount = 300000;
        RealDistribution first = gaussianDistribution;
        RealDistribution second = studentDistribution;
        double[] firstSamples = first.sample(samplesCount);
        double[] secondSamples = second.sample(samplesCount);
        int count = samplesCount / U;

        KolmogorovSmirnovTest kolmogorovSmirnovTest = new KolmogorovSmirnovTest();
        double f = 0.0;
        double s = 0.0;
        for (int i = 0; i < U; i++) {
            double[] firstBlock = Arrays.copyOfRange(firstSamples, i * count, i * count + count);
            double[] secondBlock = Arrays.copyOfRange(secondSamples, i * count, i * count + count);
            boolean firstRejected = kolmogorovSmirnovTest.kolmogorovSmirnovTest(first, firstBlock, 0.037);
            boolean secondRejected = kolmogorovSmirnovTest.kolmogorovSmirnovTest(second, secondBlock, 0.037);
            f += firstRejected ? 0 : 1.0;
            s += secondRejected ? 0 : 1.0;
        }

        System.out.println("First Series: " + f/U);
        System.out.println("Second Series: " + s/U);
    }

}
