/**
 * 
 */
package com.kingbase.db.deploy.bundle.graphical.editor;

import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.pentaho.di.graphical.model.AbstractActivityNode;

import com.kingbase.db.deploy.bundle.graphical.action.CommonReadWriterAction;
import com.kingbase.db.deploy.bundle.graphical.model.DeploySourceNode;
import com.kingbase.db.deploy.bundle.graphical.model.DeployTargetNode;
import com.kingbase.db.deploy.bundle.graphical.parts.DeployContetntsPart;
import com.kingbase.db.deploy.bundle.model.tree.CNodeEntity;

/**
 * @author jpliu
 *
 */
public class ReadWriterMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	private CreateReadWriteStatusEditor editor;
	private CNodeEntity entity;

	public ReadWriterMenuProvider(EditPartViewer viewer, ActionRegistry registry, CreateReadWriteStatusEditor editor) {
		super(viewer);
		if (registry == null) {
			throw new IllegalArgumentException();
		}
		actionRegistry = registry;
		this.editor = editor;
	}

	protected Object getSelection() {
		ISelection iSelection = editor.getSite().getSelectionProvider().getSelection();
		if (!(iSelection instanceof StructuredSelection))
			return null;
		StructuredSelection structuredSelection = (StructuredSelection) iSelection;
		Object obj = structuredSelection.getFirstElement();
		return obj;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		Object obj = getSelection();

		if (obj instanceof DeployContetntsPart) {
			DeployContetntsPart part = (DeployContetntsPart) obj;
			AbstractActivityNode node = (AbstractActivityNode) part.getModel();
			if (node instanceof DeploySourceNode) {
				entity = ((DeploySourceNode) node).getNode();
			} else if (node instanceof DeployTargetNode) {
				entity = ((DeployTargetNode) node).getNode();
			}
			CommonReadWriterAction actionStart = new CommonReadWriterAction(editor.getEditDomain().getEditorPart(), entity, "start",
					editor);
			actionRegistry.registerAction(actionStart);

			CommonReadWriterAction actionDisable = new CommonReadWriterAction(editor.getEditDomain().getEditorPart(), entity, "disable",
					editor);
			actionRegistry.registerAction(actionDisable);

			CommonReadWriterAction actionRestart = new CommonReadWriterAction(editor.getEditDomain().getEditorPart(), entity, "restart",
					editor);
			actionRegistry.registerAction(actionRestart);
			CNodeEntity mainNodeDB = editor.getContainerModel().getMainNodeDB();
			List<CNodeEntity> prepareNodeDBList = editor.getContainerModel().getPrepareNodeDBList();
			boolean flag = true;//都是停止状态
			if (mainNodeDB != null && !mainNodeDB.getType().equals("up")){
				for (CNodeEntity cNodeEntity : prepareNodeDBList) {
					if(cNodeEntity.getType().equals("up")){
						flag = false;
						break;
					}
				}
			}else{
				flag = false;
			}
			if (flag && entity.getLibrary().equals("备库")) {// 即主备DB都是未启动状态
			} else {
				menu.add(actionStart);
				menu.add(actionDisable);
				menu.add(actionRestart);
				menu.add(new Separator());
			}
			

//			if (node instanceof DeploySourceNode) {
//				DeploySourceNode node1 = (DeploySourceNode) node;
//
//				// DB节点
//				if (!node1.isPool()) {
//					if (!(node1.getNode().getType() != null && node1.getNode().getType().equals("up"))) {
//						if (!node1.isMain()) {
//							// 【提升为主节点】节点只在备DB上
//							HandAction promote = new HandAction(editor.getEditDomain().getEditorPart(), node1,
//									"promote", editor);
//							actionRegistry.registerAction(promote);
//							menu.add(promote);
//						}
//						// 【加入】节点只在故障DB上
//						HandAction attach = new HandAction(editor.getEditDomain().getEditorPart(), node1, "attach",
//								editor);
//						actionRegistry.registerAction(attach);
//						menu.add(attach);
//					}
//
//					DeleteAction actionDelete = new DeleteAction(editor.getEditDomain().getEditorPart(), node1, editor);
//					actionRegistry.registerAction(actionDelete);
//					menu.add(actionDelete);
//
//				} else {
//
//				}
//			} else if (node instanceof DeployTargetNode) {
//
//				DeployTargetNode node1 = (DeployTargetNode) node;
//				// DB节点
//				if (!node1.isPool()) {
//					if (!node1.isMain()) {
//						// 【提升为主节点】节点只在备DB上
//						HandAction promote = new HandAction(editor.getEditDomain().getEditorPart(), node1, "promote",
//								editor);
//						actionRegistry.registerAction(promote);
//						menu.add(promote);
//
//					}
//					if (!(node1.getNode().getType() != null && node1.getNode().getType().equals("down"))) {
//						// 【加入】节点只在故障DB上
//						HandAction attach = new HandAction(editor.getEditDomain().getEditorPart(), node1, "attach",
//								editor);
//						actionRegistry.registerAction(attach);
//						menu.add(attach);
//					}
//
//					DeleteAction actionDelete = new DeleteAction(editor.getEditDomain().getEditorPart(), node1, editor);
//					actionRegistry.registerAction(actionDelete);
//					menu.add(actionDelete);
//				} else {
//
//				}
//			}
		}
	}
}
