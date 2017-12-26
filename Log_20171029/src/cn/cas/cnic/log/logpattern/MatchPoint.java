package cn.cas.cnic.log.logpattern;

public class MatchPoint implements Comparable<MatchPoint>
{
 private int x;
 private int y;
 private int d;
 private int s = 1;
 private MatchPoint next = null;
 
 public MatchPoint(int px, int py)
 {
  x = px;
  y = py;
  d = x + y;
 }
 
 public int getX()
 {
  return x;
 }
 
 public int getY()
 {
  return y;
 }
 
 public int getDepth()
 {
  return d;
 }
 
 public int getSeqLen()
 {
  return s;
 }
 
 public void setSeqLen(int sl)
 {
  s = sl;
 }
 
 public int compareTo(MatchPoint p)
 {
  if (d < p.getDepth())
   return -1;
  else if (d > p.getDepth())
   return 1;
  return 0;
 }
 
 public void setNext(MatchPoint n)
 {
  next = n;
 }
 
 public MatchPoint getNext()
 {
  return next;
 }
 
 public String toString()
 {
  return "["+x+","+y+"]";
 }
}