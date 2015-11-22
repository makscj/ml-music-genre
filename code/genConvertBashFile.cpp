#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

int main()
{

  string str;

  while(getline(cin,str))
    {
      string orig = str;
      string::iterator end_pos = remove(str.begin(), str.end(), ' ');
      str.erase(end_pos, str.end());
      end_pos = remove(str.begin(), str.end(), '\'');
      str.erase(end_pos, str.end());
      string filename = str.substr(0,str.length()-4);
      cout << "mpg123 -w " << filename << ".wav " << orig << endl;
    }  
}
