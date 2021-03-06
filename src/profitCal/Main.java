package profitCal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Vector;

import algorithms.Algorithms;

import classifierBuilder.Trace;

import weka.classifiers.Classifier;

import Prediction.ClassifierPrediction;
import constants.Constants;

public class Main {
	private static final int NEIGHBOR_NUMBER = 0;

	// **********************************************************************************
	// YOU NEED TO CHANGE THE NEXT NUMBER
	// **********************************************************************************
	// 0 - RandomForest, 1 - J48, 2 - NaiveBayes, 3 - JRip, 4 - SMO
	// private static final int NUMBER_OF_YOUR_ALGORITHM = 3;

	private static final String[] SUFIXES = new String[] { "Best" };
	// private static final String[] SUFIXES = new String[] {
	// "RandomForest","J48", "NaiveBayes", "JRip", "SMO" };

	private static int normalMusicSize, normalSpeechSize;

	public static Vector<ClassifierPrediction> musicClassifiersPredictionVector;
	public static Vector<ClassifierPrediction> speechClassifiersPredictionVector;
	public static Vector<MusicSpeechClassifierInfo> musicSpeechClassifiersProfit;

	public static Map<String, Vector<Boolean>> musicTrueClassificationMap;
	public static Map<String, Vector<Boolean>> speechTrueClassificationMap;

	// =============================== Main ===============================//
	public static void main(String[] args) {
		musicSpeechClassifiersProfit = new Vector<MusicSpeechClassifierInfo>();
		for (String s : SUFIXES) {
			System.out.println("\n################### CLASSIFIER " + s + " IS LOADING #################");
			loadPredictionVectors(s);
		}
		normalMusicSize = musicClassifiersPredictionVector.size();
		normalSpeechSize = speechClassifiersPredictionVector.size();
		System.out.println("\n################# TRUE CLASSIFICATION MAP IS LOADING #################");
		loadTrueClassificationMaps();
		System.out.println("\n################# FIND THE BEST CLASSIFIERS ##########################");
		makeNewPredictions();
		findBestClassifiersCombination();
	}

	private static void makeNewPredictions() {
		int size = musicClassifiersPredictionVector.size();
		for (int k1 = 1; k1 <= NEIGHBOR_NUMBER; k1++)
			for (int index = 0; index < size; index++) {
				ClassifierPrediction newMusicElement = new ClassifierPrediction(null);
				// ClassifierPrediction newMusicElement = new
				// ClassifierPrediction(musicClassifiersPredictionVector.get(index).getClassifier());
				for (String fileName : Constants.FILE_NAMES) {
					Vector<Boolean> musicPredictionVec = musicClassifiersPredictionVector.get(index).getPrediction(fileName);
					newMusicElement.addPrediction(fileName, Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(musicPredictionVec,3),3),k1));
				}
				musicClassifiersPredictionVector.add(newMusicElement);
			}
		size = speechClassifiersPredictionVector.size();
		for (int k1 = 1; k1 <= NEIGHBOR_NUMBER; k1++)
			for (int index = 0; index < size; index++) {
				// ClassifierPrediction newMusicElement = new
				// ClassifierPrediction(musicClassifiersPredictionVector.get(index).getClassifier());
				ClassifierPrediction newSpeechElement = new ClassifierPrediction(null);
				for (String fileName : Constants.FILE_NAMES) {
					Vector<Boolean> speechPredictionVec = speechClassifiersPredictionVector.get(index).getPrediction(fileName);
					newSpeechElement.addPrediction(fileName, Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(speechPredictionVec, 13), 13), 12), k1));
				}
				speechClassifiersPredictionVector.add(newSpeechElement);
			}
	}

	// ======================= findBestClassifiersCombination
	// ======================//
	private static void findBestClassifiersCombination() {
		MusicSpeechClassifierInfo max = new MusicSpeechClassifierInfo(-1, -1, 0, 0, 0);
		for (int musicIndex = 0; musicIndex < musicClassifiersPredictionVector.size(); musicIndex++)
			// int musicIndex = NUMBER_OF_YOUR_ALGORITHM;
			for (int speechIndex = 0; speechIndex < speechClassifiersPredictionVector.size(); speechIndex++) {
				ClassifierPrediction cpMusic = musicClassifiersPredictionVector.get(musicIndex);
				ClassifierPrediction cpSpeech = speechClassifiersPredictionVector.get(speechIndex);
				double bestProfit = Double.MIN_VALUE;

				// for (int i = 0; i < 10; i++) {
				// for (int j = 0; j < 10; j++) {

				double profit = 0;
				for (String fileName : Constants.FILE_NAMES) {
					Vector<Boolean> currentMusicPredictionVector = cpMusic.getPrediction(fileName);
					Vector<Boolean> currentSpeechPredictionVector = cpSpeech.getPrediction(fileName);
					profit += Algorithms.calculateProfit(currentMusicPredictionVector, currentSpeechPredictionVector, musicTrueClassificationMap.get(fileName),
							speechTrueClassificationMap.get(fileName));

				}
				if (profit > bestProfit) {
					// bestI = i;
					// bestJ = j;
					bestProfit = profit;
				}

				// }
				// }

				musicSpeechClassifiersProfit.add(new MusicSpeechClassifierInfo(musicIndex % normalMusicSize, speechIndex % normalSpeechSize, bestProfit,
						musicIndex / normalMusicSize, speechIndex / normalSpeechSize));

				
				//System.out.println("Music Classifier " + cpMusic.getClassifier().getClass());
				//System.out.println("Speech Classifier " + cpSpeech.getClassifier().getClass());
				System.out.println("==> Profit: " + bestProfit + "\n==> MusicClassifierIndex: "
						+ musicSpeechClassifiersProfit.lastElement().musicClassifierIndex + " #SpeechClassifierIndex: "
						+ musicSpeechClassifiersProfit.lastElement().speechClassifierIndex + "\n==> Music Neighbors: " + musicIndex / normalMusicSize
						+ " #Speech Neighbors: " + speechIndex / normalSpeechSize);
				System.out.println("\n#######################################################################");
				if (max.profit < bestProfit) {
					max = musicSpeechClassifiersProfit.lastElement();}
			}
		System.out.println("############################ MUSIC ####################################\n"
				+ musicClassifiersPredictionVector.get(max.musicClassifierIndex % normalMusicSize).getClassifier().getClass().toString());
		System.out.println("############################ SPEECH ###################################\n"
				+ speechClassifiersPredictionVector.get(max.speechClassifierIndex % normalSpeechSize).getClassifier().getClass().toString());
		System.out.println("Music:" + max.musicClassifierIndex + " Speech:" + max.speechClassifierIndex + " Profit:" + max.profit + "\nMusic Neighbors:"
				+ max.musicNeighbors + " Speech Neighbors:" + max.speechNeigbors);
		saveBestClassifiers(musicClassifiersPredictionVector.get(max.musicClassifierIndex).getClassifier(),
				speechClassifiersPredictionVector.get(max.speechClassifierIndex).getClassifier());
	}

	private static void saveBestClassifiers(Classifier classifier, Classifier classifier2) {
		String fileName = "MusicBestClassifier.obj";
		Trace.printSpace("Serializing Classifier Object into " + fileName);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fos);
			out.writeObject(classifier);
			out.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		fileName = "SpeechBestClassifier.obj";
		Trace.printSpace("Serializing Classifier Object into " + fileName);
		try {
			fos = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fos);
			out.writeObject(classifier2);
			out.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// =============================== loadTrueClassificationVectors
	// ===============================//
	@SuppressWarnings("unchecked")
	private static void loadTrueClassificationMaps() {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream("MusicTrueClassificationMap.obj");
			in = new ObjectInputStream(fis);
			musicTrueClassificationMap = (Map<String, Vector<Boolean>>) in.readObject();
			fis.close();
			in.close();

			fis = new FileInputStream("SpeechTrueClassificationMap.obj");
			in = new ObjectInputStream(fis);
			speechTrueClassificationMap = (Map<String, Vector<Boolean>>) in.readObject();
			fis.close();
			in.close();
		} catch (Exception e) {

		}
	}

	// =============================== loadPredictionVectors
	// ===============================//
	@SuppressWarnings("unchecked")
	private static void loadPredictionVectors(String s) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			// if (s == SUFIXES[NUMBER_OF_YOUR_ALGORITHM]) {
			fis = new FileInputStream("MusicPredictions" + s + ".obj");
			in = new ObjectInputStream(fis);
			if (musicClassifiersPredictionVector == null)
				musicClassifiersPredictionVector = (Vector<ClassifierPrediction>) in.readObject();
			else
				musicClassifiersPredictionVector.addAll((Vector<ClassifierPrediction>) in.readObject());
			fis.close();
			in.close();
			// }

			fis = new FileInputStream("SpeechPredictions" + s + ".obj");
			in = new ObjectInputStream(fis);
			if (speechClassifiersPredictionVector == null)
				speechClassifiersPredictionVector = (Vector<ClassifierPrediction>) in.readObject();
			else
				speechClassifiersPredictionVector.addAll((Vector<ClassifierPrediction>) in.readObject());
			fis.close();
			in.close();
		} catch (Exception e) {

		}
	}
}
