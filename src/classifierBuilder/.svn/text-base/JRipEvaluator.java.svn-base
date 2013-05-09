package classifierBuilder;
import java.util.Vector;

import weka.classifiers.rules.JRip;

public class JRipEvaluator extends ClassifierBuilder {
	int f;
	float n;
	int o;
	int s;
	Vector<Vector<Boolean>> trueClass = new Vector<Vector<Boolean>>();
	Vector<Vector<Boolean>> predictionClass = new Vector<Vector<Boolean>>();

	public JRipEvaluator() {
	}

	public void setClassiferParameters(int f, float n, int o, int s) {
		this.f = f;
		this.n = n;
		this.o = o;
		this.s = s;

	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(int f, float n, int o, int s) {
		setClassiferParameters(f, n, o, s);
		buildClassifiers();
	}

	@Override
	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: Folds:"
					+ f + " # Minimum Total Weight:" + n + " # Optimization:" + o + "# Seed:" + s);
			JRip jRip = new JRip();
			jRip.setFolds(f);
			jRip.setMinNo(n);
			jRip.setOptimizations(o);
			jRip.setSeed(s);
			jRip.buildClassifier(data);
			addClassifierToVector(jRip);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
