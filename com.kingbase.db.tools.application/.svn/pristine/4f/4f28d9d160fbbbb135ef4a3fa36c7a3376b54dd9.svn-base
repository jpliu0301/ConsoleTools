package com.kingbase.db.replication.application.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * @author Duke
 * 
 */
public class OpenPreferencesAction extends Action implements ActionFactory.IWorkbenchAction {

	public static String[] items = null;

	static {
		items = new String[] { "com.kingbase.db.replication.application.preference.ReplicationDriverManagerPage" };
	}

	/**
	 * The workbench window; or <code>null</code> if this action has been
	 * <code>dispose</code>d.
	 */
	private IWorkbenchWindow workbenchWindow;

	/**
	 * Create a new <code>OpenPreferenceAction</code> This default constructor
	 * allows the the action to be called from the welcome page.
	 */
	public OpenPreferencesAction() {
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}

	/**
	 * Create a new <code>OpenPreferenceAction</code> and initialize it from the
	 * given resource bundle.
	 * 
	 * @param window
	 */
	public OpenPreferencesAction(IWorkbenchWindow window) {
		super("&Preferences");
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.workbenchWindow = window;
		setToolTipText("&Preferences");
	}

	@Override
	public void run() {
		if (workbenchWindow == null) {
			return;
		}
		PreferencesUtil.createPreferenceDialogOn(workbenchWindow.getShell(), items[0], items, null).open();
	}

	@Override
	public void dispose() {
		workbenchWindow = null;
	}

}
