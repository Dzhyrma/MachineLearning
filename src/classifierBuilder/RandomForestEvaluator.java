package classifierBuilder;
import weka.classifiers.trees.RandomForest;

public class RandomForestEvaluator extends ClassifierBuilder {
	int maxDepth;
	int numExecutionSlots;
	int numFeatures;
	int numTrees;
	int numSeed;

	public RandomForestEvaluator() {
		
	}

	// ======================================================================//
	// SetClassiferParameters //
	// ======================================================================//

	public void setClassiferParameters(int maxDepth, int numExecutionSlots,
			int numFeatures, int numTrees, int numSeed) {
		this.maxDepth = maxDepth;
		this.numExecutionSlots = numExecutionSlots;
		this.numFeatures = numFeatures;
		this.numTrees = numTrees;
		this.numSeed = numSeed;
	}

	// ======================================================================//
	// SetClassiferParameters + Evaluate //
	// ======================================================================//

	public void setClassiferParametersAndEvaluate(int maxDepth,
			int numExecutionSlots, int numFeatures, int numTrees, int numSeed) {
		setClassiferParameters(maxDepth, numExecutionSlots, numFeatures,
				numTrees, numSeed);
		buildClassifiers();
	}
	
	// ======================================================================//
	// Evaluate //
	// ======================================================================//

	public void buildClassifiers() {
		try {
			Trace.print("Buliding Classfier: MaxDepth:" + maxDepth
					+ " # numExecutionSlots:" + numExecutionSlots
					+ " # numFeatures:" + numFeatures + " # numTrees:"
					+ numTrees + " # numSeed:" + numSeed);
			RandomForest rf = new RandomForest();
			rf.setMaxDepth(maxDepth);
			rf.setNumExecutionSlots(numExecutionSlots);
			rf.setNumFeatures(numFeatures);
			rf.setNumTrees(numTrees);
			rf.setSeed(numSeed);;
			rf.buildClassifier(data);
			addClassifierToVector(rf);
			Trace.print("Classifier Built successfully ...\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
