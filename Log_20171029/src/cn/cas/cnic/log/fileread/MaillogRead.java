package cn.cas.cnic.log.fileread;

import java.util.HashMap;
import java.util.Vector;

//import cn.cas.cnic.log.fileread.FileRead.segmentInformation;

public class MaillogRead extends FileRead{
	public MaillogRead(String fileName) {
		super(fileName);
	}

	@Override  //对于mail日志，必须经等于号的处理和多出的逗号的处理
	protected Vector<String> breakdown(String str) {
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
//				result.add(strArray[1]);    //maillog分类比较多，直接将等号后面的删除进行分类
			}else {
				result.add(s);
			}
		}
		return result;
	}
	
	@Override
	protected HashMap<segmentInformation,String> dealLogByLine(String LogLine) {
		String[] splitLine = LogLine.split("[ ]+");
		HashMap<segmentInformation,String> temMap = new HashMap<segmentInformation,String>();
		StringBuilder segment = new StringBuilder();   //创建一个StringBuilder，用来存储没次的临时段位
		//第一个到第三个小段是时间段     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//第四小段是主机名段   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//第五段到第一个出现的“冒号”是代码源头段  codeSourse   
		int i = 4;
		if(splitLine[i].endsWith(":")) {
			temMap.put(segmentInformation.codeSourse, splitLine[i]);
			++i;
		}
		//剩余的部分是需要关注分类的代码内容段    codeContent  
		segment.setLength(0);
		while(i!=splitLine.length) {
			//下面的就将=号分开！！  感觉开始做不好，存储的时候还是原来的内容比较好，因此注释掉
//			String s = splitLine[i];
//			if(s.endsWith(",") || s.endsWith(":"))
//				s = s.substring(0, s.length()-1);
//			String[] strArray = s.split("=");
//			if(strArray.length == 2) {
//				segment.append(strArray[0]).append("= ").append(strArray[1]).append(' ');
//			}else {
//				segment.append(splitLine[i]).append(' ');
//			}
//			++i;
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
	
}
