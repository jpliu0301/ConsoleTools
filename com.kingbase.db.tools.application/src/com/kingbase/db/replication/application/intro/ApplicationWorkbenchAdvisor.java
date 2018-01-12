package com.kingbase.db.replication.application.intro;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.Perspective_ID;
	}

	@Override
	public void postStartup() {
		super.postStartup();
		// startVersionChecker();
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return;
		}

		// 关闭编辑器
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (activeWindow != null) {
			IWorkbenchPage PAGE = activeWindow.getActivePage();
			IEditorPart[] editorPart = PAGE.getEditors();
			for (int j = 0; j < editorPart.length; j++) {
				PAGE.closeEditor(editorPart[j], true);
			}
		}
	}
	 @Override
	public boolean preShutdown() {
		if (!saveAndCleanup()) {
			// User rejected to exit
			return false;
		} else {
			return super.preShutdown();
		}
	}

	private boolean saveAndCleanup() {
		try {
			IWorkbenchWindow window = getWorkbenchConfigurer().getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				if (!MessageDialog.openConfirm(window.getShell(), "提示",
						"你确定要退出吗?")) {
					return false;
				}
				// Close al content editors
				IWorkbenchPage workbenchPage = window.getActivePage();
				IEditorReference[] editors = workbenchPage
						.getEditorReferences();
				for (IEditorReference editor : editors) {
					IEditorPart editorPart = editor.getEditor(false);
					if (editorPart != null) {
						workbenchPage.closeEditor(editorPart, false);
					}
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
	@Override
    public void postShutdown() {
        super.postShutdown();
    }
}
