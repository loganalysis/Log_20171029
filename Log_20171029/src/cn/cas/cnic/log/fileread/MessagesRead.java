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
	
	@Override  //����messages��־�����뾭���ںŵĴ���Ͷ���Ķ��ŵĴ���
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		
//		System.out.println(String.valueOf(timeStamp));

		temMap.put(segmentInformation.timeStamp, String.valueOf(timeStamp));
		//����С������������   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//����ε���һ�����ֵġ�ð�š��Ǵ���Դͷ��  codeSourse   
		int i = 4;
		try {
			if(splitLine[i].endsWith(":")) {
				String codeSource = splitLine[i].split("\\[")[0];
				if(codeSource.endsWith(":"))
		    		codeSource = codeSource.substring(0,codeSource.length() - 1);
				temMap.put(segmentInformation.codeSourse, codeSource);
				++i;
	//			if(codeSource.equals("null"))
			} else if(splitLine[i+1].endsWith(":")) {
				String codeSource = splitLine[i] + " " + splitLine[i+1].substring(0,splitLine[i+1].length()-1);
				temMap.put(segmentInformation.codeSourse, codeSource);
				++i;  ++i;
			} else {
				Exception e = new Exception("�������޷���������");
				throw e;
			}
		} catch(Exception e) {
			throw e;
		}
		//ʣ��Ĳ�������Ҫ��ע����Ĵ������ݶ�    codeContent
		segment.setLength(0);
		while(i!=splitLine.length) {
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
}
