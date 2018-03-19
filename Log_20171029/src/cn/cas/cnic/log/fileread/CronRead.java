package cn.cas.cnic.log.fileread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import cn.cas.cnic.log.assistent.TimeOfLog;
import cn.cas.cnic.log.fileread.FileRead.segmentInformation;

public class CronRead extends FileRead{
	public CronRead(String fileName) {
		super(fileName);
	}

	@Override  //����Cron�����־���ֱ࣬�Ӹ��ݿո񻮷ּ���
	protected Vector<String> breakdown(String str) {
		Vector<String> result = new Vector<String>();
		String[] strs = str.split(" ");
		for (String s : strs)
			result.add(s);
		return result;
	}
	
	@Override //����Cron�����־���࣬���ĸ���ǩ������û�зֺţ�
	protected HashMap<segmentInformation,String> dealLogByLine(String LogLine) throws Exception {
		String[] splitLine = LogLine.split("[ ]+");
		if(splitLine.length < 5) {  //���ܳ��ֿ��У�����ֻ��ʱ�����־�� 2018-1-2
			Exception e = new Exception("������־����ʱ���ֳ��Ȳ������쳣!"); 
			throw e;
		}
		HashMap<segmentInformation,String> temMap = new HashMap<segmentInformation,String>();
		StringBuilder segment = new StringBuilder();   //����һ��StringBuilder�������洢û�ε���ʱ��λ
		//��һ����������С����ʱ���     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//������ǰ����С������ʱ������ȥ  timeStamp  2017/12/28 ������λ
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
		//����С������������   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//����ε���һ�����ֵġ�ð�š��Ǵ���Դͷ��  codeSourse   ע�⣺�����λ����Cron��־��˵���ض��У����������ܲ���ð�Ž���
//		temMap.put(segmentInformation.codeSourse, splitLine[4]);
		String codeSource = splitLine[4].split("\\[")[0];
		if(codeSource.endsWith(":"))
    		codeSource = codeSource.substring(0,codeSource.length() - 1);
		temMap.put(segmentInformation.codeSourse, codeSource);
		//ʣ��Ĳ�������Ҫ��ע����Ĵ������ݶ�    codeContent
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
