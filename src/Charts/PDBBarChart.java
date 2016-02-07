package Charts;

import TertStructure.PDB3D.PDBNucleotide.PDBNucleotide;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

/**
 * Created by oliver on 07.02.16.
 */
public class PDBBarChart
{
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private ObservableList<XYChart.Series<String, Number>> ntData, ntDataNull;
    private BarChart<String, Number> ntBarChart;
    private Stage barChartStage;


    public PDBBarChart()
    {
        initAxes();
        setNullData();
        setChart();
        setStage();

    }
    private void initAxes()
    {
        this.xAxis = new CategoryAxis();
        xAxis.setLabel("Nucleotide");
        xAxis.getCategories().addAll("Adenosine", "Cytosine", "Guanosine", "Uridine");
        this.yAxis = new NumberAxis();
        yAxis.setLabel("Occurences");
    }
    private  void setData(int[] ntCountsOverall, int[] ntCountsBasePaired)
    {
        XYChart.Series<String, Number> overallCount = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> basePairedCount = new XYChart.Series<String, Number>();
        overallCount.setName("Overall");
        overallCount.getData().addAll(new XYChart.Data("Adenosine", ntCountsOverall[0]), new XYChart.Data<String, Number>("Cytosine", ntCountsOverall[1]), new XYChart.Data<String, Number>("Guanosine", ntCountsOverall[2]), new XYChart.Data<String, Number>("Uridine", ntCountsOverall[3]));
        basePairedCount.setName("Basepaired");
        basePairedCount.getData().addAll(new XYChart.Data("Adenosine", ntCountsBasePaired[0]), new XYChart.Data<String, Number>("Cytosine", ntCountsBasePaired[1]), new XYChart.Data<String, Number>("Guanosine", ntCountsBasePaired[2]), new XYChart.Data<String, Number>("Uridine", ntCountsBasePaired[3]));
        ntData =  FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        ntData.addAll(overallCount, basePairedCount);
    }

    private  void setNullData()
    {
        XYChart.Series<String, Number> overallCount = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> basePairedCount = new XYChart.Series<String, Number>();
        overallCount.setName("Overall");
        overallCount.getData().addAll(new XYChart.Data("Adenosine", 0), new XYChart.Data<String, Number>("Cytosine", 0), new XYChart.Data<String, Number>("Guanosine", 0), new XYChart.Data<String, Number>("Uridine", 0));
        basePairedCount.setName("Basepaired");
        basePairedCount.getData().addAll(new XYChart.Data("Adenosine", 0), new XYChart.Data<String, Number>("Cytosine", 0), new XYChart.Data<String, Number>("Guanosine", 0), new XYChart.Data<String, Number>("Uridine", 0));
        ntDataNull =  FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        ntDataNull.addAll(overallCount, basePairedCount);
    }
    private void setChart()
    {
        this.ntBarChart = new BarChart<>(xAxis,yAxis);
        ntBarChart.setTitle("Basepaired nucleotides");
        ntBarChart.setData(ntDataNull);

    }

    private void setStage()
    {
        Scene stackedBarChartScene = new Scene(ntBarChart,600,300);
        barChartStage = new Stage();
        barChartStage.setScene(stackedBarChartScene);
        stackedBarChartScene.getStylesheets().add("GUI/myStyle.css");

    }

    public Stage getBarChartStage() {
        return barChartStage;
    }
    public void updateData(int[] ntCountsOverall, int[] ntCountsBasepaired)
    {
        setData(ntCountsOverall, ntCountsBasepaired);
        ntBarChart.setData(ntData);

    }

}
