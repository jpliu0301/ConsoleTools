package com.kingbase.db.deploy.bundle.graphical.action;

import java.util.UUID;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.pentaho.di.graphical.model.AbstractActivityNode;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.deploy.bundle.graphical.editor.CreateReadWriteStatusEditor;
import com.kingbase.db.deploy.bundle.graphical.model.DeployContentsModel;
import com.kingbase.db.deploy.bundle.graphical.model.DeploySourceNode;
import com.kingbase.db.deploy.bundle.graphical.model.DeployTargetNode;
import com.kingbase.db.deploy.bundle.model.tree.CNodeEntity;
import com.kingbase.db.core.util.ImageURL;
import com.kingbase.db.core.util.JschUtil;

public class HandAction extends SelectionAction {
	private AbstractActivityNode node;
	private CNodeEntity entity;// 所以操作都是在主pool上进行的
	private String type;
	private CreateReadWriteStatusEditor editor;
	public HandAction(IWorkbenchPart part, AbstractActivityNode node, String type, CreateReadWriteStatusEditor editor) {
		super(part);
		setId(UUID.randomUUID().toString());
		this.node = node;
		this.type = type;
		this.editor = editor;
		if (type.equals("promote")) {
			setText("提升为主节点");
			setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_sync));
		} else if(type.equals("attach")){
			setText("加 入");
			setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_add));
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	@Override
	public void run() {
		super.run();
		if (node instanceof DeploySourceNode) {
			entity = ((DeployContentsModel) (node.getParentModel())).getMainNodePool();
		} else if (node instanceof DeployTargetNode) {
			entity = ((DeployContentsModel) (node.getParentModel())).getMainNodePool();
		}
		Session session = null;
		ChannelExec openChannel = null;
		try {
			session = JschUtil.connect(entity.getIp(), new Integer(entity.getPort()), "root",
					entity.getRootPass());
			openChannel = (ChannelExec) session.openChannel("exec");
			if (type.equals("promote")) {

				openChannel.setCommand("su - "+entity.getUser()+" -c \"pcp_promote_node\"");
			} else if(type.equals("attach")) {

				openChannel.setCommand("su - "+entity.getUser()+" -c \"pcp_attach_node\"");
			}
			openChannel.connect();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			editor.getContainerModel().refresh();
			editor.getContainerModel().fromXML(true);
			openChannel.disconnect();
			session.disconnect();
		}
	}

}
