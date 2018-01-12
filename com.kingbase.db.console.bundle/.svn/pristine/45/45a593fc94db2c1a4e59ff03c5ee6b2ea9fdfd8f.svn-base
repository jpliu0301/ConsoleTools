package com.kingbase.db.console.bundle.model.tree;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.model.IBasicModel;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.core.util.ImageURL;
import com.kingbase.db.core.util.UIUtils;

public class LogAnalysis extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;
	private IFile file = null;
	
	private String address;
	private String password;
	private String user;
	private String port;
	private String database;
	
	private String driverPath;
	private String driverName;
	private Connection connection;
    public static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.folder);

	public LogAnalysis(IFolder folder) {
		this.folder = folder;
	}
	public LogAnalysis() {
	}

	@Override
	public Image getImage(Object arg0) {
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return "日志分析";
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

		// 刷新操作
		getChilds(this);
		setHasInit(true);
	}

	public static void getChilds(LogAnalysis logAnalysis) {
		logAnalysis.removeAll();
		// 刷新操作
		if (logAnalysis.getConnection() != null) {
			try {
				DatabaseMetaData metaData = logAnalysis.getConnection().getMetaData();
				String schemaTerm = metaData.getSchemaTerm();
				if (!schemaTerm.equals("")) { //$NON-NLS-1$
					Statement stm = logAnalysis.getConnection().createStatement();
					ResultSet resultSet = null;
					resultSet = stm.executeQuery("select id,name from log_analyse.log_analyse"); //$NON-NLS-1$
					while (resultSet.next()) {
						String id = resultSet.getString("id"); //$NON-NLS-1$
						String name = resultSet.getString("name"); //$NON-NLS-1$
						LogAnalysisInfo info = new LogAnalysisInfo();
						
						info.setConnection(logAnalysis.getConnection());
						info.setId(id);
						info.setName(name);
						logAnalysis.addChild(info);
					}
					resultSet.close();
					stm.close();
					if (resultSet != null) {
						resultSet.close();
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				MessageDialog.openError(UIUtils.getActiveShell(), "错误", e1.getMessage()); //$NON-NLS-1$
			}
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

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	public String getDriverPath() {
		return driverPath;
	}
	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
