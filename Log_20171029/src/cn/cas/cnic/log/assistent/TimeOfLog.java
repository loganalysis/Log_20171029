package cn.cas.cnic.log.assistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class TimeOfLog {
	public final static Map MonthMap = new HashMap<String,Integer>() {{    
	    put("Jun", 1);    put("Feb", 2);    put("Mar", 3);
	    put("Apr", 4);	  put("May", 5);    put("Jun", 6);
	    put("Jul", 7);	  put("Aug", 8);    put("Sep", 9);
	    put("Oct", 10);	  put("Nov", 11);    put("Dec", 12);
	}};
	/**
	 * ���ݴ����ʱ���ַ���time2-time1�õ�ʱ�����ĺ�������λ�Ǻ���
	 * @param time1 ��־�е�ʱ������ַ�������ʽΪ��Jul 2 3:19:04
	 * @param time2 ��־�е�ʱ������ַ�������ʽΪ��Jul 2 3:19:04
	 * @return long ������־�е�ʱ��������λ�Ǻ���
	 * @throws ParseException
	 */
	public static long timeInterval(String time1, String time2) throws ParseException {
		String t1Str[] = time1.split(" ");
		String t2Str[] = time2.split(" ");
		String time11 = MonthMap.get(t1Str[0]) + " " +t1Str[1] +  " "  + t1Str[2];
		String time22 = MonthMap.get(t2Str[0]) + " " +t2Str[1] +  " "  + t2Str[2];
		
		System.out.println(time11+"\t"+time22);
		
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd HH:mm:ss");
        long t1l = simpleDateFormat.parse(time11).getTime();
        long t2l = simpleDateFormat.parse(time22).getTime();
        
        System.out.println((t2l-t1l));
        
        return t2l-t1l;
	}
}
