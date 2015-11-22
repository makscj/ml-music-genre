package ml;

import java.util.ArrayList;
import java.util.Comparator;

public class NearestNeighbors {

	
	private ArrayList<Vector> data;
	private int K;
	
	
	public NearestNeighbors(ArrayList<Vector> training, int k)
	{
		data = new ArrayList<>(training);
		data.sort(new HammingCompare());
	}
	
	
	public class HammingCompare implements Comparator<Vector>
	{

		@Override
		public int compare(Vector o1, Vector o2) {
			
			int d1 = o1.getHammingDistance(o2);
			return 0;
			
		}
		
	}
	
}
