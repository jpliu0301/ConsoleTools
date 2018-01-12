package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;

import com.kingbase.db.core.util.UIUtils;

/**
 * 右键菜单
 * @author feng
 *
 */
public class ActionGroup extends org.eclipse.ui.actions.ActionGroup {

	private TableViewer tv;
	private IFile file;
	private List<ServiceTableEntity> listEntity;
	public ActionGroup(TableViewer tv,IFile file,List<ServiceTableEntity> listEntity) {
		this.tv = tv;
		this.file=file;
		this.listEntity=listEntity;
	}

	public void fillContextMenu(IMenuManager mgr) {
		MenuManager menuManager = (MenuManager) mgr;
		menuManager.add(new DeleteAction());
		menuManager.add(new UpdateAction());
		menuManager.add(new RefreshAction());
		Table table = tv.getTable();
		Menu menu = menuManager.createContextMenu(table);
		table.setMenu(menu);
	}

	/**
	 * 删除
	 * @author feng
	 *
	 */
	private class DeleteAction extends Action {
		public DeleteAction() {
			setText("删除");

		}
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
			ServiceTableEntity obj = (ServiceTableEntity) selection.getFirstElement();

			if(obj==null){
				MessageDialog.openInformation(tv.getTable().getShell(), "提示", "请选中一行数据！");
			}
			else{
				if(obj instanceof ServiceTableEntity){
					Boolean isDelete=MessageDialog.openConfirm(tv.getTable().getShell(), "提示", "确认要删除这行数据？");
					if(isDelete){
						ServiceTableEntity entity=(ServiceTableEntity)obj;
						deleteService(entity);
						tv.setInput(listEntity);
						tv.refresh();
					}

				}
			}
		}
	}
	
	/**
	 * 删除
	 * @author feng
	 *
	 */
	private class UpdateAction extends Action {
		public UpdateAction() {
			setText("修改");

		}
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
			ServiceTableEntity obj = (ServiceTableEntity) selection.getFirstElement();

			if(obj==null){
				MessageDialog.openInformation(tv.getTable().getShell(), "提示", "请选中一行数据！");
			}
			else{
				if(obj instanceof ServiceTableEntity){
						ServiceTableEntity entity=(ServiceTableEntity)obj;
						RegisterDialog dialog = new RegisterDialog(tv.getTable().getShell(),listEntity,entity);
						if(dialog.open()==Window.OK){
							entity =dialog.getEntity();
							listEntity.remove(entity);
							updateService(entity);	
							List<String> commands = new ArrayList<String>();
							commands.add(entity.getInstallPath()+"/Server/bin/sys_ctl");
							commands.add("status");
							commands.add("-D");
							commands.add(entity.getDataPath());
							String state=refCommand(commands,entity.getInstallPath());
							entity.setState(state);		
							listEntity.add(entity);
							tv.setInput(listEntity);
							tv.refresh();
						};
					

				}
			}
		}
	}


	/**
	 * 刷新
	 * @author feng
	 *
	 */
	private class RefreshAction extends Action {
		public RefreshAction() {setText("刷新");}
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
			ServiceTableEntity obj = (ServiceTableEntity) selection.getFirstElement();
			if(obj==null){
				MessageDialog.openInformation(tv.getTable().getShell(), "提示", "请选中一行数据！");
			}
			else{
				if(obj instanceof ServiceTableEntity){
					ServiceTableEntity entity=(ServiceTableEntity)obj;
					List<String> commands = new ArrayList<String>();
					commands.add(entity.getInstallPath()+"/Server/bin/sys_ctl");
					commands.add("status");
					commands.add("-D");
					commands.add(entity.getDataPath());
					String state=refCommand(commands,entity.getInstallPath());
					entity.setState(state);
					tv.refresh();

				}
			}

		}
	}

	/**
	 * 删除服务
	 */
	public void deleteService(ServiceTableEntity model){
		File fileLocal = file.getLocation().toFile();
		SAXReader reader = new SAXReader();
		Document document;
		List<Element> listEle = null;
		try {
			document = reader.read(new BufferedReader(
					new InputStreamReader(new FileInputStream(file.getLocation().toFile()), "utf-8")));
			Element root = document.getRootElement();
			listEle = root.elements("service");
			for (int i = 0, n = listEle.size(); i < n; i++) {
				Element element = listEle.get(i);
				if(element.attribute("serviceName").getText().equals(model.getServiceName())){
					root.remove(element);
					listEntity.remove(model);
				}
			}

			OutputFormat xmlFormat = UIUtils.xmlFormat();
			XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
			output.write(document);
			output.close();

		} catch (DocumentException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error", e.getMessage());
		}
	}
	
	/**
	 * 更新服务
	 * 
	 */
	protected void updateService(ServiceTableEntity model) {
		File fileLocal = file.getLocation().toFile();
		SAXReader reader = new SAXReader();
		Document document;
		List<Element> listEle = null;
		try {
			
			document = reader.read(new BufferedReader(
					new InputStreamReader(new FileInputStream(file.getLocation().toFile()), "utf-8")));
			Element root = document.getRootElement();
			listEle = root.elements("service");
			for (int i = 0, n = listEle.size(); i < n; i++) {
				Element element = listEle.get(i);
				if(element.attribute("serviceName").getText().equals(model.getServiceName())){
					element.addAttribute("type", model.getType());
					element.addAttribute("installPath", model.getInstallPath());
					element.addAttribute("dataPath", model.getDataPath());
					element.addAttribute("describe", model.getDescribe());
				}
			}
			OutputFormat xmlFormat = UIUtils.xmlFormat();
			XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
			output.write(document);
			output.close();

		} catch (DocumentException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", e.getMessage()); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * 刷新状态
	 * @param commands
	 * @return
	 */
	private String refCommand(List<String> commands,String path) {
		ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);
		if(!"".equals(path)){
			Map<String, String> env = builder.environment();
			env.put("LD_LIBRARY_PATH", path +"/Server/lib");
			env.put("Path", path +"/Server/bin:$PATH");
		}
		Process process;
		try {
			process = builder.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer errorBuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				errorBuffer.append(line + "\n");
			}
			int exitValue = process.waitFor();

			if (exitValue == 0) {
				return "运行";
			}else if(exitValue == 3) {
				return "停止";
			}
			else{
				return "异常";
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "异常";
		}
	}
	/**
	 * 检查路径存在
	 * @param entity
	 * @return
	 */
	private boolean checkPath(String path) {
		List<String> commands = new ArrayList<String>();
		commands.add("ls");
		commands.add(path);
		ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			int exitValue = process.waitFor();
			if(exitValue==0){
				return true;
			}
			else{
				return false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
