import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;


public class ReadLog {
	private static Vector<HashMap<segmentInformation,String>> _fileContent = new Vector<HashMap<segmentInformation,String>>();  //< 用来存储读取后文件的缓存向量，一个字符串代表一行
	public static enum segmentInformation {  //< 记录信息段位的枚举
		time,  //日志的时间字符串段位的键值
		hostName,  //日志的主机名字符串段位的键值
		codeSourse,  //日志的代码源头字符串段位的键值
		codeContent;   //日志的代码日志内容字符串段位的键值
	}
	private static Vector<Vector<String>> logPatterns = new Vector<Vector<String>>();  //< 用来存储日志模式
	
	/**
	 * 读取文件并存储到_fileContent的函数
	 * @param filename
	 * @return boolean
	 */
	private static boolean readAndSave(String filename) {
		//情况文本内容
		_fileContent.clear();
		//开始读取文件
		File file = new File(filename);
        BufferedReader reader = null;
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
//                System.out.println("line " + line + ": " + tempString);  //< 显示行号
                _fileContent.add(dealLogByLine(tempString));  //将一行日志分开成为含有题目的键值对
//                line++;
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
	 * 用来将一行日志分开的函数
	 * @param 需要被分开的日志行：LogLine
	 * @return 日志分开后的一个键值对：HashMap<segmentInformation,String>
	 */
	private static HashMap<segmentInformation,String> dealLogByLine(String LogLine) {
		String[] splitLine = LogLine.split("[ ]+");
		HashMap<segmentInformation,String> temMap = new HashMap<segmentInformation,String>();
		StringBuilder segment = new StringBuilder();   //创建一个StringBuilder，用来存储没次的临时段位
		//第一个到第三个小段是时间段     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//第四小段是主机名段   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//第五段到第一个出现的“冒号”是代码源头段  codeSourse   注意：这个段位有可能没有！！！！
		int i = 4;
		if(splitLine[i].endsWith(":")) {
			temMap.put(segmentInformation.codeSourse, splitLine[i]);
			++i;
		}
		//剩余的部分是需要关注分类的代码内容段    codeContent
		segment.setLength(0);
		while(i!=splitLine.length) {
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
	/**
	 * 用于生成模式的函数
	 * @param threshold 分类用的阈值
	 */
	private static void generatePatterns(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		logPatterns.clear();
		for(int i = 0 ; i != _fileContent.size() ; ++i) {
			boolean isMatched = false;  //是否匹配了，默认没有匹配
			String compareLog = _fileContent.get(i).get(SI);  //用于比较的日志内容
//			System.out.println(compareLog);
			for(int j = 0 ; j != logPatterns.size() ; ++j) {
				String sourceLog = logPatterns.get(j).get(0);
				double rate = IdenticalWordRate.getRate(sourceLog, compareLog, MM);
//				System.out.println(rate);
				if( rate > threshold) {
					isMatched = true;
					logPatterns.get(j).add(compareLog);
					break;
				}
			}
			if(!isMatched) {  //没有匹配的，就新增加一个模式组别
				Vector<String> temStr = new Vector<String>();
				temStr.add(compareLog);
				logPatterns.add(temStr);
//				System.out.println("增加了一个模式，现在模式有"+logPatterns.size());
			}
//			System.out.println("一个有"+_fileContent.size()+"，  现在处理第"+i);
		}
	}
	
	/**
	 * 测试该类的主函数
	 * @param args
	 */
	public static void main(String[] args)  {
		readAndSave("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702");   //< 测试读取保存函数
//		System.out.println(segmentInformation.time);   //< 测试枚举类
//		System.out.println(dealLogByLine("Jun 25 03:47:01 node69 /usr/sbin/cron[13325]: (sce) CMD (/usr/local/sce/fs/bin/auto_job)"));  //< 测试分离日志函数
		
		System.out.println("该文件一共有"+_fileContent.size()+"条日志");
		
		for(double i = 0.5 ; i< 0.91 ; i+=0.1) {
			generatePatterns(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);
			System.out.println("阈值是"+i+"时，使用一对一分类模式分类的数目是："+logPatterns.size());
			
			generatePatterns(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
			System.out.println("使用最长公共子序列分类模式分类的数目是："+logPatterns.size());
		}
		
//		generatePatterns(0.8);
//		
//		System.out.println(logPatterns.size());
//		for(int i = 0 ; i != logPatterns.size() ; ++i) {
//			System.out.println(logPatterns.get(i).get(0));
//		}
		System.out.println("finished!");
	}
}
