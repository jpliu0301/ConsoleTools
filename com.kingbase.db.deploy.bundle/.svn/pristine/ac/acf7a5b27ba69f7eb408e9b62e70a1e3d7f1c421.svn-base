package com.kingbase.db.deploy.bundle.model.tree;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.core.util.ImageURL;

/**
 * 集群管理
 * 
 * @author feng
 *
 */
public class DeployRoot extends CTableTreeNode implements ITreeProvider {

	private boolean hasInit = false;

	private IProject project = null;

	public DeployRoot(IProject project) {
		this.project = project;
	}

	@Override
	public Image getImage(Object arg0) {
		return ImageURL.createImage(KBDeployCore.PLUGIN_ID, ImageURL.replication);
	}

	@Override
	public String getText(Object arg0) {
		return "集群管理";
	}

	protected boolean isOpen() {
		return hasInit;
	}

	public boolean hasChildren() {
		if (!hasInit)
			return true;
		return super.hasChildren();
	}

	protected void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}

	public void refresh() {
		setHasInit(false);
		removeAll();
		treeExpanded();
	}

	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		try {
			IResource[] resources = project.members();
			for (int i = 0; i < resources.length; i++) {
				IResource res = (IResource) resources[i];
				if (res instanceof IFolder) {
					IFolder folder = (IFolder) res;
					if (folder.getName().equals("cnode")) {
						NodeEntity conFolder = new NodeEntity(folder);
						this.addChild(conFolder);
					} else if (folder.getName().equals("masterstand")) {
						MasterStandbyEntity traFolder = new MasterStandbyEntity(folder);
						this.addChild(traFolder);
					} else if (folder.getName().equals("readwrite")) {
						ReadWriteEntity traFolder = new ReadWriteEntity(folder);
						this.addChild(traFolder);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		setHasInit(true);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}
	
}