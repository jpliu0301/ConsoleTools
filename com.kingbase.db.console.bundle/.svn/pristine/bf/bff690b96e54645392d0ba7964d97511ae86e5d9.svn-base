/**
 * 
 */
package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.kingbase.db.console.bundle.model.tree.ConsoleBackups;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.AKBProgressRunnableWithPid;
import com.kingbase.db.core.util.IKBProgressRunnable;
import com.kingbase.db.core.util.KBBooleanFlag;
import com.kingbase.db.core.util.KBProgressDialog;
import com.kingbase.db.core.util.UIUtils;

/**
 * @author jpliu
 *
 *         2017年3月30日
 */
public class CreatebackupDialog extends Dialog {

	private final List<String> commands = new ArrayList<String>();
	private Button backupLogB;// 备份服务器日志
	private DataBaseInput input;
	private ConsoleBackups backupView;
	private Button fillB;
	private String CMDPATH;
	private Spinner threadT;
	private Button checkpointLB;
	private String suss = "";
	private FormToolkit toolkit;
	private String execCommand = null;

	private Shell shell;
	private boolean returnCode = false;
	private AKBProgressRunnableWithPid runnable;
//	private Text txtName;
//	private Text txtAddress;
//	private Text txtPort;
//	private Text txtPassword;
	private Button incrementalB;

	public CreatebackupDialog(Shell shell, DataBaseInput input, ConsoleBackups backupView, String CMDPATH) {
		super(shell);
		this.shell = shell;
		this.input = input;
		this.backupView = backupView;
		this.CMDPATH = CMDPATH;
		// 组件布局
		createContents();
	}

	public boolean open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return returnCode;
	}

	public void createContents() {

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("新建备份");
		final GridLayout gridLayout_31 = new GridLayout();

		gridLayout_31.verticalSpacing = 0;
		gridLayout_31.marginWidth = 0;
		gridLayout_31.marginHeight = 0;
		gridLayout_31.horizontalSpacing = 0;
		shell.setLayout(gridLayout_31);
		shell.setSize(635, 522);
		if (shell != null) {// 居中
			Monitor[] monitorArray = Display.getCurrent().getMonitors();
			Rectangle rectangle = monitorArray[0].getBounds();
			Point size = shell.getSize();
			shell.setLocation((rectangle.width - size.x) / 2, (rectangle.height - size.y) / 2);
		}

		IManagedForm managedForm = new ManagedForm(shell);
		toolkit = managedForm.getToolkit();
		final ScrolledForm form = managedForm.getForm();
		GridData data1 = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(data1);
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		toolkit.decorateFormHeading(form.getForm());

		Group group = new Group(form.getBody(), SWT.NONE);
		group.setText("基本属性");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite backupType = new Composite(group, SWT.NONE);
		GridLayout backupType_gl = new GridLayout(3, true);
		GridData backupType_gd = new GridData(GridData.FILL_HORIZONTAL);
		backupType_gl.verticalSpacing = 15;
		backupType.setLayout(backupType_gl);
		backupType.setLayoutData(backupType_gd);
		backupType.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label label = toolkit.createLabel(backupType, "备份方式", SWT.NONE);
		label.setLayoutData(new GridData());

		fillB = toolkit.createButton(backupType, "全量", SWT.RADIO);
		GridData fillB_gd = new GridData(GridData.FILL_BOTH);
		fillB.setLayoutData(fillB_gd);
		fillB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fillB.getSelection()) {
					incrementalB.setSelection(false);
				}
			}
		});
		fillB.setSelection(true);
		
		incrementalB = toolkit.createButton(backupType, "增量", SWT.RADIO);
		GridData incrementalB_gd = new GridData(GridData.FILL_BOTH);
		incrementalB.setLayoutData(incrementalB_gd);
		incrementalB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (incrementalB.getSelection()) {
					fillB.setSelection(false);
				}
			}
		});

		Label backupLogL = toolkit.createLabel(backupType, "备份服务器日志", SWT.NONE);

		backupLogB = toolkit.createButton(backupType, "", SWT.CHECK);
		backupLogB.setLayoutData(new GridData(GridData.FILL_BOTH));
		toolkit.createLabel(backupType, "", SWT.NONE);

		Label checkpointL = toolkit.createLabel(backupType, "checkpoint", SWT.NONE);

		checkpointLB = toolkit.createButton(backupType, "", SWT.CHECK);
		checkpointLB.setLayoutData(new GridData(GridData.FILL_BOTH));
		toolkit.createLabel(backupType, "", SWT.NONE);

		Label threadL = toolkit.createLabel(backupType, "开启线程数量", SWT.NONE);

		threadT = new Spinner(backupType, SWT.BORDER | SWT.WRAP);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		threadT.setLayoutData(data);
		threadT.setMaximum(10);
		threadT.setMinimum(1);
		threadT.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		toolkit.createLabel(backupType, "", SWT.NONE);

		Composite compOpera = new Composite(form.getBody(), SWT.NONE);
		compOpera.setLayout(new GridLayout(3, false));
		GridData data11 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data11);
		Label label11 = toolkit.createLabel(compOpera, "", SWT.None);
		data11 = new GridData(GridData.FILL_HORIZONTAL);
		data11.horizontalSpan = 1;
		label11.setLayoutData(data11);
		compOpera.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		final Button btnConfirm = toolkit.createButton(compOpera, "确定", SWT.PUSH);
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data11);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean check = check();
				if (!check) {
					return;
				}

				backupView.setThread(threadT.getText().trim());
				commands.clear();
				commands.add(CMDPATH);
				commands.add("backup");
				commands.add("-D");
				commands.add(backupView.getDatabasePath());
				commands.add("-B");
				commands.add(backupView.getBackupSetPath());

				if (fillB.getSelection()) {
					commands.add("-b");
					commands.add("full");
				} else if (incrementalB.getSelection()) {
					commands.add("-b");
					commands.add("page");
				}
				if (!threadT.getText().trim().equals("")) {
					commands.add("-j");
					commands.add(threadT.getText().trim());
				}

				if (checkpointLB.getSelection()) {
					commands.add("-C");
				}
				if (backupLogB.getSelection()) {
					commands.add("--backup-sys-log");
				}

				 commands.add("-d");
				 commands.add(UIUtils.getDatabase());
				 commands.add("-h");
				 commands.add(backupView.getAddress());
				 commands.add("-p");
				 commands.add(backupView.getPort());
				 commands.add("-U");
				 commands.add(backupView.getUser());
				 commands.add("-W");
				 commands.add(backupView.getPassword());
				 runnable = new AKBProgressRunnableWithPid() {
						public void run(KBBooleanFlag stopFlag) {
							execCommand = execCommand(commands,stopFlag);
						}
				};
				new KBProgressDialog(UIUtils.getActiveShell(), "新建 "+(fillB.getSelection()?"全量备份":"增量备份")).run(true, runnable);
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (!"".equals(suss)) {
						if (suss.equals("true")) {
							MessageDialog.openInformation(shell, "提示", "生成备份成功!");
							returnCode = true;
						} else {
							MessageDialog.openError(shell, "错误", "新建备份出错 \n" + (execCommand.equals("错误日志： \n") ? "用户终止!" : execCommand));
							returnCode = true;
						}
						break;
					}
				}
				if (returnCode) {
					shell.dispose();
				}
			}
		});

		Button btnCancel = toolkit.createButton(compOpera, "取消", SWT.PUSH);
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCancel.setLayoutData(data11);
		((GridData) btnCancel.getLayoutData()).widthHint = 61;

		btnCancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				returnCode = false;
				shell.dispose();
			}

		});

	}

	protected boolean check() {
		if (!fillB.getSelection() && !incrementalB.getSelection()) {
			MessageDialog.openError(UIUtils.getActiveShell(), "Error", "备份方式不能为空");
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
			if(process.getClass().getName().equals("java.lang.UNIXProcess")) {
				  /* get the PID on unix/linux systems */
				  try {
				    Field f = process.getClass().getDeclaredField("pid");
				    f.setAccessible(true);
				    runnable.setPid(f.getInt(process));
				  } catch (Throwable e) {
				  }
			}
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
			e.printStackTrace();
		}
		return null;
	}
}
