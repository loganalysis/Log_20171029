import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;


public class ReadLog {
	private static Vector<HashMap<segmentInformation,String>> _fileContent = new Vector<HashMap<segmentInformation,String>>();  //< �����洢��ȡ���ļ��Ļ���������һ���ַ�������һ��
	public static enum segmentInformation {  //< ��¼��Ϣ��λ��ö��
		time,  //��־��ʱ���ַ�����λ�ļ�ֵ
		hostName,  //��־���������ַ�����λ�ļ�ֵ
		codeSourse,  //��־�Ĵ���Դͷ�ַ�����λ�ļ�ֵ
		codeContent;   //��־�Ĵ�����־�����ַ�����λ�ļ�ֵ
	}
	private static Vector<Vector<String>> logPatterns = new Vector<Vector<String>>();  //< �����洢��־ģʽ
	
	/**
	 * ��ȡ�ļ����洢��_fileContent�ĺ���
	 * @param filename
	 * @return boolean
	 */
	private static boolean readAndSave(String filename) {
		//����ı�����
		_fileContent.clear();
		//��ʼ��ȡ�ļ�
		File file = new File(filename);
        BufferedReader reader = null;
        try {
//            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
//                System.out.println("line " + line + ": " + tempString);  //< ��ʾ�к�
                _fileContent.add(dealLogByLine(tempString));  //��һ����־�ֿ���Ϊ������Ŀ�ļ�ֵ��
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
	 * ������һ����־�ֿ��ĺ���
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
	/**
	 * ��������ģʽ�ĺ���
	 * @param threshold �����õ���ֵ
	 */
	private static void generatePatterns(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		logPatterns.clear();
		for(int i = 0 ; i != _fileContent.size() ; ++i) {
			boolean isMatched = false;  //�Ƿ�ƥ���ˣ�Ĭ��û��ƥ��
			String compareLog = _fileContent.get(i).get(SI);  //���ڱȽϵ���־����
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
			if(!isMatched) {  //û��ƥ��ģ���������һ��ģʽ���
				Vector<String> temStr = new Vector<String>();
				temStr.add(compareLog);
				logPatterns.add(temStr);
//				System.out.println("������һ��ģʽ������ģʽ��"+logPatterns.size());
			}
//			System.out.println("һ����"+_fileContent.size()+"��  ���ڴ����"+i);
		}
	}
	
	/**
	 * ���Ը����������
	 * @param args
	 */
	public static void main(String[] args)  {
		readAndSave("F:\\DoctorContent\\loganalysis\\logs201707\\cron-20170702");   //< ���Զ�ȡ���溯��
//		System.out.println(segmentInformation.time);   //< ����ö����
//		System.out.println(dealLogByLine("Jun 25 03:47:01 node69 /usr/sbin/cron[13325]: (sce) CMD (/usr/local/sce/fs/bin/auto_job)"));  //< ���Է�����־����
		
		System.out.println("���ļ�һ����"+_fileContent.size()+"����־");
		
		for(double i = 0.5 ; i< 0.91 ; i+=0.1) {
			generatePatterns(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.OBO);
			System.out.println("��ֵ��"+i+"ʱ��ʹ��һ��һ����ģʽ�������Ŀ�ǣ�"+logPatterns.size());
			
			generatePatterns(i,segmentInformation.codeContent, IdenticalWordRate.matchMethod.LCS2);
			System.out.println("ʹ������������з���ģʽ�������Ŀ�ǣ�"+logPatterns.size());
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
