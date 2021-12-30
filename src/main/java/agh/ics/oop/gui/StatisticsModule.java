package agh.ics.oop.gui;

import agh.ics.oop.observers.IOnEpochEndInvokeGUIObserver;
import agh.ics.oop.structures.EpochEndInfo;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class StatisticsModule implements IOnEpochEndInvokeGUIObserver {
    private final LineChart<Number, Number>
            animalsCountChart,
            grassCountChart,
            averageAnimalsEnergyChart,
            averageAnimalsAliveTimeChart,
            averageAnimalOffspringCountChart;
    private final XYChart.Series<Number, Number>
            animalsCountSeries,
            grassesCountSeries,
            averageAnimalsEnergySeries,
            averageAnimalsAliveTimeSeries,
            averageAnimalOffspringCountSeries;
    private final Label dominantGenesLabel;

    private final List<EpochEndInfo> epochEndInfoList;

    private LineChart<Number, Number> generateChart(
            XYChart.Series<Number, Number> series,
            String yAxisLabel, String chartTitle) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        xAxis.setForceZeroInRange(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        yAxis.setForceZeroInRange(false);
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(chartTitle);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);

        chart.getData().add(series);
        chart.setMaxHeight(200);
        return chart;
    }

    public StatisticsModule() {
        epochEndInfoList = new LinkedList<>();

        animalsCountSeries = new XYChart.Series<>();
        animalsCountChart = generateChart(animalsCountSeries, "Animals", "Animals alive");
        grassesCountSeries = new XYChart.Series<>();
        grassCountChart = generateChart(grassesCountSeries, "Grasses", "Grasses on map");
        averageAnimalsEnergySeries = new XYChart.Series<>();
        averageAnimalsEnergyChart = generateChart(averageAnimalsEnergySeries, "Energy", "Average animals energy");
        averageAnimalsAliveTimeSeries = new XYChart.Series<>();
        averageAnimalsAliveTimeChart = generateChart(averageAnimalsAliveTimeSeries, "Epochs", "Average animals life time");
        averageAnimalOffspringCountSeries = new XYChart.Series<>();
        averageAnimalOffspringCountChart = generateChart(averageAnimalOffspringCountSeries, "Offspring count", "Average animal offspring count");

        dominantGenesLabel = new Label();
    }

    private void addDataToChart (XYChart.Series<Number, Number> series, XYChart.Data<Number, Number> data) {
        var seriesData = series.getData();
        seriesData.add(data);
        if (seriesData.size() > 100)
            seriesData.remove(0);
    }

    @Override
    public void epochEnd(EpochEndInfo epochInfo) {
        epochEndInfoList.add(epochInfo);

        addDataToChart(animalsCountSeries, new XYChart.Data<>(epochEndInfoList.size(), epochInfo.animalsCount()));
        addDataToChart(grassesCountSeries, new XYChart.Data<>(epochEndInfoList.size(), epochInfo.grassCount()));
        addDataToChart(averageAnimalsEnergySeries, new XYChart.Data<>(epochEndInfoList.size(), epochInfo.averageAliveAnimalsEnergy()));
        addDataToChart(averageAnimalsAliveTimeSeries, new XYChart.Data<>(epochEndInfoList.size(), epochInfo.averageAnimalAliveTime()));
        addDataToChart(averageAnimalOffspringCountSeries, new XYChart.Data<>(epochEndInfoList.size(), epochInfo.averageAnimalOffspringCount()));

        dominantGenesLabel.setText(epochInfo.dominantGenomes().size() == 0 ? "No dominant genome." :
                "Dominant Genomes:" +
                        System.lineSeparator() + String.join(System.lineSeparator(), epochInfo.dominantGenomes()));
    }

    public Parent generateCharts() {
        VBox charts = new VBox(
                animalsCountChart,
                grassCountChart,
                averageAnimalsEnergyChart,
                averageAnimalsAliveTimeChart,
                averageAnimalOffspringCountChart,
                dominantGenesLabel
        );
        charts.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(charts);

        return scrollPane;
    }

    public void saveToFile(String fileName) {
        File csvFile = new File(fileName + ".csv");
        PrintWriter writer;
        try {
            writer = new PrintWriter(csvFile);
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "File [" + csvFile.getName() + "] not found.").showAndWait();
            return;
        }

        float
                animalsCountAverage = 0,
                grassCountAverage = 0,
                averageAliveAnimalsEnergyAverage = 0,
                averageAnimalAliveTimeAverage = 0,
                averageAnimalOffspringCountAverage = 0;

        for (EpochEndInfo info : epochEndInfoList) {
            animalsCountAverage += (float) info.animalsCount() / epochEndInfoList.size();
            grassCountAverage += (float) info.grassCount() / epochEndInfoList.size();
            averageAliveAnimalsEnergyAverage += info.averageAliveAnimalsEnergy() / epochEndInfoList.size();
            averageAnimalAliveTimeAverage += info.averageAnimalAliveTime() / epochEndInfoList.size();
            averageAnimalOffspringCountAverage += info.averageAnimalOffspringCount() / epochEndInfoList.size();

            writer.println(
                    info.animalsCount() + "," +
                    info.grassCount() + "," +
                    info.averageAliveAnimalsEnergy() + "," +
                    info.averageAnimalAliveTime() + "," +
                    info.averageAnimalOffspringCount());
        }
        writer.println(
                animalsCountAverage + "," +
                grassCountAverage + "," +
                averageAliveAnimalsEnergyAverage + "," +
                averageAnimalAliveTimeAverage + "," +
                averageAnimalOffspringCountAverage);
        writer.close();
    }
}
