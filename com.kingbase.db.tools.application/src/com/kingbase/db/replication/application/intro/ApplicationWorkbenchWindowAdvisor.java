package com.kingbase.db.replication.application.intro;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.Bundle;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private String viewId ;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer,viewId);
    }
    
	public void preWindowOpen() {
		String title = "Kingbase 工具";
		Map<String, String> map = new HashMap<String,String>();
		map.put("com.kingbase.db.replication.bundle", "逻辑同步工具" + "-com.kingbase.db.replication.application.view.ReplicationView");
		map.put("com.kingbase.db.deploy.bundle", "数据库部署工具" + "-com.kingbase.db.tools.application.view.DeployView");
		map.put("com.kingbase.db.console.bundle", "控制台工具" + "-com.kingbase.db.console.bundle.views.ConsoleView");
		for (String key : map.keySet()) {
			Bundle bundle = Platform.getBundle(key);
			if (bundle != null) {
				String string = map.get(key);
				title = (string.split("-"))[0];// 工具名
				map = null;
				viewId = (string.split("-"))[1];// 菜单对象
				break;
			}
		}

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		Rectangle screenSize = Display.getDefault().getClientArea();
		configurer.setInitialSize(new Point(screenSize.width, screenSize.height));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShellStyle(SWT.MIN | SWT.MAX | SWT.CLOSE);
		configurer.setTitle(title);
	}
    
    public void postWindowOpen() {
		super.postWindowOpen();
		Shell shell = getWindowConfigurer().getWindow().getShell();
		Rectangle screenSize = Display.getDefault().getClientArea();
		Rectangle frameSize = shell.getBounds();
		shell.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
}
