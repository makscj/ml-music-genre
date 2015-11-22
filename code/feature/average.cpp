#include <vector>
#include <iostream>
#include <string>


using namespace std;

int main()
{
  double N = 1;

  int dim;
  cin >> dim;

  double total [dim];
  int arr = 0;
  double read;
  while(cin >> read)
    {
      if(arr == dim-1)
	{
	  total[arr] += read;
	  arr = 0;
	  N++;
	}
      else
	{
	  total[arr] += read;
	  arr++;
	}
    }

  for(int i = 0; i < dim; i++)
    {
      total[i] = total[i]/N;
      cout << total[i] << " ";
    }

  
}
