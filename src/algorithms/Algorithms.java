package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import constants.Constants;

public class Algorithms {

	public static Vector<Boolean> neighborNegotiiation(Vector<Boolean> predictionVec, int numberOfNeighbors) {
		Vector<Boolean> predictionVecNew = new Vector<Boolean>();
		int predictedAsMusic = 0;
		for (int i = 0; i < numberOfNeighbors; i++)
			if (predictionVec.get(i))
				predictedAsMusic++;
		for (int i = 0; i < predictionVec.size(); i++) {
			predictedAsMusic += (i + numberOfNeighbors < predictionVec.size()) && predictionVec.get(i + numberOfNeighbors) ? 1 : 0;
			predictedAsMusic -= (i - numberOfNeighbors - 1 >= 0) && predictionVec.get(i - numberOfNeighbors - 1) ? 1 : 0;
			boolean isMusic = (Math.min(i + numberOfNeighbors, predictionVec.size()) - Math.max(0, i - numberOfNeighbors) + 1 - predictedAsMusic) < predictedAsMusic;
			predictionVecNew.add(isMusic);
		}
		return predictionVecNew;
	}

	public static double calculateProfit(Vector<Boolean> musicPredictionVec, Vector<Boolean> speechPredictionVec, Vector<Boolean> musicTrueClassificationVec,
			Vector<Boolean> speechTrueClassificationVec) {
		double res = 0;
		for (int i = 0; i < musicPredictionVec.size(); i++) {
			res += Constants.COSTS[musicTrueClassificationVec.get(i) ? ((!speechTrueClassificationVec.get(i)) ? 0 : 1) : 2][musicPredictionVec.get(i) ? (!speechPredictionVec
					.get(i) ? 0 : 1) : 2];
		}
		return res;
	}

	public static Instances getDataFromFile(String fileName) {
		try {
			System.out.println("\n# Loading data " + fileName);
			DataSource source = new DataSource(fileName);
			Instances data = source.getDataSet();
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			return data;
		} catch (Exception e) {
		}
		return null;
	}

	public static Vector<Boolean> predict(Classifier classifier, Instances file) {
		Vector<Boolean> predictionInstances = new Vector<Boolean>();
		try {
			for (int j = 0; j < file.numInstances(); j++) {
				predictionInstances.add(classifier.classifyInstance(file.get(j)) == 1 ? true : false);
			}

		} catch (Exception ex) {
			predictionInstances = null;
		}
		return predictionInstances;
	}

	public static void writeTestResults(Vector<Boolean> prediction, String testFileName, boolean isMusic) {
		FileWriter fos = null;
		BufferedWriter out = null;
		try {
			fos = new FileWriter((isMusic ? Constants.TEST_MUSIC_RESULT_FOLDER : Constants.TEST_SPEECH_RESULT_FOLDER) + testFileName.substring(0, testFileName.length() - 9) + (isMusic ? "_music" : "_speech") + ".pred");
			out = new BufferedWriter(fos);
			for (Boolean instance : prediction) {
				out.write(instance ? "1\n" : "0\n");
			}
			out.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
