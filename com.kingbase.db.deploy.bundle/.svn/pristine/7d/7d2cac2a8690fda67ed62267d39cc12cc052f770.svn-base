package com.kingbase.db.deploy.bundle.graphical.action;

import java.util.List;
import java.util.Timer;
import java.util.UUID;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.kingbase.db.core.util.ImageURL;
import com.kingbase.db.core.util.JschUtil;
import com.kingbase.db.core.util.UIUtils;
import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.deploy.bundle.editor.ErrorDialog;
import com.kingbase.db.deploy.bundle.graphical.editor.CreateReadWriteStatusEditor;
import com.kingbase.db.deploy.bundle.model.tree.CNodeEntity;
import com.kingbase.db.deploy.bundle.model.tree.PoolEntity;

public class CommonReadWriterAction extends SelectionAction {

	private CNodeEntity entity;// 所以操作都是在主pool上进行的
	private CNodeEntity mainPool;// 所以操作都是在主pool上进行的
	private String type;
	private CreateReadWriteStatusEditor editor;
	private PoolEntity poolEntity;

	//目前只支持到对应的机器上进行启动、停止
	public CommonReadWriterAction(IWorkbenchPart part, CNodeEntity entity, String type, CreateReadWriteStatusEditor editor) {
		super(part);
		setId(UUID.randomUUID().toString());
		this.type = type;
		this.editor = editor;
		this.entity = entity;
		this.poolEntity = editor.getContainerModel().getNode().getPoolEntity();
		this.mainPool = editor.getContainerModel().getMainNodePool();
		if (type.equals("start")) {
			setText("启 动");
			setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_enable));
		} else if (type.equals("disable")) {
			setText("停 止");
			setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_delete));
		} else if (type.equals("restart")) {
			setText("重 启");
			setImageDescriptor(ImageURL.createImageDescriptor(KBDeployCore.PLUGIN_ID, ImageURL.right_sync));
		}
	}

	@Override
	public void run() {
		super.run();
		
		
		Session session = null;
		Session sessionPool = null;
		try {
			session = JschUtil.connect(entity.getIp(), new Integer(entity.getPort()), "root", entity.getRootPass());
			if(!entity.getIp().equals(mainPool.getIp())){
				
				sessionPool = JschUtil.connect(mainPool.getIp(), new Integer(mainPool.getPort()), "root", mainPool.getRootPass());
			}
			boolean flag = mainPool.getType().equals("up");
			boolean flag1 = entity.getType().equals("up") ;
			String command = "";
			if (type.equals("start")) {

				if (entity.getLibrary().contains("分发")) {
					command = startPool(entity);
					JschUtil.execCommand1(session, command);
				} else if (entity.getLibrary().contains("库")) {// 重启库的时候需要重启pool
					command = startDB(entity); 
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, stopPool(mainPool));
					JschUtil.execCommand1(session, command);
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, startPool(mainPool));
				}
			} else if (type.equals("disable")) {

				if (entity.getLibrary().contains("分发")) {
					command = stopPool(entity);
					JschUtil.execCommand1(session, command);
				} else if (entity.getLibrary().contains("库")) {
					command = stopDB(entity);
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, stopPool(mainPool));
					JschUtil.execCommand1(session, command);
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, startPool(mainPool));
				}
			} else if (type.equals("restart")) {

				if (entity.getLibrary().contains("分发")) {// 先停止后启动
					command = (flag1 ? stopPool(entity) + ";" : "") + startPool(entity);
					JschUtil.execCommand1(session, command);
				} else if (entity.getLibrary().contains("库")) {
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, stopPool(mainPool));
					JschUtil.execCommand1(session, restartDB(entity));
					JschUtil.execCommand1(sessionPool!= null ? sessionPool:session, startPool(mainPool));
				}
			}
			List<CNodeEntity> nodeList = editor.getContainerModel().getNodeList();
			for (CNodeEntity node : nodeList) {
				if (entity.getLibrary().contains("分发")
						&& entity.getIp().equals(node.getIp())
						&& entity.getName().equals(node.getName())
						&& entity.getdPath().startsWith(node.getdPath())){
					if(type.equals("disable")){
						node.setType("down");
					}else if(type.equals("start")){
						node.setType("up");
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			Timer timer = editor.getContainerModel().getTimer();
			if (timer != null) {
				timer.cancel();
			}
			
			editor.getContainerModel().refresh();
			editor.getContainerModel().fromXML(true);
			editor.getContainerModel().timeTask(5000);
		}
	}

	@Override
	protected boolean calculateEnabled() {

		if (entity.getType().equals("down")) {
			if (type.equals("start")) {
				return true;
			} else if (type.equals("disable")) {
				return false;
			} else if (type.equals("restart")) {
				return true;
			}
		} else if (entity.getType().equals("up")) {
			if (type.equals("start")) {
				return false;
			} else if (type.equals("disable")) {
				return true;
			} else if (type.equals("restart")) {
				return true;
			}
		} else {
			if (type.equals("start")) {
				return true;
			} else if (type.equals("disable")) {
				return false;
			} else if (type.equals("restart")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 启动pool
	 * 
	 */
	private String startPool(CNodeEntity entity){
		return "su - "+entity.getUser()+" -c \" rm -f " +entity.getdPath()+"run/kingbasecluster/kingbasecluster_status; export LD_LIBRARY_PATH="+entity.getdPath()+"kingbasecluster/lib; cd " + entity.getdPath() + "kingbasecluster/bin/;./kingbasecluster -a ../etc/cluster_hba.conf -f ../etc/kingbasecluster.conf -F ../etc/pcp.conf -n>./log.log 2>&1 &\"";
	}
	/**
	 * 启动DB
	 * 
	 */
	private String startDB(CNodeEntity entity){
		return "su - " + entity.getUser() + " -c \"cd " + entity.getdPath() + "db/bin/;LD_LIBRARY_PATH=../lib ./sys_ctl start -D ../data -l ../kingbase.log\"";
	}
	/**
	 * 停止pool
	 * 
	 */
	private String stopPool(CNodeEntity entity){
		return "su - " + entity.getUser() + " -c \"export LD_LIBRARY_PATH="+entity.getdPath()+"kingbasecluster/lib; cd " + entity.getdPath() + "kingbasecluster/bin/;./pcp_stop_kingbasecluster -U"+poolEntity.getPcpUser()+" -w -p "+poolEntity.getPcp_port()+"\"";
	}
	/**
	 * 停止DB
	 * 
	 */
	private String stopDB(CNodeEntity entity){
		return "su - " + entity.getUser() + " -c \"cd " + entity.getdPath() + "db/bin/;LD_LIBRARY_PATH=../lib ./sys_ctl stop -D ../data -l ../kingbase.log\"";
	}
	/**
	 * 重启DB
	 * 
	 */
	private String restartDB(CNodeEntity entity){
		return "su - " + entity.getUser() + " -c \"cd " + entity.getdPath() + "db/bin/;LD_LIBRARY_PATH=../lib ./sys_ctl restart -D ../data -l ../kingbase.log\"";
	}
}
