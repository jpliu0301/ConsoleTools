/**
 * 
 */
package com.kingbase.db.console.bundle.model.tree;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.model.IBasicModel;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.console.bundle.views.ConsoleView;
import com.kingbase.db.core.util.ImageURL;

/**
 * @author jpliu
 *
 */
public class ConsoleFile extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;
	private IFile file = null;
    public static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.folder);
	public ConsoleFile(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public Image getImage(Object arg0) {
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return "物理备份";
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

	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		file = (IFile) folder.findMember("backupSet.xml");
		if (file != null && file.exists()) {
			getBackupSetSource(file);
		}
		setHasInit(true);
	}

	private void getBackupSetSource(IFile file) {

		List<Element> listEle = ConsoleView.getlistEle(this);
		for (int i = 0, n = listEle.size(); i < n; i++) {
			ConsoleBackupSet backupSet = new ConsoleBackupSet();
			Element element = listEle.get(i);

			backupSet.setBackupSetName(element.element("backupSetName").getStringValue());
			backupSet.setBackupSetPath(element.element("backupSetPath").getStringValue());
			backupSet.setDatabasePath(element.element("databasePath").getStringValue());
			backupSet.setClientPath(element.element("clientPath").getStringValue());

			backupSet.setAddress(element.element("address").getStringValue());
			backupSet.setPort(element.element("port").getStringValue());
			backupSet.setUser(element.element("user").getStringValue());
			backupSet.setPassword(element.element("password").getStringValue());
			this.addChild(backupSet);
		}
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
