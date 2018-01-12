package com.kingbase.db.deploy.bundle.model.tree;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.core.util.ImageURL;

/**
 * 节点信息
 * 
 * @author feng
 *
 */
public class CNodeEntity extends CTableTreeNode implements ITreeProvider {

	private String name;
	private String ip;
	private String port;
	private String rootPass;
	private String user;
	private String dPath;
	private String nodePath;
	
	//均用于展示监控图
	private String type;
	private String library;
	private Point location;

	private String listenerPost;

	public String getLibrary() {
		return library;
	}
	
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getRootPass() {
		return rootPass;
	}

	public void setRootPass(String rootPass) {
		this.rootPass = rootPass;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getdPath() {
		return dPath;
	}

	public void setdPath(String dPath) {
		this.dPath = dPath;
	}

	public boolean isHasInit() {
		return hasInit;
	}

	private boolean hasInit = false;

	public boolean isOpen() {
		return hasInit;
	}

	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}

	public boolean hasChildren() {
		 
		return false;
	}

	private boolean isSaveP = true;

	public boolean isSaveP() {
		return isSaveP;
	}

	public void setSaveP(boolean isSaveP) {
		this.isSaveP = isSaveP;
	}

	public String getListenerPost() {
		return listenerPost;
	}

	public void setListenerPost(String listenerPost) {
		this.listenerPost = listenerPost;
	}

	public Point getLocation() {
		return location;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	@Override
	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		setHasInit(false);
	}

	public void refresh() {
		setHasInit(false);
		removeAll();
		treeExpanded();
	}

	@Override
	public Image getImage(Object element) {

		return ImageURL.createImage(KBDeployCore.PLUGIN_ID, ImageURL.replication);
	}

	@Override
	public String getText(Object element) {
		return name;
	}
}