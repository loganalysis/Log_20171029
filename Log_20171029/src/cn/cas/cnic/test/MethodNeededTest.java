package cn.cas.cnic.test;

import java.util.Vector;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.formatfactory.FormatFactory;

public class MethodNeededTest {
	public static FileRead getFileReadFromFileName(String fileName) {
		String className = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.indexOf("-"));
		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		className += "Factory";
		className = "cn.cas.cnic.log.formatfactory."+className;
//		System.out.println(className);
		Class<?> c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		FormatFactory ff = null;
		try {
			ff = (FormatFactory)c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ff.createRead(fileName);
	}
	
	
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
	
	public static String generateLinePattern(String s1, String s2, String reg) {
		String[] splitLine1 = s1.split("[ ]+");
		String[] splitLine2 = s2.split("[ ]+");
		int min = splitLine1.length<splitLine2.length?splitLine1.length:splitLine2.length;
		int max = splitLine1.length>splitLine2.length?splitLine1.length:splitLine2.length;
		if(splitLine1.length < splitLine2.length) {
			String[] tem = splitLine1;
			splitLine1 = splitLine2;
			splitLine2 = tem;
		}
		
		StringBuilder segment = new StringBuilder();   //创建一个StringBuilder，用来存储没次的临时段位
		for(int i = 0 ; i != max; ++i ) {
			if(i<min && !splitLine1[i].equals(splitLine2[i])) {
				if(reg.length() == 0) {
					segment.append("|*| ");
				}
				else {
					String[] regSplit=splitLine1[i].split(reg);
					if(regSplit.length == 1)
						segment.append("|*| ");
					else
						segment.append(regSplit[0]).append(reg).append("|*| ");
				}
			}
			else
				segment.append(splitLine1[i]).append(" ");
		}
		
		return segment.toString();
	}
	
}
