package classifierBuilder;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;

public class SVMEvaluator extends ClassifierBuilder {
	double e;
	double exp;
	boolean b;

	public SVMEvaluator() {
	}

	// ======================================================================//
	// SetClassiferParameters //
	// ======================================================================//

	public void setClassiferParameters(double e, double exp, boolean b) {
		this.e = e;
		this.exp = exp;
		this.b = b;
	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(double e, double exp, boolean b) {
		setClassiferParameters(e, exp, b);
		buildClassifiers();
	}

	// ======================================================================//
	// Evaluate //
	// ======================================================================//

	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: Epsilon:" + e + " #Build Logistic Model:" + b + " #Kernel Exponenet:" + exp);
			SMO svm = new SMO();
			svm.setEpsilon(e);
			PolyKernel kl = new PolyKernel();
			kl.setExponent(exp);
			svm.setKernel(kl);
			svm.setBuildLogisticModels(b);
			svm.buildClassifier(data);
			addClassifierToVector(svm);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
