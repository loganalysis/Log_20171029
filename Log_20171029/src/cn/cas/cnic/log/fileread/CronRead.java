package cn.cas.cnic.log.fileread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import cn.cas.cnic.log.assistent.TimeOfLog;

public class CronRead extends FileRead{
	public CronRead(String fileName) {
		super(fileName);
	}

	@Override  //对于Cron类的日志分类，直接根据空格划分即可
	protected Vector<String> breakdown(String str) {
		Vector<String> result = new Vector<String>();
		String[] strs = str.split(" ");
		for (String s : strs)
			result.add(s);
		return result;
	}
	
	@Override //对于Cron类的日志分类，第四个标签最后可能没有分号！
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
//			System.out.println(time11);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			System.out.println(time11);
			e.printStackTrace();
			return null;
//			System.exit(0);
		}
		
//		System.out.println(String.valueOf(timeStamp));

		temMap.put(segmentInformation.timeStamp, String.valueOf(timeStamp));
		//第四小段是主机名段   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//第五段到第一个出现的“冒号”是代码源头段  codeSourse   注意：这个段位对于Cron日志来说，必定有，但是最后可能不是冒号结束
		temMap.put(segmentInformation.codeSourse, splitLine[4]);
		//剩余的部分是需要关注分类的代码内容段    codeContent
		int i = 5;
		segment.setLength(0);
		while(i!=splitLine.length) {
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
}
