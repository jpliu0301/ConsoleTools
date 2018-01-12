package cfd;

public class User {
	private String name;  
	  
    public User()  
    {}  
  
    public User(String name)  
    {  
        this.name=name;  
    }  
  
    @Override  
    public String toString() {  
        return name;  
    }  
  
    public void finalize(){  
        System.out.println("Finalizing ... "+name);  
    }  
}
