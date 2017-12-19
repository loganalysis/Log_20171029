package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.MessagesRead;

public class MessagesFactory extends FormatFactory{
	@Override
	public FileRead createRead(String fileName) {
		return new MessagesRead(fileName);
	}

}
