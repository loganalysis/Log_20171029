package cn.cas.cnic.test;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCaseNewTwo {
	@Before
	public void prepare(){
        System.out.println(" ��ʼ���в��ԣ�");
    }
	@After
    public void destroy(){
        System.out.println(" ���Խ�����");
    }
	
//	@Test  //����ǳ���������������
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
	
	@Test //�����ڴ���Ϣ�ĺ���
	public void testMemory() {
		System. out .println( " �ڴ���Ϣ :" );
		Runtime currRuntime = Runtime.getRuntime ();

	       int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);

	       int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);

	       System. out .println( nFreeMemory + "M/" + nTotalMemory +"M(free/total)" );
	}
}
