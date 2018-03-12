package cn.cas.cnic.test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.formatfactory.FormatFactory;
import cn.cas.cnic.log4j.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MethodNeededTest {
	private static Logger logger = LogManager.getLogger(MethodNeededTest.class.getName());  
	
	
	public static FileRead getFileReadFromFileName(String fileName) {
		String oringalName = fileName;
		File file = new File(fileName);
		if(file.isDirectory()) {
			File[] fileArray=file.listFiles();
			fileName = fileArray[0].getAbsolutePath();
		}
		logger.debug(fileName);
		String className = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.lastIndexOf("-"));
		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		className += "Factory";
		className = "cn.cas.cnic.log.formatfactory."+className;
//		logger.debug(className);
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
		return ff.createRead(oringalName);
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
	
	public final static Map MonthMap = new HashMap<String,Integer>() {{    
	    put("Jun", 1);    put("Feb", 2);    put("Mar", 3);
	    put("Apr", 4);	  put("May", 5);    put("Jun", 6);
	    put("Jul", 7);	  put("Aug", 8);    put("Sep", 9);
	    put("Oct", 10);	  put("Nov", 11);    put("Dec", 12);
	}};
	public static long showTime(String time1, String time2) throws ParseException {
		String t1Str[] = time1.split(" ");
		String t2Str[] = time2.split(" ");
		String time11 = MonthMap.get(t1Str[0]) + " " +t1Str[1] +  " "  + t1Str[2];
		String time22 = MonthMap.get(t2Str[0]) + " " +t2Str[1] +  " "  + t2Str[2];
		
		logger.debug(time11+"\t"+time22);
		
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd HH:mm:ss");
        long t1l = simpleDateFormat.parse(time11).getTime();
        long t2l = simpleDateFormat.parse(time22).getTime();
//        logger.debug(t1+"\t"+t2+"\t"+(t2l-t1l));
        return t2l-t1l;
	}
}
