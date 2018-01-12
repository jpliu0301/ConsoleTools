package com.kingbase.db.core.util;

/**
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;


/**
 * 
 * @author jpliu
 *
 */
public class KBProgressDialog {
	private ProgressMonitorDialog dialog;
	public KBBooleanFlag stopFlag = new KBBooleanFlag();

	private String message = "";
	long pid = 0;

	public KBProgressDialog(Shell shell, String message) {
		dialog = new ProgressMonitorDialog(shell);
		this.message = message;
	}

	public void run(boolean cancelable, final IKBProgressRunnable runnable) {
		new Thread() {
			public void run() {
				runnable.run(stopFlag);
			}
		}.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(runnable instanceof AKBProgressRunnableWithPid){
			pid = ((AKBProgressRunnableWithPid) runnable).getPid();
		}
		try {
			dialog.run(true, cancelable, new KBRunnable());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class KBRunnable implements IRunnableWithProgress {
		public void run(IProgressMonitor monitor) {
			monitor.beginTask(message, 30);
			int i = 0;
			while (true) {
				if (monitor.isCanceled() || stopFlag.getFlag()) {
					stopFlag.setFlag(true);
					if(pid>0)
						kill();
					break;
				}
				if ((++i) == 30) {
					i = 0;
					monitor.beginTask(message, 30);
				}
				try {
					Thread.sleep(200);
				} catch (Throwable t) {
				}
				monitor.worked(1);
			}
			monitor.done();
		}

		private String kill() {
			List<String> commands = new ArrayList<String>();
			commands.add("kill");
			commands.add(pid+"");	
			ProcessBuilder builder = new ProcessBuilder(commands);
			builder.redirectErrorStream(true);
			Process process;
			StringBuffer txtDetailStr = new StringBuffer();
			try {
				process = builder.start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String line;
				while ((line = br.readLine()) != null) {
					txtDetailStr.append(line + "\n");
				}
				process.waitFor();
				int exitValue = process.exitValue();
				process.destroy();
				if (exitValue != 0) {// 若是执行成功，则会返回0，出错则不会为0
					return "错误日志：\n" + commands.toString() + "\n" + txtDetailStr.toString();
				}
			} catch (IOException e) {
				e.printStackTrace();
				
				return "错误日志：\n" + e.getMessage();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return "错误日志：\n" + e.getMessage();
			}
			return null;
		}
	}

}