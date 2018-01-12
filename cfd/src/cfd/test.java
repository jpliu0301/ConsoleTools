package cfd;

import java.lang.ref.SoftReference;

public class test {

	public static void main(String[] args) {
		
		Son son = new Son();
//		System.out.println(Son.b);//静态块只能加载一次，自上而下的加载
//		System.out.println(Son.c);
		
		SoftReference<Father> sf = new SoftReference<Father>(son);
		son = null;
		Son s = (Son) sf.get();
		System.out.println(s.d);
	}
	
}
