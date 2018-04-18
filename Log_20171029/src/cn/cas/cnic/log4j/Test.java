package cn.cas.cnic.log4j;

import java.io.File;

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
    	
//    	String a = "C:\\Users\\dell\\Desktop\\cron\\程序名_出现次数.xls";
//    	logger.debug(a.lastIndexOf("."));

//    	String s = "CROND[121]:";
//    	String codeSource = s.split("\\[")[0];
//    	
//    	if(codeSource.endsWith(":"))
//    		codeSource = codeSource.substring(0,codeSource.length() - 1);
//    	String logSourchandContent = "crond (root) CMD (LANG=C LC_ALL=C /usr/bin/mrtg /etc/mrtg/mrtg.cfg --lock-file /var/lock/mrtg/mrtg_l --confcache-file /var/lib/mrtg/mrtg.ok)";
//    	logger.debug(logSourchandContent.substring(logSourchandContent.indexOf(" ")+1));
    	
    	String fileName = "15104580000";
//    	String baseName = fileName.split("\\")[-1];
    	Long lo= Long.valueOf(fileName);
    	int te = int(lo);
    	System.out.println(int(lo));
    }  

}