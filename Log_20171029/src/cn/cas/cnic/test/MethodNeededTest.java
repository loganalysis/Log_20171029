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
}
