package classifierBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Instances;

public abstract class ClassifierBuilder {
	Instances data;
	boolean isMusic;
	Vector<Classifier> tempVec = new Vector<Classifier>();

	/*******************************************************
	 * buildClassifiers
	 ******************************************************/
	public abstract void buildClassifiers();

	/*******************************************************
	 * setData
	 ******************************************************/
	public void setData(Instances data, boolean isMusic) {
		this.isMusic = isMusic;
		this.data = data;
	}
	/*******************************************************
	 * addClassifierToVector
	 ******************************************************/
	public void addClassifierToVector(Classifier classifier)
	{
		tempVec.add(classifier);
	}
	/*******************************************************
	 * flushClassifierObject
	 ******************************************************/
	public void flushClassifierObjects() {
		String fileName = isMusic ? "MusicClassifiers.obj"
				: "SpeechClassifiers.obj";
		Trace.printSpace("Serializing Classifier Object into " + fileName);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		Vector<Classifier> classifierVec = loadClassifierVector();
		if (classifierVec == null)
			classifierVec = new Vector<Classifier>();
		classifierVec.addAll(tempVec);
		tempVec.removeAllElements();
		try {
			fos = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fos);
			out.writeObject(classifierVec);
			out.close();
			fos.close();
			Trace.printSpace("Classifier Vector Persisted with size: "
					+ classifierVec.size() + "\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*******************************************************
	 * loadClassifierVector
	 ******************************************************/
	@SuppressWarnings("unchecked")
	private Vector<Classifier> loadClassifierVector() {
		Vector<Classifier> vec = new Vector<Classifier>();
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(isMusic ? "MusicClassifiers.obj"
					: "SpeechClassifiers.obj");
			in = new ObjectInputStream(fis);
			vec = (Vector<Classifier>) in.readObject();
			in.close();
			return vec;
		} catch (Exception e) {

		}
		return null;
	}
}
