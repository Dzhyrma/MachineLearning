package classifierBuilder;
import weka.classifiers.lazy.IBk;

public class IBKEvaluator extends ClassifierBuilder {
	int k;

	public IBKEvaluator() {
		
	}

	// ======================================================================//
	// SetClassiferParameters //
	// ======================================================================//

	public void setClassiferParameters(int k) {
		this.k = k;
	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(int k) {
		setClassiferParameters(k);
		buildClassifiers();
	}
	
	// ======================================================================//
	// Evaluate //
	// ======================================================================//

	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: K:" + k);
			IBk iBk = new IBk();
			iBk.setKNN(k);
			iBk.buildClassifier(data);
			addClassifierToVector(iBk);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
