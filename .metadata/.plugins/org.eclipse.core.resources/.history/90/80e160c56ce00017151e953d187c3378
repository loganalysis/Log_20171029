package cn.cas.cnic.log.fileread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

//import ReadLog.segmentInformation;

public abstract class FileRead {
	protected String _fileName = null;  //< 用于存储文件的名字
	protected Vector<HashMap<segmentInformation,String>> _fileContent = new Vector<HashMap<segmentInformation,String>>();  //< 用来存储读取后文件的缓存向量，一个字符串代表一行
	protected Vector<Vector<String>> _logPatterns = new Vector<Vector<String>>();  //< 用来存储日志模式
	public enum segmentInformation {  //< 记录信息段位的枚举                                         //注意：java的枚举隐式的使用了static final修饰符进行修饰！
		time,  //< 日志的时间字符串段位的键值
		hostName,  //< 日志的主机名字符串段位的键值
		codeSourse,  //< 日志的代码源头字符串段位的键值
		codeContent;   //< 日志的代码日志内容字符串段位的键值
	}
	/**
	 * 用来拆分字符串的抽象方法，需要根据不同格式来定义
	 * @param s
	 * @return
	 */
	abstract protected Vector<String> breakdown(String str);
	/**
	 * 得到模式的方法
	 * @param threshold  支持阈值
	 * @param SI   需要匹配的内容的键值
	 * @param MM   匹配的方法
	 */
	public void getPattern(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		_logPatterns.clear();
		for(int i = 0 ; i != _fileContent.size() ; ++i) {
			boolean isMatched = false;  //是否匹配了，默认没有匹配
			String compareLog = _fileContent.get(i).get(SI);  //用于比较的日志内容
//			System.out.println(compareLog);
			for(int j = 0 ; j != _logPatterns.size() ; ++j) {
				String sourceLog = _logPatterns.get(j).get(0);
				double rate = IdenticalWordRate.getRate(breakdown(sourceLog), breakdown(compareLog), MM);  //这里用到抽象方法！
//				System.out.println(rate);
				if( rate > threshold || rate == threshold) {
					isMatched = true;
					_logPatterns.get(j).add(compareLog);
					break;
				}
			}
			if(!isMatched) {  //没有匹配的，就新增加一个模式组别
				Vector<String> temStr = new Vector<String>();
				temStr.add(compareLog);
				_logPatterns.add(temStr);
//				System.out.println("增加了一个模式，现在模式有"+logPatterns.size());
			}
//			System.out.println("一个有"+_fileContent.size()+"，  现在处理第"+i);
		}
	}
	//后面是辅助测试的共有函数
	public int getPatternNum() {
		return _logPatterns.size();
	}
	public Vector<Vector<String>> getLogPatterns() {
		return _logPatterns;
	}
	public int getFileNum() {
		return _fileContent.size();
	}
	public Vector<HashMap<segmentInformation,String>> getFileContent() {
		return _fileContent;
	}
	public String getFileName() {
		return _fileName;
	}
	/**
	 * 将模式写入到指定文件的方法
	 * @param fileName
	 */
	public void writePattern(String fileName) {
        Iterator iterator = _logPatterns.iterator();
        File file = new File(fileName);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            while(iterator.hasNext()){
            	Vector<String> tem = (Vector<String>)iterator.next();
                writer.write(tem.elementAt(0));
                writer.newLine();//换行
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	    
	/**
	 * 构造函数
	 * @param fileName
	 */
	public FileRead(String fileName) {
		_fileName = fileName;
		readAndSave();
	}
	/**
	 * 读取文件并保存内容到_fileContent变量里面的工具函数
	 * @return
	 */
	private boolean readAndSave() {
		//情况文本内容
		_fileContent.clear();
		//开始读取文件
		File file = new File(_fileName);
        BufferedReader reader = null;
        try {
//		            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//		            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
//		                System.out.println("line " + line + ": " + tempString);  //< 显示行号
                _fileContent.add(dealLogByLine(tempString));  //将一行日志分开成为含有题目的键值对
//		                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return true;
	}
	/**
	 * 用来将一行日志分开的工具函数
	 * @param 需要被分开的日志行：LogLine
	 * @return 日志分开后的一个键值对：HashMap<segmentInformation,String>
	 */
	abstract protected HashMap<segmentInformation,String> dealLogByLine(String LogLine);
	
}
