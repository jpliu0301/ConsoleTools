package com.kingbase.db.replication.application.intro;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	public final static String APPLICATION_ID = "com.kingbase.db.replication.application";
	public static final String ENV_USER_HOME = "user.home";
	public static final String DBEAVER_DEFAULT_DIR = ".kingbase/@@TOOL_NAME@@"; //$NON-NLS-1$

	@Override
	public Object start(IApplicationContext context) {
		Location instanceLoc = Platform.getInstanceLocation();
		String defaultHomePath = getDefaultWorkspaceLocation()
				.getAbsolutePath();
		Display display = null;
		try {
			URL defaultHomeURL = new URL("file", //$NON-NLS-1$
					null, defaultHomePath);

			if (!instanceLoc.set(defaultHomeURL, true)) {

				if (display == null) {
					display = PlatformUI.createDisplay();
				}
			}

			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} catch (Throwable e) {

		} finally {
			if (display != null) {
				display.dispose();
			}
		}

		if (display == null) {
			display = PlatformUI.createDisplay();
		}

		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

	private static File getDefaultWorkspaceLocation() {
		return new File(System.getProperty(ENV_USER_HOME), DBEAVER_DEFAULT_DIR);
	}
}
