package cfd;

import java.util.Random;

public class Son extends Father{

	static {
		System.out.println("**优先加载静态块*****son init");
	}
	public static final int c = new Random().nextInt(70);//若是调用常量，则不会加载类的初始化,若是变量的话，类还是会初始化
	public int d = 11;//若是调用常量，则不会加载类的初始化,若是变量的话，类还是会初始化
}
