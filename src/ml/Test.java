package ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	public static void main(String[] args) 
	{
		String[] genres = {"blues", "classical", "country", "disco", "hiphop", "jazz", "metal", "pop", "reggae", "rock"};
		String[] genres4 = {"classical","jazz","metal","pop"};

		ArrayList<ArrayList<Vector>> data = new ArrayList<ArrayList<Vector>>();
		
		try {
			for(String genre : genres)
			{
				data.add(Util.readData(genre, true));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Vector> testing = Util.testSet(data); 
		
		ArrayList<Vector> training = new ArrayList<Vector>();
		for(ArrayList<Vector> list : data)
		{
			training.addAll(list);
		}
		
		boolean crossVal = false;
		
		//ArrayList<Vector> allT = Util.applyProductTransform(all);
		//ArrayList<Vector> testingT = Util.applyProductTransform(testing);
		
		runSVM(genres, crossVal, training, testing);
		
		

		
		
	}
	
	

	private static void runSVM(String[] genres, boolean crossVal, ArrayList<Vector> training, ArrayList<Vector> testing)
	{
		if(crossVal)
		{
			double[] C = {0.001, 0.01, 0.1, 1, 10};
			double[] Rho = {0.0001, 0.001, 0.01, 0.1, 1};
			
			double[][] best = new double[genres.length][2];
			
			for(int i = 0; i < genres.length; i++)
			{
				best[i] = SVM.crossValidation(training, 10, false, genres[i], C, Rho);
			}
			
			for(int i = 0; i < genres.length; i++)
			{
				System.out.println(genres[i]+ ": " + best[i][0] + " " + best[i][1]);
			}
		}
		else
		{
			double c = 1.0;
			double r = 0.1;
			
			ArrayList<Vector> classifiers = new ArrayList<Vector>();
			
			for(String genre : genres)
			{
				Vector weight = SVM.SGD(training, r, c, 20, genre);
				classifiers.add(weight);
				System.out.println(genre + " " + Util.collectAccuracy(testing, weight, genre));
			}
			
			
			for(Vector example : testing)
			{
				String label = Util.predictLabel(classifiers, genres, example);
				System.out.println("Actual: " + example.getStringLabel() + "\tPredict: " + label);
			}
		}
		
	}
	
	private static void runLogisticRegression(String[] genres, boolean crossVal, ArrayList<Vector> training, ArrayList<Vector> testing)
	{
		if(crossVal)
		{
			double[] Sigma = {10, 20, 50, 100, 200, 400};
			double[] Rho = {0.0001, 0.001, 0.01, 0.1, 1};
			
			double[][] best = new double[genres.length][2];
			
			for(int i = 0; i < genres.length; i++)
			{
				best[i] = LogisticRegression.crossValidation(training, 10, false, genres[i], Sigma, Rho);
			}
			
			for(int i = 0; i < genres.length; i++)
			{
				System.out.println(genres[i]+ ": " + best[i][0] + " " + best[i][1]);
			}
		}
		else
		{
			double s = 1;
			double r = 0.1;
			
			ArrayList<Vector> classifiers = new ArrayList<Vector>();
			
			for(String genre : genres)
			{
				Vector weight = LogisticRegression.SGD(training, r, s, 20, genre);
				classifiers.add(weight);
				System.out.println(genre + " " + Util.collectAccuracy(testing, weight, genre));
			}
			
			
			for(Vector example : testing)
			{
				String label = Util.predictLabel(classifiers, genres, example);
				System.out.println("Actual: " + example.getStringLabel() + "\tPredict: " + label);
			}
		}
	}
	
	private static void runPerceptron()
	{
		
	}
	
}
