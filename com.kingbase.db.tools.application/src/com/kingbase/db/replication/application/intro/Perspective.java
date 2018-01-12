package com.kingbase.db.replication.application.intro;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String Perspective_ID = "com.kingbase.db.replication.application.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		String editorArea = layout.getEditorArea();
		layout.createFolder("left",IPageLayout.LEFT,0.20f,editorArea);
		layout.createPlaceholderFolder("rigth",IPageLayout.RIGHT,0.8f,editorArea);
	}
}
