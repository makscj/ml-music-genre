package ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Development {

	
	
	public static void main(String[] args) throws IOException
	{
		ArrayList<Vector> data = readData();
		
		System.out.println(data);
		
		Vector w = SVM.SGD(data, 0.01, 50, 10, null);
		System.out.println(w);
		
		double norm = w.get(0) + w.get(1) + w.get(2);
		double[] toPrint = {w.get(0)/norm, w.get(1)/norm, w.get(2)/norm};
		double[] actual = {-3.0/(-3+5+1),5.0/(-3+5+1),1.0/(-3+5+1)};
		System.out.println(Arrays.toString(toPrint));
		System.out.println(Arrays.toString(actual));
		
		
		Util.collectAccuracy(data, w, null);
	}
	
	public static ArrayList<Vector> readData() throws IOException
	{
		ArrayList<Vector> data = new ArrayList<Vector>();

		
		BufferedReader br = new BufferedReader(new FileReader("./2data"));
		String line = br.readLine();
		while(line != null)
		{
			String[] tokens = line.split(" ");
			double[] nd = {1, Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])};
			Vector nv = new Vector(nd, Integer.parseInt(tokens[2]));
			data.add(nv);
			line = br.readLine();
		}	
		br.close();	
				
		return data;
	}
}
