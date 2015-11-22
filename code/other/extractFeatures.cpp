#include <iostream>
#include <string>
#include <boost/algorithm/string.hpp>
#include <vector>

int main()
{

  char ATTRIB = '@';
  char COMMENT = '%';
  std::string END_VECTOR = "./test";

  std::string line;

  std::string label;

  int att_count = -1;

  int print_count = 0;

  while(getline(std::cin, line))
    {
      if(line[0] == COMMENT)
	continue;
      if(line[0] == ATTRIB)
	{
	  if(line[1] == 'a')
	    {
	      std::vector<std::string> labels;
	      boost::split(labels, line, boost::is_any_of(" "));
	      if(labels[1].compare("output") == 0)
		label = labels[2].substr(1,labels[2].length()-2);
	      else
		att_count++;
	    }
	  continue;
	}  

      if(print_count == 0)
	{
	  std::cout << att_count << std::endl;
	  std::cout << label << std::endl;
	  print_count = 1;
	}
      std::vector<std::string> example;
      boost::split(example, line, boost::is_any_of(","));

      for(int i = 0; i < example.size()-1; i++)
	{
	  std::cout << example[i] << std::endl;
	}
      
    }
  
}
