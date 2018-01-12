package com.kingbase.db.core.editorinput;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.pentaho.di.viewer.CBasicTreeViewer;
import org.pentaho.di.viewer.CTableTreeNode;

public class DataBaseInput implements IEditorInput {

	private CTableTreeNode node;
	private String name;
	private CBasicTreeViewer treeView;
	private String type;

	public DataBaseInput(CTableTreeNode node, String name,
			CBasicTreeViewer treeView) {
		this.node = node;
		this.name = name;
		this.treeView = treeView;
	}

	public DataBaseInput(CTableTreeNode node, String name, String type,
			CBasicTreeViewer treeView) {
		this.node = node;
		this.name = name;
		this.type = type;
		this.treeView = treeView;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj)
			return false;

		if (!(obj instanceof DataBaseInput))
			return false;

		if (!getName().equals(((DataBaseInput) obj).getName()))
			return false;
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	public CTableTreeNode getNode() {
		return node;
	}

	public void setNode(CTableTreeNode node) {
		this.node = node;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CBasicTreeViewer getTreeView() {
		return treeView;
	}

	public void setTreeView(CBasicTreeViewer treeView) {
		this.treeView = treeView;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
