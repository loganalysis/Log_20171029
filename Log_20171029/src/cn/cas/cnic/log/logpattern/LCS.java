package cn.cas.cnic.log.logpattern;

import java.util.Vector;
import java.util.HashMap;
import java.util.Collections;
import java.util.regex.Pattern;

public class LCS
{
 //private Vector<MatchPoint> mps = null;

 public static Vector<String> getLCS(Vector<String> v1, Vector<String> v2)
 {
  //找出所有词汇相同的交点及其深度
  Vector<MatchPoint> mps = new Vector<MatchPoint>();
  HashMap<String, Vector<Integer>> v1map = new HashMap<String, Vector<Integer>>();
  for (int i = 0; i < v1.size(); i++)
  {
   String str = v1.elementAt(i);
   Vector<Integer> vec = v1map.get(str);
   if (vec == null)
   {
    vec = new Vector<Integer>();
    vec.add(new Integer(i));
    v1map.put(str, vec);
   }
   else
   {
    vec.add(new Integer(i));
   }
  }
  for (int i = 0; i < v2.size(); i++)
  {
   String str = v2.elementAt(i);
   Vector<Integer> vec = v1map.get(str);
   if (vec != null)
   {
    for (Integer x : vec)
    {
     mps.add(new MatchPoint(x.intValue(), i));
    }
   }
  }
  if (mps.size() < 1)
   return new Vector<String>();
  
  //将所有交点按深度排序
  Collections.sort(mps);
  //System.out.println(mps);
  
  //计算每个结点的LCS深度
  int lcslength = 1;
  mps.elementAt(mps.size()-1).setSeqLen(1);
  int maxLCSlength = 1;
  MatchPoint headPoint = mps.elementAt(mps.size()-1);
  for (int i = mps.size()-2; i >= 0; i--)
  {
   MatchPoint mp = mps.elementAt(i);
   int currentPostDepth = mp.getDepth();
   int currentMaxPost = 0;
   for (int j = i; j < mps.size(); j++)
   {
    MatchPoint posterity = mps.elementAt(j);
    if (posterity.getX() > mp.getX() && posterity.getY() > mp.getY() && posterity.getDepth() >= currentPostDepth)
    {
     //System.out.println("Posterity "+posterity+"("+posterity.getDepth()+") of point "+mp+"("+mp.getDepth()+")");
     currentPostDepth = posterity.getDepth();
     if (posterity.getSeqLen() > currentMaxPost)
     {
      currentMaxPost = posterity.getSeqLen();
      mp.setNext(posterity);
      mp.setSeqLen(currentMaxPost+1);
      //System.out.println("Found better posterity "+posterity+" for point "+mp+" with SeqLen "+posterity.getSeqLen());
      if (mp.getSeqLen() > maxLCSlength)
      {
       maxLCSlength = mp.getSeqLen();
       headPoint = mp;
       //System.out.println("Updated headPoint "+mp+" with LCSlength "+maxLCSlength);
      }
     }
    }
   }
  }
  
  //生成LCS
  Vector<String> lcs = new Vector<String>();
  
 
  
  MatchPoint currentMP = headPoint;
  while (currentMP != null)
  {
   lcs.add(v1.elementAt(currentMP.getX()));
   currentMP = currentMP.getNext();
  }
  
     return lcs;
 }
 
 
 
public static Vector<String> breakdown(String str)
{
	  Vector<String> result = new Vector<String>();
	  String[] strs = str.split(" ");
	  for (String s : strs)
	  {
		   boolean eqsign = false;
		   String[] attr = s.split("=");
		   if (attr.length == 2)
		   {
			   if (attr[0].length() > 0 && attr[1].length() > 0)
			   {
				   	result.add(attr[0]+"=");
				   	eqsign = true;
			   }
		   }	
		   if (!eqsign)
		   {
			   if (Pattern.matches(WordConvertPatterns.PATTERN_NUM,s))
				   	result.add("<NUMBER>");
			   else if (Pattern.matches(WordConvertPatterns.PATTERN_DIGITNUM,s))
					result.add("<DECIMAL>");
			   else if (Pattern.matches(WordConvertPatterns.PATTERN_IP,s))
					result.add("<IP>");
			   else if (Pattern.matches(WordConvertPatterns.PATTERN_MEMADDR,s))
					result.add("<MEMADDR>");
			   else if (Pattern.matches(WordConvertPatterns.PATTERN_HEXNUM,s))
					result.add("<HEXNUM>");
			   else
					result.add(s);
		   }
	  }
	  return result;
}
 
 public static void main(String[] args)
 {
  //Vector<String> a = breakdown("057851A090C: from=<root@s1.hr.scgrid.cn>, size=671, nrcpt=1 (queue active)");
  //Vector<String> b = breakdown("057851A090C: removed");
  Vector<String> a = breakdown("Invalid user admin from 195.154.33.138");
  Vector<String> b = breakdown("Failed password for invalid user admin from 195.154.33.138 port 52056 ssh2");
  //Vector<String> a = breakdown("a c f b d c e");
  //Vector<String> b = breakdown("a d b c e c");
	 
//	Vector<String> a = breakdown("a c");
//	Vector<String> b = breakdown("d");
	 
  Vector<String> c = LCS.getLCS(a,b);
  System.out.println(a);
  System.out.println(b);
  System.out.println(c);
 }
}