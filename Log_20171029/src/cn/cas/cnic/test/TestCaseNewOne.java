package cn.cas.cnic.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.cas.cnic.log.fileread.FileRead;
import cn.cas.cnic.log.fileread.IdenticalWordRate;
import cn.cas.cnic.log.fileread.FileRead.segmentInformation;
import cn.cas.cnic.log.formatfactory.FormatFactory;

public class TestCaseNewOne {
	@Before
	public void prepare(){
        System.out.println(" ��ʼ���в��ԣ�");
    }
	@After
    public void destroy(){
        System.out.println(" ���Խ�����");
    }
	@Test //����Cron��־
	public void testCron() throws Exception {
		String fileName = "F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702";
		String className = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.indexOf("-"));
		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		className += "Factory";
		className = "cn.cas.cnic.log.formatfactory."+className;
		System.out.println(className);
		Class<?> c =Class.forName(className);  
		FormatFactory ff = (FormatFactory)c.newInstance();
		FileRead fr = ff.createRead(fileName);
		
		System.out.println(fr.toString());
		
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
	}

//	@Test //����ģʽ���ɺ���
	public void testGenerateLinePattern() {
		String s1 = "a b c d e f g";
		String s2 = "h b j d l f n";
		
		System.out.println(MethodNeededTest.generateLinePattern(s1,s2));
	}
}