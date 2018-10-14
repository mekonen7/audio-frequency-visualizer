package audio_analysis;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class AnalyzerUI extends Application {
    
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();


    private static int MIN_X, MAX_X, MIN_Y, MAX_Y;
    private byte[] data;

    private void init(Stage primaryStage) {
	
	MIN_X = 0;
	MAX_X = 22500;
	MIN_Y = 0;
	MAX_Y = 20000;
	
	
	NumberAxis xAxis = new NumberAxis();
	xAxis.setLabel("Freq.");
        xAxis.setTickLabelsVisible(true);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

	NumberAxis yAxis = new NumberAxis(MIN_Y, MAX_Y, MAX_Y/10);
	yAxis.setLabel("Power");


	final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

	lineChart.setCreateSymbols(false);
	lineChart.setAnimated(false);
	lineChart.setTitle("Frequency Spectrograph");
	//lineChart.setHorizontalGridLinesVisible(true);

	// Set Name for Series
	series1.setName("Amplitude line");

	// Add Chart Series
	lineChart.getData().add(series1);
	
	primaryStage.setScene(new Scene(lineChart,1000,500));
    }

    @Override
    public void start(Stage stage) {
	stage.setTitle("Frequency chart");
	init(stage);
	stage.show();

	AudioCapture.setFormat(44100, 8);
	AudioCapture.setUp();
	

	prepareTimeline();
    }


    private void prepareTimeline() {
	new AnimationTimer() {
	    @Override
	    public void handle(long now) {
		addDataToSeries();
	    }
	}.start();
    }

    
    
    private void addDataToSeries() {

	
	series1.getData().remove(0, series1.getData().size());
	
	data = AudioCapture.capture();

	double[] arr = FFT.fftReal(data);
	
	for (int i = 0; i < arr.length; i++) { 
	    series1.getData().add(new XYChart.Data<>(i*22050/(arr.length), arr[i]));	//Nyquist frequency 44100 -> 22050
	}
    }

    public static void main(String[] args) {
	launch(args);
    }
}