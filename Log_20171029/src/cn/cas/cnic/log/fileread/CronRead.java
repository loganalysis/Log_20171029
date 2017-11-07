package cn.cas.cnic.log.fileread;

import java.util.Vector;

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
}
