package com.kingbase.db.console.bundle.editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;
import org.pentaho.di.util.SWTUtil;
import com.kingbase.db.console.bundle.KBConsoleCore;
import com.kingbase.db.console.bundle.model.tree.LogAnalysis;
import com.kingbase.db.console.bundle.views.ConsoleView;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.IKBProgressRunnable;
import com.kingbase.db.core.util.KBBooleanFlag;
import com.kingbase.db.core.util.KBProgressDialog;
import com.kingbase.db.core.util.PlatformUtil;
import com.kingbase.db.core.util.UIUtils;

/**
 * 服务器信息修改
 * @author feng
 *
 */
public class ServerAnalysisEditor extends EditorPart {
	public static final String ID = "com.kingbase.db.console.bundle.editor.ServerAnalysisEditor";
	private DataBaseInput input;
	private Text txtName;
	private Text txtPassword;
	private Text txtAddress;
	private Text txtPort;
	private LogAnalysis node;
	private FormToolkit toolkit;
	private Text txtDriverName;
	private String driverName = "com.kingbase8.Driver";
	private Text txtDriverPath;
	private String driverPath;
	private Connection connection;
	private LogAnalysis logAnalysis;
	private boolean result=false;

	public ServerAnalysisEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		this.input = (DataBaseInput) input;
		this.node = (LogAnalysis) this.input.getNode();

		driverPath = getFileInBundle("driver/kingbase8-8.2.0.jar").toString(); //$NON-NLS-1$
	}

	public File getFileInBundle(String path) {
		return PlatformUtil.getConfigurationFile(KBConsoleCore.PLUGIN_ID, path);
	}

	@Override
	public void createPartControl(Composite parent) {

		GridData parent_gd = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(parent_gd);
		parent.setLayout(new GridLayout());
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		IManagedForm managedForm = new ManagedForm(parent);
		toolkit = managedForm.getToolkit();
		final ScrolledForm form = managedForm.getForm();
		GridData data1 = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(data1);
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		toolkit.decorateFormHeading(form.getForm());

		Group group1 = new Group(form.getBody(), SWT.NONE);
		group1.setText("服务信息");
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(GridData.FILL_BOTH));
		group1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite compositeInfo = new Composite(group1, SWT.NONE);
		GridLayout layout1 = new GridLayout(2, false);
		layout1.marginTop = 20;
		layout1.marginLeft = 30;
		layout1.marginRight = 30;
		layout1.marginBottom = 20;
		layout1.horizontalSpacing = 20;
		layout1.verticalSpacing = 20;
		compositeInfo.setLayout(layout1);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeInfo.setLayoutData(data);
		compositeInfo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label lbName1 = toolkit.createLabel(compositeInfo, "驱动名称", SWT.NONE);
		lbName1.setLayoutData(new GridData());
		txtDriverName = new Text(compositeInfo, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		txtDriverName.setLayoutData(data);
		txtDriverName.setText(driverName); // $NON-NLS-1$
		txtDriverName.setTextLimit(63);
		UIUtils.verifyTextNotSpace(txtDriverName);

		Label lbDriverPath = toolkit.createLabel(compositeInfo, "驱动路径", SWT.NONE);
		lbDriverPath.setLayoutData(new GridData());

		Composite compositePath = new Composite(compositeInfo, SWT.None);
		GridLayout layout21 = new GridLayout(2, false);
		layout21.horizontalSpacing = 2;
		layout21.verticalSpacing = 0;
		layout21.marginTop = 0;
		compositePath.setLayout(layout21);
		GridData dataw = new GridData(GridData.FILL_HORIZONTAL);
		compositePath.setLayoutData(dataw);
		compositePath.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		txtDriverPath = new Text(compositePath, SWT.BORDER | SWT.READ_ONLY);
		dataw = new GridData(GridData.FILL_HORIZONTAL);
		txtDriverPath.setLayoutData(dataw);
		txtDriverPath.setText(driverPath);
		Button btn1 = new Button(compositePath, SWT.NONE);
		btn1.setText("选择");
		btn1.setLayoutData(new GridData());

		btn1.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(compositePath.getShell());
				dialog.setFilterExtensions(new String[] { "*.*" }); //$NON-NLS-1$
				String path = dialog.open();
				txtDriverPath.setText(path == null ? "" : path); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Label lbAddress = toolkit.createLabel(compositeInfo, "IP地址", SWT.NONE);
		lbAddress.setLayoutData(new GridData());
		txtAddress = new Text(compositeInfo, SWT.BORDER);
		txtAddress.setLayoutData(data);
		txtAddress.setTextLimit(63);
		txtAddress.setText("");
		UIUtils.verifyTextNotSpace(txtAddress);

		Label lbPort = toolkit.createLabel(compositeInfo, "端口号", SWT.NONE);
		lbPort.setLayoutData(new GridData());
		txtPort = new Text(compositeInfo, SWT.BORDER);
		txtPort.setLayoutData(data);
		txtPort.setText("");
		txtPort.setTextLimit(63);
		UIUtils.verifyTextNumber(txtPort);

		Label lbUserName = toolkit.createLabel(compositeInfo, "用户名", SWT.NONE);
		lbUserName.setLayoutData(new GridData());
		txtName = new Text(compositeInfo, SWT.BORDER);
		txtName.setLayoutData(data);
		txtName.setText("");
		txtName.setTextLimit(63);
		UIUtils.verifyTextNotSpace(txtName);

		Label lbPassword = toolkit.createLabel(compositeInfo, "密码", SWT.NONE);
		lbPassword.setLayoutData(new GridData());
		txtPassword = new Text(compositeInfo, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setLayoutData(data);
		txtPassword.setText("");
		txtPassword.setTextLimit(63);

		txtName.setText(node.getUser() == null ? "" : node.getUser());
		txtPassword.setText(node.getPassword() == null ? "" : node.getPassword());
		txtPort.setText(node.getPort() == null ? "" : node.getPort());
		// txtDatabase.setText(node.getDatabase() == null ? "" :
		// node.getDatabase());
		txtAddress.setText(node.getAddress() == null ? "" : node.getAddress());
		txtDriverName.setText(node.getDriverName() == null ? driverName : node.getDriverName());
		txtDriverPath.setText(node.getDriverPath() == null ? driverPath : node.getDriverPath());

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

		final Button btnConfirm = new Button(compOpera, SWT.PUSH);
		btnConfirm.setText("确定");
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data111);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!check()) {
					return;
				}
				logAnalysis = new LogAnalysis();
				logAnalysis.setAddress(txtAddress.getText().trim());
				logAnalysis.setUser(txtName.getText().trim());
				logAnalysis.setPassword(txtPassword.getText().trim());
				logAnalysis.setPort(txtPort.getText().trim());
				logAnalysis.setDatabase(UIUtils.getDatabase());
				logAnalysis.setDriverName(txtDriverName.getText().trim());
				logAnalysis.setDriverPath(txtDriverPath.getText().trim());
				
				new KBProgressDialog(UIUtils.getActiveShell(), "生成服务器信息").run(false, new IKBProgressRunnable() {
					public void run(KBBooleanFlag stopFlag) {

						connection= ConsoleView.getConnection(logAnalysis);
						stopFlag.setFlag(true);
						result=true;
					}
				});
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if(result){
						if (connection == null) {
							MessageDialog.openInformation(parent.getShell(), "提示", "服务器连接失败，请检查");
							break;
						} else {
							logAnalysis.setConnection(connection);
						}
						if (node.getAddress() != null) {
							try {
								if (node.getConnection() != null) {
									node.getConnection().close();
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							MessageDialog.openInformation(parent.getShell(), "提示", "服务器信息更新成功!");
							updateLogAnalysisXML(logAnalysis);
						} else {
							MessageDialog.openInformation(parent.getShell(), "提示", "服务器信息新建成功!");
							createLogAnalysisXML(logAnalysis);
						}
						LogAnalysis.getChilds(node);
						node.setHasInit(true);
						input.getTreeView().refresh();
						UIUtils.closeEditor(ServerAnalysisEditor.this);
						break;
					}
				}
				
			}
		});

		Button btnCancel = new Button(compOpera, SWT.PUSH);
		btnCancel.setText("取消");
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCancel.setLayoutData(data111);
		((GridData) btnCancel.getLayoutData()).widthHint = 61;

		btnCancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				UIUtils.closeEditor(ServerAnalysisEditor.this);
			}
		});
	}

	protected boolean check() {
		if (txtDriverName.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "驱动名称不能为空");
			txtDriverName.setFocus();
			return false;
		}
		if (txtDriverPath.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "驱动路径不能为空");
			txtDriverPath.setFocus();
			return false;
		}
		if (txtName.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "用户名不能为空");
			txtName.setFocus();
			return false;
		}
		if (txtPassword.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "用户密码不能为空");
			txtPassword.setFocus();
			return false;
		}
		if (txtAddress.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "IP地址不能为空");
			txtAddress.setFocus();
			return false;
		}
		boolean verifyIP = UIUtils.verifyIP(txtAddress);
		if (!verifyIP) {
			return false;
		}
		if (txtPort.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "端口号不能为空");
			txtPort.setFocus();
			return false;
		}
		
		return true;
	}

	protected void createLogAnalysisXML(LogAnalysis logAnalysis) {

		node.setAddress(logAnalysis.getAddress());
		node.setUser(logAnalysis.getUser());
		node.setPassword(logAnalysis.getPassword());
		node.setPort(logAnalysis.getPort());
		node.setDatabase(UIUtils.getDatabase());
		node.setDriverName(logAnalysis.getDriverName());
		node.setDriverPath(logAnalysis.getDriverPath());
		node.setConnection(logAnalysis.getConnection());
		
		IFolder folder = node.getFolder();
		IFile file = (IFile) folder.findMember("logAnalysis.xml");
		SWTUtil.asyncExecThread(new Runnable() {

			public void run() {
				SAXReader reader = new SAXReader();
				try {
					File fileLocal = file.getLocation().toFile();
					Document document = reader.read(fileLocal);
					Element root = document.getRootElement();
					Element eleConnection = root.addElement("logAnalysis"); //$NON-NLS-1$

					Element element = eleConnection.addElement("address"); //$NON-NLS-1$
					element.addText(logAnalysis.getAddress());
					element = eleConnection.addElement("port"); //$NON-NLS-1$
					element.addText(logAnalysis.getPort());
					element = eleConnection.addElement("user"); //$NON-NLS-1$
					element.addText(logAnalysis.getUser());
					element = eleConnection.addElement("database"); //$NON-NLS-1$
					element.addText(logAnalysis.getDatabase());
					element = eleConnection.addElement("password"); //$NON-NLS-1$
					element.addText(logAnalysis.getPassword());

					element = eleConnection.addElement("driverName"); //$NON-NLS-1$
					element.addText(logAnalysis.getDriverName());
					element = eleConnection.addElement("driverPath"); //$NON-NLS-1$
					element.addText(logAnalysis.getDriverPath());

					OutputFormat xmlFormat = UIUtils.xmlFormat();
					XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
					output.write(document);
					output.close();

				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 更新订阅xml
	 * 
	 */
	public void updateLogAnalysisXML(LogAnalysis logAnalysis) {

		node.setAddress(logAnalysis.getAddress());
		node.setUser(logAnalysis.getUser());
		node.setPassword(logAnalysis.getPassword());
		node.setPort(logAnalysis.getPort());
		node.setDatabase(UIUtils.getDatabase());
		node.setDriverName(logAnalysis.getDriverName());
		node.setDriverPath(logAnalysis.getDriverPath());
		node.setConnection(logAnalysis.getConnection());
		
		IFolder folder = node.getFolder();
		IFile ifile = (IFile) folder.findMember("logAnalysis.xml");
		File fileLocal = ifile.getLocation().toFile();
		SAXReader reader = new SAXReader();
		List<Element> listEle = null;
		Document document;
		try {
			document = reader.read(fileLocal);
			Element root = document.getRootElement();
			listEle = root.elements();
			for (int i = 0, n = listEle.size(); i < n; i++) {
				Element element = listEle.get(i);
				element.element("address").setText(logAnalysis.getAddress()); //$NON-NLS-1$
				element.element("port").setText(logAnalysis.getPort()); //$NON-NLS-1$
				element.element("database").setText(UIUtils.getDatabase()); //$NON-NLS-1$
				element.element("driverName").setText(logAnalysis.getDriverName()); //$NON-NLS-1$
				element.element("driverPath").setText(logAnalysis.getDriverPath()); //$NON-NLS-1$
				element.element("user").setText(logAnalysis.getUser()); //$NON-NLS-1$
				element.element("password").setText(logAnalysis.getPassword()); //$NON-NLS-1$
			}
			OutputFormat xmlFormat = UIUtils.xmlFormat();
			XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
			output.write(document);
			output.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error", e.getMessage()); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error", e.getMessage()); //$NON-NLS-1$
		}
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
