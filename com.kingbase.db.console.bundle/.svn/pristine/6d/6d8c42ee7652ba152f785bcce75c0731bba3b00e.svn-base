package com.kingbase.db.console.bundle.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class IoTuningCore {

	private ArrayList<String> cmd;

	private volatile double pageCost;
	public String errMsg="";

	private Document doc;//每次explain之后的xml都被解析到doc

	private double cost1111;
	private double cost1112;
	private double cost2111;
	private double blocks;
	private int indexRows;
	private double operatorTimes;

	private double seq_page_cost;
	private double random_page_cost;
	private double cpu_tuple_cost;
	private double cpu_index_tuple_cost;
	private double cpu_operator_cost;
	
	private int pid;

	private BufferedReader br;
	
	/*返回格式:
		“参数名称=参数值
	 	 参数名称=参数值
	 	 .............” */
	public String getParameters(String installCatalog, String dataCatalog, String passwd) throws Exception {
		
		checkDtrace(installCatalog); 
		
		cmd = initCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);  //初始化数据库

		cmd = startCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);  //开启数据库

		Connection con = getConnection();
		prepareWork(con);  //建表，填充数据，分析表，checkpoint，关连接
		
		cmd = stopCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);  //关闭数据库
				
		cmd = clearCacheCMD(passwd);
		execCMD(cmd,installCatalog); //清缓存
		
		//计算 seq_page_cost和cpu_tuple_cost

		cmd = startCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);  //开启数据库
		
		Connection conn = getConnection();
		pid = queryPid(conn);  

		cmd = sysTapScript(pid,installCatalog,passwd,dataCatalog);
		execSysTap(cmd,installCatalog,dataCatalog,passwd, conn);
		

		if(pageCost == 0) {
			throw new Exception(errMsg);
		}
		seq_page_cost = pageCost;
		computeCpuTupleCost();				//cpu_tuple_cost
		
		//计算random_page_cost、cpu_index_tuple_cost和cpu_operator_cost

		setOff(conn);//关闭SeqScan和BitmapScan
		
		setParameters(conn, 1, 1, 1, 1);
		execExplain(conn);
 
		Element root = doc.getRootElement();
		Element query =root.element("Query");
		Element plan = query.element("Plan");
		Element totalCost = plan.element("Total-Cost");
		Element planRows = plan.element("Plan-Rows");
		cost1111 = Double.parseDouble(totalCost.getText());
		indexRows = Integer.parseInt(planRows.getText());
		
		setParameters(conn, 1, 1, 1, 2);
		execExplain(conn);
		root = doc.getRootElement();
		query =root.element("Query");
		plan = query.element("Plan");
		totalCost = plan.element("Total-Cost");
		cost1112 = Double.parseDouble(totalCost.getText());
		
		setParameters(conn, 2, 1, 1, 1);//设置参数，得到该参数设置下的cost
		execExplain(conn);
		root = doc.getRootElement();
		query =root.element("Query");
		plan = query.element("Plan");
		totalCost = plan.element("Total-Cost");
		cost2111 = Double.parseDouble(totalCost.getText());
		
		blocks = cost2111 - cost1111;
		operatorTimes = cost1112 - cost1111;

		conn.close();

		cmd = stopCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);//关闭数据库
		
		cmd = clearCacheCMD(passwd);
		execCMD(cmd,installCatalog);//清缓存

		cmd = startCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);//开启数据库

		Connection connection = getConnection();
		pid = queryPid(connection);//获取pid
 
		cmd = sysTapScript(pid,installCatalog,passwd,dataCatalog);
		execSysTap(cmd,installCatalog,dataCatalog,passwd, connection);
		
		setOff(connection);//关闭seqScan和BitmapScan
		execExplain(connection);
		
		if(pageCost == 0) {
			throw new Exception(errMsg);
		}
		random_page_cost = pageCost;
		computeCpuIndexTupleCost();					//cpu_index_tuple_cost
		cpu_operator_cost = cpu_index_tuple_cost/2;	//cpu_operator_cost

		connection.close();
		
		return "seq_page_cost="+seq_page_cost+"\nrandom_page_cost="+random_page_cost+"\ncpu_tuple_cost="
		+cpu_tuple_cost+"\ncpu_index_tuple_cost="+cpu_index_tuple_cost
		+"\ncpu_operator_cost="+cpu_operator_cost;
	}

	private void checkDtrace(String installCatalog) throws Exception {
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add(installCatalog+"/bin/sys_config");
		cmd.add("--configure");
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process process  = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		boolean dtrace = false;
		StringBuffer CMDoutput = new StringBuffer();
		while ((line = br.readLine()) != null) {
			if(line.contains("dtrace")) {
				dtrace = true;
			}
			CMDoutput.append(line + "\n");
			
		}
		if(!dtrace) 
			throw new Exception("Fatal:The \"--enable-dtrace\" option is not added while compiling the database.");

		int exitValue = process.waitFor();

		if (exitValue != 0) {
			throw new Exception(CMDoutput.toString());
		}
	}

	

	public void deleteDatabase(String dataCatalog) throws Exception {
		cmd = deleteCMD(dataCatalog);
		execCMD(cmd, null);		
	}
	private ArrayList<String> sysTapScript(int pid, String installCatalog, String passwd,String dataCatalog) throws Exception {
		File file = new File(dataCatalog+"/script.sh");
		OutputStream os=new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(os);
		pw.println("stap -e '");
		pw.println("global a");
		pw.println("probe process(\""+installCatalog+"/bin/kingbase\").mark(\"query__start\") {");
		pw.println("  delete a");
		pw.println("  println(\"query__start \", user_string($arg1), \"pid:\", pid())");
		pw.println("}");
		pw.println("probe vfs.read.return {");
		pw.println("  t = gettimeofday_ns() - @entry(gettimeofday_ns())");
		pw.println("  # if (execname() == \"kingbase\" && devname != \"N/A\")");
		pw.println("  a[pid()] <<< t");
		pw.println("}");
		pw.println("probe process(\""+installCatalog+"/bin/kingbase\").mark(\"query__done\") {");
		pw.println("  if (@count(a[pid()]))");
		pw.println("    printdln(\"**\", pid(), @count(a[pid()]), @avg(a[pid()]))");
		pw.println("  println(\"query__done \", user_string($arg1), \"pid:\", pid())");
		pw.println("if (@count(a[pid()])) {");
		pw.println("println(@hist_log(a[pid()]))");
		pw.println("#println(@hist_linear(a[pid()],1024,4096,100))");
		pw.println("}");
		pw.println("delete a");
		pw.println("}' -x "+pid);
		pw.flush();
		pw.close();

		ArrayList<String> c = new ArrayList<String>();
		c.add("chmod");
		c.add("+x");
		c.add(dataCatalog+"/script.sh");
		execCMD(c,null);

		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("bash");
		cmd.add("-c");
		cmd.add("echo " + passwd + " | sudo -S bash " + dataCatalog+"/script.sh"+" >"+dataCatalog+"/aa"+ " 2>&1");  
		return cmd;

	}

	private ArrayList<String> deleteCMD(String dataCatalog) {
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("rm");
		cmd.add("-rf");
		cmd.add(dataCatalog);
		return cmd;
	}

	/*实际时间(解析XML可得）  = 读取的块数（即blocks）*读取每块的时间（即random_page_cost）
	+ cpu处理每条记录的时间（即cpu_tuple_cost）*记录行数（即indexRows) 
	+ 索引行数（也是indexRows）*每条索引处理时间（所求）
	+cpu_operator_cost（默认为每条索引处理时间的一半）*operatorTimes*/
	private void computeCpuIndexTupleCost() {
		Element root = doc.getRootElement();
		Element query =root.element("Query");
		Element plan = query.element("Plan");
		Element totalCost = plan.element("Total-Cost");
		double actualTime = Double.parseDouble(totalCost.getText());
		cpu_index_tuple_cost = (actualTime - blocks*random_page_cost - cpu_tuple_cost*indexRows)/(indexRows+operatorTimes/2);
	}


	private void setOff(Connection conn) throws SQLException {
		Statement sta = conn.createStatement();
		sta.execute("set enable_seqscan=off");
		sta.execute("set enable_bitmapscan=off");
		sta.close();
	}


	private void setParameters(Connection conn, int i, int j, int k, int l) throws SQLException {
		Statement sta = conn.createStatement();
		sta.execute("set random_page_cost = "+i);
		sta.execute("set cpu_tuple_cost = "+j);
		sta.execute("set cpu_index_tuple_cost = "+k);
		sta.execute("set cpu_operator_cost = "+l);
		sta.close();
	}


	//实际结束时间（从XML可得）-实际开始时间（从XML可得）=读取的块数（从XML可得）*每块的开销（即seq_page_cost）+ 行数（10100000）*每行开销（所求）
	private void computeCpuTupleCost() {
		Element root = doc.getRootElement();
		Element query =root.element("Query");
		Element plan = query.element("Plan");
		Element startTime =  plan.element("Actual-Startup-Time");
		Element totalTime =  plan.element("Actual-Total-Time");
		Element readBlcocks = plan.element("Shared-Read-Blocks");
		double begin = Double.parseDouble(startTime.getText());
		double end = Double.parseDouble(totalTime.getText());
		int blocks = Integer.parseInt(readBlcocks.getText());
		cpu_tuple_cost = (end - begin -blocks * seq_page_cost)/1100000;
	}


	private void execExplain(Connection conn) throws SQLException, DocumentException {
		Statement sta = conn.createStatement();
		ResultSet rs = sta.executeQuery("explain (analyze,verbose,costs,buffers,timing,format XML) select * from tbl_cost_align; ");
		rs.next();
		doc = DocumentHelper.parseText(rs.getString(1));
		rs.close();
		sta.close();
	}

	private int queryPid(Connection conn) throws SQLException {
		Statement sta = conn.createStatement();
		ResultSet rs = sta.executeQuery("select sys_backend_pid()");
		rs.next();
		int pid = rs.getInt(1);
		return pid;
	}


	private ArrayList<String> clearCacheCMD(String passwd) {
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("bash");
		cmd.add("-c");
		cmd.add("echo " + passwd + " | sudo -S sysctl -w vm.drop_caches=3 2>&1");
		return cmd;
	}

	private ArrayList<String> stopCMD(String installCatalog, String dataCatalog) {
		ArrayList<String> cmd = new ArrayList<String>();
		String CMDPATH = installCatalog + "/bin/sys_ctl";
		cmd.add(CMDPATH);
		cmd.add("stop");
		cmd.add("-D");
		cmd.add(dataCatalog);
		cmd.add("-w");
		return cmd;
	}

	private void prepareWork(Connection con) throws SQLException{
		Statement sta = con.createStatement();
		sta.execute("create table tbl_cost_align (id int, info text, crt_time timestamp)");
		sta.execute("insert into tbl_cost_align select (random()*2000000000)::int, md5(random()::text), clock_timestamp() from generate_series(1,100000)");
		sta.execute("insert into tbl_cost_align select (random()*2000000000)::int, md5(random()::text), clock_timestamp() from generate_series(1,1000000)");
		sta.execute("analyze tbl_cost_align");		
		sta.execute("checkpoint");
		con.close();
	}


	private ArrayList<String> startCMD(String installCatalog,String dataCatalog) throws Exception {
		ArrayList<String> cmd = new ArrayList<String>();
		String CMDPATH = installCatalog + "/bin/sys_ctl";
		cmd.add(CMDPATH);
		cmd.add("start");
		cmd.add("-D");
		cmd.add(dataCatalog);
		cmd.add("-l");
		cmd.add(dataCatalog + "/kingbase.log");
		cmd.add("-w");
		cmd.add("-o");
		cmd.add("-p 6666");
		return cmd;
	}

	private ArrayList<String>initCMD(String installCatalog, String dataCatalog){
		ArrayList<String> cmd = new ArrayList<String>();
		String CMDPATH = installCatalog + "/bin/initdb";
		cmd.add(CMDPATH);
		cmd.add("-D");
		cmd.add(dataCatalog);
		cmd.add("-U");
		cmd.add("pig");
		cmd.add("-W");
		cmd.add("123");
		return cmd;
	}

	private void execCMD(ArrayList<String> buffer, String installCatalog) throws Exception {
		ProcessBuilder builder = new ProcessBuilder(buffer);
		builder.redirectErrorStream(true);
		if (installCatalog != null) {
			Map<String, String> env = builder.environment();
			env.put("LD_LIBRARY_PATH", installCatalog + "/lib");
		}
		Process process = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuffer CMDoutput = new StringBuffer();
		while ((line = br.readLine()) != null) {			
			CMDoutput.append(line + "\n");
		}

		int exitValue = process.waitFor();

		if (exitValue != 0) {
			errMsg = CMDoutput.toString();
			throw new Exception(CMDoutput.toString());
		}
	}

	private void execSysTap(ArrayList<String> buffer, String installCatalog,String dataCatalog, String passwd, Connection conn) throws Exception   {
		ProcessBuilder builder = new ProcessBuilder(buffer);
		builder.redirectErrorStream(true);
		builder.start();
		Thread.sleep(10000);
		execExplain(conn); 
		Thread.sleep(8000);
		
		File file = new File(dataCatalog+"/aa");
		FileReader fr = new FileReader(file);
		br = new BufferedReader(fr);
		int index=0;
		String line="";
		StringBuffer sb = new StringBuffer();
					
		while((line = br.readLine()) !=  null){	
			sb.append(line);
			if(Pattern.matches("\\d+\\*\\*\\d+\\*\\*\\d+", line)){
					index=line.lastIndexOf("**");
					
					System.out.println(line);
					pageCost = Double.parseDouble(line.substring(index+2));
					pageCost = pageCost/1000000;
					break;
			}		
			
	   }
		errMsg = "can't get message form stap."+sb.toString();
		cmd = killSysTap(passwd);
	    execCMD(cmd, installCatalog);
				
	}
		



	private ArrayList<String> killSysTap(String passwd) {
		ArrayList<String> cmd= new ArrayList<String>();
		cmd.add("bash");
		cmd.add("-c");
		cmd.add("echo " + passwd + " | " + "sudo -S killall -9 stap  >/dev/null  2>&1");
		return cmd;
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException  {

		String userName = "pig";
		String password = "123";
		String url = "jdbc:kingbase8://localhost:6666/TEST";
		Class.forName("com.kingbase8.Driver");
		Properties props = new Properties();
		props.put("user", userName); //$NON-NLS-1$
		props.put("password", password); //$NON-NLS-1$
		props.put("preferQueryMode", "simple");

		Connection con = DriverManager.getConnection(url,props);

		return con;
	}

	public void closeDatabase(String installCatalog, String dataCatalog) throws Exception {
		cmd = stopCMD(installCatalog,dataCatalog);
		execCMD(cmd,installCatalog);		
	}


}