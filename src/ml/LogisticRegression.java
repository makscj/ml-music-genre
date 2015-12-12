package ml;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 
 * @author Maks Cegielski-Johnson
 * Machine Learning - CS 5350
 * Fall 2015
 * 
 * Runs experiments on Logistic Regression using Stochastic Gradient Descent  
 */
public class LogisticRegression {



	/**
	 * Runs Stochastic Gradient Descent
	 * @param examples - the set of examples
	 * @param rate0 - the initial rate
	 * @param sigma - the Sigma2 hyperparameter
	 * @param epochT - the number of epochs
	 * @param w - an initial weight vector
	 * @param t - an initial time
	 * @return weight vector for the training set
	 */
	public static Vector SGD(ArrayList<Vector> examples, double rate0, double sigma, int epochT, String label)
	{
		double rate = rate0;
		int t = 0;
		Vector w = new Vector(examples.get(0).getDimension());
		//NegativeLogLikelihood = new ArrayList<Double>();
		for(int epoch = 0; epoch  < epochT; epoch++)
		{			
			for(int i = 0; i < examples.size(); i++)
			{				
				Vector x = examples.get(i);
				rate = rate0/(1 + (rate0*t)/200);
				Vector gradient = gradient(w, x, sigma, label);
				gradient = gradient.scale(-rate);
				w = w.add(gradient);
				t++;				
			}
		}
		return w;
	}



	public static Vector gradient(Vector w, Vector x, double sigma2, String label)
	{
		double y = x.getLabel(label);
		double trans = w.transpose(x);
		trans *= y;
		double bigTerm = 1/(Math.exp(trans) + 1);
		bigTerm *= -y;

		double[] toVec = new double[x.getDimension()];

		for(int i = 0; i < x.getDimension(); i++)
		{
			toVec[i] = bigTerm*x.get(i) + (2/Math.pow(sigma2, 2))*w.get(i);
		}

		return new Vector(toVec, 0);
	}



	/**
	 * Runs cross validation on a data file
	 * @param data - the data set for cross validation
	 * @param fold - the number of folds
	 * @param Sigma_2 - the set of C hyperparameters to test
	 * @param Rho - the set of Rho hyperparameters to test
	 * @return returns [bestC,bestRho]
	 * Also prints out the accuracy for each combination of c and rho values.
	 */
	public static double[] crossValidation(ArrayList<Vector> data, int fold, boolean verbose, String label, double[] Sigma, double[] Rho)
	{

		ArrayList<ArrayList<Vector>> cross = Util.createCrossValidationSet(data, fold);

		int epoch = 10;

		//Max Accuracy   Best Rho         Best C
		double maxA = 0; double maxP = 0; double maxS = 0;


		for(double sigma : Sigma)
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
					Vector w = SGD(trainSet, rho, sigma, epoch, label);
					//Compute accuracy of the testing set on the training sets
					accuracy += Util.collectAccuracy(cross.get(i), w, label);			
				}
				//Get the statistical accuracy
				accuracy /= fold;

				//Keep track of the best hyperparameters
				if(accuracy > maxA)
				{
					maxA = accuracy;
					maxS = sigma;
					maxP = rho;
				}
				if(verbose)
					System.out.println(accuracy + "\t" + sigma + "\t" + rho);
			}

		}

		System.out.println("\tBEST SIGMA: " + maxS + "\tBEST RHO: " + maxP + "\t WITH ACCURACY: " + maxA);
		double[] ret = {maxS, maxP};
		return ret;
	}

}
