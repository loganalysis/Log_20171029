package cn.cas.cnic.log;

public abstract class LogProcess {
	public abstract void ReadAndProcess(String fileName);
	public abstract void ProcessData();
	public abstract void Analysis();
}
