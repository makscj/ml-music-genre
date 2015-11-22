package ml;

import java.util.ArrayList;

public class CrossValidation {

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
	public static double[] crossValidation(ArrayList<Vector> data, int fold, double[] C, double[] Rho, boolean verbose, String label)
	{
		int size = data.size();
		int bound = (int) (size/fold + Math.ceil((size%fold)/(fold*1.0)));
		ArrayList<ArrayList<Vector>> cross = new ArrayList<ArrayList<Vector>>(fold);
		int epoch = 10;
		
		//Partiton the data for cross-validation
		for(int i = 0; i < size;)
		{
			ArrayList<Vector> list = new ArrayList<Vector>();
			for(int j = i; j < i + bound; j++)
			{
				if(j == size)
					break;
				list.add(data.get(j));
			}
			i += bound;
			cross.add(list);
		}
		
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
					accuracy += collectAccuracy(cross.get(i), w, label);			
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
	
	/**
	 * Runs the ArrayList of test vectors against the weight vector and computes the accuracy. Label is used for multi-class classification. 
	 * @param test
	 * @param w
	 * @param label
	 * @return
	 */
	public static double collectAccuracy(ArrayList<Vector> test, Vector w, String label)
	{
		double accuracy = 0;
		double size = 0;
		
		for(Vector x : test)
		{
			double y = x.getLabel(label);
			if(y*w.transpose(x) > 0)
				accuracy++;
			size++;
		}
		
		return accuracy/size;
	}
}
