package ml;

import java.util.Arrays;

/**
 * 
 * @author Maks Cegielski-Johnson
 *
 * Implementation of a Vector class, used for Machine Learning. 
 * 
 * This class is particularly useful and unique because it contains terms for the bias and label.
 * 
 * The bias term goes in position 0 of the vector.
 * The vector class has a private variable to store the label 
 * 
 * V = [b, x1, x2, x3, ..., xn]
 */
public class Vector {
	
	/**
	 * Stores the values of the vector
	 */
	private double[] data;
	
	/**
	 * The dimension of the vector
	 */
	private int dimension;
	
	/**
	 * The label for the vector
	 */
	private int label;
	
	/**
	 * The String classifier for this label, used instead of integer.
	 */
	private String sLabel;
	
	/**
	 * Specific for NLP, stores the String that the vector represents.
	 */
	private String storage;
	
	/**
	 * Constructs an empty vector given a size, setting label to 0.
	 * @param size
	 */
	public Vector(int size)
	{
		data = new double[size];
		dimension = size;
		label = 0;
	}
	


	/**
	 * Constructs a vector given an array of the features and an integer label.
	 * @param example - array of double features
	 * @param label - integer label
	 */
	public Vector(double[] example, int label)
	{
		data = Arrays.copyOf(example, example.length);
		dimension = data.length;
		this.label = label;
	}
	
	/**
	 * Construct a vector using a String as the label, rather than an integer.
	 * @param example
	 * @param label
	 */
	public Vector(double[] example, String label)
	{
		data = Arrays.copyOf(example, example.length);
		dimension = data.length;
		this.sLabel = label;
	}
	
	/**
	 * Compare the label of the Vector to that of the "compare" string. Return 1 if same, -1 otherwise.
	 * @param compare
	 * @return
	 */
	public int getLabel(String compare)
	{
		if(compare == null)
		{
			return this.label;
		}
		if(compare.equals(sLabel))
			return 1;
		else
			return -1;
	}
	
	/**
	 * For NLP. Get the String that this vector represents.
	 * @return
	 */
	public String getStorage()
	{
		return storage;
	}
	
	/**
	 * For NLP. Store the String that this vector represents.
	 * @param store
	 */
	public void setStorage(String store)
	{
		storage = store;
	}
	
	/**
	 * Set the label
	 * @param label
	 */
	public void setLabel(int label)
	{
		this.label = label;
	}
		
	/**
	 * Fill the vector with the value (v).
	 * @param v
	 */
	public void fill(int v)
	{
		Arrays.fill(data, v);
	}
	
	/**
	 * Compute the transpose of this vector with another vector
	 * @param other
	 * @return
	 */
	public double transpose(Vector other)
	{
		double ret = 0;
		
		for(int i = 0; i < this.data.length; i++)
		{
			ret += other.data[i]*this.data[i];
		}
		return ret;
		
	}
	
	/**
	 * Get the label of the vector. 
	 * @return
	 */
	public double getLabel()
	{
		return label;
	}
	
	/**
	 * Get the String label
	 * @return
	 */
	public String getStringLabel()
	{
		return sLabel;
	}
	
	/**
	 * Return the dimension of the vector.
	 * @return
	 */
	public int getDimension()
	{
		return dimension;
	}
	
	/**
	 * Get the value of the vector at a given position
	 * @param position
	 * @return
	 */
	public double get(int position)
	{
		return data[position];
	}
	
	/**
	 * Set the value of the vector at a given integer position
	 * @param position
	 * @param value
	 */
	public void set(int position, int value)
	{
		data[position] = value;
	}
	
	/**
	 * Set the bias term of this vector.
	 * @param value
	 */
	public void setBias(int value)
	{
		data[0] = value;
	}
	
	/**
	 * Computes the vector sum of this vector and another vector.
	 * @param other
	 * @return
	 */
	public Vector add(Vector other)
	{
		double[] ret = new double[data.length];
		for(int i = 0; i < dimension; i++)
		{
			ret[i] = this.data[i]+other.data[i];
		}
		
		return new Vector(ret, 0);
	}
	
	/**
	 * Returns the scaled form of this Vector, given a scale term.
	 * @param value
	 * @return
	 */
	public Vector scale(double value)
	{
		double[] ret = data.clone();
		for(int i = 0; i < dimension; i++)
		{
			ret[i] *= value;
		}
		
		return new Vector(ret, this.label);
	}
	
	/**
	 * Compute the 2-norm/Euclidean distance of this vector to the origin. 
	 * @return
	 */
	public double distanceFromOrigin()
	{
		double dist = 0;
		
		for(int i = 0; i < dimension; i++)
		{
			dist += data[i]*data[i];
		}
		return Math.sqrt(dist);
	}
	
	
	public int getHammingDistance(Vector other)
	{
		int distance = 0;
		for(int i = 0; i < this.data.length; i++)
		{
			if(this.data[i] != other.data[i])
			{
				distance++;
			}
		}
		return distance;
	}
	
	/**
	 * Return the normalization of this vector
	 * @return
	 */
	public Vector normalize()
	{
		double total = 0;
		double[] ret = data.clone();
		for(int i = 0; i < dimension; i++)
		{
			total += data[i];
		}
		for(int i = 0; i < dimension; i++)
		{
			ret[i] /= total;
		}
		return new Vector(ret, this.label);
	}
	
	public String toString()
	{
		String res = "{"+data[0];
		for(int i = 1; i < dimension+1; i++)
		{
			if(i == data.length - 1)
			{
				res += " " + data[i] + "}";
				String label = sLabel;
				if(sLabel == null) label = ""+this.getLabel();
				return res + ":"+label;
			}
			else
			{
				res += " " + data[i];
			}
		}
		return res + ":" + this.getLabel();
	}
	

}
