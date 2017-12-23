package cn.cas.cnic.test;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.IdenticalWordRate;
import cn.cas.cnic.log.fileread.FileRead.segmentInformation;
import cn.cas.cnic.log.formatfactory.CronFactory;
import cn.cas.cnic.log.formatfactory.FormatFactory;
import cn.cas.cnic.log.formatfactory.MaillogFactory;


/**
 * 测试案例
 * @author dell
 * 该测试案例包括对文件读取并分类的结果
 */
public class TestCase {
	@Before
	public void prepare(){
        System.out.println(" 开始进行测试：");
    }
	@After
    public void destroy(){
        System.out.println(" 测试结束！");
    }
//	@Test   //测试getRate函数是否能够得到准确的比例
	public void testRate() {
//		System.out.println(IdenticalWordRate.getRate(Vector<String>("a a a b"), "a b c b", matchMethod.OBO));
	}
//	@Test   //测试CronRead
	public void testCronRead() {
		FormatFactory ff = new CronFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702");
		fr.getPattern(0.5, FileRead.segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
		System.out.println(fr.getPatternNum());
	}
//	@Test   //测试MailRead
	public void testMailRead() {
		FormatFactory ff = new MaillogFactory();
//		FormatFactory ff = new CronFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702");
		fr.getPattern(0.5, FileRead.segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS1);
		System.out.println(fr.getPatternNum());
	}
//	@Test //测试breakdown函数
	public void testBreakdown() {
		FormatFactory ff = new MaillogFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702");
		Vector<HashMap<segmentInformation,String>> fileContent = fr.getFileContent();
		for(int i = 0 ; i != fileContent.size()/120 ; ++i) {
			String str = new String(fileContent.get(i).get(segmentInformation.codeContent));
			System.out.println(str);
			Vector<String> temV = MethodNeededTest.breakdown(str);
			System.out.println(temV);
		}
//		String str = new String("from=<root@scec>, size=686, class=0, nrcpts=1, msgid=<201706242145.v5OLj1jm016759@scec>, proto=ESMTP, daemon=MTA, relay=localhost.localdomain [127.0.0.1]");
//		Vector<String> temV = MethodNeededTest.breakdown(str);
//		System.out.println(temV);
	}
//	@Test  //测试String使用split函数
	public void testString() {
		String str = new String("message-id=<20170624194509.28B21653DA@v.cngrid.net>");
		String[] temStr = str.split("=");
		System.out.println(temStr.length);
	}
	
//	@Test  //测试文件名拆开函数
	public void fileNameSplit() {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\aaa";
		
		File dir = new File(fileName);  
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("文件夹创建成功！");
		}
		double i = 0.2333333;
		String result = String.format("%.2f",i);
		System.out.println(fileName+"\\"+result+".txt");
	}
	
//	@Test //使用不同的阈值对于cron日志进行不同的比较算法进行测试
	public void testBiasAndAlgorithms() {
		FormatFactory ff = new MaillogFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702");
//		FileRead fr = ff.createRead("C:\\Users\\dell\\Desktop\\NEW_PATTERN-events.log");
		System.out.println("该文件一共有"+fr.getFileNum()+"条日志");
		String inputFileName = fr.getFileName();
		inputFileName = inputFileName +"分类文件";
		File dir = new File(inputFileName);  
		if (!dir.exists())
			dir.mkdir();
		String writeFileName = "";
		long startTime = 0;
		long endTime = 0;
		for(double i = 0.5 ; i< 0.71 ; i+=0.1) {
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
			System.out.println("使用第一种最长公共子序列分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS1"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用第二种最长公共子序列分类模式分类的数目是："+fr.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS2"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
		}
		
		fr.writePattern("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702.txt");
	}
	
	@Test  //测试反射---得出结论：反射必须使用全名！！！！！！
	public void testReflect() throws Exception {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702";
		String className = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.indexOf("-"));
		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		className += "Factory";
		className = "cn.cas.cnic.log.formatfactory."+className;
		
		System.out.println(className);
		
		Class<?> c =Class.forName(className);  
		FormatFactory ff = (FormatFactory)c.newInstance();
		
		FileRead fr = ff.createRead(fileName);
		
		
		System.out.println(fr.toString());
	}
	
//	@Test  //直接按照空格分开和根据“=”分开的mail/messages日志进行对比
	public void testMailWithDifMeth() {
		FormatFactory ff_cron = new CronFactory();
		FormatFactory ff_mail = new MaillogFactory();
		FileRead fr_cron = ff_cron.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702");
		FileRead fr_mail = ff_mail.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702");
		System.out.println("该Messages文件一共有"+fr_cron.getFileNum()+"条日志");
		long startTime = 0;
		long endTime = 0;
		
		for(double i = 0.5 ; i< 0.91 ; i+=0.1) {
			System.out.println("阈值是"+i+"时:");
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr_cron.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);			
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用空格拆分分类的数目是："+fr_cron.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
			
			startTime = System.currentTimeMillis();    //获取开始时间
			fr_mail.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);			
			endTime = System.currentTimeMillis();    //获取结束时间
			System.out.println("使用等号拆分分类的数目是："+fr_mail.getPatternNum()+"   使用时间是："+(endTime - startTime) + "ms");
		}
	}
}
