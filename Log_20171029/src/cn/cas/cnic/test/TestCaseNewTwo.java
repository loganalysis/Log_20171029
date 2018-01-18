package cn.cas.cnic.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCaseNewTwo {
	@Before
	public void prepare(){
        System.out.println(" 开始进行测试：");
    }
	@After
    public void destroy(){
        System.out.println(" 测试结束！");
    }
	
//	@Test  //测试浅拷贝和深拷贝的问题
	public void testVector() {
		Vector<Vector<Integer>> a = new Vector<Vector<Integer>>();
		Vector<Integer>	b = new Vector<Integer>();
		b.add(1); b.add(2);
		a.add(b);
		System.out.println(a);
		
		Vector<Integer> test = a.get(0);
		test.add(9);
//		inta = 9;
		
		System.out.println(a);
		
	}
	
//	@Test //测试内存信息的函数
	public void testMemory() {
		System. out .println( " 内存信息 :" );
		Runtime currRuntime = Runtime.getRuntime ();

	       int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);

	       int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);

	       System. out .println( nFreeMemory + "M/" + nTotalMemory +"M(free/total)" );
	}
	
//	@Test   //测试使用excel表格存数据，但是上限是short,即32767,太少了！
	public void testExcel() throws IOException {
	    Workbook wb = new HSSFWorkbook();
	    Sheet sheet = wb.createSheet("new sheet");

	    // Create a row and put some cells in it. Rows are 0 based.
	    Row row = sheet.createRow((short) 0);

	    // Aqua background
	    CellStyle style = wb.createCellStyle();
	    style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
	    style.setFillPattern(CellStyle.BIG_SPOTS);
	    Cell cell = row.createCell((short) 1);
	    cell.setCellValue("X");
	    cell.setCellStyle(style);

	    // Orange "foreground", foreground being the fill foreground not the font color.
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    cell = row.createCell((short) 2);
	    cell.setCellValue("X");
	    cell.setCellStyle(style);

	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
	    wb.write(fileOut);
	    fileOut.close();
	}
}
