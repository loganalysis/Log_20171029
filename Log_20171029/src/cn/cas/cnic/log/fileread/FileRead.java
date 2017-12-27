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
	 * @return ���ز�ֺ�ĵ����ַ�����ɵ��ַ�������
	 */
	abstract protected Vector<String> breakdown(String str);
	/**
	 * ������һ����־�ֿ��Ĺ��ߺ���
	 * @param ��Ҫ���ֿ�����־�У�LogLine
	 * @return ��־�ֿ����һ����ֵ�ԣ�HashMap<segmentInformation,String>
	 */
	abstract protected HashMap<segmentInformation,String> dealLogByLine(String LogLine);
	/**
	 * ���캯��
	 * @param fileName
	 */
	public FileRead(String fileName) {
		_fileName = fileName;
		readAndSave();
	}
	/**
	 * ���ݴ�����ļ�����ģʽ�ķ���
	 * @param threshold  ֧����ֵ
	 * @param SI   ��Ҫƥ������ݵļ�ֵ
	 * @param MM   ƥ��ķ���
	 */
	public void getPattern(double threshold , segmentInformation SI, IdenticalWordRate.matchMethod MM) {
		_logPatterns.clear();
		PatternUnpersistence();  //����ģʽ���־û�****************���Խ׶κ���
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
		PatternPersistence();  //����ģʽ�־û�****************���Խ׶κ���
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
		//д֮ǰ���Ƚ���־ģʽ������Ŀ��С��һ����
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
            	writer.write("�ܹ��У�"+_logPatterns.size()+"����־ģʽ"+"     �����ǵڣ�"+(i+1)+"��ģʽ");
            	writer.newLine();
            	if(tem.size()>1) {
            		writer.write(generateLinePattern(tem.elementAt(0), tem.elementAt(1),"="));   //���������˴���*��ģʽ�Ľ��,���ҽ������=�Ž����˷ֿ�
            	}
            	else
            		writer.write(tem.elementAt(0));
            	writer.newLine();
            	writer.write("��������������ģʽ������־������"+tem.size()+"   ռ���ļ��ģ�"+tem.size()*100.0/_fileContent.size()+"%"+"   �ֱ��ǣ�");
            	writer.newLine();
            	for(int j = 0; j != tem.size() ; ++j) {
            		writer.write(tem.elementAt(j));
            		writer.newLine();
            	}
            	writer.write("*******************************");
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
	 * ���ݶ���Ϣ�ļ�дֵ���ļ��еĺ���
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
	 * ����ʱ����������������ĺ���
	 * @param fileName
	 * @param time
	 */
	public void GenerateFeatureVector(String fileName, double time) {
		
	}
	/**
	 * ģʽ�־û���־ģʽ�ĺ���������ʱ���У����Ҫ���˽�У���������������******************
	 */
	public void PatternPersistence() {
		String FileClassName = _fileName.substring(_fileName.lastIndexOf("\\")+1,_fileName.indexOf("-"));
		FileClassName = FileClassName.substring(0, 1).toUpperCase() + FileClassName.substring(1);
		String PersistenceName = _fileName.substring(0,_fileName.lastIndexOf("\\")+1) + FileClassName + "Persistecne";
		//���û���ļ��У����½�һ���ļ���
		File dir = new File(PersistenceName);  
		if (!dir.exists())
			dir.mkdir();
		//Ȼ���ٽ��ļ�������
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
            		writer.write(generateLinePattern(tem.elementAt(0), tem.elementAt(1),"="));   //���������˴���*��ģʽ�Ľ��,���ҽ������=�Ž����˷ֿ�
            	}
            	else
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
	 * ģʽ���־û���־ģʽ�ĺ���������ʱ���У����Ҫ���˽�У���������������******************
	 */
	public void PatternUnpersistence() {
		String FileClassName = _fileName.substring(_fileName.lastIndexOf("\\")+1,_fileName.indexOf("-"));
		FileClassName = FileClassName.substring(0, 1).toUpperCase() + FileClassName.substring(1);
		String PersistenceName = _fileName.substring(0,_fileName.lastIndexOf("\\")+1) + FileClassName + "Persistecne";
		PersistenceName = PersistenceName + "\\" + FileClassName + "Pattern.txt";
		System.out.println(PersistenceName);
		
		File file = new File(PersistenceName);
		if(file.exists()) {
			System.out.println("�����ļ�"+file.getName());
			BufferedReader reader = null;
	        try {
//			            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
//			            int line = 1;
	            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
	            while ((tempString = reader.readLine()) != null) {
	            	//System.out.println("line " + line + ": " + tempString);  //< ��ʾ�к�
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
	 * ��������������ַ�����������־ģʽ�ĺ���
	 * @param s1
	 * @param s2
	 * @return ƥ�����ַ���
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
		
		StringBuilder segment = new StringBuilder();   //����һ��StringBuilder�������洢û�ε���ʱ��λ
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
