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

public class DeleteAction extends SelectionAction {

	private AbstractActivityNode node;
	private CNodeEntity entity;
	private CreateReadWriteStatusEditor editor;
	public DeleteAction(IWorkbenchPart part, AbstractActivityNode node, CreateReadWriteStatusEditor editor) {
		super(part);
		setId(UUID.randomUUID().toString());
		this.node = node;
		this.editor = editor;
		setText("删 除");
		setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_delete));
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
			openChannel.setCommand("su - "+entity.getUser()+" -c \"pcp_detach_node\"");
			openChannel.connect();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			openChannel.disconnect();
			session.disconnect();
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}