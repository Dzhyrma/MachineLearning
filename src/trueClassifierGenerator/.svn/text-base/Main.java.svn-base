package trueClassifierGenerator;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import algorithms.Algorithms;
import constants.*;

import weka.core.Instances;

public class Main {
	public static Map<String, Vector<Boolean>> musicTrueClassificationMap;
	public static Map<String, Vector<Boolean>> speechTrueClassificationMap;
	public static boolean isMusic;

	public static void main(String[] args) {
		musicTrueClassificationMap = new TreeMap<String, Vector<Boolean>>();
		speechTrueClassificationMap = new TreeMap<String, Vector<Boolean>>();
		generateTrueClassifier();
		flushMaps();
	}

	private static void flushMaps() {
		String musicFileName = "MusicTrueClassificationMap.obj";
		String speechFileName = "SpeechTrueClassificationMap.obj";
		System.out.println("Serializing Maps Object into " + musicFileName + " and " + speechFileName);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(musicFileName);
			out = new ObjectOutputStream(fos);

			out.writeObject(musicTrueClassificationMap);
			fos.close();
			out.close();

			System.out.println("Music Map Object Persisted with size: " + musicTrueClassificationMap.size() + "\n");

			fos = new FileOutputStream(speechFileName);
			out = new ObjectOutputStream(fos);

			out.writeObject(speechTrueClassificationMap);
			fos.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Vector<Boolean> getClassAttribute(Instances instances) {
		Vector<Boolean> retVec = new Vector<Boolean>();
		for (int i = 0; i < instances.numInstances(); i++) {
			double actual = instances.get(i).classValue();
			retVec.add(actual >= 1.0 ? true:false);
		}
		return retVec;
	}

	private static void generateTrueClassifier() {
		for (int i = 0; i < Constants.FILE_NAMES.length; i++) {
			String fileName =  Constants.FILE_NAMES[i];
			Instances ins = Algorithms.getDataFromFile(Constants.MUSIC_FOLDER + fileName);
			Vector<Boolean> classAttribute = getClassAttribute(ins);
			musicTrueClassificationMap.put(fileName, classAttribute);
			
			ins = Algorithms.getDataFromFile(Constants.SPEECH_FOLDER + fileName);
			classAttribute = getClassAttribute(ins);
			speechTrueClassificationMap.put(fileName, classAttribute);
		}
	}
}
