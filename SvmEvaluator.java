
import java.util.Random;

import weka.core.SelectedTag;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.Puk;
import weka.classifiers.Classifier;

public class SvmEvaluator extends Evaluator  {
  boolean buildLogisticModels;
  double c;
  boolean checksTurnedOff;
  boolean debug;
  double epsilon;
  String filterType;
  String kernel;
  int numFolds;
  int randomSeed;
  double toleranceParameters;
  
  //======================================================================//
  //                       SetClassiferParameters                         //
  //======================================================================//
  
  public void setClassiferParameters(boolean buildLogisticModels, double c, boolean checksTurnedOff, boolean debug,
		  double epsilon, String filterType, String kernel, int numFolds, int randomSeed, double toleranceParameters) {
	  
	  this.buildLogisticModels = buildLogisticModels;
	  this.c = c;
	  this.checksTurnedOff = checksTurnedOff ;
	  this.debug = debug;
	  this.epsilon = epsilon;
	  this.filterType = filterType;
	  this.kernel = kernel;
	  this.numFolds = numFolds;
	  this.randomSeed = randomSeed;
	  this.toleranceParameters = toleranceParameters;
	  
  }
  
  //======================================================================//
  //                  SetClassiferParameters + Evaluate                   //
  //======================================================================//
	
  public void setClassiferParametersAndEvaluate(boolean buildLogisticModels, double c, boolean checksTurnedOff, boolean debug,
		  double epsilon, String filterType, String kernel, int numFolds, int randomSeed, double toleranceParameters) {
	  
		setClassiferParameters(buildLogisticModels, c, checksTurnedOff, debug, epsilon, filterType, kernel, numFolds, randomSeed,
				toleranceParameters);
		evaluate();
  }
	
  //======================================================================//
  //                             Evaluate                                 //
  //======================================================================//
  
  public void evaluate() {
		try{
			System.out.println("\n1 Evaluating Classfier: buildLogisticModels:" + buildLogisticModels
					+ " # c:" + c
					+ " # checksTurnedOff:" + checksTurnedOff
					+ " # debug:" + debug
					+ " # epsilon:" + epsilon
					+ " # filterType:" + filterType
					+ " # kernel:" + kernel
					+ " # numFolds:" + numFolds
					+ " # randomSeed:" + randomSeed
					+ " # toleranceParameters:" + toleranceParameters );
			
            SMO smo = new SMO();
            smo.setBuildLogisticModels(buildLogisticModels);
            smo.setC(c);
            smo.setChecksTurnedOff(checksTurnedOff);
            smo.setDebug(debug);
            smo.setEpsilon(epsilon);
            
            SelectedTag st = null;
            
			if(filterType == "Normalize")
				st = new SelectedTag(smo.FILTER_NORMALIZE, smo.TAGS_FILTER);
			else if (filterType == "Standardize")
				st = new SelectedTag(smo.FILTER_STANDARDIZE, smo.TAGS_FILTER);
			else
				st = new SelectedTag(smo.FILTER_NONE,smo.TAGS_FILTER);
            
            smo.setFilterType(st);
            
            Kernel kl = null;
            
            if(kernel == "PolyKernel") 
            	kl = new PolyKernel();
            else if (kernel == "Puk") 
            	kl = new Puk();	
            
            smo.setKernel(kl);
            smo.setNumFolds(numFolds);
            smo.setRandomSeed(randomSeed);
            smo.setToleranceParameter(toleranceParameters);
            
			evaluation = new Evaluation(data);
			// Random(1) using seed = 1
			evaluation.crossValidateModel(smo, data, 10, new Random(1));
			flushPrdictedData(smo);
			flushData();
			System.out.println("\nEvaluation Finised ...");
		}catch(Exception e){}
	}
	
    //======================================================================//
	//                            FlushData                                 //
	//======================================================================//
	
	public void flushData() 
	{
		String [] data = new String[]{"buildLogisticModels:\t" + buildLogisticModels,
				"c:\t" + c,
				"checksTurnedOff:\t" + checksTurnedOff,
				"debug:\t" + debug,
				"epsilon:\t" + epsilon,
				"filterType:\t" + filterType,
				"kernel:\t" + kernel,
				"numFolds:\t" + numFolds,
				"randomSeed:\t" + randomSeed,
				"toleranceParameters:\t" + toleranceParameters };
		
		super.flushData(data);
	}
	
	//======================================================================//
	//                           FlushPrdictedData                          //
	//======================================================================//
	public void flushPrdictedData(SMO smo) {
		super.flushPrdictedData(smo);
	}
}
