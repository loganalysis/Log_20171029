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
	protected String _fileName = null;  //< ���ڴ洢�ļ�������
	protected Vector<HashMap<segmentInformation,String>> _fileContent = new Vector<HashMap<segmentInformation,String>>();  //< �����洢��ȡ���ļ��Ļ���������һ���ַ�������һ��
	protected Vector<Vector<String>> _logPatterns = new Vector<Vector<String>>();  //< �����洢��־ģʽ
	public enum segmentInformation {  //< ��¼��Ϣ��λ��ö��                                         //ע�⣺java��ö����ʽ��ʹ����static final���η��������Σ�
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
	 * @param threshold  ֧����ֵ
	 * @param SI   ��Ҫƥ������ݵļ�ֵ
	 * @param MM   ƥ��ķ���
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
				if( rate > threshold || rate == threshold) {
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
//			System.out.println("һ����"+_fileContent.size()+"��  ���ڴ����"+i);
		}
	}
	//�����Ǹ������ԵĹ��к���
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
	 * ��ģʽд�뵽ָ���ļ��ķ���
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
                writer.newLine();//����
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
	 * ���캯��
	 * @param fileName
	 */
	public FileRead(String fileName) {
		_fileName = fileName;
		readAndSave();
	}
	/**
	 * ��ȡ�ļ����������ݵ�_fileContent��������Ĺ��ߺ���
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
	abstract protected HashMap<segmentInformation,String> dealLogByLine(String LogLine);
	
}
