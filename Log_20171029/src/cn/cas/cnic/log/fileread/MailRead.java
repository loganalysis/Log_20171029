package cn.cas.cnic.log.fileread;

import java.util.Vector;

//import cn.cas.cnic.log.fileread.FileRead.segmentInformation;

public class MailRead extends FileRead{
	public MailRead(String fileName) {
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
				result.add(strArray[1]);
			}else {
				result.add(s);
			}
		}
		return result;
	}

}
