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
			examples = shuffle(examples);
			for(int i = 0; i < examples.size(); i++)
			{
				Vector x = examples.get(i);
				rate = rate0/(1 + (rate0*t)/C);
				Vector E = w;
				if(x.getLabel(label)*w.transpose(x) <= 1)
					E = w.add(x.scale(-1*C*x.getLabel(label)));
				w = w.add(E.scale(-1*rate));
				t++;				
			}
		}
		return w;
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
	 * Winner takes all prediction for One-vs-All training.
	 * @param classifier - The classifiers to compare to
	 * @param labels - The possible labels that each classifier can take
	 * @param example - The example to predict
	 * @return - The correct label for the prediction. The assumption is that the indices for labels and classifiers match.
	 */
	public static String predictLabel(ArrayList<Vector> classifier, String[] labels, Vector example)
	{
		double max = classifier.get(0).transpose(example);;
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
	
	/**
	 * Shuffles a given ArrayList of vectors. Used for cross validation.
	 * @param array
	 * @return
	 */
	private static ArrayList<Vector> shuffle(ArrayList<Vector> array)
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

	
}

