package ml;

import java.util.ArrayList;
import java.util.Random;

public class GenerateData {

	
	public static void main(String[] args)
	{
		Random rand = new Random();
		double[][] coordinates = new double[200][3];
		ArrayList<Vector> data = new ArrayList<Vector>();
		for(int i = 0; i < 200; i++)
		{
			coordinates[i][0] = 1;
			coordinates[i][1] = rand.nextInt(200);
			coordinates[i][2] = rand.nextInt(200);
		}
		
		double[] w = {3, -7, 1};
		
		for(int i = 0; i < 200; i++)
		{
			double transpose = 0;
			for(int j = 0; i <3; i++)
			{
				transpose += w[j]*coordinates[i][j];
			}
			double label = Math.signum(transpose);
			data.add(new Vector(coordinates[i], (int)label));
		}
		
		
	}
	
}
