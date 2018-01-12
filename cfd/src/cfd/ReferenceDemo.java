package cfd;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

public class ReferenceDemo {
	private static ReferenceQueue<User> referenceQueue = new ReferenceQueue<User>();  
    private static final int size = 10;  
  
    public static void checkQueue(){  
//        Reference<? extends User> reference = referenceQueue.poll();  
//        if(reference!=null){  
//            System.out.println("In queue : "+reference.get());  
//        } 
    	Reference<? extends User> ref = null;
        while ((ref =  referenceQueue.poll()) != null) {
            System.out.println("cd===");
        }
    }  
  
    public static void testSoftReference()  
    {  
        Set<SoftReference<User>> softReferenceSet = new HashSet<SoftReference<User>>();  
        for (int i = 0; i < size; i++) {  
            SoftReference<User> ref = new SoftReference<User>(new User("Soft 软引用 " + i), referenceQueue);  
            System.out.println("Just created1: " + ref.get());  
            softReferenceSet.add(ref);  
        }  
//        System.gc();  
        checkQueue();  
    }  
  
    public static void testWeaKReference()  
    {  
        Set<WeakReference<User>> weakReferenceSet = new HashSet<WeakReference<User>>();  
        for (int i = 0; i < size; i++) {  
            WeakReference<User> ref = new WeakReference<User>(new User("Weak 弱引用 " + i), referenceQueue);  
            System.out.println("Just created2: " + ref.get());  
            weakReferenceSet.add(ref);  
        }  
        System.gc();  
        checkQueue();  
    }  
  
    public static void testPhantomReference()  
    {  
        Set<PhantomReference<User>> phantomReferenceSet = new HashSet<PhantomReference<User>>();  
        for (int i = 0; i < size; i++) {  
            PhantomReference<User> ref =  
                    new PhantomReference<User>(new User("Phantom 虚引用" + i), referenceQueue);  
            System.out.println("Just created3: " + ref.get());  
            phantomReferenceSet.add(ref);  
        }  
        System.gc();  
        checkQueue();  
    }  
  
    public static void main(String[] args) {  
        testSoftReference();  
//        testWeaKReference();  
//        testPhantomReference();  
    }  
}
