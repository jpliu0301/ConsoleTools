package com.kingbase.db.deploy.bundle.model.tree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.pentaho.di.model.IBasicModel;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.core.util.ImageURL;
import com.kingbase.db.deploy.bundle.KBDeployCore;

/**
 * 节点
 * @author feng
 *
 */
public class NodeEntity extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;

	public NodeEntity(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public Image getImage(Object arg0) {
		return ImageURL.createImage(KBDeployCore.PLUGIN_ID,ImageURL.tree_database_enable);
	}

	@Override
	public String getText(Object arg0) {
		return "节点";
	}


	public IFolder getFolder() {
		return folder;
	}
	private boolean hasInit = false;

	public boolean isOpen() {
		return hasInit;
	}

	public boolean hasChildren() {
		if (!hasInit)
			return true;
		return super.hasChildren();
	}

	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}

	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		try {
			IFile file = null;
				file = (IFile) folder.findMember("cnode.xml");
				if (file != null && file.exists()) {
					getReleaseSource(file);
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		setHasInit(true);
	}
	
	@SuppressWarnings("unchecked")
	private void getReleaseSource(IFile file) throws DocumentException {

		List<CNodeEntity> list = new ArrayList<CNodeEntity>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new BufferedReader(new InputStreamReader(new FileInputStream(file.getLocation().toFile()),"utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> listEle = root.elements();

		for (int i = 0, n = listEle.size(); i < n; i++) {
			CNodeEntity nodeEntity = new CNodeEntity();
			Element element = listEle.get(i);
			if (element.element("cnode") != null){
				continue;
			}
			nodeEntity.setName(element.element("name").getStringValue());
			nodeEntity.setIp(element.element("IP").getStringValue());
			nodeEntity.setPort(element.element("port").getStringValue());
			nodeEntity.setUser(element.element("user").getStringValue());
			nodeEntity.setRootPass(element.element("rootPassword").getStringValue());
			nodeEntity.setdPath(element.element("defaultPath").getStringValue());
			nodeEntity.setNodePath(element.element("nodePath").getStringValue());
			
			list.add(nodeEntity);
			
		}
		
		Collections.sort(list, new Comparator<CNodeEntity>() {

			@Override
			public int compare(CNodeEntity o1, CNodeEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (CNodeEntity node : list) {
			this.addChild(node);
		}

	}
    @Override
	public IBasicModel[] getChildren() {
		return super.getChildren();
	}

	public void refresh() {
		setHasInit(false);
		try {
			folder.refreshLocal(1, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		removeAll();
		treeExpanded();
	}

	
}