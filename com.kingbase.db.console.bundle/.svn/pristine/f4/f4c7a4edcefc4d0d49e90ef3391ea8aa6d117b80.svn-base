package com.kingbase.db.console.bundle.editor;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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

import com.kingbase.db.console.bundle.model.IoTuningCore;
//import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.UIUtils;

public class IoTuningEditor extends EditorPart {
	public static final String ID = "com.kingbase.db.console.bundle.editor.IoTuningEditor";

	HashMap<String,Float> map;
	Composite parent;

	private Text installCatalogT;
	private Text dataCatalogT;
	private Text passwdT;

	private Button btnClientSelect;
	private Button btnDBSelect;

	private String curFolder1 = null;
	private String curFolder2 = null;


	private String installCatalog;
	private String dataCatalog;
	private String passwd;

	public volatile String result;
	public volatile String errMsg="";

	public IoTuningCore core;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		GridLayout layout1 = new GridLayout();
		layout1.marginTop = 6;
		layout1.marginLeft = 7;
		layout1.marginRight = 7;
		layout1.marginBottom = 11;
		parent.setLayout(layout1);
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Group group = new Group(parent, SWT.NONE);
		group.setText("选择路径");
		group.setLayout(layout1);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite composite = new Composite(group, SWT.NONE);
		GridLayout layoutcom = new GridLayout(3, false);
		GridData data = new GridData(GridData.FILL_BOTH);
		layoutcom.marginTop = 0;
		layoutcom.marginLeft = 0;
		layoutcom.marginRight = 0;
		layoutcom.marginBottom = 0;
		composite.setLayout(layoutcom);
		composite.setLayoutData(data);

		Composite compFile = new Composite(composite, SWT.NONE);
		compFile.setLayout(new GridLayout(3, false));
		GridData data1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		data1.horizontalSpan = 3;
		compFile.setLayoutData(data1);

		Label label1 = new Label(compFile, SWT.NONE);
		label1.setText("安装目录:");
		label1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		installCatalogT= new Text(compFile, SWT.BORDER | SWT.READ_ONLY);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		installCatalogT.setLayoutData(data1);

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
					installCatalogT.setText(dir);
				}
			}
		});

		Label label2 = new Label(compFile, SWT.NONE);
		label2.setText("数据目录:");
		dataCatalogT = new Text(compFile, SWT.BORDER | SWT.READ_ONLY);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		dataCatalogT.setLayoutData(data1);
		label2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		btnDBSelect = new Button(compFile, SWT.PUSH);
		btnDBSelect.setText("选择...");
		data1 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnDBSelect.setLayoutData(data1);
		((GridData) btnDBSelect.getLayoutData()).widthHint = 61;
		btnDBSelect.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder2);
				String dir = dialog.open();
				if (dir != null) {
					curFolder2 = dialog.getFilterPath();
					dataCatalogT.setText(dir);
				}
			}
		});

		Label labelName = new Label(compFile, SWT.NONE);
		labelName.setText("密码");
		passwdT = new Text(compFile, SWT.BORDER|SWT.PASSWORD);
		data1 = new GridData(GridData.FILL_HORIZONTAL);
		passwdT.setLayoutData(data1);
		new Label(compFile, SWT.NONE);
		labelName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		passwdT.setTextLimit(63);
		passwdT.setMessage("Please input your sudo password");


		Composite compOpera = new Composite(group, SWT.NONE);
		compOpera.setLayout(new GridLayout(3, false));
		GridData data11 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data11);
		Label label111 = new Label(compOpera, SWT.None);
		label111.setText("");
		data11 = new GridData(GridData.FILL_HORIZONTAL);
		data11.horizontalSpan = 1;
		label111.setLayoutData(data11);
		compOpera.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label111.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		final Button btnConfirm = new Button(compOpera, SWT.PUSH);
		btnConfirm.setText("确定");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data11);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!check()) {
					return;
				} 
				installCatalog = installCatalogT.getText();
				dataCatalog = dataCatalogT.getText()+"..data";
				passwd = passwdT.getText();

				ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
				try {
					dialog.run(true,false, new KBRunnable());
					if(result!=null) {
						MessageDialog.openInformation(parent.getShell(), "result", result);
					}
					else {
						MessageDialog.openError(parent.getShell(), "error", errMsg);
					}
					errMsg = "";
				} catch (InvocationTargetException e1) {					
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}		
			}
		});	

		Button btnCancel = new Button(compOpera, SWT.PUSH);
		btnCancel.setText("取消");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCancel.setLayoutData(data11);
		((GridData) btnCancel.getLayoutData()).widthHint = 61;
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UIUtils.closeEditor(IoTuningEditor.this);
			}
		});

	}

	class KBRunnable implements IRunnableWithProgress {

		public void run(IProgressMonitor monitor) {
			monitor.beginTask("start",IProgressMonitor.UNKNOWN);
			try {
				core = new IoTuningCore();
				monitor.setTaskName("working");
				result  = core.getParameters(installCatalog, dataCatalog, passwd);				
			} catch (Exception e) {						
				e.printStackTrace();
				errMsg += e.getMessage();
			}finally {
				try {
					core.closeDatabase(installCatalog,dataCatalog);
					
				} catch (Exception e1) {								
					e1.printStackTrace();
					errMsg += e1.getMessage();
				}finally{
					try {
						core.deleteDatabase(dataCatalog);
						
					} catch (Exception e1) {
						e1.printStackTrace();
						errMsg += e1.getMessage();						
					}finally {
						if(errMsg == "") {
							errMsg += core.errMsg;
						}
					}
				}
			}

			monitor.done();
		}
	}
	private boolean check() {

		if (installCatalogT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "安装目录不能为空");
			return false;
		}
		if (dataCatalogT.getText().trim().equals("")) {
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "数据目录不能为空");
			return false;
		}
		if (passwdT.getText().trim().equals("")){
			MessageDialog.openError(UIUtils.getActiveShell(), "错误", "密码不能为空");
			return false;
		}

		return true;
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
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}