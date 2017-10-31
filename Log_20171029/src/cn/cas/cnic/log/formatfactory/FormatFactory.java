package cn.cas.cnic.log.formatfactory;

import cn.cas.cnic.log.fileread.FileRead;

public abstract class FormatFactory {
	abstract protected FileRead createRead(String fileName);
}
