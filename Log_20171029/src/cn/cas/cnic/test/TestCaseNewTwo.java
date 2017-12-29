package cn.cas.cnic.test;

import java.util.Vector;

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
	
	@Test  //测试浅拷贝和深拷贝的问题
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
	
}
