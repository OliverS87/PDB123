package Charts;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;


/**
 * Created by oliver on 07.02.16.
 * Simple barchart to show the number of
 * nucleotides and base-paired nucleotides
 * from the loaded PDBfile.
 * An instance of this class is an empty
 * barchart plot. Counts are updated with
 * updateData().
 */
public class PDBBarChart {
    // Barchart: x-Axis is categoric and y-Axis is numerical
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    // Two observable lists: ntDataNull contains zeros for all 4 nucleotide count
    // values. ntData is filled with the actual data.
    // Initially, barchart's data source is ntDataNull until "actual" data is added.
    private ObservableList<XYChart.Series<String, Number>> ntData, ntDataNull;
    private BarChart<String, Number> ntBarChart;
    // Barchart is shown in a new window
    private Stage barChartStage;


    // Constructor
    public PDBBarChart() {
        initAxes();
        setNullData();
        setChart();
        setStage();
    }

    // Set Axes
    private void initAxes() {
        this.xAxis = new CategoryAxis();
        xAxis.setLabel("Nucleotide");
        xAxis.getCategories().addAll("Adenosine", "Cytosine", "Guanosine", "Uridine");
        this.yAxis = new NumberAxis();
        yAxis.setLabel("Occurences");
    }

    // Initialize barchart with xAxis and yAxis and ntDataNull as initial data source
    private void setChart() {
        this.ntBarChart = new BarChart<>(xAxis, yAxis);
        ntBarChart.setTitle("Nucleotide counts");
        ntBarChart.setData(ntDataNull);
    }

    // Set style of barchart window
    private void setStage() {
        Scene stackedBarChartScene = new Scene(ntBarChart, 600, 300);
        barChartStage = new Stage();
        barChartStage.setScene(stackedBarChartScene);
        stackedBarChartScene.getStylesheets().add("GUI/mainStyle.css");

    }

    // Setup inital data source ntDataNull
    private void setNullData() {
        XYChart.Series<String, Number> overallCount = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> basePairedCount = new XYChart.Series<String, Number>();
        overallCount.setName("Overall");
        overallCount.getData().addAll(new XYChart.Data("Adenosine", 0), new XYChart.Data<String, Number>("Cytosine", 0), new XYChart.Data<String, Number>("Guanosine", 0), new XYChart.Data<String, Number>("Uridine", 0));
        basePairedCount.setName("Basepaired");
        basePairedCount.getData().addAll(new XYChart.Data("Adenosine", 0), new XYChart.Data<String, Number>("Cytosine", 0), new XYChart.Data<String, Number>("Guanosine", 0), new XYChart.Data<String, Number>("Uridine", 0));
        ntDataNull = FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        ntDataNull.addAll(overallCount, basePairedCount);
    }

    // Set data source ntData with actual nucleotide counts
    private void setData(int[] ntCountsOverall, int[] ntCountsBasePaired) {
        XYChart.Series<String, Number> overallCount = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> basePairedCount = new XYChart.Series<String, Number>();
        overallCount.setName("Overall");
        overallCount.getData().addAll(new XYChart.Data("Adenosine", ntCountsOverall[0]), new XYChart.Data<String, Number>("Cytosine", ntCountsOverall[1]), new XYChart.Data<String, Number>("Guanosine", ntCountsOverall[2]), new XYChart.Data<String, Number>("Uridine", ntCountsOverall[3]));
        basePairedCount.setName("Basepaired");
        basePairedCount.getData().addAll(new XYChart.Data("Adenosine", ntCountsBasePaired[0]), new XYChart.Data<String, Number>("Cytosine", ntCountsBasePaired[1]), new XYChart.Data<String, Number>("Guanosine", ntCountsBasePaired[2]), new XYChart.Data<String, Number>("Uridine", ntCountsBasePaired[3]));
        ntData = FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        ntData.addAll(overallCount, basePairedCount);
    }

    // Return stage
    public Stage getBarChartStage() {
        return barChartStage;
    }

    // Update ntData with new nucleotide counts
    public void updateData(int[] ntCountsOverall, int[] ntCountsBasepaired) {
        setData(ntCountsOverall, ntCountsBasepaired);
        ntBarChart.setData(ntData);
    }

}
