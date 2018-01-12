package com.kingbase.db.console.bundle.model.tree;

import java.sql.Connection;

import org.eclipse.swt.graphics.Image;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.core.util.ImageURL;

public class LogAnalysisInfo extends CTableTreeNode implements ITreeProvider {

	
	private String id;
	private String name;
	private Connection connection;
	private String message="";
	public static final Image image = ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.file);
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Image getImage(Object arg0) {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public String getText(Object arg0) {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
