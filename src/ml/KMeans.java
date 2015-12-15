package ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class KMeans {
	
	
	public static void main(String[] args)
	{
		double[][] testdata = new double[][] {{4,6},{6,7},{5,8},{3,0},{2,2},{3,3},{7,5},{8,6},{9,5}};
		//double[][] testdata = new double[][] {{4},{6},{500},{501},{499},{3},{-200},{-234},{-213}};
		ArrayList<Vector> data = new ArrayList<Vector>();
		for(int i = 0; i < testdata.length; i++)
		{
			Vector put = new Vector(testdata[i],0);
			data.add(put);
		}
		
		HashMap<Vector, Integer> labels = trainClassifier(data, 3);
		
		Iterator it = labels.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Vector, Integer> pair = (Entry<Vector, Integer>)it.next();
			Vector temp = pair.getKey();
			System.out.println(temp + "\t" + pair.getValue());
		}
	}
	
	public static HashMap<Vector, Integer> trainClassifier(ArrayList<Vector> data, int K)
	{
		HashMap<Vector, Integer> means = new HashMap<Vector, Integer>();
		int dimension = data.get(0).getDimension();
		ArrayList<Vector> mus = new ArrayList<Vector>();
		Random rand = new Random();
		
		for(int i = 0; i < data.size(); i++)
		{
			means.put(data.get(i), 0);
		}
		
		for(int i = 0; i < K; i++)
		{
			double[] build = new double[dimension];
			for(int j = 0; j < dimension; j++)
			{
				build[j] = rand.nextDouble();
			}
			mus.add(new Vector(build,0));
		}
		
		for(int epoch = 0; epoch < 10000; epoch++)
		{

			for(Vector x : data)
			{
				means.replace(x, argmin(x,mus));
			}
			double[][] new_mu = new double[mus.size()][mus.get(0).getDimension()];
			double[] mu_counter = new double[mus.size()];
			Iterator it = means.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<Vector, Integer> pair = (Entry<Vector, Integer>)it.next();
				Vector temp = pair.getKey();
				for(int i = 0; i < temp.getDimension(); i++)
				{
					new_mu[pair.getValue()][i] += temp.get(i);
				}
				mu_counter[pair.getValue()]++;
			}
			
			for(int i = 0; i < new_mu.length; i++)
			{
				for(int j = 0; j < new_mu[i].length; j++)
				{
					if(mu_counter[i] == 0) new_mu[i][j] = mus.get(i).get(j);
					else
						new_mu[i][j]/=mu_counter[i];
				}
			}
			
			for(int i = 0; i < mus.size(); i++)
			{
				mus.set(i, new Vector(new_mu[i], 0));
			}
		}
		
		return means;

	}
	
	public static int argmin(Vector x, ArrayList<Vector> mu)
	{
		double min = -Math.log(0);
		int arg = -1;
		for(int i = 0; i < mu.size(); i++)
		{
			double distance = 0;
			for(int j = 0; j < mu.get(i).getDimension(); j++)
			{
				distance += Math.pow(x.get(j) - mu.get(i).get(j),2);
			}
			if(distance < min)
			{
				min = distance;
				arg = i;
			}
		}
		
		return arg;
	}
	

}
