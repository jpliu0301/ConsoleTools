package com.kingbase.db.console.bundle.editor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import com.kingbase.db.console.bundle.model.tree.LogAnalysisInfo;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.UIUtils;

public class BrowserEditor extends EditorPart {
	public static final String ID = "com.kingbase.db.console.bundle.editor.BrowserEditor";
	private String name;
	private DataBaseInput input;
	private LogAnalysisInfo node;
	private Connection sourceCon;
	private List<String> list = new ArrayList<String>();

	public BrowserEditor() {
		// TODO Auto-generated constructor stub
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
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		setSite(site);
		setInput(input);
		this.name = input.getName();
		setPartName(name);
		this.input = (DataBaseInput) input;
		this.node = (LogAnalysisInfo) this.input.getNode();
		sourceCon = node.getConnection();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("加载日志...", -1);
				
				if(node.getMessage()==""){
					for (int i = 0; i < 8; i++) {
						if (monitor.isCanceled()) {
							break;
						}
						TimeUnit.MILLISECONDS.sleep(500L);
						monitor.worked(1);
					}
					getMessage(node);
				}
				else{
					list.add(node.getMessage());
				}
				monitor.done();
			}
		};
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(
				PlatformUI.getWorkbench().getDisplay().getActiveShell());
		dialog.setCancelable(false);
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void getMessage(LogAnalysisInfo logAnaly) {
		// 刷新操作
		try {
			DatabaseMetaData metaData = sourceCon.getMetaData();
			String schemaTerm = metaData.getSchemaTerm();
			if (!schemaTerm.equals("")) { //$NON-NLS-1$
				Statement stm = sourceCon.createStatement();
				ResultSet resultSet = null;
				resultSet = stm
						.executeQuery("select log_analyse.get_report(" + logAnaly.getId() + ")"); //$NON-NLS-1$
				while (resultSet.next()) {
					list.add(resultSet.getString(1));
				}
				if (list.size() > 0) {
					node.setMessage(list.get(0));
				}
				if (resultSet != null) {
					resultSet.close();
				}
				stm.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			MessageDialog.openError(UIUtils.getActiveShell(),
					"错误", e1.getMessage()); //$NON-NLS-1$
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
	public void createPartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout());
		parent.setFocus();

		Browser browser = new Browser(parent, SWT.None);
		if (list.size() > 0) {
			browser.setText(list.get(0));
		}

		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
