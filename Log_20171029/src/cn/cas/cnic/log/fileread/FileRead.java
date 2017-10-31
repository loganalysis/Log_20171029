package cn.cas.cnic.log.fileread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;



//import ReadLog.segmentInformation;

public abstract class FileRead {
	protected String _fileName = null;  //< ���ڴ洢�ļ�������
	protected static Vector<HashMap<segmentInformation,String>> _fileContent = new Vector<HashMap<segmentInformation,String>>();  //< �����洢��ȡ���ļ��Ļ���������һ���ַ�������һ��
	protected static Vector<Vector<String>> _logPatterns = new Vector<Vector<String>>();  //< �����洢��־ģʽ
	public static enum segmentInformation {  //< ��¼��Ϣ��λ��ö��
		time,  //< ��־��ʱ���ַ�����λ�ļ�ֵ
		hostName,  //< ��־���������ַ�����λ�ļ�ֵ
		codeSourse,  //< ��־�Ĵ���Դͷ�ַ�����λ�ļ�ֵ
		codeContent;   //< ��־�Ĵ�����־�����ַ�����λ�ļ�ֵ
	}
	/**
	 * ��������ַ����ĳ��󷽷�����Ҫ���ݲ�ͬ��ʽ������
	 * @param s
	 * @return
	 */
	abstract protected Vector<String> breakdown(String str);
	/**
	 * �õ�ģʽ�ķ���
	 * @param threshold
	 * @param SI
	 * @param MM
	 */
	public void getPattern(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		_logPatterns.clear();
		for(int i = 0 ; i != _fileContent.size() ; ++i) {
			boolean isMatched = false;  //�Ƿ�ƥ���ˣ�Ĭ��û��ƥ��
			String compareLog = _fileContent.get(i).get(SI);  //���ڱȽϵ���־����
//			System.out.println(compareLog);
			for(int j = 0 ; j != _logPatterns.size() ; ++j) {
				String sourceLog = _logPatterns.get(j).get(0);
				double rate = IdenticalWordRate.getRate(breakdown(sourceLog), breakdown(compareLog), MM);  //�����õ����󷽷���
//				System.out.println(rate);
				if( rate > threshold) {
					isMatched = true;
					_logPatterns.get(j).add(compareLog);
					break;
				}
			}
			if(!isMatched) {  //û��ƥ��ģ���������һ��ģʽ���
				Vector<String> temStr = new Vector<String>();
				temStr.add(compareLog);
				_logPatterns.add(temStr);
//				System.out.println("������һ��ģʽ������ģʽ��"+logPatterns.size());
			}
//			System.out.println("һ����"+_fileContent.size()+"��  ���ڴ�����"+i);
		}
	}
	/**
	 * ���캯��
	 * @param fileName
	 */
	public FileRead(String fileName) {
		_fileName = fileName;
		readAndSave();
	}
	/**
	 * ��ȡ�ļ����������ݵ�_fileContent��������ĺ���
	 * @return
	 */
	private boolean readAndSave() {
		//����ı�����
		_fileContent.clear();
		//��ʼ��ȡ�ļ�
		File file = new File(_fileName);
        BufferedReader reader = null;
        try {
//		            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//		            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
//		                System.out.println("line " + line + ": " + tempString);  //< ��ʾ�к�
                _fileContent.add(dealLogByLine(tempString));  //��һ����־�ֿ���Ϊ������Ŀ�ļ�ֵ��
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
	 * ������һ����־�ֿ��Ĺ��ߺ���
	 * @param ��Ҫ���ֿ�����־�У�LogLine
	 * @return ��־�ֿ����һ����ֵ�ԣ�HashMap<segmentInformation,String>
	 */
	private static HashMap<segmentInformation,String> dealLogByLine(String LogLine) {
		String[] splitLine = LogLine.split("[ ]+");
		HashMap<segmentInformation,String> temMap = new HashMap<segmentInformation,String>();
		StringBuilder segment = new StringBuilder();   //����һ��StringBuilder�������洢û�ε���ʱ��λ
		//��һ����������С����ʱ���     time
		segment.append(splitLine[0]).append(' ').append(splitLine[1]).append(' ').append(splitLine[2]);
		temMap.put(segmentInformation.time, segment.toString());
		//����С������������   hostname
		temMap.put(segmentInformation.hostName, splitLine[3]);
		//����ε���һ�����ֵġ�ð�š��Ǵ���Դͷ��  codeSourse   ע�⣺�����λ�п���û�У�������
		int i = 4;
		if(splitLine[i].endsWith(":")) {
			temMap.put(segmentInformation.codeSourse, splitLine[i]);
			++i;
		}
		//ʣ��Ĳ�������Ҫ��ע����Ĵ������ݶ�    codeContent
		segment.setLength(0);
		while(i!=splitLine.length) {
			segment.append(splitLine[i]).append(' ');		
			++i;
		}
		temMap.put(segmentInformation.codeContent, segment.toString());
		return temMap;
	}
	
}