package ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {


	public static ArrayList<ArrayList<Vector>> createCrossValidationSet(ArrayList<Vector> data, int fold)
	{
		int size = data.size();
		int bound = (int) (size/fold + Math.ceil((size%fold)/(fold*1.0)));
		ArrayList<ArrayList<Vector>> cross = new ArrayList<ArrayList<Vector>>(fold);


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
		return cross;
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


	/**
	 * Shuffles the data set, which is an ArrayList of vectors.
	 * @param array
	 * @return
	 */
	public static ArrayList<Vector> shuffle(ArrayList<Vector> array)
	{
		int index = array.size()-1;
		while(index>1)
		{
			int randomIndex = (int)(Math.random()*index);
			Vector temp = array.get(index);
			array.set(index--,array.get(randomIndex));
			array.set(randomIndex,temp);
		}
		return array;
	}


	/**
	 * Winner takes all prediction for One-vs-All training.
	 * @param classifier - The classifiers to compare to
	 * @param labels - The possible labels that each classifier can take
	 * @param example - The example to predict
	 * @return - The correct label for the prediction. The assumption is that the indices for labels and classifiers match.
	 */
	public static String predictLabel(ArrayList<Vector> classifier, String[] labels, Vector example)
	{
		double max = Math.log(0);
		int argmax = 0;

		for(int i = 0; i < classifier.size(); i++)
		{
			double value = classifier.get(i).transpose(example);
			if(value > max)
			{
				max = value;
				argmax = i;
			}
		}

		return labels[argmax];
	}



	public static ArrayList<Vector> applyProductTransform(ArrayList<Vector> data)
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

	/**
	 * Goes through the data set and creates a test set of examples, removing each item from the data set that is added to the test set.
	 * @param data
	 * @return
	 */
	public static ArrayList<Vector> testSet(ArrayList<ArrayList<Vector>> data, int[] examples)
	{
		Random rng = new Random();
		ArrayList<Vector> ret = new ArrayList<Vector>();
		if(examples == null)
		{
			for(ArrayList<Vector> list : data)
			{
				for(int i = 0; i < 10; i++)
				{
					int j = rng.nextInt(list.size());
					Vector example = list.remove(j);
					ret.add(example);
				}
			}
		}
		else
		{
			for(ArrayList<Vector> list : data)
			{
				for(int i : examples)
				{
					Vector example = list.remove(i);
					ret.add(example);
				}
			}
		}
		return ret;
	}

	public static ArrayList<Vector> reduceSpace(ArrayList<Vector> data, ArrayList<Integer> ignore)
	{
		ArrayList<Vector> retData = new ArrayList<Vector>();

		for(Vector v : data)
		{
			double[] holder = new double[v.getDimension() - ignore.size()];
			int entry = 0;
			for(int i = 0; i < v.getDimension(); i++)
			{
				if(!ignore.contains(i))
				{
					continue;
				}
				else
				{
					holder[entry++] = v.get(i);
				}
			}
			retData.add(new Vector(holder, v.getStringLabel()));
		}

		return retData;
	}

	public static ArrayList<Vector> readData(String genre, boolean simple) throws IOException
	{
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Vector> data = new ArrayList<Vector>();

		Pattern p = Pattern.compile("(@attribute|@relation|@data|%|^$)");

		//Read the arff file for a specific genre
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
			double[] vector = new double[input.length];
			for(int i = 0; i < input.length-1; i++)
				vector[i] = Double.parseDouble(input[i]);
			//Adding the bias term
			vector[input.length-1] = 1;
			Vector v = new Vector(vector, genre);
			data.add(v);
			line = br.readLine();
		}
		br.close();	

		return data;
	}
}
