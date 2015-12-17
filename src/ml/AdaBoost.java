package ml;

import java.util.ArrayList;

public class AdaBoost {
	
	
	
	public static Object[] trainClassifier(ArrayList<Vector> data, String label)
	{
		int maxDimension = data.get(0).getDimension();
		double[] D_Weights = new double[data.size()];
		int T = 300;
		ArrayList<Double> alpha = new ArrayList<Double>();
		ArrayList<Vector> h_List = new ArrayList<Vector>();
		
		//Initialize the error weights to be uniform
		for(int i = 0; i< data.size(); i++)
		{
			D_Weights[i] = 1.0/data.size();
		}
		

		for(int t = 0; t < T; t++)
		{
			double minError = -Math.log(0);
			Vector bestH = null;
			//Loop through each hypothesis and find the one with the minimum error. 
			for(int dim = 1; dim < maxDimension; dim++)
			{
				Vector h = weakClassifier(data, dim, 2, 0.01, 10, label);
				double error = computeError(data, h, D_Weights, label);
				if(error < minError)
				{
					bestH = h;
					minError = error;
				}
			}
			//If there is no best hypothesis (error is 0), then we're done looping.
			if(bestH == null)
			{
				break;
			}
			//Store the best hypothesis
			h_List.add(bestH);
			//Compute alpha (vote)
			alpha.add(0.5*Math.log((1-minError)/(minError)));
			double normalization = 0;
			//Update the weights of the training samples 
			for(int i = 0; i < data.size(); i++)
			{
				Vector x = data.get(i);
				D_Weights[i] = D_Weights[i]*Math.exp(-alpha.get(t)*x.getLabel(label)*h_List.get(t).transpose(x));
				normalization += D_Weights[i];
			}
			for(int i = 0; i < data.size(); i++)
			{
				D_Weights[i] /= normalization;
			}
		}
		
		Object[] ret = new Object[2];
		ret[0] = alpha;
		ret[1] = h_List;
		
		return ret;
				
	}
	
	public static double predict(Object[] adaboost, Vector example)
	{
		ArrayList<Double> a = (ArrayList<Double>)adaboost[0];
		ArrayList<Vector> h = (ArrayList<Vector>)adaboost[1];
		
		double sum = 0;
		for(int i = 0; i < a.size(); i++)
		{
			sum += a.get(i)*h.get(i).transpose(example);
		}
		
		return sum;
	}
	
	public static String predictLabel(ArrayList<Object[]> classifier, String[] labels, Vector example)
	{
		double max = Math.log(0);
		int argmax = 0;

		for(int i = 0; i < classifier.size(); i++)
		{
			double value = predict(classifier.get(i), example);
			if(value > max)
			{
				max = value;
				argmax = i;
			}
		}

		return labels[argmax];
	}
	
	private static double computeError(ArrayList<Vector> data, Vector h, double[] D, String label)
	{
		double error = 0;
		for(int i = 0; i<data.size(); i++)
		{
			Vector x = data.get(i);
			if(x.getLabel(label)*h.transpose(x) < 0)
				error += D[i];
		}
		
		return error;
	}
	
	
	//We know bias is the first element, so we don't mask that out.
	private static Vector weakClassifier(ArrayList<Vector> examples, int dimension, int epochT, double rate0, double C, String label)
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
				w.set(0, w.add(E.scale(-1*rate)).get(0));
				w.set(dimension, w.add(E.scale(-1*rate)).get(dimension));
				t++;				
			}
		}
		return w;
				
	}
	
	public static double LinearKernel(Vector w, Vector x, int power)
	{
		return Math.pow(w.transpose(x),power);
	}

}
