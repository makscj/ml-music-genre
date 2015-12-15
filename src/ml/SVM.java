package ml;

import ml.Vector;

import java.util.ArrayList;

public class SVM {


	/**
	 * Trains the classifier for Multiclass SVM, by using a label for One-vs-All training.
	 * @param examples - The examples to train on
	 * @param rate0 - the learning rate
	 * @param C - The C term
	 * @param epochT - The number of epochs
	 * @param label - The label to train for
	 * @return - Weight vector for the classifier.
	 */
	public static Vector SGD(ArrayList<Vector> examples, double rate0, double C, int epochT, String label)
	{
		double rate;
		int t = 0;
		Vector w = new Vector(examples.get(0).getDimension());
		
		for(int epoch = 0; epoch  < epochT; epoch++)
		{
			examples = Util.shuffle(examples);
			for(int i = 0; i < examples.size(); i++)
			{
				Vector x = examples.get(i);
				rate = rate0/(1 + (rate0*t)/C);
				Vector E = w;
				if(x.getLabel(label)*LinearKernel(w,x,1) <= 1)
					E = w.add(x.scale(-1*C*x.getLabel(label)));
				w = w.add(E.scale(-1*rate));
				t++;				
			}
		}
		return w;
	}
	
	public static double RBFKernel(Vector w, Vector x, double c)
	{
		double sum = 0;
		for(int i = 0; i < x.getDimension(); i++)
		{
			sum += Math.pow(w.get(i) - x.get(i), 2);
		}
		return Math.exp(-sum/c);
	}
	
	public static double LinearKernel(Vector w, Vector x, int power)
	{
		return Math.pow(w.transpose(x),power);
	}
	
	
	public static ArrayList<Vector> trainClassifiers(ArrayList<Vector> data, String[] labels, double rate, double C, int epoch)
	{
		ArrayList<Vector> classifiers = new ArrayList<Vector>();
		
		for(String label : labels)
		{
			classifiers.add(SGD(data, rate, C, epoch, label));
		}		

		return classifiers;
	}
	


	
	/**
	 * Perform cross validation on a data set
	 * @param data - The data set to test
	 * @param fold - The number of folds
	 * @param C - The C values to test
	 * @param Rho - The rho values to test
	 * @param verbose - If set to true, will print out all the accuracies for hyperparameters.
	 * @param label - The label for which to test crossvalidation on.
	 * @return
	 */
	public static double[] crossValidation(ArrayList<Vector> data, int fold, boolean verbose, String label, double[] C, double[] Rho)
	{
		
		ArrayList<ArrayList<Vector>> cross = Util.createCrossValidationSet(data, fold);
		
		int epoch = 10;
		
		//Max Accuracy   Best Rho         Best C
		double maxA = 0; double maxP = 0; double maxC = 0;
		
		//Loop through all the C hyperparameters
		for(double c : C)
		{
			//Loop through all the rho hyperparameters
			for(double rho : Rho)
			{
				double accuracy = 0;
				
				//Looping through sets chosen to stay out
				for(int i = 0; i < fold; i++)
				{					
					
					ArrayList<Vector> trainSet = new ArrayList<Vector>();
					//Collect the training sets
					for(int j = 0; j < fold; j++)
					{
						if(i == j) continue;//Skip the "testing set"
						trainSet.addAll(cross.get(j));
					}
					//Compute the weight vector for the training sets
					Vector w = SVM.SGD(trainSet, rho, c, epoch, label);
					//Compute accuracy of the testing set on the training sets
					accuracy += Util.collectAccuracy(cross.get(i), w, label);			
				}
				//Get the statistical accuracy
				accuracy /= fold;
				
				//Keep track of the best hyperparameters
				if(accuracy > maxA)
				{
					maxA = accuracy;
					maxC = c;
					maxP = rho;
				}
				if(verbose)
					System.out.println(accuracy + "\t" + c + "\t" + rho);
			}
		}
		if(verbose)
			System.out.println("--- BEST C: " + maxC + "\tBEST RHO: " + maxP + "\t WITH ACCURACY: " + maxA);
		//Return the best hyperparameters.
		double[] ret = {maxC, maxP};
		return ret;
	}
	
}

