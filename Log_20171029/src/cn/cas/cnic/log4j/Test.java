package cn.cas.cnic.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *@author linbingwen
 *@2018年3月18日9:14:21
 */
public class Test {
	private static Logger logger = LogManager.getLogger(Test.class.getName());  

    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        // System.out.println("This is println message.");  

//        // 记录debug级别的信息  
//        logger.debug("This is debug message.");  
//        // 记录info级别的信息  
//        logger.info("This is info message.");  
//        // 记录error级别的信息  
//        logger.error("This is error message.");  
    	
    	String a = "C:\\Users\\dell\\Desktop\\cron\\程序名_出现次数.xls";
    	logger.debug(a.lastIndexOf("."));
    	
    }  

}