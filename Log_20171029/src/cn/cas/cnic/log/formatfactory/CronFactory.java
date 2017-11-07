package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.CronRead;
import cn.cas.cnic.log.fileread.FileRead;

public class CronFactory extends FormatFactory{
	@Override
	public FileRead createRead(String fileName) {
		return new CronRead(fileName);
	}
}
