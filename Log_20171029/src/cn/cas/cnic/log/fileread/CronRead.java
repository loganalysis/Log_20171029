package cn.cas.cnic.log.fileread;

import java.util.Vector;

import cn.cas.cnic.log.fileread.FileRead.segmentInformation;

public class CronRead extends FileRead{
	public CronRead(String fileName) {
		super(fileName);
	}

	@Override
	protected Vector<String> breakdown(String str) {
		Vector<String> result = new Vector<String>();
		String[] strs = str.split(" ");
		for (String s : strs)
			result.add(s);
		return result;
	}
}
