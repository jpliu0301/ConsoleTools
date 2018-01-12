/**
 * 
 */
package com.kingbase.db.deploy.bundle.graphical.model;

import java.util.UUID;

import org.pentaho.di.graphical.model.BendpointConnection;

import com.kingbase.db.core.editorinput.DataBaseInput;

/**
 * @author jpliu
 *
 */
public class DeployConnection extends BendpointConnection {

	private static final long serialVersionUID = 1L;
	private DataBaseInput input;
	private String status = "";

	public DeployConnection(DataBaseInput input) {
		this(UUID.randomUUID().toString(), "连接");
		this.input = input;
	}

	public DeployConnection(String id, String name) {
		super(id, name);
	}

	public void attachSource() {
		super.attachSource();
	}

	/**
	 * 绑定目标
	 */
	public void attachTarget() {
		super.attachTarget();
	}

	public DataBaseInput getInput() {
		return input;
	}

	public void setInput(DataBaseInput input) {
		this.input = input;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
