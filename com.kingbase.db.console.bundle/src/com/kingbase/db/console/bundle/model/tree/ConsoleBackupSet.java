/**
 * 
 */
package com.kingbase.db.console.bundle.model.tree;

import org.eclipse.swt.graphics.Image;
import org.pentaho.di.model.IBasicModel;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.core.util.ImageURL;

/**
 * @author jpliu
 *
 */
public class ConsoleBackupSet extends CTableTreeNode implements ITreeProvider {

	private String backupSetName;// 备份集名称
	private String backupSetPath;
	private String databasePath;
	private String clientPath;
	
	private String address;
	private String password;
	private String user;
	private String port;
	public static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.tree_subscriber_enable);

	@Override
	public Image getImage(Object arg0) {
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return backupSetName;
	}

	@Override
	public String getName() {
		return backupSetName;
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

	public String getBackupSetName() {
		return backupSetName;
	}

	public void setBackupSetName(String backupSetName) {
		this.backupSetName = backupSetName;
	}

	public String getBackupSetPath() {
		return backupSetPath;
	}

	public void setBackupSetPath(String backupSetPath) {
		this.backupSetPath = backupSetPath;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	public String getClientPath() {
		return clientPath;
	}

	public void setClientPath(String clientPath) {
		this.clientPath = clientPath;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
}
