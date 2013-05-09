package data.test;

import java.util.Map;
import java.util.Vector;

import constants.Constants;
import weka.classifiers.Classifier;
import weka.core.Instances;
import algorithms.Algorithms;
import algorithms.ObjectReader;

public class MainTestData {
	public static void main(String[] args) {
		/*
		 * System.out.println("Training data for Music Classifier.\n");
		 * 
		 * Classifier musicClasifier = null; Classifier speechClassifier = null;
		 * try { System.out.println("Speech classifier\n"); Instances
		 * trainSpeech = Algorithms.getDataFromFile("speech_training" + "." +
		 * "arff"); speechClassifier =
		 * ObjectReader.loadObject("SpeechBestClassifier" + "." + "obj");
		 * speechClassifier.buildClassifier(trainSpeech);
		 * System.out.println("classifier built\n");
		 * ObjectWriter.writeObject(speechClassifier, "NewBestSpeechClassifier"
		 * + "." + "obj");
		 * 
		 * System.out.println("\nMusic Classifier\n"); Instances trainMusic =
		 * Algorithms.getDataFromFile("music_training" + "." + "arff");
		 * musicClasifier = ObjectReader.loadObject("MusicBestClassifier" + "."
		 * + "obj");
		 * 
		 * musicClasifier.buildClassifier(trainMusic);
		 * System.out.println("classifier built\n"); // evaluationBest = new
		 * Evaluation(train); ObjectWriter.writeObject(musicClasifier,
		 * "NewMusicBestClassifier.obj");
		 * System.out.println("Finished building...Geezus\n"); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace();
		 */
		double Profit = 0;
		Map<String, Vector<Boolean>> Musicfiles = ObjectReader.loadObject("MusicTrueClassificationMap.obj");
		Map<String, Vector<Boolean>> Speechfiles = ObjectReader.loadObject("SpeechTrueClassificationMap.obj");
		for (String key : Musicfiles.keySet()) {
			Vector<Boolean> musicPrediction = Musicfiles.get(key);
			Vector<Boolean> speechPrediction = Speechfiles.get(key);
			Profit += Algorithms.calculateProfit(musicPrediction, speechPrediction, musicPrediction, speechPrediction);
		}
		System.out.println(Profit);
		System.out.println("Loading music classifier...");
		Classifier musicClasifier = ObjectReader.loadObject("NewBestMusicClassifier.obj");
		System.out.println("Loading speech classifier...");
		Classifier speechClassifier = ObjectReader.loadObject("NewBestSpeechClassifier.obj");
		double maxProfit = 0;
		for (String testFile : Constants.TEST_FILE_NAMES) {
			Instances musicFile = Algorithms.getDataFromFile(Constants.TEST_MUSIC_FOLDER + testFile);
			Vector<Boolean> musicPrediction = Algorithms.neighborNegotiiation(
					Algorithms.neighborNegotiiation(Algorithms.predict(musicClasifier, musicFile), 3), 3);
			Algorithms.writeTestResults(musicPrediction, testFile, true);
			Instances speechFile = Algorithms.getDataFromFile(Constants.TEST_SPEECH_FOLDER + testFile);
			Vector<Boolean> speechPrediction = Algorithms.neighborNegotiiation(Algorithms.neighborNegotiiation(
					Algorithms.neighborNegotiiation(Algorithms.predict(speechClassifier, speechFile), 13), 13), 12);
			Algorithms.writeTestResults(speechPrediction, testFile, false);
			maxProfit += Algorithms.calculateProfit(musicPrediction, speechPrediction, musicPrediction, speechPrediction);
		}
		System.out.println(maxProfit);
	}
}
