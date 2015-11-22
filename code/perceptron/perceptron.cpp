#include <iostream>
#include <vector>
#include <string>
#include <cstdlib>
#include <fstream>
#include <stdexcept>


using namespace std;


vector<double> perceptron(vector<vector<double> > e, vector<double> ys, int dim, double rate);

vector<double> perceptron(vector<vector<double> > examples, vector<double> ys, vector<double> w, int dim, double rate);

vector<double> randomVector(int dim);

double dotProduct(vector<double> w, vector<double> x);

vector<double> vectorSum(vector<double> left, vector<double> right);


vector<double> vectorScale(double scalar, vector<double> v);

int main(int argc, char* argv[])
{
  //Either have the exFeat function read multiple things into one file and then stream that in, but keep the label for each example
  //Or create N files and pass the names in, then read those files in and continue to train the algorithm.
  
  //Read in data

  if(argc < 2 || argc > 3)
    {
      cout << "Please enter the attribute you want to train and optionally an existing weight vector" << endl;
      return -1;
    }
  if(argc == 3)
    {
      //Read weight file into weight vector
    }
  else
    {
      string label;
      double read;
      int dim;
      cin >> dim;
      int arr = 0;
  
      vector<vector<double> > examples;

      vector<double> x;
      vector<double> y;
      //cout << dim << endl;
      x.push_back(1);
  
      while(cin >> read)
	{
	  if(arr == dim - 1)
	    {
	      x.push_back(read);
	      examples.push_back(x);
	      arr++;
	      x.clear();
	      //beginning bias term
	      x.push_back(1);
	  
	    }
	  else if(arr == dim)
	    {
	      y.push_back(read);
	      arr = 0;
	    }
	  else
	    {
	      x.push_back(read);
	      arr++;
	    }
	}
      vector<double> weight;

      if(argc == 3)//Use weight file
	weight = perceptron(examples, y, dim, 1);
      else
	weight = perceptron(examples, y, dim, 1);
      string genre (argv[1]);
      string fname = genre + "_weight";
      ofstream outfile(fname.c_str());
  
      for(int i = 0; i < weight.size(); i++)
	outfile << weight[i] << " ";
      outfile.close();
    }
  
}

vector<double> perceptron(vector<vector<double> > examples, vector<double> ys, vector<double> w, int dim, double rate)
{
    
  //TODO epochs and shuffling

  for(int i = 0; i < examples.size(); i++)
    {
      if(ys[i]*dotProduct(w, examples[i]) <= 0)
	{
	  w = vectorSum(w, vectorScale(rate*ys[i], examples[i]));
	}
    }

  return w;
}

vector<double> perceptron(vector<vector<double> > examples, vector<double> ys, int dim, double rate)
{
  vector<double> w = randomVector(dim+1);

  return perceptron(examples, ys, w, dim, rate);

}

vector<double> vectorSum(vector<double> l, vector<double> r)
{

  if(l.size() != r.size())
    throw invalid_argument("vectors not equal length - vector sum");

  
  vector<double> result;

  for(int i = 0; i < l.size(); i++)
    {
      double s = l[i] + r[i];
      result.push_back(s);
    }

  return result;
}

vector<double> vectorScale(double scalar, vector<double> v)
{
  vector<double> result;

  for(int i = 0; i < v.size(); i++)
    {
      result.push_back(scalar*v[i]);
    }
  
  return result;
}

double dotProduct(vector<double> w, vector<double> x)
{
  if(w.size() != x.size())
    throw invalid_argument("vectors not equal length -- dot product");
  double product = 0;

  for(int i = 0; i < w.size(); i++)
    {
      product += w[i]*x[i];
    }
  
  return product;
}

vector<double> randomVector(int dim)
{
  vector<double> r;

  for(int i = 0; i < dim; i++)
    {
      r.push_back((double)rand()/(RAND_MAX));
    }
  return r;
}
