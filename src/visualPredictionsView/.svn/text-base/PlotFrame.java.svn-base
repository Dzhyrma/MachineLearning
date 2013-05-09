package visualPredictionsView;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class PlotFrame extends JFrame implements Runnable {
	private String chart1 = "True Class Classification Chart";
	private String chart2 = "Prediction Class Classification Chart";

	private XYSeries trueClassClassificationChart_Music;
	private XYSeries trueClassClassificationChart_Speech;
	private XYSeries predictionClassClassificationChart_Music;
	private XYSeries predictionClassClassificationChart_Speech;

	private Vector<Boolean> currentTrueMusic;
	private Vector<Boolean> currentTrueSpeech;

	private Vector<Boolean> currentPredictionMusic;
	private Vector<Boolean> currentPredictionSpeech;

	public PlotFrame(Vector<Boolean> currentTrueMusic, Vector<Boolean> currentTrueSpeech, Vector<Boolean> currentPredictionMusic,
			Vector<Boolean> currentPredictionSpeech, String fileName, String title) {

		super(title + " # " + fileName);
		this.currentTrueMusic = currentTrueMusic;
		this.currentTrueSpeech = currentTrueSpeech;
		this.currentPredictionMusic = currentPredictionMusic;
		this.currentPredictionSpeech = currentPredictionSpeech;
		setLayout(null);
		setLocation(0, 0);
		setSize(1000, 700);
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);
	}

	public PlotFrame(Vector<Boolean> currentTrueMusic, Vector<Boolean> currentTrueSpeech, Vector<Boolean> currentPredictionMusic,
			Vector<Boolean> currentPredictionSpeech, String fileName, String title, String chart1, String chart2) {

		super(title + " # " + fileName);
		this.currentTrueMusic = currentTrueMusic;
		this.currentTrueSpeech = currentTrueSpeech;
		this.currentPredictionMusic = currentPredictionMusic;
		this.currentPredictionSpeech = currentPredictionSpeech;
		this.chart1 = chart1;
		this.chart2 = chart2;
		setLayout(null);
		setLocation(0, 0);
		setSize(1000, 700);
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);
	}

	private void initChart() {
		// ========== True Class Classification Chart
		trueClassClassificationChart_Music = new XYSeries("Music");
		trueClassClassificationChart_Speech = new XYSeries("Speech");
		XYSeriesCollection trueCC = new XYSeriesCollection();
		trueCC.addSeries(trueClassClassificationChart_Music);
		trueCC.addSeries(trueClassClassificationChart_Speech);
		JFreeChart trueChart = ChartFactory.createXYAreaChart(chart1, "Instance Number", "Classificatoin", trueCC, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel trueChartPanel = new ChartPanel(trueChart);
		trueChartPanel.setLocation(20, 20);
		trueChartPanel.setSize(900, 300);
		getContentPane().add(trueChartPanel);

		// ========== Prediction Class Classification Chart
		predictionClassClassificationChart_Music = new XYSeries("Music");
		predictionClassClassificationChart_Speech = new XYSeries("Speech");
		XYSeriesCollection predCC = new XYSeriesCollection();
		predCC.addSeries(predictionClassClassificationChart_Music);
		predCC.addSeries(predictionClassClassificationChart_Speech);
		JFreeChart predChart = ChartFactory.createXYAreaChart(chart2, "Instance Number", "Classificatoin", predCC, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel predChartPanel = new ChartPanel(predChart);
		predChartPanel.setLocation(20, 330);
		predChartPanel.setSize(900, 300);
		getContentPane().add(predChartPanel);
	}

	private void plotCharts() {
		trueClassClassificationChart_Music.clear();
		trueClassClassificationChart_Speech.clear();
		predictionClassClassificationChart_Music.clear();
		predictionClassClassificationChart_Speech.clear();
		System.out.println("Plotting " + chart1 + " Values ...");
		for (int i = 0; i < currentTrueMusic.size(); i++) {
			trueClassClassificationChart_Music.add(i, currentTrueMusic.get(i) ? 1 : 0);
			predictionClassClassificationChart_Music.add(i, currentPredictionMusic.get(i) ? 1 : 0);
		}
		System.out.println("Plotting " + chart2 + " Values ...");
		for (int i = 0; i < currentTrueSpeech.size(); i++) {
			trueClassClassificationChart_Speech.add(i, currentTrueSpeech.get(i) ? 0.5 : 0);
			predictionClassClassificationChart_Speech.add(i, currentPredictionSpeech.get(i) ? 0.5 : 0);
		}
	}

	@Override
	public void run() {
		initChart();
		plotCharts();
		setVisible(true);
	}
}
