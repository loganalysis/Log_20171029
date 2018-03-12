package cn.cas.cnic.test;

import java.io.Console;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestCaseNewOne {
	private static Logger logger = LogManager.getLogger(TestCaseNewOne.class.getName()); 
	
	@Before
	public void prepare(){
        System.out.println(" ��ʼ���в��ԣ�");
    }
	@After
    public void destroy(){
        System.out.println(" ���Խ�����");
    }
	
//	@Test //�����µ�SLC����
	public void testNewSLC() {
//		Vector<String> a = cn.cas.cnic.log.logpattern.LCS.breakdown("Invalid user admin from 195.154.33.138");
//		Vector<String> b = cn.cas.cnic.log.logpattern.LCS.breakdown("Failed password for invalid user admin from 195.154.33.138 port 52056 ssh2");
//		System.out.println(IdenticalWordRate.getRate(a, b, matchMethod.LCSNew));
	
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCSNew);
		
	}
	
//	@Test //����Cron��־������ʹ���˷��似��
	public void testCron() throws Exception {
		String fileName = "C:\\Users\\dell\\Desktop\\cron\\cron-20171008";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		
//		System.out.println(fr.toString());
		
		System.out.println("���ļ�һ����"+fr.getFileNum()+"����־");
		String inputFileName = fr.getFileName();
		inputFileName = inputFileName +"�����ļ�";
		File dir = new File(inputFileName);  
		if (!dir.exists())
			dir.mkdir();
		String writeFileName = "";
		long startTime = 0;
		long endTime = 0;
		for(double i = 0.5 ; i< 0.51/*0.71*/ ; i+=0.1) {
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
			System.out.println("ʹ����ͨ����������з���ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCS"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
			
//			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
//			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
//			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
//			System.out.println("ʹ�õڶ�������������з���ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
//			writeFileName = inputFileName + "\\" +result+"_LCS2"+ ".txt";
//			System.out.println(writeFileName);
//			fr.writePattern(writeFileName);
			
			startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
			fr.getPattern(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCSNew);
			endTime = System.currentTimeMillis();    //��ȡ����ʱ��
			System.out.println("ʹ���Ż��ĳ����������з���ģʽ�������Ŀ�ǣ�"+fr.getPatternNum()+"   ʹ��ʱ���ǣ�"+(endTime - startTime) + "ms");
			writeFileName = inputFileName + "\\" +result+"_LCSNew"+ ".txt";
//			System.out.println(writeFileName);
			fr.writePattern(writeFileName);
		}
		Vector<segmentInformation> segToWrite = new Vector<segmentInformation>();
		segToWrite.add(segmentInformation.timeStamp);
		fr.writeContentBySegment(inputFileName+"\\"+"test.txt", segToWrite);
	}
	
//	@Test  //����д�ļ�ƥ�����ݵ��ļ��еĺ���
	public void writeSourceOfLog() {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\messages-20170702";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		fileName += "_codeContent.txt";
		Vector<segmentInformation> segToWrite = new Vector<segmentInformation>();
        segToWrite.add(segmentInformation.codeContent);
		fr.writeContentBySegment(fileName, segToWrite);
	}
	
//	@Test //����ģʽ���ɺ���
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
	
//	@Test  //����ʱ�����ʱ��ת������
	public void testTime() throws ParseException {
		String fileName = "F:\\DoctorContent\\log-related\\a��־��������\\���ɷַ�����־\\secure-20170702";
//		String fileName = "C:\\Users\\dell\\Desktop\\secure\\secure-20170709";
		FileRead fr = MethodNeededTest.getFileReadFromFileName(fileName);
		logger.info("���ļ�һ����"+fr.getFileNum()+"����־");
		fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
//		System.out.println("����ģʽ�ɹ�");
		//������������������ͬʱд��һ���ļ�
		fr.GenerateFeatureVector("F:\\DoctorContent\\log-related\\a��־��������\\���ɷַ�����־\\statisticsTheData_secure", 300000);
		//����д���ļ�ģʽ
		fr.writePattern(null);
		//������������ַ����Ķ�λ����Ӧ������д��һ���ļ���
		Vector<segmentInformation> segToWrite = new Vector<segmentInformation>();
        segToWrite.add(segmentInformation.logPatternNum);
		segToWrite.add(segmentInformation.time);
        segToWrite.add(segmentInformation.timeStamp);
        segToWrite.add(segmentInformation.hostName);
        segToWrite.add(segmentInformation.codeSourse);
        segToWrite.add(segmentInformation.codeContent);
        segToWrite.add(segmentInformation.VectorNum);
        
		fr.writeContentBySegment(null, segToWrite);
	}
	
	@Test //���ݵ�ǰ�ļ���λ�����ɳ־û��ļ��ĺ���
	public void GenPerFun() {
	    //�����ļ��У�
	    String fileName = "C:\\Users\\dell\\Desktop\\cron";
	    File file = new File(fileName);
	    File[] fileArray=file.listFiles();
        if(fileArray!=null){
            for (int i = 0; i < fileArray.length; i++) {
                File temFile = fileArray[i];
                String filename = temFile.getName();    //�õ��������ڴ�����ļ���
                String suffix = filename.substring(filename.lastIndexOf(".") + 1);    //�õ��ļ���׺
//                logger.debug("�ļ���׺[{}]",suffix);
                if(temFile.isDirectory() || filename.lastIndexOf(".") != -1)
                    continue;
                System.out.println(temFile.toString());
                FileRead fr = MethodNeededTest.getFileReadFromFileName(temFile.toString());
                System.out.println("���ļ�һ����"+fr.getFileNum()+"����־");
                fr.getPattern(0.5,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCSNew);
            }
        } 
	}
	
//	@Test
	public void TestLongArray() {
		double[]  a = new double[10];
		a[1] = 2;
		System.out.println(Arrays.toString(a));
 	}
	
//	@Test
	public void testEnumStr() {
	    Vector<segmentInformation> tem = new Vector<segmentInformation>() ;
	    tem.add(segmentInformation.codeContent);
	    tem.add(segmentInformation.hostName);
	    
	    System.out.println(tem.get(0).toString());
	}
}
