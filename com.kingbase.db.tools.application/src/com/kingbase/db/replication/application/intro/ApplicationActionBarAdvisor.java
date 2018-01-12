package com.kingbase.db.replication.application.intro;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;

import com.kingbase.db.replication.application.action.AboutBoxAction;
import com.kingbase.db.replication.application.action.ToggleViewAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    protected IAction aboutAction;
    private String viewId;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer, String viewId) {
	super(configurer);
	this.viewId = viewId;
    }

    protected void makeActions(IWorkbenchWindow window) {

	aboutAction = new AboutBoxAction(window);
    }

    protected void fillMenuBar(IMenuManager menuBar) {

	MenuManager windowMenu = new MenuManager(" 窗口  ", IWorkbenchActionConstants.M_WINDOW); //$NON-NLS-1$
	menuBar.add(windowMenu);

	windowMenu.add(new ToggleViewAction(viewId));
	windowMenu.add(new Separator());

	MenuManager helpMenu = new MenuManager(" 帮助  ", "dbhelp"); //$NON-NLS-1$
	menuBar.add(helpMenu);

	// Help
	helpMenu.add(action(getActionBarConfigurer().getWindowConfigurer().getWindow(), IWorkbenchCommandConstants.HELP_WELCOME, "欢迎", null, false));
	helpMenu.add(aboutAction);
	helpMenu.add(new Separator());
	helpMenu.add(action(getActionBarConfigurer().getWindowConfigurer().getWindow(), IWorkbenchCommandConstants.HELP_HELP_CONTENTS, "帮助内容", null, false));
	helpMenu.add(action(getActionBarConfigurer().getWindowConfigurer().getWindow(), IWorkbenchCommandConstants.WINDOW_SHOW_KEY_ASSIST, "显示快捷键", null, false));

    }

    protected CommandContributionItem action(IServiceLocator serviceLocator, String commandId, String name, String toolTip, boolean showText) {
	final CommandContributionItemParameter contributionParameters = new CommandContributionItemParameter(serviceLocator, null, commandId, null, null, null, null, name, null, toolTip,
		CommandContributionItem.STYLE_PUSH, null, false);
	if (showText) {
	    contributionParameters.mode = CommandContributionItem.MODE_FORCE_TEXT;
	}

	return new CommandContributionItem(contributionParameters);
    }
}
