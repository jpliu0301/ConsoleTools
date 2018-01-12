package com.kingbase.db.deploy.bundle.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.kingbase.db.deploy.bundle.model.tree.CNodeEntity;
import com.kingbase.db.deploy.bundle.model.tree.PoolEntity;
import com.kingbase.db.deploy.bundle.model.tree.PosEntity;
import com.kingbase.db.deploy.bundle.model.tree.TableNodeEntity;

public class ReadWriteThreePage {
	private Text txt;

	public Composite getCom2(Composite composite1, String type){
		Composite comm1=new Composite(composite1,SWT.NONE);
		comm1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		comm1.setLayout(new GridLayout(2,false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		comm1.setLayoutData(gd);
		comm1.setVisible(false);
		gd.exclude = true;
		Group groupNode=new Group(comm1,SWT.NONE);
		groupNode.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		groupNode.setText("预览信息");
		groupNode.setLayout(new GridLayout());
		GridData dataGroup=new GridData(SWT.FILL,SWT.FILL,true,true);
		dataGroup.horizontalSpan=2;
		groupNode.setLayoutData(dataGroup);

		txt =new Text(groupNode,SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		txt.setLayoutData(data);
		
		txt.setEditable(false);
		return comm1;
	}
	
	public void setText(PosEntity entity,PoolEntity entity1,CNodeEntity[] models){
		
		String mainDBLib="";
		String mainDBIp="";
		String mainDBPort="";
		String mainNodeUser="";
		String mainDBPath="";
		List<TableNodeEntity> listBei=new ArrayList<TableNodeEntity>();

		for (int i = 0; i <entity.getListDb().size(); i++) {
			if(entity.getListDb().get(i).getLibrary().equals("主库")){
				mainDBLib=entity.getListDb().get(i).getPhysicalMachine();
				mainDBPort=entity.getListenerPort();
				mainNodeUser = entity.getListDb().get(i).getNodeEntity().getUser();
				String nodePath = entity.getListDb().get(i).getNodeEntity().getNodePath();
				mainDBPath = nodePath.replaceAll("\\\\", "")+entity.getName()+"/";
			}else if(entity.getListDb().get(i).getLibrary().equals("备库")){
				listBei.add(entity.getListDb().get(i));
			}
		}
		for (int i = 0; i < models.length; i++) {
			if(models[i].getName().equals(mainDBLib)){
				mainDBIp=models[i].getIp();
			}
		}
		String message="";
		for (int i = 0; i < listBei.size(); i++) {
			
			String nodePath = listBei.get(i).getNodeEntity().getNodePath();
			message += "备库IP:" + listBei.get(i).getNodeEntity().getIp() + "\n备库端口:" + entity.getListenerPort()
					+ "\n备库节点用户:" + listBei.get(i).getNodeEntity().getUser() 
					+ "\n备库数据库用户:" + entity.getDbUser() 
//					+ "\n备库数据库密码:" + entity.getDbPassword()
					+ "\n备库路径:" + nodePath.replaceAll("\\\\", "")+entity.getName()+"/"+ "\n\n";
		}
		String mainPoolLib="";
		String mainPoolIp="";
		String mianPoolPort=entity1.getPort();
		String mainNodePoolUser="";
		String mainPoolPath="";
		List<TableNodeEntity> listBei1=new ArrayList<TableNodeEntity>();

		for (int i = 0; i <entity1.getListPool().size(); i++) {
			if(entity1.getListPool().get(i).getLibrary().equals("主分发")){
				mainPoolLib=entity1.getListPool().get(i).getPhysicalMachine();
				mainNodePoolUser = entity1.getListPool().get(i).getNodeEntity().getUser();
				
				String nodePath = entity1.getListPool().get(i).getNodeEntity().getNodePath();
				mainPoolPath = nodePath.replaceAll("\\\\", "")+entity.getName()+"/";
			}else if(entity1.getListPool().get(i).getLibrary().equals("备分发")){
				listBei1.add(entity1.getListPool().get(i));
			}
		}
		for (int i = 0; i < models.length; i++) {
			if(models[i].getName().equals(mainPoolLib)){
				mainPoolIp=models[i].getIp();
			}
		}
		message+="\n主分发IP:"+mainPoolIp+"\n主分发端口:"+mianPoolPort+"\n主分发节点用户:"+mainNodePoolUser+"\n主分发PCP用户:"+entity1.getPcpUser()+/*"\n主分发PCP密码:"+entity1.getPcpPass()+*/"\n主分发路径:"+mainPoolPath+"\n\n";
		for (int i = 0; i < listBei1.size(); i++) {
			
			String nodePath = listBei1.get(i).getNodeEntity().getNodePath();
			message += "备分发IP:" + listBei1.get(i).getNodeEntity().getIp() + "\n备分发端口:" + entity1.getPort() 
			        + "\n备分发节点用户:" + listBei1.get(i).getNodeEntity().getUser() 
			        + "\n备分发PCP用户:"+entity1.getPcpUser()
//			        + "\n备分发PCP密码:"+entity1.getPcpPass()
					+ "\n备分发路径:" + nodePath.replaceAll("\\\\", "")+entity.getName()+"/" + "\n\n";
		}
		txt.setText("集群名称:"+entity.getName()+"\n\n"+
				"集群设置:\n主库IP:"+mainDBIp+"\n主库端口:"+mainDBPort
				+ "\n主库节点用户:"+mainNodeUser
				+ "\n主库数据库用户:" + entity.getDbUser() 
//				+ "\n主库数据库密码:" + entity.getDbPassword()
				+ "\n主库路径:"+mainDBPath+"\n\n"+message);
	}
}
