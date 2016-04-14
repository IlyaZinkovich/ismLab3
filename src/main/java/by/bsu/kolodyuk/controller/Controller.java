package by.bsu.kolodyuk.controller;


import by.bsu.kolodyuk.generator.A5Generator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import java.net.URL;
import java.util.ResourceBundle;

import static by.bsu.kolodyuk.util.ChartUtils.prepareChart;
import static org.apache.commons.math3.distribution.CauchyDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY;

public class Controller implements Initializable
{
    @FXML
    XYChart<Integer, Double> pascalChart;
    @FXML
    XYChart<Double, Double> gaussianChart;
    @FXML
    XYChart<Double, Double> cauchyChart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateValues();
    }

    public void generateValues() {

        RandomGenerator generator = new A5Generator();

        PascalDistribution pascalDistribution = new PascalDistribution(generator, 50, 0.5);
        NormalDistribution gaussianDistribution = new NormalDistribution(generator, 0, 1, DEFAULT_INVERSE_ABSOLUTE_ACCURACY );
        CauchyDistribution cauchyDistribution = new CauchyDistribution(generator, 0, 1, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);

        prepareChart(pascalChart, pascalDistribution, 200);
        prepareChart(gaussianChart, gaussianDistribution, 200, 10);
        prepareChart(cauchyChart, cauchyDistribution, 200, 50);
    }

}
