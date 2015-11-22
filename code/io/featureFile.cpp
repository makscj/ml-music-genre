#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <cstdlib>
#include <stdexcept>
#include <boost/algorithm/string.hpp>

using namespace std;


int main(int argc, char* argv[])
{

  char AT = '@';
  char COMMENT = '%';

  string label(argv[1]);


  int file_count = argc - 2;  

  vector<char*> files;

  ofstream output;
  string out_name = label + "_Features";
  output.open(out_name.c_str());

  int att_print = 0;
  
  for(int i = 2; i < argc; i++)
    {
      ifstream input;
      input.open(argv[i]);

      string line;

      int att_count = -1;

      string file_label;



      while(getline(input, line))
	{
	  if(line[0] == COMMENT)
	    {
	      continue;
	    }
	  if(line[0] == AT)
	    {
	      if(line[1] == 'a')
		{
		  vector<string> splt;
		  boost::split(splt, line, boost::is_any_of(" "));
		  if(splt[1].compare("output") == 0)
		    {
		      file_label = splt[2].substr(1,splt[2].length()-2);
		    }
		  else
		    {
		      att_count++;
		    }
		}
	      continue;
	    }

	  if(att_print == 0)
	    {
	      att_print = 1;
	      output << att_count << endl;
	    }
	  vector<string> example;
	  boost::split(example, line, boost::is_any_of(","));
	  for(int i = 0; i < example.size(); i++)
	    {
	      if(i == example.size() - 1 && i != 0)
		{
		  if(file_label.compare(label) == 0)
		    {
		      output << 1 << endl;
		    }
		  else
		    {
		      output << -1 << endl;
		    }
		}
	      else
		{
		  output << example[i] << " ";
		}
	    }//for each x in attribute
	}//End of while read line
      input.close();
    }//End of for i in files
  output.close();
  
}//End of main
