package classifierBuilder;
import java.util.Vector;

import weka.classifiers.trees.J48;

public class J48Evaluator extends ClassifierBuilder {

	float c;
	int m;
	Vector<Vector<Boolean>> trueClass = new Vector<Vector<Boolean>>();
	Vector<Vector<Boolean>> predictionClass = new Vector<Vector<Boolean>>();

	public J48Evaluator() {
	}

	public void setClassiferParameters(int m, float c) {
		this.m = m;
		this.c = c;

	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(int m, float c) {
		setClassiferParameters(m, c);
		buildClassifiers();
	}
	
	@Override
	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: Confidence Number:"
					+ c + " # Minimum Number of Objects:" + m);
			J48 j48 = new J48();
			j48.setMinNumObj(m);
			j48.setConfidenceFactor(c);
			j48.buildClassifier(data);
			addClassifierToVector(j48);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
