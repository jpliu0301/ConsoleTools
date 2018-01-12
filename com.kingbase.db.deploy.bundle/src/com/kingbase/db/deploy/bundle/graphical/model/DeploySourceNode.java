package com.kingbase.db.deploy.bundle.graphical.model;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Point;
import org.pentaho.di.graphical.model.ActivitySourceNode;

import com.kingbase.db.deploy.bundle.model.tree.CNodeEntity;
import com.kingbase.db.core.editorinput.DataBaseInput;

public class DeploySourceNode extends ActivitySourceNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataBaseInput input;
	private CNodeEntity node;
	private boolean isPool;
	private boolean isMain;
	public DeploySourceNode(DataBaseInput input, CNodeEntity node, boolean isPool,boolean isMain) {
		super(UUID.randomUUID().toString(), "源点");
		this.input = input;
		this.node = node;
		this.isPool = isPool;
		this.isMain = isMain;
		setName(node.getIp()+"\n      "+node.getListenerPost());
	}

	public void fromXML(Point point) {
		setLocation(point);
	}

	public DataBaseInput getInput() {
		return input;
	}

	public void setInput(DataBaseInput input) {
		this.input = input;
	}

	public CNodeEntity getNode() {
		return node;
	}

	public void setNode(CNodeEntity node) {
		this.node = node;
	}

	public boolean isPool() {
		return isPool;
	}

	public void setPool(boolean isPool) {
		this.isPool = isPool;
	}

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}
 

}
