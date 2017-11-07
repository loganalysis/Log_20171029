package cn.cas.cnic.log.fileread;
import java.util.Vector;

public class IdenticalWordRate {
	public static int counter1 = 0;
	public static int counter2 = 0;
	
	public enum matchMethod{
		LCS1, //第一种最长公共子序列匹配
		LCS2, //第二种最长公共子序列匹配
		OBO;  //一个词一个词的匹配
	}
	
	 /**
	  * 计算两个字符串直接的单词率
	  * @param s1
	  * @param s2
	  * @return
	  */
	 public static double getRate(Vector<String> a, Vector<String> b, matchMethod MM) {
//		 Vector<String> a = breakdown(s1);
//		 Vector<String> b = breakdown(s2);
		 Vector<String> v3 = null;
		 switch (MM) {
		 case LCS1:
			  v3 = getLCS1(a,b);
			 break;
		 case LCS2:
			 v3 = getLCS2(a,b);
			 break;
		 case OBO:
			 v3 = getOBO(a,b);
			 break;
		 }
		 
//		 System.out.println(v3.size());
		 
		 double rate = v3.size()*2.0/(a.size()+b.size()); 
		 return rate;
	 }
	
	private static Vector<String> getLCS2(Vector<String> a, Vector<String> b)
	 {
	  Vector<String> v1 = null;
	  Vector<String> v2 = null;
	  if (b.size() > a.size())
	  {
	   v1 = b;
	   v2 = a;
	  }
	  else
	  {
	   v1 = a;
	   v2 = b;
	  }
	  Vector<String> lcs = new Vector<String>();
	  int bi = 0;
	  int bj = 0;
	  int l = 1;
	  boolean found = false;
	  do {
	   l = 1;
	   int ci = 0;
	   int cj = 0;
	   found = false;
	   
	   counter2++;
	   if (v1.elementAt(bi).equals(v2.elementAt(bj)))
	   {
	    lcs.add(v1.elementAt(bi));
	    bi++;
	    bj++;
	    found = true;
	    continue;
	   }
	   else
	   {
	    while ((bi+bj+l < v1.size()+v2.size()) && !found)
	    {
	     ci = bi+l;
	     cj = bj;
	     if (ci >= v1.size())
	     {
	      ci = v1.size()-1;
	      cj = bj+(l+bi-v1.size()+1);
	     }
	     while (ci >= bi && ci < v1.size() && cj >= bj && cj < v2.size() && !found)
	     {
//	      System.out.println("("+ci+","+cj+") bi:"+bi+" bj:"+bj+" l:"+l);
	      counter2++;
	      if (v1.elementAt(ci).equals(v2.elementAt(cj)))
	      {
	       lcs.add(v1.elementAt(ci));
	       bi = ci+1;
	       bj = cj+1;
	       found = true;
	      }
	      else
	      {
	       ci--;
	       cj++;
	      }
	     }
	     l++;
	    }
	   }
	  } while (bi < v1.size() && bj < v2.size() && found);
	  return lcs;
	 }

	 private static Vector<String> getLCS1(Vector<String> v1, Vector<String> v2)
	 {
	  int[][] d = new int[v1.size()][v2.size()];
	  for (int i = 0; i < v1.size(); i++)
	   for (int j = 0; j < v2.size(); j++)
	   {
	    counter1++;
	    if (v1.elementAt(i).equals(v2.elementAt(j)))
	    {
	     if (i-1 < 0 || j-1 < 0)
	      d[i][j] = 1;
	     else
	      d[i][j] = d[i-1][j-1] + 1;
	    }
	    else
	    {
	     if (i-1 < 0 && j-1 < 0)
	      d[i][j] = 0;
	     else if (i-1 < 0)
	      d[i][j] = d[i][j-1];
	     else if (j-1 < 0)
	      d[i][j] = d[i-1][j];
	     else if (d[i][j-1] > d[i-1][j])
	      d[i][j] = d[i][j-1];
	     else
	      d[i][j] = d[i-1][j];
	    }
	   }
	  
	  Vector<String> result = new Vector<String>();
	  int ci = v1.size()-1;
	  int cj = v2.size()-1;
	  int c = d[ci][cj];
	  //if (c == 0)
	  // return result;
	  while (c > 0)
	  {
	   if (ci > 0 && cj > 0) // not at border
	   {
	    if (c > d[ci][cj-1] && c > d[ci-1][cj] && c > d[ci-1][cj-1]) // is featured
	    {
	     result.add(0,v1.elementAt(ci));
	     c--;
	     ci--;
	     cj--;
	    }
	    else if (c == d[ci-1][cj-1])
	    {
	     ci--;
	     cj--;
	    }
	    else if (c == d[ci-1][cj])
	    {
	     ci--;
	    }
	    else
	    {
	     cj--;
	    }
	   }
	   else if (ci > 0) // at left border
	   {
	    if (c > d[ci-1][cj])
	    {
	     result.add(0,v1.elementAt(ci));
	     c--;
	     ci--;
	    }
	    else
	    {
	     ci--;
	    }
	   }
	   else if (cj > 0) // at top border
	   {
	    if (c > d[ci][cj-1])
	    {
	     result.add(0,v1.elementAt(ci));
	     c--;
	     cj--;
	    }
	    else
	    {
	     cj--;
	    }
	   }
	   else // at top-left corner
	   {
	    result.add(0,v1.elementAt(ci));
	    c--;
	   }
	  }
	  return result;
	 }
	 /**
	  * 通过一个一个比较的方法得到共有的自串
	  * @param v1
	  * @param v2
	  * @return
	  */
	 private static Vector<String> getOBO(Vector<String> v1, Vector<String> v2){
		 Vector<String> result = new Vector<String>();
		 
		 if(v1.size() != v2.size())
			 return result;
		 
		 for(int i=0 ; i!=v1.size() ; ++i) {
//			 System.out.println(v1.get(i)+v2.get(i));
			 if(v1.get(i).equals(v2.get(i)))    //java不能用==  因为==比较的是地址！
				 result.add(v1.get(i));
		 }
//		 System.out.println(result);
		 return result;
	 }
	 
//	 private static Vector<String> breakdown(String str)
//	 {
//	  Vector<String> result = new Vector<String>();
//	  String[] strs = str.split(" ");
//	  for (String s : strs)
//	   result.add(s);
//	  return result;
//	 }
	 
	 
	 public static void main(String[] args) {
		
//		  Vector<String> a = breakdown("Invalid user admin from 195.154.33.138");
//		  Vector<String> b = breakdown("Failed password for invalid user admin from 195.154.33.138 port 52056 ssh2");
//		  Vector<String> v3 = getLCS1(a,b);
//		  System.out.println(""+v3.size()+" :: "+v3.toString());
//		  if (v3.size()*4 >= (a.size()+a.size()))
//		   System.out.println("same pattern");
//		  else
//		   System.out.println("different pattern");
//		  
//		  Vector<String> v4 = getLCS2(a,b);
//		  System.out.println(""+v4.size()+" :: "+v4.toString());
//		  
//		  System.out.println("counter1: "+counter1+" === counter2: "+counter2);
//		 System.out.println(getRate("a a a b", "a b c b", matchMethod.OBO));
		 
	}

}
