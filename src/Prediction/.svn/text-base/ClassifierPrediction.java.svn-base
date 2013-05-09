package Prediction;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import weka.classifiers.Classifier;

@SuppressWarnings("serial")
public class ClassifierPrediction implements Serializable {
	Map<String, Vector<Boolean>> predictions;
	Classifier classifier;

	public ClassifierPrediction(Classifier classifier) {
		predictions = new TreeMap<String, Vector<Boolean>>();
		this.classifier = classifier;
	}

	public void addPrediction(String name, Vector<Boolean> prediction) {
		if (predictions != null)
			predictions.put(name, prediction);
	}

	public Vector<Boolean> getPrediction(String name) {
		if (predictions != null && predictions.containsKey(name))
			return predictions.get(name);
		return null;
	}

	public Classifier getClassifier() {
		return classifier;
	}
}
