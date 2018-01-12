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
public class IoTuning extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;
	private IFile file = null;
	private static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.folder);

	public IoTuning(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public Image getImage(Object arg0) {
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return "性能调优";
	}

	private boolean hasInit = false;

	public boolean isOpen() {
		return hasInit;
	}

	public boolean hasChildren() {
		
		return false;
	}

	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}

	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		file = (IFile) folder.findMember("ioTuning.xml");
		if (file != null && file.exists()) {
			
		}
		setHasInit(true);
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
