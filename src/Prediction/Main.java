package Prediction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import algorithms.Algorithms;
import algorithms.ObjectReader;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import constants.Constants;

public class Main {
	private static Vector<Classifier> classifiers;
	private static Classifier mClassifier, sClassifier;
	private static Vector<ClassifierPrediction> classifierPredictionMusic = new Vector<ClassifierPrediction>();
	private static Vector<ClassifierPrediction> classifierPredictionSpeech = new Vector<ClassifierPrediction>();

	// ====================================================================================//
	// Main //
	// ====================================================================================//
	public static void main(String[] args) throws Exception {
		/*
		 * boolean isMusic = true; System.out
		 * .println("\n\n=================== Classifiers =============\n\n");
		 * classifiers = loadClassifierVector(isMusic); System.out
		 * .println("\n\n=================== Predictions =============\n\n");
		 * classifierPredictionMusic = loadPredictionVector(isMusic); System.out
		 * .println("\n\n=================== Music ===================\n\n");
		 * Instances[] musicTestData = loadTestData(isMusic,
		 * Constants.FILE_NAMES.length); evaluate(musicTestData, isMusic);
		 * savePredictionVector(classifierPredictionMusic, isMusic); isMusic =
		 * false; System.out
		 * .println("\n\n=================== Classifiers =============\n\n");
		 * classifiers = loadClassifierVector(isMusic); System.out
		 * .println("\n\n=================== Predictions =============\n\n");
		 * classifierPredictionMusic = loadPredictionVector(isMusic); System.out
		 * .println("\n\n=================== Speech ==================\n\n");
		 * 
		 * Instances[] speechTestData = loadTestData(isMusic,
		 * Constants.FILE_NAMES.length); evaluate(speechTestData, isMusic);
		 * savePredictionVector(classifierPredictionSpeech, isMusic);
		 */
		mClassifier = ObjectReader.loadObject("NewBestMusicClassifier.obj");
		sClassifier = ObjectReader.loadObject("NewBestSpeechClassifier.obj");
		evaluate2();
		savePredictionVector(classifierPredictionSpeech, false);
		savePredictionVector(classifierPredictionMusic, true);
	}

	private static void evaluate2() {
		ClassifierPrediction mClassifierPrediction = new ClassifierPrediction(mClassifier);
		ClassifierPrediction sClassifierPrediction = new ClassifierPrediction(sClassifier);
		for (int i = 0; i < Constants.FILE_NAMES.length; i++) {
			Vector<Boolean> mPredictionInstances = new Vector<Boolean>();
			Vector<Boolean> sPredictionInstances = new Vector<Boolean>();
			Instances musicTestData = Algorithms.getDataFromFile(Constants.MUSIC_FOLDER + Constants.FILE_NAMES[i]);
			Instances speechTestData = Algorithms.getDataFromFile(Constants.SPEECH_FOLDER + Constants.FILE_NAMES[i]);
			try {
				for (int j = 0; j < musicTestData.numInstances(); j++) {
					double[] pM = mClassifier.distributionForInstance(musicTestData.get(j));
					double[] pS = sClassifier.distributionForInstance(speechTestData.get(j));
					double pMpS = pM[1] * pS[1];
					double pMpNS = pM[1] * pS[0];
					double pMS = (pMpS + pMpNS) * 0.75 - pM[0];
					double pMNS = pMpNS - 0.5 * pMpS - 3 * pM[0];
					if (pMS < 0 && pMNS < 0) {
						mPredictionInstances.add(false);
						sPredictionInstances.add(pS[1] > pS[0] ? true : false);
					} else if (pMS > pMNS) {
						mPredictionInstances.add(true);
						sPredictionInstances.add(true);
					} else {
						mPredictionInstances.add(true);
						sPredictionInstances.add(false);
					}
				}

			} catch (Exception ex) {
				System.out.println("Prediction of the Instance problem!");
			}
			mClassifierPrediction.addPrediction(Constants.FILE_NAMES[i], mPredictionInstances);
			sClassifierPrediction.addPrediction(Constants.FILE_NAMES[i], sPredictionInstances);
			System.out.println("File " + Constants.FILE_NAMES[i] + " is finished...");
		}
		classifierPredictionMusic.add(mClassifierPrediction);
		classifierPredictionSpeech.add(sClassifierPrediction);
		System.out.println("Classifier: Finished...");
	}

	// ====================================================================================//
	// loadTestData //
	// ====================================================================================//
	public static Instances[] loadTestData(boolean isMusic, int num) {
		try {
			Instances[] res = new Instances[num];
			System.out.println("\n# Loading test data");
			for (int i = 0; i < num; i++) {
				String fileName = (isMusic ? Constants.MUSIC_FOLDER : Constants.SPEECH_FOLDER) + Constants.FILE_NAMES[i];
				DataSource source = new DataSource(fileName);
				res[i] = source.getDataSet();
				if (res[i].classIndex() == -1)
					res[i].setClassIndex(res[i].numAttributes() - 1);
				System.out.println("\n# " + fileName + " is loaded...");
			}
			return res;
		} catch (Exception e) {
		}
		return null;
	}

	// ====================================================================================//
	// loadData //
	// ====================================================================================//
	public static Instances loadData(String fileName) {
		try {
			System.out.println("\n# Loading data");
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
	// Evaluate //
	// ====================================================================================//
	public static void evaluate(Instances[] testData, boolean isMusic) {
		if (isMusic && classifierPredictionMusic == null)
			classifierPredictionMusic = new Vector<ClassifierPrediction>();
		else if (!isMusic && classifierPredictionSpeech == null)
			classifierPredictionSpeech = new Vector<ClassifierPrediction>();
		for (Classifier classifier : classifiers) {
			ClassifierPrediction classifierPrediction = new ClassifierPrediction(classifier);
			for (int i = 0; i < testData.length; i++) {
				Vector<Boolean> predictionInstances = new Vector<Boolean>();
				try {
					for (int j = 0; j < testData[i].numInstances(); j++) {
						predictionInstances.add(classifier.classifyInstance(testData[i].get(j)) == 1 ? true : false);
					}

				} catch (Exception ex) {
					System.out.println("Prediction of the Instance problem!");
				}
				classifierPrediction.addPrediction(Constants.FILE_NAMES[i], predictionInstances);
				System.out.println("File " + Constants.FILE_NAMES[i] + " is finished...");
			}
			if (isMusic)
				classifierPredictionMusic.add(classifierPrediction);
			else
				classifierPredictionSpeech.add(classifierPrediction);
			System.out.println("Classifier: " + classifier + "\n Finished...");
		}

	}

	// ====================================================================================//
	// LoadClassifierVector //
	// ====================================================================================//
	@SuppressWarnings({ "unchecked", "unused" })
	private static Vector<Classifier> loadClassifierVector(boolean isMusic) {
		Vector<Classifier> vec = new Vector<Classifier>();
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(isMusic ? "MusicClassifiersJRip.obj" : "SpeechClassifiersJRip.obj");
			in = new ObjectInputStream(fis);
			vec = (Vector<Classifier>) in.readObject();
			in.close();
			return vec;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	// ====================================================================================//
	// LoadPredictionVector //
	// ====================================================================================//
	@SuppressWarnings({ "unchecked", "unused" })
	private static Vector<ClassifierPrediction> loadPredictionVector(boolean isMusic) {
		Vector<ClassifierPrediction> vec = new Vector<ClassifierPrediction>();
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(isMusic ? "MusicPredictions.obj" : "SpeechPredictions.obj");
			in = new ObjectInputStream(fis);
			vec = (Vector<ClassifierPrediction>) in.readObject();
			in.close();
			return vec;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	private static void savePredictionVector(Vector<ClassifierPrediction> classifierPredictions, boolean isMusic) {
		String fileName = isMusic ? "MusicPredictions.obj" : "SpeechPredictions.obj";
		System.out.println("# Serializing prediction Object into " + fileName);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		// evaluations = readEvaluationsVector();
		if (classifierPredictions == null)
			classifierPredictions = new Vector<ClassifierPrediction>();
		// evaluations.add(new Pair<Evaluation, Classifier>(evaluation,
		// classifier));
		try {
			fos = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fos);
			out.writeObject(classifierPredictions);
			out.close();
			System.out.println("# Prediction Map Persisted with size: " + classifierPredictions.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
