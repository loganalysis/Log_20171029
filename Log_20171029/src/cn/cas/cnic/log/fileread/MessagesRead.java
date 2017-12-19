package cn.cas.cnic.log.fileread;

import java.util.HashMap;
import java.util.Vector;

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
				result.add(strArray[1]);
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
		StringBuilder segment = new StringBuilder();   //����һ��StringBuilder�������洢û�ε���ʱ��λ
		//��һ����������С����ʱ���     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//����С������������   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//����ε���һ�����ֵġ�ð�š��Ǵ���Դͷ��  codeSourse   
		int i = 4;
		if(splitLine[i].endsWith(":")) {
			temMap.put(segmentInformation.codeSourse, splitLine[i]);
			++i;
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
