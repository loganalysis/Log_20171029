package cn.cas.cnic.test;

import java.util.Vector;

public class MethodNeededTest {
	public static Vector<String> breakdown(String str) {
		Vector<String> result = new Vector<String>();
		String[] strs = str.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			if(s.endsWith(",") || s.endsWith(":"))
				s = s.substring(0, s.length()-1);
			String[] strArray = s.split("=");
			if(strArray.length == 2) {
				sb.setLength(0);
				sb.append(strArray[0]).append("=");
				result.add(sb.toString());   
				result.add(strArray[1]);
			}else {
				result.add(s);
			}
		}
		return result;
	}
	
	public static String generateLinePattern(String s1, String s2) {
		String[] splitLine1 = s1.split("[ ]+");
		String[] splitLine2 = s2.split("[ ]+");
		int min = splitLine1.length<splitLine2.length?splitLine1.length:splitLine2.length;
		int max = splitLine1.length>splitLine2.length?splitLine1.length:splitLine2.length;
		if(splitLine1.length < splitLine2.length) {
			String[] tem = splitLine1;
			splitLine1 = splitLine2;
			splitLine2 = tem;
		}
		
		StringBuilder segment = new StringBuilder();   //����һ��StringBuilder�������洢û�ε���ʱ��λ
		for(int i = 0 ; i != max; ++i ) {
			if(i<min && !splitLine1[i].equals(splitLine2[i]))
				segment.append("* ");
			else
				segment.append(splitLine1[i]).append(" ");
		}
		
		return segment.toString();
	}
	
}