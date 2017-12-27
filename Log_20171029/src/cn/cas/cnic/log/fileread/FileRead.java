package cn.cas.cnic.log.fileread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	 * @return 返回拆分后的单个字符串组成的字符串向量
	 */
	abstract protected Vector<String> breakdown(String str);
	/**
	 * 用来将一行日志分开的工具函数
	 * @param 需要被分开的日志行：LogLine
	 * @return 日志分开后的一个键值对：HashMap<segmentInformation,String>
	 */
	abstract protected HashMap<segmentInformation,String> dealLogByLine(String LogLine);
	/**
	 * 构造函数
	 * @param fileName
	 */
	public FileRead(String fileName) {
		_fileName = fileName;
		readAndSave();
	}
	/**
	 * 根据传入的文件生成模式的方法
	 * @param threshold  支持阈值
	 * @param SI   需要匹配的内容的键值
	 * @param MM   匹配的方法
	 */
	public void getPattern(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		_logPatterns.clear();
		PatternUnpersistence();  //进行模式反持久化****************测试阶段函数
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
		PatternPersistence();  //进行模式持久化****************测试阶段函数
	}
	//后面是辅助测试的公有函数
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
		//写之前我先将日志模式按照数目大小排一下序
		Collections.sort(_logPatterns,new Comparator<Vector<String>>() {
            public int compare(Vector<String> left, Vector<String> right) {
                return (right.size()-left.size());
            }
        });
		
        File file = new File(fileName);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            for(int i = 0 ; i != _logPatterns.size() ; ++i) {
            	Vector<String> tem = _logPatterns.elementAt(i);
            	writer.write("总共有："+_logPatterns.size()+"个日志模式"+"     现在是第："+(i+1)+"个模式");
            	writer.newLine();
            	if(tem.size()>1) {
            		writer.write(generateLinePattern(tem.elementAt(0), tem.elementAt(1),"="));   //这里生成了带有*的模式的结果,而且结果对于=号进行了分开
            	}
            	else
            		writer.write(tem.elementAt(0));
            	writer.newLine();
            	writer.write("——————该模式包含日志条数："+tem.size()+"   占总文件的："+tem.size()*100.0/_fileContent.size()+"%"+"   分别是：");
            	writer.newLine();
            	for(int j = 0; j != tem.size() ; ++j) {
            		writer.write(tem.elementAt(j));
            		writer.newLine();
            	}
            	writer.write("*******************************");
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
	 * 根据段信息的键写值到文件中的函数
	 * @param fileName
	 * @param SI
	 */
	public void writeContentBySegment(String fileName, segmentInformation SI) {
		Iterator<HashMap<segmentInformation,String>> iterator = _fileContent.iterator();
        File file = new File(fileName);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            while(iterator.hasNext()){
            	writer.write(iterator.next().get(SI));
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
	 * 根据时间段生成特征向量的函数
	 * @param fileName
	 * @param time
	 */
	public void GenerateFeatureVector(String fileName, double time) {
		
	}
	/**
	 * 模式持久化日志模式的函数，测试时公有，最后要变成私有！！！！！！！！******************
	 */
	public void PatternPersistence() {
		String FileClassName = _fileName.substring(_fileName.lastIndexOf("\\")+1,_fileName.indexOf("-"));
		FileClassName = FileClassName.substring(0, 1).toUpperCase() + FileClassName.substring(1);
		String PersistenceName = _fileName.substring(0,_fileName.lastIndexOf("\\")+1) + FileClassName + "Persistecne";
		//如果没有文件夹，先新建一个文件夹
		File dir = new File(PersistenceName);  
		if (!dir.exists())
			dir.mkdir();
		//然后再将文件名加入
		PersistenceName = PersistenceName + "\\" + FileClassName + "Pattern.txt";
		System.out.println(PersistenceName);
		
		File file = new File(PersistenceName);
		FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            for(int i = 0 ; i != _logPatterns.size() ; ++i) {
            	Vector<String> tem = _logPatterns.elementAt(i);
            	if(tem.size()>1) {
            		writer.write(generateLinePattern(tem.elementAt(0), tem.elementAt(1),"="));   //这里生成了带有*的模式的结果,而且结果对于=号进行了分开
            	}
            	else
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
	 * 模式反持久化日志模式的函数，测试时公有，最后要变成私有！！！！！！！！******************
	 */
	public void PatternUnpersistence() {
		String FileClassName = _fileName.substring(_fileName.lastIndexOf("\\")+1,_fileName.indexOf("-"));
		FileClassName = FileClassName.substring(0, 1).toUpperCase() + FileClassName.substring(1);
		String PersistenceName = _fileName.substring(0,_fileName.lastIndexOf("\\")+1) + FileClassName + "Persistecne";
		PersistenceName = PersistenceName + "\\" + FileClassName + "Pattern.txt";
		System.out.println(PersistenceName);
		
		File file = new File(PersistenceName);
		if(file.exists()) {
			System.out.println("存在文件"+file.getName());
			BufferedReader reader = null;
	        try {
//			            System.out.println("以行为单位读取文件内容，一次读一整行：");
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
//			            int line = 1;
	            // 一次读入一行，直到读入null为文件结束
	            while ((tempString = reader.readLine()) != null) {
	            	//System.out.println("line " + line + ": " + tempString);  //< 显示行号
	            	Vector<String> temVector = new Vector<String>();
	            	temVector.add(tempString);
	            	_logPatterns.add(temVector);
	            	//line++;
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
		}
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
	 * 根据输出的两个字符串来生成日志模式的函数
	 * @param s1
	 * @param s2
	 * @return 匹配后的字符串
	 */
	private static String generateLinePattern(String s1, String s2, String reg) {
		String[] splitLine1 = s1.split("[ ]+");
		String[] splitLine2 = s2.split("[ ]+");
		int min = splitLine1.length<splitLine2.length?splitLine1.length:splitLine2.length;
		int max = splitLine1.length>splitLine2.length?splitLine1.length:splitLine2.length;
		if(splitLine1.length < splitLine2.length) {
			String[] tem = splitLine1;
			splitLine1 = splitLine2;
			splitLine2 = tem;
		}
		
		StringBuilder segment = new StringBuilder();   //创建一个StringBuilder，用来存储没次的临时段位
		for(int i = 0 ; i != max; ++i ) {
			if(i<min && !splitLine1[i].equals(splitLine2[i])) {
				if(reg.length() == 0) {
					segment.append("|*| ");
				}
				else {
					String[] regSplit=splitLine1[i].split(reg);
					if(regSplit.length == 1)
						segment.append("|*| ");
					else if(regSplit.length > 1)
						segment.append(regSplit[0]).append(reg).append("|*| ");
				}
			}
			else
				segment.append(splitLine1[i]).append(" ");
		}
		
		return segment.toString();
	}
	
}
