package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.pentaho.di.viewer.CTableTreeNode;
import com.kingbase.db.console.bundle.model.tree.ServiceManagementEntity;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.UIUtils;

/**
 * 服务管理编辑器
 * 
 * @author feng
 *
 */
public class ServiceManagerEditor extends EditorPart {
	public static final String ID = "com.kingbase.db.console.bundle.editor.ServiceManagerEditor";
	private TableViewer tableViewer;
	private StyledText txtDetail;
	private DataBaseInput input;
	private IFile file;
	private Button btnStart;
	private Button btnStop;
	private String errorMessage;
	private List<ServiceTableEntity> listEntity = new ArrayList<ServiceTableEntity>();

	public ServiceManagerEditor() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		this.input = (DataBaseInput) input;
		CTableTreeNode node = this.input.getNode();
		if (node instanceof ServiceManagementEntity) {
			ServiceManagementEntity serviceManager = (ServiceManagementEntity) node;
			IFolder folder = serviceManager.getFolder();
			file = (IFile) folder.findMember("serviceManagement.xml");
			readService();

		}
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout1 = new GridLayout();
		layout1.marginTop = 6;
		layout1.marginLeft = 7;
		layout1.marginRight = 7;
		layout1.marginBottom = 11;
		parent.setLayout(layout1);
		parent.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));

		SashForm form = new SashForm(parent, SWT.VERTICAL);
		form.setLayout(new GridLayout());
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Group group1 = new Group(form, SWT.WRAP);
		group1.setText("服务管理");
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group1.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));

		tableViewer = new TableViewer(group1, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		GridData data = new GridData(GridData.FILL_BOTH);
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(data);

		// 设置表列
		String[] string = new String[] { "服务名称", "类型", "状态" };
		for (int i = 0; i < string.length; i++) {
			TableColumn column = new TableColumn(table, SWT.CENTER);
			column.setText(string[i]);
			column.setWidth(200);
		}
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableServiceManagerProvider());
		tableViewer.setInput(listEntity);
		ActionGroup actionGroup = new ActionGroup(tableViewer, file, listEntity);
		actionGroup.fillContextMenu(new MenuManager());

		Group group2 = new Group(form, SWT.WRAP);
		group2.setText("详细信息");
		group2.setLayout(new GridLayout());
		group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group2.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));

		txtDetail = new StyledText(group2, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		txtDetail.setTextLimit(4000);
		GridData grid1 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		txtDetail.setLayoutData(grid1);
		txtDetail.setEditable(false);

		form.setWeights(new int[] { 4, 7 });

		Composite compOpera = new Composite(parent, SWT.NONE);
		compOpera.setLayout(new GridLayout(6, false));
		GridData data11 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data11);
		Label label11 = new Label(compOpera, SWT.None);
		label11.setText("");
		data11 = new GridData(GridData.FILL_HORIZONTAL);
		data11.horizontalSpan = 1;
		label11.setLayoutData(data11);
		compOpera.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		label11.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));

		Button btnRegister = new Button(compOpera, SWT.PUSH);
		btnRegister.setText("注册");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnRegister.setLayoutData(data11);
		((GridData) btnRegister.getLayoutData()).widthHint = 75;
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				RegisterDialog dialog = new RegisterDialog(parent.getShell(),
						listEntity);
				if (dialog.open() == Window.OK) {
					ServiceTableEntity entity = dialog.getEntity();
					createService(entity);
					List<String> commands = new ArrayList<String>();
					commands.add(entity.getInstallPath() + "/Server/bin/sys_ctl");
					commands.add("status");
					commands.add("-D");
					commands.add(entity.getDataPath());
					String state = refCommand(commands,entity.getInstallPath());
					entity.setState(state);

					listEntity.add(entity);
					tableViewer.setInput(listEntity);
					tableViewer.refresh();
					
				}
				;
			}
		});

		btnStart = new Button(compOpera, SWT.PUSH);
		btnStart.setText("启动");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnStart.setLayoutData(data11);
		btnStart.setEnabled(false);
		((GridData) btnStart.getLayoutData()).widthHint = 61;
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tableViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection sselection = (IStructuredSelection) selection;
					Object obj = sselection.getFirstElement();
					if (obj instanceof ServiceTableEntity) {
						ServiceTableEntity entity = (ServiceTableEntity) obj;
						List<String> commands = new ArrayList<String>();
						commands.add(entity.getInstallPath() + "/Server/bin/sys_ctl");
						commands.add("start");
						commands.add("-w");
						commands.add("-D");
						commands.add(entity.getDataPath());
						commands.add("-l");
						commands.add(entity.getDataPath()
								+ "/sys_log/startup.log");
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(
								null);
						try {
							dialog.run(true, false, new KBRunnable(commands,
									entity, "启动数据库",entity.getInstallPath()));
							if (entity.getState().equals("运行")) {
								MessageDialog.openInformation(
										UIUtils.getActiveShell(), "提示",
										"启动服务成功!");
							} else {
								MessageDialog.openError(
										UIUtils.getActiveShell(), "提示",
										"启动服务失败!" + errorMessage);
							}
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						tableViewer.refresh();
					}
				}

			}
		});

		btnStop = new Button(compOpera, SWT.PUSH);
		btnStop.setText("停止");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnStop.setLayoutData(data11);
		btnStop.setEnabled(false);
		((GridData) btnStop.getLayoutData()).widthHint = 61;
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ISelection selection = tableViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection sselection = (IStructuredSelection) selection;
					Object obj = sselection.getFirstElement();
					if (obj instanceof ServiceTableEntity) {
						ServiceTableEntity entity = (ServiceTableEntity) obj;

						List<String> commands = new ArrayList<String>();
						commands.add(entity.getInstallPath() + "/Server/bin/sys_ctl");
						commands.add("stop");
						commands.add("-w");
						commands.add("-D");
						commands.add(entity.getDataPath());
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(
								null);
						try {
							dialog.run(true, false, new KBRunnable(commands,
									entity, "停止数据库",entity.getInstallPath()));
							if (entity.getState().equals("停止")) {
								MessageDialog.openInformation(
										UIUtils.getActiveShell(), "提示",
										"停止服务成功!");
							} else {
								MessageDialog.openError(
										UIUtils.getActiveShell(), "提示",
										"停止服务失败!" + errorMessage);
							}
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						tableViewer.refresh();
					}
				}
			}
		});

		final Button btnCancel = new Button(compOpera, SWT.PUSH);
		btnCancel.setText("取消");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCancel.setLayoutData(data11);
		((GridData) btnCancel.getLayoutData()).widthHint = 61;
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIUtils.closeEditor(ServiceManagerEditor.this);
			}
		});

		tableViewer.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tableViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection sselection = (IStructuredSelection) selection;
					Object obj = sselection.getFirstElement();
					if (obj instanceof ServiceTableEntity) {
						ServiceTableEntity entity = (ServiceTableEntity) obj;
						String message = "服务名称: " + entity.getServiceName()
								+ "\n" + "数据库安装路径: " + entity.getInstallPath()
								+ "\n" + "数据目录: " + entity.getDataPath() + "\n"
								+ "类型: " + entity.getType() + "\n" + "描述: "
								+ entity.getDescribe();
						txtDetail.setText(message);
						if (entity.getState().equals("异常")) {
							btnStart.setEnabled(true);
							btnStop.setEnabled(true);
						} else {
							if (entity.getState().equals("启动")) {
								btnStart.setEnabled(false);
								btnStop.setEnabled(true);
							} else if (entity.getState().equals("停止")) {
								btnStart.setEnabled(true);
								btnStop.setEnabled(false);
							} else if (entity.getState().equals("未启动")) {
								btnStart.setEnabled(true);
								btnStop.setEnabled(false);
							} else if (entity.getState().equals("运行")) {
								btnStart.setEnabled(false);
								btnStop.setEnabled(true);
							}

						}
					}
				}
			}
		});
	}

	/**
	 * 读取服务
	 */
	public void readService() {
		File fileLocal = file.getLocation().toFile();
		SAXReader reader = new SAXReader();
		Document document;
		List<Element> listEle = null;
		try {
			document = reader
					.read(new BufferedReader(new InputStreamReader(
							new FileInputStream(file.getLocation().toFile()),
							"utf-8")));
			Element root = document.getRootElement();
			listEle = root.elements("service");
			for (int i = 0, n = listEle.size(); i < n; i++) {
				Element element = listEle.get(i);
				ServiceTableEntity model = new ServiceTableEntity();
				model.setServiceName(element.attribute("serviceName").getText());
				model.setType(element.attribute("type").getText());
				model.setInstallPath(element.attribute("installPath").getText());
				model.setDataPath(element.attribute("dataPath").getText());
				model.setDescribe(element.attribute("describe").getText());
				listEntity.add(model);
			}

			OutputFormat xmlFormat = UIUtils.xmlFormat();
			XMLWriter output = new XMLWriter(new FileWriter(fileLocal),
					xmlFormat);
			output.write(document);
			output.close();

		} catch (DocumentException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error",
					e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(), "Error",
					e.getMessage());
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param entity
	 */
	public void createService(ServiceTableEntity entity) {
		SAXReader reader = new SAXReader();
		File fileLocal = file.getLocation().toFile();
		Document document;
		try {
			document = reader.read(new BufferedReader(new InputStreamReader(
					new FileInputStream(fileLocal), "utf-8")));

			Element root = document.getRootElement();
			Element element = root.addElement("service");
			element.addAttribute("serviceName", entity.getServiceName());
			element.addAttribute("type", entity.getType());
			element.addAttribute("installPath", entity.getInstallPath());
			element.addAttribute("dataPath", entity.getDataPath());
			element.addAttribute("describe", entity.getDescribe());
			OutputFormat xmlFormat = UIUtils.xmlFormat();
			XMLWriter output = null;
			output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
			output.write(document);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行启动停止命令
	 * 
	 * @param commands
	 * @param execS
	 */
	private void execCommand(List<String> commands,String path) {
		errorMessage = "";
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
			errorMessage = errorBuffer.toString();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		}
	}

	/**
	 * 刷新状态
	 * 
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
			} else if (exitValue == 3) {
				return "停止";
			} else {
				return "异常";
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "异常";
		}
	}
	

	public void refTable() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						tableViewer.getTable().getDisplay()
								.asyncExec(new Runnable() {

									@Override
									public void run() {
										if (listEntity.size() > 0) {
											for (int i = 0; i < listEntity
													.size(); i++) {
												ServiceTableEntity entity = listEntity
														.get(i);
												List<String> commands = new ArrayList<String>();
												commands.add(entity
														.getInstallPath()
														+ "/Server/bin/sys_ctl");
												commands.add("status");
												commands.add("-D");
												commands.add(entity
														.getDataPath());
												String state = refCommand(commands,entity.getInstallPath());
												entity.setState(state);
												tableViewer.refresh();
											}
										}

									}
								});
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		});
		thread.start();
	}

	class TableServiceManagerProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ServiceTableEntity entity = (ServiceTableEntity) element;
			switch (columnIndex) {
			case 0:
				return entity.getServiceName();
			case 1:
				return entity.getType();
			case 2:
				return entity.getState();
			default:
				return "";
			}
		}

	}

	class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else {
				return new Object[0];
			}
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	class KBRunnable implements IRunnableWithProgress {

		private List<String> commands;
		private ServiceTableEntity entity;
		private String title;
		private String path;

		public KBRunnable(List<String> commands, ServiceTableEntity entity,
				String title,String path) {
			this.commands = commands;
			this.entity = entity;
			this.title = title;
			this.path=path;
		}

		public void run(IProgressMonitor monitor) {
			monitor.beginTask(this.title, IProgressMonitor.UNKNOWN);
			try {

				execCommand(commands,path);
				commands = new ArrayList<String>();
				commands.add(entity.getInstallPath() + "/Server/bin/sys_ctl");
				commands.add("status");
				commands.add("-D");
				commands.add(entity.getDataPath());
				String state = refCommand(commands,entity.getInstallPath());
				entity.setState(state);
			} catch (Exception e) {
				e.printStackTrace();
			}

			monitor.done();
		}
	}

}
