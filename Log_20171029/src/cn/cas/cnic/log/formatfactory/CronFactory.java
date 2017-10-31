package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.CronRead;
import cn.cas.cnic.log.fileread.FileRead;

public class CronFactory extends FormatFactory{
	@Override
	protected FileRead createRead(String fileName) {
		return new CronRead(fileName);
	}
}
