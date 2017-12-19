package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.SecureRead;

public class SecureFactory extends FormatFactory{
	@Override
	public FileRead createRead(String fileName) {
		return new SecureRead(fileName);
	}

}
