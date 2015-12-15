package ml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class parent extends JPanel{   
	
	static int width = 500;
	static int height = 500;
	static Graphics gr;
	
	public void paintComponent (Graphics g)     
	{
		super.paintComponent(g);
		gr = g;
		Graphics2D g2d  = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		Dimension size = getSize();
		java.awt.Insets  insets= getInsets();
		int w =  size.width - insets.left - insets.right;
		int h =  size.height - insets.top - insets.bottom;
		
		ArrayList<Vector> data = new ArrayList<Vector>();
//		try {
//			data = Development.readData();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Random rand = new Random();
		double[][] coordinates = new double[200][3];
		//ArrayList<Vector> data = new ArrayList<Vector>();
		for(int i = 0; i < 200; i++)
		{
			coordinates[i][0] = 1;
			coordinates[i][1] = rand.nextInt(width);
			coordinates[i][2] = rand.nextInt(height);
		}
		
		double[] we = {3, -1, 1};
		
		for(int i = 0; i < 200; i++)
		{
			double transpose = 0;
			for(int j = 0; j <3; j++)
			{
				transpose += we[j]*coordinates[i][j];
			}
			double label = Math.signum(transpose);
			data.add(new Vector(coordinates[i], (int)label));
		}
		int start = 0;
		int end = width;
		double func1 = (-we[0] - we[1]*start)/we[2];
		double func2 = (-we[0] - we[1]*end)/we[2];
		
		g2d.drawLine(start, (int)func1, end, (int)func2);
		
		Vector weight = LogisticRegression.SGD(data, 0.01, 600, 500, null);
		
		g2d.setColor(Color.RED);
		
		double func1w = (-weight.get(0) - weight.get(1)*start)/weight.get(2);
		double func2w = (-weight.get(0) - weight.get(1)*end)/weight.get(2);
		g2d.drawLine(start, (int)func1w, end, (int)func2w);
		for(Vector v : data)
		{
			double x = v.get(1);
			double y = v.get(2);
			if(v.getLabel() == -1)
			{
				g2d.setColor(Color.BLUE);
			}
			else
			{
				g2d.setColor(Color.RED);
			}
			g2d.fillOval((int)x, (int)y, 5, 5);
		}
	}



	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Points");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new parent());

		frame.setSize(width, height);
		frame.setVisible(true);
		

	}


	
} 