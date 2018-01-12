package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import com.kingbase.db.console.bundle.model.tree.ConsoleBackupSet;
import com.kingbase.db.console.bundle.model.tree.ConsoleFile;
import com.kingbase.db.core.util.UIUtils;

/**
 * 删除窗体
 * 
 * @author feng
 *
 */
public class BackupSetDeleteDialog extends Dialog {
	private Shell shell;
	private int returnCode = Window.CANCEL;
	private ConsoleBackupSet backupSet;

	public BackupSetDeleteDialog(Shell shell, ConsoleBackupSet backupSet) {
		super(shell);
		this.shell = shell;
		this.backupSet = backupSet;

		// 组件布局
		createContents();
	}

	public int open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return returnCode;
	}

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("确认删除");
		final GridLayout gridLayout_31 = new GridLayout();

		gridLayout_31.verticalSpacing = 0;
		gridLayout_31.marginWidth = 0;
		gridLayout_31.marginHeight = 0;
		gridLayout_31.horizontalSpacing = 0;
		shell.setLayout(gridLayout_31);
		shell.setSize(350, 200);
		if (shell != null) {// 居中
			Monitor[] monitorArray = Display.getCurrent().getMonitors();
			Rectangle rectangle = monitorArray[0].getBounds();
			Point size = shell.getSize();
			shell.setLocation((rectangle.width - size.x) / 2, (rectangle.height - size.y) / 2);
		}

		Composite parent = new Composite(shell, SWT.NONE);
		GridLayout layout1 = new GridLayout();
		layout1.marginLeft = 40;
		layout1.marginRight = 7;
		layout1.marginBottom = 11;
		layout1.horizontalSpacing = 20;
		layout1.verticalSpacing = 20;
		layout1.marginTop = 30;
		parent.setLayout(layout1);

		Label label = new Label(parent, SWT.NONE);
		label.setText("是否删除当前备份集？");
		GridData label_gl = new GridData();
		label.setLayoutData(label_gl);

		final Button btnDelete = new Button(parent, SWT.CHECK);
		btnDelete.setText("是否删除已经备份的文件");
		GridData btnDelete_gl = new GridData();
		btnDelete.setLayoutData(btnDelete_gl);

		Composite compOpera = new Composite(shell, SWT.NONE);
		compOpera.setLayout(new GridLayout(3, false));
		GridData data11 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data11);
		Label label11 = new Label(compOpera, SWT.None);
		label11.setText("");
		data11 = new GridData(GridData.FILL_HORIZONTAL);
		data11.horizontalSpan = 1;
		label11.setLayoutData(data11);

		final Button btnConfirm = new Button(compOpera, SWT.PUSH);
		btnConfirm.setText("确定");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data11);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (btnDelete.getSelection()) {// 确认删除，并且勾选了按钮
					// 先删除本地文件
					StringBuffer buffer = new StringBuffer();
					StringBuffer appendbuffer = buffer.append("rm -rf ").append(backupSet.getBackupSetPath());
					java.lang.Runtime rt = java.lang.Runtime.getRuntime();// 执行命令行
					StringBuffer txtDetailStr = new StringBuffer();
					try {
						Process p = rt.exec(appendbuffer.toString());

						// 异常输出
						BufferedReader errorStr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
						String line = null;
						while ((line = errorStr.readLine()) != null) {
							txtDetailStr.append(line + "\n");
						}
						p.waitFor();
						int exitValue = p.exitValue();
						if (exitValue != 0) {
							MessageDialog.openInformation(UIUtils.getActiveShell(), "错误",
									appendbuffer.toString() + "\n" + txtDetailStr.toString());
							return;
						}
						p.destroy();
					} catch (IOException e1) {
						e1.printStackTrace();
						MessageDialog.openInformation(UIUtils.getActiveShell(), "错误", "删除本地文件失败\n" + e1.getMessage());
						return;
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						MessageDialog.openInformation(UIUtils.getActiveShell(), "错误", "删除本地文件失败\n" + e1.getMessage());
						return;
					}
				}
				// 删除xml上面的信息
				IFolder folder = ((ConsoleFile) backupSet.getParentModel()).getFolder();
				IFile file = (IFile) folder.findMember("backupSet.xml");
				deleteXmlNode(file.getLocation().toFile(), backupSet.getName());
				MessageDialog.openInformation(shell, "提示", "删除成功!");
				returnCode = Window.OK;
				shell.dispose();
			}
		});

		final Button btnCanel = new Button(compOpera, SWT.PUSH);
		btnCanel.setText("取消");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCanel.setLayoutData(data11);
		((GridData) btnCanel.getLayoutData()).widthHint = 61;

		btnCanel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	/**
	 * 删除xml节点
	 */
	private void deleteXmlNode(File file, String name) {
		SAXReader reader = new SAXReader();
		List<Element> listEle = null;
		Document document = null;
		try {
			document = reader.read(file);
			Element root = document.getRootElement();
			listEle = root.elements();
			if (listEle == null || listEle.size() == 0) {
				return;
			}
			for (Element element : listEle) {
				if (element.element("backupSetName").getStringValue().equals(name)) { //$NON-NLS-1$
					root.elements().remove(element);
				}
			}
			try {
				OutputFormat xmlFormat = UIUtils.xmlFormat();
				File fileLocal = file;
				XMLWriter output = new XMLWriter(new FileWriter(fileLocal), xmlFormat);
				output.write(document);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
