#include <vector>
#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <boost/lexical_cast.hpp>
#include <boost/algorithm/string.hpp>

using namespace std;

int predict(vector<double> w, vector<double> x);

int main(int argc, char* argv [])
{
  if(argc != 2)
    cout << "Please provide a testing file" << endl;

  string in_w;

  vector<string> genres;
  vector<vector<double> > weights;

  ifstream test(argv[1]);

  while(cin >> in_w)
    {
      genres.push_back(in_w);
      ifstream input(in_w.c_str());
      string line;
      getline(input, line);
      vector<string> weight_str;
      boost::split(weight_str, line, boost::is_any_of(" "));
      vector<double> w;
      for(int i = 0; i < weight_str.size(); i++)
	{
	  string::size_type sz;
	  //	  cout << weight_str[i] << endl;
	  try
	    {
	      w.push_back(boost::lexical_cast<double>(weight_str[i]));
	    }
	  catch(const boost::bad_lexical_cast &)
	    {
	      break;
	    }
	}
      weights.push_back(w);
      input.close();
    }
  string line;
  int count = 0;
  int trash;
  getline(test, line);
  while(getline(test, line))
    {
      vector<string> splt;
      //cout << "LINE" << line << endl;
      boost::split(splt, line, boost::is_any_of(" "));
      vector<double> x;
      for(int i = 0; i < splt.size()-1; i++)
	{
	  try
	    {
	      x.push_back(boost::lexical_cast<double>(splt[i]));
	    }
	  catch(const boost::bad_lexical_cast &)
	    {
	      break;
	    }
	}
      string out_gen ("");
      for(int i = 0; i < weights.size(); i++)
	{
	  int prediction = predict(weights[i], x);
	  if(prediction == 1)
	    {
	      out_gen += genres[i] + " ";
	    }
	}
      cout << "File " << count << " -- " << out_gen << endl;
      count += 1;
    }
  //read in test file
  
}


int predict(vector<double> w, vector<double> x)
{
  double res = 0;

  for(int i = 0; i < w.size(); i++)
    res += w[i]*x[i];

  if(res < 0)
    return -1;
  else
    return 1;
}
