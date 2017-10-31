package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.MailRead;

public class MailFactory extends FormatFactory{
	@Override
	protected FileRead createRead(String fileName) {
		return new MailRead(fileName);
	}
}
