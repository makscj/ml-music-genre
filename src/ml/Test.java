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
		String[] genres4 = {"blues", "classical", "country", "disco", "hiphop", "jazz", "metal", "pop", "reggae", "rock"};
		String[] genres = {"classical","jazz","metal","pop"};

		ArrayList<ArrayList<Vector>> data = new ArrayList<ArrayList<Vector>>();
		
		try {
			for(String genre : genres)
			{
				data.add(readData(genre, true));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Vector> testing = testSet(data); 
		
		ArrayList<Vector> all = new ArrayList<Vector>();
		for(ArrayList<Vector> list : data)
		{
			all.addAll(list);
		}
		
		boolean crossVal = false;
		
		ArrayList<Vector> allT = applyProductTransform(all);
		ArrayList<Vector> testingT = applyProductTransform(testing);
		
		if(crossVal)
		{
			double[] C = {0.001, 0.01, 0.1, 1, 10};
			double[] Rho = {0.0001, 0.001, 0.01, 0.1, 1};
			
			double[][] best = new double[genres.length][2];
			
			for(int i = 0; i < genres.length; i++)
			{
				best[i] = CrossValidation.crossValidation(allT, 10, C, Rho, true, genres[i]);
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
				Vector weight = SVM.SGD(allT, r, c, 20, genre);
				classifiers.add(weight);
				System.out.println(genre + " " + CrossValidation.collectAccuracy(testingT, weight, genre));
			}
			
			
			for(Vector example : testingT)
			{
				String label = SVM.predictLabel(classifiers, genres, example);
				System.out.println("Actual: " + example.getStringLabel() + "\tPredict: " + label);
			}
		}
		

		
		
	}

	private static ArrayList<Vector> applyProductTransform(ArrayList<Vector> data)
	{
		int dimension = data.get(0).getDimension();
		ArrayList<Vector> transformations = new ArrayList<Vector>();
		for(Vector vector : data)
		{
			int counter = 0;
			double[] transform = new double[(dimension*(dimension-1))/2 + dimension + 1];
			for(int i = 0; i < dimension; i++)
			{
				for(int j = i; j < dimension; j++)
				{
					transform[counter++] = vector.get(i)*vector.get(j);
				}
			}
			transform[transform.length-1] = vector.getLabel();
			transformations.add(new Vector(transform, vector.getStringLabel()));
		}
				
		return transformations;
	}
	
	public static ArrayList<Vector> testSet(ArrayList<ArrayList<Vector>> data)
	{
		Random rng = new Random();
		ArrayList<Vector> ret = new ArrayList<Vector>();
		for(ArrayList<Vector> list : data)
		{
			for(int i = 0; i < 10; i++)
			{
				int j = rng.nextInt(list.size());
				Vector example = list.remove(j);
				ret.add(example);
			}
		}
		
		return ret;
	}
	
	public static ArrayList<Vector> readData(String genre, boolean simple) throws IOException
	{
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Vector> data = new ArrayList<Vector>();
		
		Pattern p = Pattern.compile("(@attribute|@relation|@data|%|^$)");

		
		BufferedReader br = new BufferedReader(new FileReader("./features/"+genre+".arff"));
		String line = br.readLine();
		while(line != null)
		{
			String[] split = line.split(" ");
			Matcher m = p.matcher(split[0]);
			if(m.find() || split[0].equals(""))
			{
				line = br.readLine();
				continue;
			}
			String[] input = split[0].split(",");
			double[] vector = new double[input.length-1];
			for(int i = 0; i < input.length-1; i++)
				vector[i] = Double.parseDouble(input[i]);
			Vector v = new Vector(vector, genre);
			data.add(v);
			line = br.readLine();
		}
		br.close();	
				
		return data;
	}
	
}
