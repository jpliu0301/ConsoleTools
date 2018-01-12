package com.kingbase.db.console.bundle.views;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.pentaho.di.viewer.CBasicTreeViewer;
import org.pentaho.di.viewer.CTableTreeLabelProvider;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.CTreeStruredContentProvider;

import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.console.bundle.editor.BackupSetDeleteDialog;
import com.kingbase.db.console.bundle.editor.BrowserEditor;
import com.kingbase.db.console.bundle.editor.CheckBackupSetEditor;
import com.kingbase.db.console.bundle.editor.CreateBackupSetEditor;
import com.kingbase.db.console.bundle.editor.CreateLogAnalysisEditor;
import com.kingbase.db.console.bundle.editor.IoTuningEditor;
import com.kingbase.db.console.bundle.editor.ServerAnalysisEditor;
import com.kingbase.db.console.bundle.editor.ServiceManagerEditor;
import com.kingbase.db.console.bundle.model.tree.ConsoleBackupSet;
import com.kingbase.db.console.bundle.model.tree.ConsoleFile;
import com.kingbase.db.console.bundle.model.tree.ConsoleRoot;
import com.kingbase.db.console.bundle.model.tree.IoTuning;
import com.kingbase.db.console.bundle.model.tree.LogAnalysis;
import com.kingbase.db.console.bundle.model.tree.LogAnalysisInfo;
import com.kingbase.db.console.bundle.model.tree.ServiceManagementEntity;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.ImageURL;
import com.kingbase.db.core.util.PlatformUtil;
import com.kingbase.db.core.util.UIUtils;

/**
 * 物理备份
 * 
 * @author jpliu
 *
 */
public class ConsoleView extends ViewPart {

	public static final String ID = "com.kingbase.db.console.bundle.views.ConsoleView";
	private CBasicTreeViewer dbConsoleTree;
	private List<CTableTreeNode> list = new ArrayList<CTableTreeNode>();
	private Object object = null;

	public ConsoleView() {
		this.initWorkspace();
	}

	private void initWorkspace() {
		IProject proejct = PlatformUtil.getConsoleProject("Console");
		ConsoleRoot colonyRoot = new ConsoleRoot(proejct);
		list.add(colonyRoot);

	}

	@Override
	public void createPartControl(Composite parent) {

		dbConsoleTree = new CBasicTreeViewer(parent);
		GridData data = new GridData(GridData.FILL_BOTH);
		Tree tree = dbConsoleTree.getTree();
		tree.setLayoutData(data);

		dbConsoleTree.setLabelProvider(new LabelProvider());
		dbConsoleTree.setContentProvider(new ContentProvider());
		dbConsoleTree.setInput(list);
		
		dbConsoleTree.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				StructuredSelection selectObj = ((StructuredSelection) dbConsoleTree.getSelection());
				object = selectObj.getFirstElement();
				if (object != null && object instanceof ConsoleBackupSet) {
					ConsoleBackupSet backupSet = (ConsoleBackupSet) object;

					PlatformUtil.openEditor(new DataBaseInput(backupSet, "查看 "+backupSet.getName(), dbConsoleTree),
							CheckBackupSetEditor.ID);
				}
				else if (object != null && object instanceof ServiceManagementEntity) {
					ServiceManagementEntity service = (ServiceManagementEntity) object;

					PlatformUtil.openEditor(new DataBaseInput(service, "服务管理", dbConsoleTree),
							ServiceManagerEditor.ID);
				}
				else if (object != null && object instanceof IoTuning) {
					IoTuning service = (IoTuning) object;

					PlatformUtil.openEditor(new DataBaseInput(service, "性能调优", dbConsoleTree),
							IoTuningEditor.ID);
				}
				else if (object != null && object instanceof LogAnalysisInfo) {
					LogAnalysisInfo analysis = (LogAnalysisInfo) object;
					
					PlatformUtil.openEditor(new DataBaseInput(analysis, analysis.getName(), dbConsoleTree),
							BrowserEditor.ID);
				}
			}
		});

		MenuManager menuMgr = addPopupMenu();
		Menu fContextMenu = menuMgr.createContextMenu(tree);
		tree.setMenu(fContextMenu);

	}

	/**
	 * 右键菜单
	 * 
	 * @return
	 */
	protected MenuManager addPopupMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				if (dbConsoleTree != null) {
					StructuredSelection selectObj = ((StructuredSelection) dbConsoleTree.getSelection());
					object = selectObj.getFirstElement();
					if (object instanceof ConsoleRoot) {
						manager.add(actionRefresh);
					} else if (object instanceof ConsoleFile) {
						manager.add(createBackupSet);
						manager.add(new Separator());
						manager.add(registerBackupSet);
						manager.add(new Separator());
						manager.add(actionRefresh);
					}else if(object instanceof ConsoleBackupSet){
						manager.add(updateBackupSet);
						manager.add(new Separator());
						manager.add(checkBackupSet);
						manager.add(new Separator());
						manager.add(deleteBackupSet);
						manager.add(new Separator());
						manager.add(actionRefresh);
					}else if(object instanceof IoTuning){
						manager.add(ioTuning);
					}else if(object instanceof LogAnalysis){
						LogAnalysis tfFolder = (LogAnalysis) object;
						manager.add(createLogAnalysis);
						manager.add(new Separator());
						manager.add(serverLogAnalysis);
						manager.add(new Separator());
						manager.add(actionRefresh);
						if(tfFolder.getConnection()==null){
							createLogAnalysis.setEnabled(false);
						}else{
							createLogAnalysis.setEnabled(true);
						}
					}else if(object instanceof LogAnalysisInfo){
						manager.add(deleteLogAnalysis);
					}
				}
			}
		});
		return menuMgr;
	}
	
	private Action deleteLogAnalysis = new Action("删除", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_delete)){
		public void run() { 
			if (object != null && object instanceof LogAnalysisInfo) {
				LogAnalysisInfo tfFolder = (LogAnalysisInfo) object;
				LogAnalysis parentMeta = (LogAnalysis) tfFolder.getParentModel();
				Connection connection = tfFolder.getConnection();
				if (connection != null) {
					try {
						Statement sta = connection.createStatement();
						sta.execute("delete from log_analyse.log_analyse where id = " + tfFolder.getId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				parentMeta.removeChild(tfFolder);
				parentMeta.setHasInit(true);
				dbConsoleTree.refresh();
				MessageDialog.openConfirm(UIUtils.getActiveShell(), "提示", "日志删除成功！");
				PlatformUtil.closeEditor(new DataBaseInput(tfFolder, tfFolder.getName(), dbConsoleTree),true);
			}
		}
	};
	private Action createLogAnalysis = new Action("新建日志分析", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_add)){
		public void run() { 
			if (object != null && object instanceof LogAnalysis) {
				LogAnalysis tfFolder = (LogAnalysis) object;
				PlatformUtil.openEditor(new DataBaseInput(tfFolder, "新建日志分析", dbConsoleTree), CreateLogAnalysisEditor.ID);
			}
		}
	};
	private Action serverLogAnalysis = new Action("服务器信息", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_update)) {
		public void run() { 
			if (object != null && object instanceof LogAnalysis) {
				LogAnalysis tfFolder = (LogAnalysis) object;
				PlatformUtil.openEditor(new DataBaseInput(tfFolder, "服务器信息","create", dbConsoleTree), ServerAnalysisEditor.ID);
			}
		}
	};
	private Action createBackupSet = new Action("新建备份集", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_add)) {
		public void run() { 
			if (object != null && object instanceof ConsoleFile) {
				ConsoleFile tfFolder = (ConsoleFile) object;
				PlatformUtil.openEditor(new DataBaseInput(tfFolder, "新建备份集","create", dbConsoleTree), CreateBackupSetEditor.ID);
			}
		}
	};
	private Action ioTuning = new Action("性能调优", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_add)){
		public void run() { 
			if (object != null && object instanceof IoTuning) {
				CTableTreeNode tfFolder = (IoTuning)object;
				PlatformUtil.openEditor(new DataBaseInput(tfFolder ,"性能调优","create", dbConsoleTree), IoTuningEditor.ID);
			}
		}
	
	};
	private Action registerBackupSet = new Action("注册备份集", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_add)) {
		public void run() { 
			if (object != null && object instanceof ConsoleFile) {
				ConsoleFile tfFolder = (ConsoleFile) object;
				PlatformUtil.openEditor(new DataBaseInput(tfFolder, "注册备份集","register", dbConsoleTree), CreateBackupSetEditor.ID);
			}
		}
	};
	private Action updateBackupSet = new Action("更新备份集", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_update)) {
		public void run() { 
			if (object != null && object instanceof ConsoleBackupSet) {
				ConsoleBackupSet backupSet = (ConsoleBackupSet) object;
				PlatformUtil.openEditor(new DataBaseInput(backupSet, "更新备份集 "+backupSet.getName(),"update", dbConsoleTree), CreateBackupSetEditor.ID);
			}
		}
	};
	private Action deleteBackupSet = new Action("删除备份集", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_delete)) {
		public void run() { 
			if (object != null && object instanceof ConsoleBackupSet) {
				ConsoleBackupSet backupSet = (ConsoleBackupSet) object;
				ConsoleFile parentMeta = (ConsoleFile) backupSet.getParentModel();
				BackupSetDeleteDialog dialog = new BackupSetDeleteDialog(UIUtils.getActiveShell(),backupSet);
				int open = dialog.open();
				if (open == 0) {
					parentMeta.removeChild(backupSet);
					parentMeta.setHasInit(true);
					dbConsoleTree.refresh();
					
					PlatformUtil.closeEditor(new DataBaseInput(backupSet, "更新备份集 "+backupSet.getName(),"update", dbConsoleTree),true);
					PlatformUtil.closeEditor(new DataBaseInput(backupSet, "查看 " + backupSet.getName(), "check", dbConsoleTree),true);

				}
			}
		}
	};
	private Action checkBackupSet = new Action("查看备份集", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_add)) {
		public void run() {
			if (object != null && object instanceof ConsoleBackupSet) {
				ConsoleBackupSet backupSet = (ConsoleBackupSet) object;
				PlatformUtil.openEditor(
						new DataBaseInput(backupSet, "查看 " + backupSet.getName(), "check", dbConsoleTree), CheckBackupSetEditor.ID);
			}
		}
	};
	private Action actionRefresh = new Action("刷新", //$NON-NLS-1$
			ImageURL.createImageDescriptor(KBConsoleCore.PLUGIN_ID, ImageURL.right_refresh)) {
		public void run() {
			if (object != null && object instanceof ConsoleRoot) {
				ConsoleRoot tRoot = (ConsoleRoot) object;
				CollapseallTree();
				tRoot.refresh();
			} else if (object != null && object instanceof ConsoleFile) {
				ConsoleFile tfFolder = (ConsoleFile) object;
				tfFolder.removeAll();
				tfFolder.refresh();
				dbConsoleTree.refresh();
			} else if (object != null && object instanceof LogAnalysis) {
				LogAnalysis tfFolder = (LogAnalysis) object;
				tfFolder.removeAll();
				tfFolder.refresh();
				dbConsoleTree.refresh();
			}   
		}
	};
	
	@Override
	public void setFocus() {
	}
	public void CollapseallTree() {
		this.dbConsoleTree.collapseAll();
	}

	public static List<Element> getlistEle(ConsoleFile ifile) {
		IFolder folder = ifile.getFolder();
		IFile file = (IFile) folder.findMember("backupSet.xml");
		if(!file.exists()){
			return new ArrayList<Element>();
		}
		SAXReader reader = new SAXReader();
		List<Element> listEle = null;
		Document document;
		try {
			document = reader.read(file.getLocation().toFile());
			Element root = document.getRootElement();
			listEle = root.elements();
		} catch (DocumentException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", e.getMessage()); //$NON-NLS-1$
		}
		return listEle;
	}

	class LabelProvider extends CTableTreeLabelProvider {
		public Image getImage(Object element) {
			if (element instanceof IResource) {
				return ImageURL.createImage(KBConsoleCore.PLUGIN_ID, ImageURL.replication);
			}
			return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof IResource) {
				return ((IResource) element).getName();
			}
			return super.getText(element);
		}
	}

	class ContentProvider extends CTreeStruredContentProvider {

		public Object[] getChildren(Object parentElement) {
			return super.getChildren(parentElement);
		}
		
		@Override
		public boolean hasChildren(Object element) {
			if(element instanceof ServiceManagementEntity){
				return false;
			}
			return super.hasChildren(element);
		}
	}
	public static Connection getConnection(LogAnalysis logAnalysis) {
		String sqlUrl = "jdbc:kingbase8://" + logAnalysis.getAddress() + ":" + logAnalysis.getPort() + "/" +logAnalysis.getDatabase();
		String driverName = logAnalysis.getDriverName();
		String userName = logAnalysis.getUser();
		String password = logAnalysis.getPassword();
		
		File file = PlatformUtil.getConfigurationFile(KBConsoleCore.PLUGIN_ID, logAnalysis.getDriverPath());
		Connection connection = null;
		try {
			URL url = file.toURI().toURL();
			URL[] urls = new URL[] { url };
			ClassLoader loader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
			Class driverClassName = loader != null ? loader.loadClass(driverName) : Class.forName(driverName);
			if (driverClassName != null) {
				Driver driver = (Driver) driverClassName.newInstance();
				DriverManager.registerDriver(driver);
				if (driver.acceptsURL(sqlUrl)) {
					Properties props = new Properties();
					props.put("user", userName); //$NON-NLS-1$
					props.put("password", password); //$NON-NLS-1$
					connection = driver.connect(sqlUrl, props);
				}
			}
		} catch (Exception e1) {
			return null;
		}
		return connection;
	}
}
