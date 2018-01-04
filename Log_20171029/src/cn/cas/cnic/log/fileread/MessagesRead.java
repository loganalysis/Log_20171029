package cn.cas.cnic.log.fileread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import cn.cas.cnic.log.assistent.TimeOfLog;
import cn.cas.cnic.log.fileread.FileRead.segmentInformation;

public class MessagesRead extends FileRead{
	public MessagesRead(String fileName) {
		super(fileName);
	}
	
	@Override  //对于messages日志，必须经等于号的处理和多出的逗号的处理
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
//				result.add(strArray[1]);
			}else {
				result.add(s);
			}
		}
		return result;
	}
	
	@Override
	protected HashMap<segmentInformation,String> dealLogByLine(String LogLine) {
		String[] splitLine = LogLine.split("[ ]+");
		if(splitLine.length < 5)   //可能出现空行，或者只有时间的日志！ 2018-1-2
			return null;
		HashMap<segmentInformation,String> temMap = new HashMap<segmentInformation,String>();
		StringBuilder segment = new StringBuilder();   //创建一个StringBuilder，用来存储没次的临时段位
		//第一个到第三个小段是时间段     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//这里用前三个小段生成时间戳存进去  timeStamp  2017/12/28 新增段位
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd HH:mm:ss");
		String time11 = TimeOfLog.MonthMap.get(splitLine[0]) + " " +splitLine[1] +  " "  + splitLine[2];
		long timeStamp = 0;
		try {
			timeStamp = simpleDateFormat.parse(time11).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(String.valueOf(timeStamp));

		temMap.put(segmentInformation.timeStamp, String.valueOf(timeStamp));
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
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
}
