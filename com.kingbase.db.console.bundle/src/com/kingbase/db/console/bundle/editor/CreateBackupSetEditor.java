package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.pentaho.di.util.SWTUtil;
import org.pentaho.di.viewer.CBasicTreeViewer;
import org.pentaho.di.viewer.CTableTreeNode;

import com.kingbase.db.console.bundle.model.tree.ConsoleBackupSet;
import com.kingbase.db.console.bundle.model.tree.ConsoleFile;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.IKBProgressRunnable;
import com.kingbase.db.core.util.KBBooleanFlag;
import com.kingbase.db.core.util.KBProgressDialog;
import com.kingbase.db.core.util.UIUtils;

public class CreateBackupSetEditor extends EditorPart {
	public static final String ID = "com.kingbase.db.console.bundle.editor.CreateBackupSetEditor";

	private final List<String> commands = new ArrayList<String>();
	private DataBaseInput input;
	private Text backupSetCatalogT;
	private Text databaseCatalogT;
	private Text backupSetNameT;
	private Button btnSelect;
	private Button btnDBSelect;
	private Button btnClientSelect;
	private String suss = "";
	private String type = "";
	private String execCommand = null;
	private String curFolder1 = null;
	private CBasicTreeViewer dbRConSoleTree;
	private Text clientToolT;
	private String CMDPATH = "";

	private ConsoleFile tfFolder;
	private ConsoleBackupSet backupSet;

	private Text txtName;

	private Text txtPassword;

	private Text txtAddress;

	private Text txtPort;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		this.input = (DataBaseInput) input;
		this.dbRConSoleTree = this.input.getTreeView();
		CTableTreeNode node = this.input.getNode();
		if (node instanceof ConsoleFile) {
			this.tfFolder = (ConsoleFile) node;
			if (this.input.getType().equals("register")) {
				type = "register";
			} else {
				type = "new";
			}
		}else if(node instanceof ConsoleBackupSet){
			this.backupSet = (ConsoleBackupSet) node;
			this.tfFolder = (ConsoleFile) backupSet.getParentModel();
			if (this.input.getType().equals("update")) {
				type = "update";
			}
		}
	}

	@Override
	public void createPartControl(final Composite parent) {

		GridLayout layout1 = new GridLayout();
		layout1.marginTop = 6;
		layout1.marginLeft = 7;
		layout1.marginRight = 7;
		layout1.marginBottom = 11;
		parent.setLayout(layout1);
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Group group = new Group(parent, SWT.NONE);
		group.setText("基本属性");
		group.setLayout(layout1);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite compFile = new Composite(group, SWT.NONE);
		compFile.setLayout(new GridLayout(3, false));
		GridData data1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		data1.horizontalSpan = 5;
		compFile.setLayoutData(data1);

		Label labelName = new Label(compFile, SWT.NONE);
		labelName.setText("备份集名称 :");
		backupSetNameT = new Text(compFile, SWT.BORDER);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		backupSetNameT.setLayoutData(data1);
		new Label(compFile, SWT.NONE);
		labelName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		backupSetNameT.setTextLimit(63);
		UIUtils.verifyTextNotSpace(backupSetNameT);

		Label label = new Label(compFile, SWT.NONE);
		label.setText("备份集目录 :");
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		backupSetCatalogT = new Text(compFile, SWT.BORDER | SWT.READ_ONLY);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		backupSetCatalogT.setLayoutData(data1);
		if(type.equals("new")){
			backupSetCatalogT.setMessage("选择空的的备份集目录");
		}else{
			
			backupSetCatalogT.setMessage("选择已存在的备份集");
		}

		btnSelect = new Button(compFile, SWT.PUSH);
		btnSelect.setText("选择...");
		data1 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnSelect.setLayoutData(data1);
		((GridData) btnSelect.getLayoutData()).widthHint = 61;
		btnSelect.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder1);
				String dir = dialog.open();
				if (dir != null) {
					curFolder1 = dialog.getFilterPath();
					curFolder1.replaceAll(" ", "");
					backupSetCatalogT.setText(dir);
				}

			}
		});
		Label label1 = new Label(compFile, SWT.NONE);
		label1.setText("数据库目录:");
		databaseCatalogT = new Text(compFile, SWT.BORDER | SWT.READ_ONLY);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		databaseCatalogT.setLayoutData(data1);
		label1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		btnDBSelect = new Button(compFile, SWT.PUSH);
		btnDBSelect.setText("选择...");
		data1 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnDBSelect.setLayoutData(data1);
		((GridData) btnDBSelect.getLayoutData()).widthHint = 61;
		btnDBSelect.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder1);
				String dir = dialog.open();
				if (dir != null) {
					curFolder1 = dialog.getFilterPath();
					curFolder1.replaceAll(" ", "");
					databaseCatalogT.setText(dir);
				}
			}
		});

		Label label11 = new Label(compFile, SWT.NONE);
		label11.setText("客户端工具 :");
		label11.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		clientToolT = new Text(compFile, SWT.BORDER | SWT.READ_ONLY);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		clientToolT.setLayoutData(data1);

		btnClientSelect = new Button(compFile, SWT.PUSH);
		btnClientSelect.setText("选择...");
		data1 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnClientSelect.setLayoutData(data1);
		((GridData) btnClientSelect.getLayoutData()).widthHint = 61;
		btnClientSelect.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder1);
				String dir = dialog.open();
				if (dir != null) {
					curFolder1 = dialog.getFilterPath();
					curFolder1.replaceAll(" ", "");
					clientToolT.setText(dir);
				}
			}
		});
		
		Group group1 = new Group(parent, SWT.NONE);
		group1.setText("账号信息");
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(GridData.FILL_BOTH));
		group1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Composite comp = new Composite(group1, SWT.NONE);
		GridLayout comp_gl = new GridLayout(2, false);
		GridData comp_gd = new GridData(GridData.FILL_HORIZONTAL);
		comp_gl.verticalSpacing = 10;
		comp.setLayout(comp_gl);
		comp.setLayoutData(comp_gd);
		comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Label lbName = new Label(comp, SWT.NONE);
		lbName.setText("用户名：");
		lbName.setLayoutData(new GridData());
		txtName = new Text(comp,SWT.BORDER);
		GridData data11 = new GridData(GridData.FILL_HORIZONTAL);
		txtName.setLayoutData(data11);
		txtName.setTextLimit(63);
		UIUtils.verifyTextNotSpace(txtName);
		lbName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Label lbPassword = new Label(comp, SWT.NONE);
		lbPassword.setText("密码：");
		lbPassword.setLayoutData(new GridData());
		txtPassword = new Text(comp,SWT.BORDER | SWT.PASSWORD);
		txtPassword.setLayoutData(data11);
		txtPassword.setTextLimit(63);
		lbPassword.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label lbAddress = new Label(comp, SWT.NONE);
		lbAddress.setText("IP地址：");
		lbAddress.setLayoutData(new GridData());
		txtAddress = new Text(comp, SWT.BORDER);
		txtAddress.setText("localhost");
		txtAddress.setLayoutData(data11);
		txtAddress.setTextLimit(63);
		txtAddress.setEnabled(false);
		lbAddress.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label lbPort = new Label(comp, SWT.NONE);
		lbPort.setText("Port：");
		lbPort.setLayoutData(new GridData());
		txtPort = new Text(comp, SWT.BORDER);
		txtPort.setText("54321");
		txtPort.setLayoutData(data11);
		txtPort.setTextLimit(63);
		UIUtils.verifyTextNumber(txtPort);
		lbPort.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite compOpera = new Composite(parent, SWT.NONE);
		compOpera.setLayout(new GridLayout(3, false));
		GridData data111 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data111);
		Label label111 = new Label(compOpera, SWT.None);
		label111.setText("");
		data111 = new GridData(GridData.FILL_HORIZONTAL);
		data111.horizontalSpan = 1;
		label111.setLayoutData(data111);
		compOpera.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label111.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		if (type.equals("update")) {
			clientToolT.setText(backupSet.getClientPath());
			databaseCatalogT.setText(backupSet.getDatabasePath());
			backupSetNameT.setText(backupSet.getBackupSetName());
			backupSetCatalogT.setText(backupSet.getBackupSetPath());
			txtName.setText(backupSet.getUser());
			txtPassword.setText(backupSet.getPassword());
			txtPort.setText(backupSet.getPort());
		}

		final Button btnConfirm = new Button(compOpera, SWT.PUSH);
		btnConfirm.setText("确定");
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data111);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (! check()) {
					return;
				}
				if (!checkName()) {
					return;
				}
				if (clientToolT.getText() != null) {
					CMDPATH = clientToolT.getText() + File.separator + "sys_rman";
					CMDPATH = CMDPATH.replace('\\', '/');
				}
				commands.clear();
				commands.add(CMDPATH);
				commands.add("init");
				commands.add("-D");
				commands.add(databaseCatalogT.getText().trim());
				commands.add("-B");
				commands.add(backupSetCatalogT.getText());
				
				commands.add("-d");
				commands.add(UIUtils.getDatabase());
				commands.add("-h");
				commands.add(txtAddress.getText().trim());
				commands.add("-p");
				commands.add(txtPort.getText().trim());
				commands.add("-U");
				commands.add(txtName.getText().trim());
				commands.add("-W");
				commands.add(txtPassword.getText().trim());

				ConsoleBackupSet newBackupSet = new ConsoleBackupSet();
				newBackupSet.setBackupSetName(backupSetNameT.getText());
				newBackupSet.setBackupSetPath(backupSetCatalogT.getText());
				newBackupSet.setDatabasePath(databaseCatalogT.getText());
				newBackupSet.setClientPath(clientToolT.getText());
				
				newBackupSet.setAddress(txtAddress.getText().trim());
				newBackupSet.setUser(txtName.getText().trim());
				newBackupSet.setPassword(txtPassword.getText().trim());
				newBackupSet.setPort(txtPort.getText().trim());

				if (type.equals("new")) {

					new KBProgressDialog(UIUtils.getActiveShell(), "新建备份集").run(false, new IKBProgressRunnable() {
						public void run(KBBooleanFlag stopFlag) {
							execCommand = execCommand(commands, stopFlag);
						}
					});
					while (true) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						if (!"".equals(suss)) {
							if (suss.equals("true")) {
								MessageDialog.openInformation(parent.getShell(), "提示", "新建备份集成功!");
								createBackupSetXML(newBackupSet);
								dbRConSoleTree.expandToLevel(3);
								tfFolder.refresh();
								dbRConSoleTree.refresh();
							} else {
								MessageDialog.openError(parent.getShell(), "错误",
										"新建备份集出错\n" + execCommand == null ? "" : execCommand);
								return;
							}
							break;
						}
					}
				} else if (type.equals("register")) {
					if (!showPath()) {
						return;
					}
					createBackupSetXML(newBackupSet);
					MessageDialog.openInformation(parent.getShell(), "提示", "注册备份集成功!");
				} else if (type.equals("update")) {

					if (!newBackupSet.getBackupSetPath().equals(backupSet.getBackupSetPath())) {
						if (!showPath()) {
							return;
						}
					}
					tfFolder.removeChild(backupSet);
					tfFolder.addChild(newBackupSet);
					tfFolder.setHasInit(false);
					dbRConSoleTree.refresh();
					updateXML(backupSet, newBackupSet);
					MessageDialog.openInformation(parent.getShell(), "提示", "修改备份集成功!");
				}
				UIUtils.closeEditor(CreateBackupSetEditor.this);
			}
		});

		Button btnCancel = new Button(compOpera, SWT.PUSH);
		btnCancel.setText("取消");
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCancel.setLayoutData(data111);
		((GridData) btnCancel.getLayoutData()).widthHint = 61;

		btnCancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				UIUtils.closeEditor(CreateBackupSetEditor.this);
			}
		});
	}

	protected void createBackupSetXML(ConsoleBackupSet backupSet) {

		CTableTreeNode node = ((DataBaseInput) getEditorInput()).getNode();
		IFolder folder = tfFolder.getFolder();
		IFile file = (IFile) folder.findMember("backupSet.xml");
		(tfFolder).addChild(backupSet);
		tfFolder.setHasInit(false);
		SWTUtil.asyncExecThread(new Runnable() {

			public void run() {
				SAXReader reader = new SAXReader();
				try {
					File fileLocal = file.getLocation().toFile();
					Document document = reader.read(fileLocal);
					Element root = document.getRootElement();
					Element eleConnection = root.addElement("backupSet"); //$NON-NLS-1$

					Element element = eleConnection.addElement("backupSetName"); //$NON-NLS-1$
					element.addText(backupSet.getBackupSetName());

					element = eleConnection.addElement("backupSetPath"); //$NON-NLS-1$
					element.addText(backupSet.getBackupSetPath());

					element = eleConnection.addElement("databasePath"); //$NON-NLS-1$
					element.addText(backupSet.getDatabasePath());

					element = eleConnection.addElement("clientPath"); //$NON-NLS-1$
					element.addText(backupSet.getClientPath());

					element = eleConnection.addElement("address"); //$NON-NLS-1$
					element.addText(backupSet.getAddress());
					element = eleConnection.addElement("port"); //$NON-NLS-1$
					element.addText(backupSet.getPort());
					element = eleConnection.addElement("user"); //$NON-NLS-1$
					element.addText(backupSet.getUser());
					element = eleConnection.addElement("password"); //$NON-NLS-1$
					element.addText(backupSet.getPassword());

					OutputFormat xmlFormat = UIUtils.xmlFormat();
					XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
					output.write(document);
					output.close();

					dbRConSoleTree.expandToLevel(3);
					tfFolder.refresh();
					dbRConSoleTree.refresh();
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 更新发布节点xml
	 * 
	 */
	protected void updateXML(ConsoleBackupSet oldbackupSet, ConsoleBackupSet newbackupSet) {

		IFolder folder = ((ConsoleFile) backupSet.getParentModel()).getFolder();
		IFile file = (IFile) folder.findMember("backupSet.xml");
		File fileLocal = file.getLocation().toFile();
		SAXReader reader = new SAXReader();
		List<Element> listEle = null;
		Document document;
		try {
			document = reader.read(fileLocal);
			Element root = document.getRootElement();
			listEle = root.elements();
			for (int i = 0, n = listEle.size(); i < n; i++) {
				Element element = listEle.get(i);
				if (element.element("backupSetName").getStringValue().equals(oldbackupSet.getBackupSetName())) { //$NON-NLS-1$
					element.element("backupSetName").setText(newbackupSet.getBackupSetName()); //$NON-NLS-1$
					element.element("backupSetPath").setText(newbackupSet.getBackupSetPath()); //$NON-NLS-1$
					element.element("databasePath").setText(newbackupSet.getDatabasePath()); //$NON-NLS-1$
					element.element("clientPath").setText(newbackupSet.getClientPath()); //$NON-NLS-1$
					
					element.element("address").setText(newbackupSet.getAddress()); 
					element.element("port").setText(newbackupSet.getPort()); //$NON-NLS-
					element.element("user").setText(newbackupSet.getUser()); //$NON-NLS-
					element.element("password").setText(newbackupSet.getPassword()); //$NON-NLS-
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
	
	private boolean checkName() {
		if (!type.equals("update")) {
			CTableTreeNode node = ((DataBaseInput) getEditorInput()).getNode();
			Collection childrenList = node.getChildrenList();
			if (childrenList != null && childrenList.size() > 0) {
				for (Object object : childrenList) {
					ConsoleBackupSet obj = (ConsoleBackupSet) object;
					if (obj.getName().equals(backupSetNameT.getText())) {
						MessageDialog.openError(UIUtils.getActiveShell(), "提示", "已存在此名称节点，请重命名");
						backupSetNameT.setFocus();
						return false;
					}
					if (obj.getBackupSetPath().equals(backupSetCatalogT.getText())) {
						MessageDialog.openError(UIUtils.getActiveShell(), "提示", "此备份集目录已存在，请重新输入");
						backupSetCatalogT.setFocus();
						return false;
					}
				}
			}
		} else {
			CTableTreeNode node = ((DataBaseInput) getEditorInput()).getNode();
			Collection childrenList = ((CTableTreeNode) (node.getParentModel())).getChildrenList();
			if (childrenList != null && childrenList.size() > 0) {
				for (Object object : childrenList) {
					if (node == object) {
						continue;
					}
					ConsoleBackupSet obj = (ConsoleBackupSet) object;
					if (obj.getName().equals(backupSetNameT.getText())) {
						MessageDialog.openError(UIUtils.getActiveShell(), "提示", "已存在此名称节点，请重命名");
						backupSetNameT.setFocus();
						return false;
					}
					if (obj.getBackupSetPath().equals(backupSetCatalogT.getText())) {
						MessageDialog.openError(UIUtils.getActiveShell(), "提示", "此备份集目录已存在，请重新输入");
						backupSetCatalogT.setFocus();
						return false;
					}
				}
			}
		}
		return true;
	}
	private boolean check() {
		if (backupSetNameT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "备份集的名称不能为空");
			return false;
		}
		if (backupSetCatalogT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "备份集的目录不能为空");
			return false;
		}
		if (databaseCatalogT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "数据库目录不能为空");
			return false;
		}
		if (clientToolT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "客户端工具不能为空");
			return false;
		}
		if(txtPassword.getText().trim().equals("")){
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "用户密码不能为空");
			txtPassword.setFocus();
			return false;
		}
		if(txtAddress.getText().trim().equals("")){
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "IP地址不能为空");
			txtAddress.setFocus();
			return false;
		}
		boolean verifyIP = UIUtils.verifyIP(txtAddress);
		if (!verifyIP) {
			return false;
		}
		if(txtPort.getText().trim().equals("")){
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "端口号不能为空");
			txtPort.setFocus();
			return false;
		}
		
		return true;
	}

	private String execCommand(List<String> buffer, KBBooleanFlag stopFlag) {
		ProcessBuilder builder = new ProcessBuilder(buffer);
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();
			// Read out dir output
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			Thread.sleep(200);
			String line;
			StringBuffer errorBuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				errorBuffer.append(line + "\n");
			}

			int exitValue = process.waitFor();

			if (exitValue != 0) {
				stopFlag.setFlag(true);
				suss = "false";
				return "错误日志： \n" + errorBuffer.toString();
			} else {
				stopFlag.setFlag(true);
				suss = "true";
				return null;
			}
		} catch (IOException | InterruptedException e) {
			stopFlag.setFlag(true);
			suss = "false";
			return "错误日志：\n" + e.getMessage();
		}
	}

	private String execShowCommand(List<String> buffer) {
		ProcessBuilder builder = new ProcessBuilder(buffer);
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();
			// Read out dir output
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer errorBuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				errorBuffer.append(line + "\n");
			}

			int exitValue = process.waitFor();

			if (exitValue != 0) {
				suss = "false";
				MessageDialog.openError(UIUtils.getActiveShell(), "错误", "错误日志： \n" + errorBuffer.toString());
				return "错误日志： \n" + errorBuffer.toString();
			} else {
				suss = "true";
				return null;
			}
		} catch (IOException | InterruptedException e) {
			suss = "false";
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "错误日志： \n" + e.getMessage());
		}
		return "";
	}

	private boolean showPath() {
		commands.clear();
		commands.add(CMDPATH);
		commands.add("show");
		commands.add("-D");
		commands.add(databaseCatalogT.getText().trim());
		commands.add("-B");
		commands.add(backupSetCatalogT.getText());

		String execShowCommand = execShowCommand(commands);
		if (execShowCommand == null) {
			return true;
		}
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

}
