package com.kingbase.db.console.bundle.model.tree;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.model.IBasicModel;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;
import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.core.util.ImageURL;

public class ServiceManagementEntity extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;
	private IFile file = null;
	public static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.folder);

	public ServiceManagementEntity(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public Image getImage(Object arg0) {
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return "服务管理";
	}

	private boolean hasInit = false;

	public boolean isOpen() {
		return hasInit;
	}

	public boolean hasChildren() {
		boolean hasChildren = super.hasChildren();
		if (!hasInit)
			return true;
		return hasChildren;
	}

	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}



	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	@Override
	public IBasicModel[] getChildren() {
		return super.getChildren();
	}

	public void refresh() {
		setHasInit(false);
		removeAll();
		treeExpanded();
	}

	public IFolder getFolder() {
		return folder;
	}

	public void setFolder(IFolder folder) {
		this.folder = folder;
	}

}