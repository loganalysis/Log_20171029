package cn.cas.cnic.log.logpattern;

public class WordConvertPatterns
{
 public static final String PATTERN_NUM = "[0-9]+";
 public static final String PATTERN_DIGITNUM = "[0-9]+.[0-9]+";
 public static final String PATTERN_IP = "[0-9]+.[0-9]+.[0-9]+.[0-9]+";
 public static final String PATTERN_USER = "[A-Za-z][A-Za-z0-9_]+";
 public static final String PATTERN_ATTR = "[A-Za-z]+=[A-Za-z0-9_.,:]+";
 public static final String PATTERN_MEMADDR = "[a-f0-9]{16}";
 public static final String PATTERN_HEXNUM = "0x[a-f0-9]+";
 
}