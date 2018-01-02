package cn.cas.cnic.test;

import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.IdenticalWordRate;
import cn.cas.cnic.log.fileread.IdenticalWordRate.matchMethod;
import cn.cas.cnic.log.fileread.FileRead.segmentInformation;
import cn.cas.cnic.log.formatfactory.FormatFactory;


public class TestCaseNewOne {
	@Before
	public void prepare(){
        System.out.println(" 开始进行测试：");
    }
	@After
    public void destroy(){
        System.out.println(" 测试结束！");
    }
	
//	@Test //测试新的SLC方法
	public void testNewSLC() {
//		Vector<String> a = cn.cas.cnic.log.logpattern.LCS.breakdown("Invalid user admin from 195.154.33.138");
//		Vector<String> b = cn.cas.cnic.log.logpattern.LCS.breakdown("Failed password for invalid user admin from 195.154.33.138 port 52056 ssh2");
//		System.out.println(IdenticalWordRate.getRate(a, b, matchMethod.LCSNew));
	
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCSNew);
		
	}
	
//	@Test //测试Cron日志，这里使用了反射技术
	public void testCron() throws Exception {
		String fileName = "C:\\Users\\dell\\Desktop\\cron\\cron-20171008";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		
//		System.out.println(fr.toString());
		
		System.out.println("该文件一共有"+fr.getFileNum()+"条日志");
		String inputFileName = fr.getFileName();
		inputFileName = inputFileName +"分类文件";
		File dir = new File(inputFileName);  
		if (!dir.exists())
			dir.mkdir();
		String writeFileName = "";
		long startTime = 0;
		long endTime = 0;
		for(double i = 0.5 ; i< 0.51/*0.71*/ ; i+=0.1) {
			String result = String.format("%.1f",i);
			System.out.println("阈值是"+result+"时:");
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用一对一分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_OBO"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS1);
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用普通最长公共子序列分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
//			startTime = System.currentTimeMillis();    //获取开始时间
//			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
//			endTime = System.currentTimeMillis();    //获取结束时间
//			System.out.println("使用第二种最长公共子序列分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
//			writeFileName = inputFileName + "\\" +result+"_LCS2"+ ".txt";
//			System.out.println(writeFileName);
//			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCSNew);
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用优化的长公共子序列分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCSNew"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
		}
		fr.writeContentBySegment(inputFileName+"\\"+"test.txt", segmentInformation.timeStamp);
	}
	
//	@Test  //测试写文件匹配内容到文件中的函数
	public void writeSourceOfLog() {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fileName += "_codeContent.txt";
		fr.writeContentBySegment(fileName, segmentInformation.codeContent);
	}
	
//	@Test //测试模式生成函数
	public void testGenerateLinePattern() {
		String s1 = "a b c d l=34 f g";
		String s2 = "h b j d l=45 f n";
		
		System.out.println(MethodNeededTest.generateLinePattern(s1,s2,"="));
		
	}
	
//	@Test
	public void testPersistence() {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\secure-20170723";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
//		fr.PatternPersistence();
//		fr.PatternUnpersistence();
		System.out.println(fr.getPatternNum());
	}
	
	@Test  //测试时间戳和时间转化函数
	public void testTime() throws ParseException {
		String fileName = "C:\\Users\\34791\\Desktop\\loganalysis\\logs201707\\cron-20170709";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
		fr.GenerateFeatureVector(null, 1000);
	}
	
//	@Test
	public void TestLongArray() {
		double[]  a = new double[10];
		a[1] = 2;
		System.out.println(Arrays.toString(a));
 	}
	
}
