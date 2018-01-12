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

import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.core.util.ImageURL;

/**
 * 主备同步
 * @author feng
 *
 */
public class MasterStandbyEntity extends CTableTreeNode implements ITreeProvider {

	private IFolder folder = null;

	public MasterStandbyEntity(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public Image getImage(Object arg0) {
		return ImageURL.createImage(KBDeployCore.PLUGIN_ID,ImageURL.tree_database_enable);
	}

	@Override
	public String getText(Object arg0) {
		return "主备同步";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "主备同步";
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
	
	public void treeExpanded() {
		if (isOpen()) {
			return;
		}
		try {
			IFile file = null;
				file = (IFile) folder.findMember("master.xml");
				if (file != null && file.exists()) {
					getReleaseSource(file);
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		setHasInit(true);
	}
	
	private void getReleaseSource(IFile file) throws DocumentException {

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new BufferedReader(new InputStreamReader(new FileInputStream(file.getLocation().toFile()),"UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> listEle = root.elements();

		List<CReadWriteEntity> listChild = new ArrayList<CReadWriteEntity>();
		for (int i = 0, n = listEle.size(); i < n; i++) {
			CReadWriteEntity nodeEntity = new CReadWriteEntity();
			Element element = listEle.get(i);
			if (element.element("master") != null){
				continue;
			}
			nodeEntity.setName(element.element("name").getStringValue());
			PosEntity psoEntity=new PosEntity();
			psoEntity.setName(element.element("name").getStringValue());
			psoEntity.setDbzipPath(element.element("dbzipPath").getStringValue());
			
			List<TableNodeEntity> list=new ArrayList<TableNodeEntity>();
			Element ele=element.element("dbNodes");
			List<Element> listEleNode=ele.elements("dbNode");
			for (int j = 0; j < listEleNode.size(); j++) {
				TableNodeEntity en=new TableNodeEntity();
				en.setLibrary(listEleNode.get(j).attributeValue("library"));
				en.setPhysicalMachine(listEleNode.get(j).attributeValue("physicalMachine"));
				en.setNodeType(listEleNode.get(j).attributeValue("nodeType"));
				en.setListenerAddress(listEleNode.get(j).attributeValue("listenerAddress"));
				en.setListenerPost(listEleNode.get(j).attributeValue("listenerPost"));
				
				List<Element> cnodeList = listEleNode.get(j).elements("cnode");
				for (Element element2 : cnodeList) {
					CNodeEntity node = new CNodeEntity();
					node.setName(element2.element("name").getStringValue());
					node.setIp(element2.element("IP").getStringValue());
					node.setPort(element2.element("port").getStringValue());
					node.setUser(element2.element("user").getStringValue());
					node.setRootPass(element2.element("rootPassword").getStringValue());
					node.setdPath(element2.element("defaultPath").getStringValue());
					en.setNodeEntity(node);
				}
				list.add(en);
			}
			psoEntity.setListDb(list);
			
			psoEntity.setIsDbBase(Boolean.parseBoolean(element.element("isDbBase").getStringValue()));
			if(element.element("isDbBase").getStringValue().equals("true")){
				psoEntity.setMax_wal(element.element("max_wal_senders").getStringValue());
				psoEntity.setMax_standby_arc(element.element("max_standby_archive_delay").getStringValue());
				psoEntity.setWal_keep(element.element("wal_keep_segments").getStringValue());
				psoEntity.setMax_standby_str(element.element("max_standby_streaming_delay").getStringValue());
				psoEntity.setReplication(element.element("replication_timeout").getStringValue());
				psoEntity.setWal_receiver(element.element("wal_receiver_status_interval").getStringValue());
				psoEntity.setHot_standby(element.element("hot_standby_feedback").getStringValue());
				psoEntity.setListenerPort(element.element("listenerPort").getStringValue());
				psoEntity.setDbUser(element.element("dbUser").getStringValue());
				psoEntity.setDbPassword(element.element("dbPassword").getStringValue());
				psoEntity.setInsensitive(element.element("caseInsensitive").getStringValue().equals("true"));
			}
			else{
				psoEntity.setDbzipPath(element.element("posPath").getStringValue());
				
				List<KeyValueEntity> listk=new ArrayList<KeyValueEntity>();
				Element elek=element.element("dbKeyValueNodes");
				List<Element> listEleNodek=elek.elements("dbKeyValueNode");
				for (int j = 0; j < listEleNodek.size(); j++) {
					KeyValueEntity en=new KeyValueEntity();
					en.setKey(listEleNode.get(i).getName());
					en.setValue((listEleNode.get(i).attributeValue(listEleNode.get(i).getName())));
					listk.add(en);
				}
				psoEntity.setListKey1(listk);
			}
			
			PoolEntity poolEntity=new PoolEntity();
			
			List<TableNodeEntity> list1=new ArrayList<TableNodeEntity>();
			Element ele1=element.element("poolNodes");
			List<Element> listEleNode1=ele1.elements("poolNode");
			for (int j = 0; j < listEleNode1.size(); j++) {
				TableNodeEntity en=new TableNodeEntity();
				en.setLibrary(listEleNode1.get(j).attributeValue("library"));
				en.setPhysicalMachine(listEleNode1.get(j).attributeValue("physicalMachine"));
				en.setNodeType(listEleNode1.get(j).attributeValue("nodeType"));
				en.setListenerAddress(listEleNode1.get(j).attributeValue("listenerAddress"));
				en.setListenerPost(listEleNode1.get(j).attributeValue("listenerPost"));
				
				List<Element> cnodeList = listEleNode1.get(j).elements("cnode");
				for (Element element2 : cnodeList) {
					CNodeEntity node = new CNodeEntity();
					node.setName(element2.element("name").getStringValue());
					node.setIp(element2.element("IP").getStringValue());
					node.setPort(element2.element("port").getStringValue());
					node.setUser(element2.element("user").getStringValue());
					node.setRootPass(element2.element("rootPassword").getStringValue());
					node.setdPath(element2.element("defaultPath").getStringValue());
					en.setNodeEntity(node);
				}
				list1.add(en);
			}
			poolEntity.setListPool(list1);
			poolEntity.setPoolzipPath(element.element("poolzipPath").getStringValue());

			poolEntity.setIsPoolBase(Boolean.parseBoolean(element.element("isPoolBase").getStringValue()));
			if(element.element("isPoolBase").getStringValue().equals("true")){
				
				poolEntity.setDelegate_IP(element.element("delegate_IP").getStringValue());
				poolEntity.setPort(element.element("port").getStringValue());
				poolEntity.setPcp_port(element.element("pcp_port").getStringValue());
				poolEntity.setCheck_db(element.element("check_db").getStringValue());
				poolEntity.setWd_port(element.element("wd_port").getStringValue());
				poolEntity.setNetcard(element.element("netcard").getStringValue());
				poolEntity.setPcpUser(element.element("pcpUser").getStringValue());
				poolEntity.setPcpPass(element.element("pcpPass").getStringValue());
			}
			else{
				poolEntity.setPoolPath(element.element("poolPath").getStringValue());

				List<KeyValueEntity> listk=new ArrayList<KeyValueEntity>();
				Element elek=element.element("poolKeyValueNodes");
				List<Element> listEleNodek=elek.elements("poolKeyValueNode");
				for (int j = 0; j < listEleNodek.size(); j++) {
					KeyValueEntity en=new KeyValueEntity();
					en.setKey(listEleNode.get(i).getName());
					en.setValue((listEleNode.get(i).attributeValue(listEleNode.get(i).getName())));
					listk.add(en);
				}
				poolEntity.setListKey2(listk);
			}
			nodeEntity.setPosEntity(psoEntity);
			nodeEntity.setPoolEntity(poolEntity);
			
			listChild.add(nodeEntity);
		}
		Collections.sort(listChild, new Comparator<CReadWriteEntity>() {

			@Override
			public int compare(CReadWriteEntity o1, CReadWriteEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (CReadWriteEntity node : listChild) {
			this.addChild(node);
		}

	}
}
	