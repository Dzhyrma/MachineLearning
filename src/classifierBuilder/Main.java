package classifierBuilder;

import classifierBuilder.IBKEvaluator;
import classifierBuilder.J48Evaluator;
import classifierBuilder.JRipEvaluator;
import classifierBuilder.NaiveBayesEvaluator;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	// ====================================================================================//
	// Main //
	// ====================================================================================//
	public static void main(String[] args) throws Exception {
		System.out.println("\n\n=================== Music ===================\n\n");
		Instances musicData = loadData("music1.arff");
		evaluateSMO(musicData, true);
		System.out.println("\n\n=================== Speech===================\n\n");
		Instances speechData = loadData("speech1.arff");
		evaluateSMO(speechData, false);
	}

	// ====================================================================================//
	// loadData //
	// ====================================================================================//
	public static Instances loadData(String fileName) {
		try {
			Trace.print("Loading data");
			DataSource source = new DataSource(fileName);
			Instances data = source.getDataSet();
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			return data;
		} catch (Exception e) {
		}
		return null;
	}

	// ====================================================================================//
	// evaluateRandomForest //
	// ====================================================================================//
	public static void evaluateRandomForest(Instances data, boolean isMusic) {
		RandomForestEvaluator rf = new RandomForestEvaluator();
		rf.setData(data, isMusic);
		int[] maxDepthArray = new int[] { 5, 25 };
		int[] numOfExecArray = new int[] { 5, 10, 15 };
		int[] numOfFeaturesArray = new int[] { 50, 150, 300 };
		int[] numOfTreesArray = new int[] { 5, 15, 35 };

		for (int i = 0; i < maxDepthArray.length; i++)
			for (int j = 0; j < numOfExecArray.length; j++) {
				for (int k = 0; k < numOfFeaturesArray.length; k++)
					for (int v = 0; v < numOfTreesArray.length; v++) {
						rf.setClassiferParametersAndEvaluate(maxDepthArray[i], numOfExecArray[j], numOfFeaturesArray[k], numOfTreesArray[v], 1);
					}
				rf.flushClassifierObjects();
			}
	}

	// ====================================================================================//
	// evaluateJ48 //
	// ====================================================================================//
	public static void evaluateJ48(Instances data, boolean isMusic) {
		J48Evaluator j48 = new J48Evaluator();
		j48.setData(data, isMusic);
		float[] cArray = new float[] { 0.05f, 0.1f, 0.25f, 0.5f };
		int[] mArray = new int[] { 2, 4, 8, 16, 32, 64 };

		for (int i = 0; i < cArray.length; i++) {
			for (int j = 0; j < mArray.length; j++) {

				j48.setClassiferParametersAndEvaluate(mArray[j], cArray[i]);

			}
			j48.flushClassifierObjects();
		}
	}

	// ====================================================================================//
	// evaluateIBk //
	// ====================================================================================//
	public static void evaluateIBk(Instances data, boolean isMusic) {
		IBKEvaluator iBk = new IBKEvaluator();
		iBk.setData(data, isMusic);
		int[] kArray = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };

		for (int i = 0; i < kArray.length; i++) {
			iBk.setClassiferParametersAndEvaluate(kArray[i]);

		}
		iBk.flushClassifierObjects();

	}

	// ====================================================================================//
	// evaluateNaiveBayes //
	// ====================================================================================//
	public static void evaluateNaiveBayes(Instances data, boolean isMusic) {
		NaiveBayesEvaluator naiveBayes = new NaiveBayesEvaluator();
		naiveBayes.setData(data, isMusic);
		naiveBayes.setClassiferParametersAndEvaluate(false, false);
		naiveBayes.setClassiferParametersAndEvaluate(true, false);
		naiveBayes.setClassiferParametersAndEvaluate(false, true);
		naiveBayes.flushClassifierObjects();
	}

	// ====================================================================================//
	// evaluateJRip //
	// ====================================================================================//
	public static void evaluateJRip(Instances data, boolean isMusic) {
		JRipEvaluator jRipEvaluator = new JRipEvaluator();
		jRipEvaluator.setData(data, isMusic);

		int[] foldsArray = new int[] { 3 };
		int[] minNoArray = new int[] { 1, 2, };
		int[] optimizationArray = new int[] { 2 };
		int[] seedArray = new int[] { 1 };

		for (int i = 0; i < foldsArray.length; i++)
			for (int j = 0; j < minNoArray.length; j++)
				for (int k = 0; k < optimizationArray.length; k++)
					for (int v = 0; v < seedArray.length; v++) {
						jRipEvaluator.setClassiferParametersAndEvaluate(foldsArray[i], minNoArray[j], optimizationArray[k], seedArray[v]);
					}
		jRipEvaluator.flushClassifierObjects();
	}

	// ====================================================================================//
	// evaluateSMO //
	// ====================================================================================//
	public static void evaluateSMO(Instances data, boolean isMusic) {
		SVMEvaluator sVMEvaluator = new SVMEvaluator();
		sVMEvaluator.setData(data, isMusic);

		double[] eArray = new double[] { 0.001 };
		double[] expArray = new double[] { 1, 2, 4};
		boolean[] bArray = new boolean[] { true, false};

		for (int i = 0; i < eArray.length; i++)
			for (int j = 0; j < expArray.length; j++)
				for (int k = 0; k < bArray.length; k++)
						sVMEvaluator.setClassiferParametersAndEvaluate(eArray[i], expArray[j], bArray[k]);
		sVMEvaluator.flushClassifierObjects();
	}
}
