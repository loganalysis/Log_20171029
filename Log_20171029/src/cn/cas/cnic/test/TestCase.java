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
 * ���԰���
 * @author dell
 * �ò��԰����������ļ���ȡ������Ľ��
 */
public class TestCase {
	@Before
	public void prepare(){
        System.out.println(" ��ʼ���в��ԣ�");
    }
	@After
    public void destroy(){
        System.out.println(" ���Խ�����");
    }
//	@Test   //����getRate�����Ƿ��ܹ��õ�׼ȷ�ı���
	public void testRate() {
//		System.out.println(IdenticalWordRate.getRate(Vector<String>("a a a b"), "a b c b", matchMethod.OBO));
	}
//	@Test   //����CronRead
	public void testCronRead() {
		FormatFactory ff = new CronFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702");
		fr.getPattern(0.5, FileRead.segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
		System.out.println(fr.getPatternNum());
	}
//	@Test   //����MailRead
	public void testMailRead() {
		FormatFactory ff = new MaillogFactory();
//		FormatFactory ff = new CronFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702");
		fr.getPattern(0.5, FileRead.segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS1);
		System.out.println(fr.getPatternNum());
	}
//	@Test //����breakdown����
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
//	@Test  //����Stringʹ��split����
	public void testString() {
		String str = new String("message-id=<20170624194509.28B21653DA@v.cngrid.net>");
		String[] temStr = str.split("=");
		System.out.println(temStr.length);
	}
	
//	@Test  //�����ļ����𿪺���
	public void fileNameSplit() {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\aaa";
		
		File dir = new File(fileName);  
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("�ļ��д����ɹ���");
		}
		double i = 0.2333333;
		String result = String.format("%.2f",i);
		System.out.println(fileName+"\\"+result+".txt");
	}
	
//	@Test //ʹ�ò�ͬ����ֵ����cron��־���в�ͬ�ıȽ��㷨���в���
	public void testBiasAndAlgorithms() {
		FormatFactory ff = new MaillogFactory();
		FileRead fr = ff.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\maillog-20170702");
//		FileRead fr = ff.createRead("C:\\Users\\dell\\Desktop\\NEW_PATTERN-events.log");
		System.out.println("���ļ�һ����"+fr.getFileNum()+"����־");
		String inputFileName = fr.getFileName();
		inputFileName = inputFileName +"�����ļ�";
		File dir = new File(inputFileName);  
		if (!dir.exists())
			dir.mkdir();
		String writeFileName = "";
		long startTime = 0;
		long endTime = 0;
		for(double i = 0.5 ; i< 0.71 ; i+=0.1) {
			String result = String.format("%.1f",i);
			System.out.println("��ֵ��"+result+"ʱ:");
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ��һ��һ����ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_OBO"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS1);
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ�õ�һ������������з���ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS1"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ�õڶ�������������з���ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS2"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
		}
		
		fr.writePattern("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702.txt");
	}
	
	@Test  //���Է���---�ó����ۣ��������ʹ��ȫ��������������
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
	
//	@Test  //ֱ�Ӱ��տո�ֿ��͸��ݡ�=���ֿ���mail/messages��־���жԱ�
	public void testMailWithDifMeth() {
		FormatFactory ff_cron = new CronFactory();
		FormatFactory ff_mail = new MaillogFactory();
		FileRead fr_cron = ff_cron.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702");
		FileRead fr_mail = ff_mail.createRead("F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702");
		System.out.println("��Messages�ļ�һ����"+fr_cron.getFileNum()+"����־");
		long startTime = 0;
		long endTime = 0;
		
		for(double i = 0.5 ; i< 0.91 ; i+=0.1) {
			System.out.println("��ֵ��"+i+"ʱ:");
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr_cron.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);			
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ�ÿո��ַ������Ŀ�ǣ�"+fr_cron.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr_mail.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);			
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ�õȺŲ�ַ������Ŀ�ǣ�"+fr_mail.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
		}
	}
}
