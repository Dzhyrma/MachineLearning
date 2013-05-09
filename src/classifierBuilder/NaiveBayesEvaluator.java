package classifierBuilder;
import weka.classifiers.bayes.NaiveBayes;

public class NaiveBayesEvaluator extends ClassifierBuilder {
	boolean kernel;
	boolean supervised;

	public NaiveBayesEvaluator() {
		
	}

	// ======================================================================//
	// SetClassiferParameters //
	// ======================================================================//

	public void setClassiferParameters(boolean kernel, boolean supervised) {
		this.kernel = kernel;
		this.supervised = supervised;
	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(boolean kernel, boolean supervised) {
		setClassiferParameters(kernel, supervised);
		buildClassifiers();
	}
	
	// ======================================================================//
	// Evaluate //
	// ======================================================================//

	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: Kernel:" + kernel + " Supervised:" + supervised);
			NaiveBayes naiveBayes = new NaiveBayes();
			naiveBayes.setUseKernelEstimator(kernel);
			naiveBayes.setUseSupervisedDiscretization(supervised);
			naiveBayes.buildClassifier(data);
			addClassifierToVector(naiveBayes);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
