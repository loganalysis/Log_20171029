package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.MaillogRead;

public class MaillogFactory extends FormatFactory{
	@Override
	public FileRead createRead(String fileName) {
		return new MaillogRead(fileName);
	}
}
