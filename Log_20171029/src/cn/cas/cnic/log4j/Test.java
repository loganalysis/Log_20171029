package cn.cas.cnic.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *@author linbingwen
 *@2018��3��18��9:14:21
 */
public class Test {
	private static Logger logger = LogManager.getLogger(Test.class.getName());  

    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        // System.out.println("This is println message.");  

//        // ��¼debug�������Ϣ  
//        logger.debug("This is debug message.");  
//        // ��¼info�������Ϣ  
//        logger.info("This is info message.");  
//        // ��¼error�������Ϣ  
//        logger.error("This is error message.");  
    	
    	String a = "C:\\Users\\dell\\Desktop\\cron\\������_���ִ���.xls";
    	logger.debug(a.lastIndexOf("."));
    	
    }  

}