package com.kingbase.db.deploy.bundle.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.pentaho.di.graphical.editpart.AbstractEditPart;
import org.pentaho.di.graphical.model.AbstractActivityNode;

import com.kingbase.db.deploy.bundle.graphical.figure.DeployFigure;
import com.kingbase.db.deploy.bundle.graphical.model.DeploySourceNode;
import com.kingbase.db.deploy.bundle.graphical.model.DeployTargetNode;

public class DeployContetntsPart extends AbstractEditPart {

	@Override
	protected IFigure createFigure() {

		AbstractActivityNode model = (AbstractActivityNode) getModel();
		if (model instanceof DeploySourceNode) {
			DeploySourceNode sourceNode = (DeploySourceNode) model;
			String tooltip = getSourceTooltip(sourceNode);
			DeployFigure wfigure = new DeployFigure(tooltip, sourceNode.isPool(),sourceNode.isMain(),sourceNode.getNode().getType());
			return wfigure;
		} else if (model instanceof DeployTargetNode) {

			DeployTargetNode targetNode = (DeployTargetNode) model;
			String tooltip = getTargetTooltip(targetNode);
			DeployFigure wfigure = new DeployFigure(tooltip, targetNode.isPool(),targetNode.isMain(),targetNode.getNode().getType());
			return wfigure;
		}
		return null;
	}

	@Override
	protected void createEditPolicies() {
	}

	private String getSourceTooltip(DeploySourceNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" host： ");
		buffer.append(node.getNode().getIp() + "\n");
		buffer.append(" port： ");
		buffer.append(node.getNode().getPort() + "\n");

		if (node.isPool() && node.isMain()) {
			buffer.append(" 类型：主pool "+ "\n");
		}else if(!node.isPool() && node.isMain()){
			buffer.append(" 类型：主库 "+ "\n");
		}else if(node.isPool() && !node.isMain()){
			buffer.append(" 类型：备pool "+ "\n");
		}else{
			buffer.append(" 类型：备库 "+ "\n");
		}
		buffer.append(" 状态： ");
		buffer.append(node.getNode().getType() + "\n");

		return buffer.toString();
	}

	private String getTargetTooltip(DeployTargetNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" host： ");
		buffer.append(node.getNode().getIp() + "\n");
		buffer.append(" port： ");
		buffer.append(node.getNode().getPort() + "\n");
		if (node.isPool() && node.isMain()) {
			buffer.append(" 类型：主pool "+ "\n");
		}else if(!node.isPool() && node.isMain()){
			buffer.append(" 类型：主库 "+ "\n");
		}else if(node.isPool() && !node.isMain()){
			buffer.append(" 类型：备pool "+ "\n");
		}else{
			buffer.append(" 类型：备库 "+ "\n");
		}
		buffer.append(" 状态： ");
		buffer.append(node.getNode().getType() + "\n");
		return buffer.toString();
	}

}
