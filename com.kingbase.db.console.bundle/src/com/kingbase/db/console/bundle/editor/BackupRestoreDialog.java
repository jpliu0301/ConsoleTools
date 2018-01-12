/**
 * 
 */
package com.kingbase.db.console.bundle.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
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
 */
public class BackupRestoreDialog extends Dialog {

	private StyledText txtDetail;
	private ConsoleBackups backup;
	private DataBaseInput input;
	private AKBProgressRunnableWithPid runnable;
	private Shell shell;
	private String CMDPATH;
	private FormToolkit toolkit;
	private String execCommandStr = null;
	private String curFolder1 = null;
	private final List<String> commands = new ArrayList<String>();
	private List<String> tablespacelist = new ArrayList<String>();
	private Map<String, String> tablespaceMap = new HashMap<String, String>();
	private String suss = "";
	/**
	 * @param strList
	 * @param txtDetail
	 * @param parent
	 */
	public BackupRestoreDialog(Shell shell, DataBaseInput baseInput, ConsoleBackups backupView, String detailStr,
			String CMDPATH) {
		super(shell);

		this.shell = shell;
		this.input = baseInput;
		this.backup = backupView;
		this.CMDPATH = CMDPATH;

		initData();
		createContents();
		// 打开还原dialog,将详细数据带入
		txtDetail.setText(detailStr);
	}

	public void open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("备份还原");
		final GridLayout gridLayout_31 = new GridLayout();
		gridLayout_31.verticalSpacing = 0;
		gridLayout_31.marginWidth = 0;
		gridLayout_31.marginHeight = 0;
		gridLayout_31.horizontalSpacing = 0;
		shell.setLayout(gridLayout_31);
		shell.setSize(735, 645);
		if (shell != null) {// 居中
			Monitor[] monitorArray = Display.getCurrent().getMonitors();
			Rectangle rectangle = monitorArray[0].getBounds();
			Point size = shell.getSize();
			shell.setLocation((rectangle.width - size.x) / 2, (rectangle.height - size.y) / 2);
		}
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		IManagedForm managedForm = new ManagedForm(shell);
		toolkit = managedForm.getToolkit();
		final ScrolledForm form = managedForm.getForm();
		GridData data1 = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(data1);
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		toolkit.decorateFormHeading(form.getForm());

		final Composite parent = new Composite(form.getBody(), SWT.NONE);
		GridLayout layout1 = new GridLayout();
		parent.setLayout(layout1);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		SashForm form1 = new SashForm(parent, SWT.VERTICAL);
		form1.setLayout(new GridLayout());
		form1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		form1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Group group1 = new Group(form1, SWT.WRAP);
		group1.setText("还原选项");
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite btnComposite = new Composite(group1, SWT.NONE);
		GridLayout layout11 = new GridLayout(5, false);
		btnComposite.setLayout(layout11);
		GridData gd1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		btnComposite.setLayoutData(gd1);
		btnComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Button btnRecovery = toolkit.createButton(btnComposite, "inclusive          ", SWT.CHECK);
		GridData btnRecovery_gd = new GridData();
		btnRecovery_gd.widthHint = 300;
		btnRecovery.setLayoutData(btnRecovery_gd);

		Button btnTimeLine = toolkit.createButton(btnComposite, "时间线", SWT.RADIO);
		GridData btnTimeLine_gd = new GridData();
		btnTimeLine_gd.widthHint = 70;
		btnTimeLine.setLayoutData(btnTimeLine_gd);
		btnTimeLine.setSelection(true);

		Button btnTimestamp = toolkit.createButton(btnComposite, "时间戳", SWT.RADIO);
		GridData btnTimestamp_gd = new GridData();
		btnTimestamp_gd.widthHint = 70;
		btnTimestamp.setLayoutData(btnTimestamp_gd);

		Button btnTransaction = toolkit.createButton(btnComposite, "事务号", SWT.RADIO);
		GridData btnTransaction_gd = new GridData();
		btnTransaction_gd.widthHint = 70;
		btnTransaction.setLayoutData(btnTransaction_gd);

		Text restoreT = new Text(btnComposite, SWT.BORDER);
		restoreT.setText("1");
		GridData restoreT_gd = new GridData();
		restoreT_gd.widthHint = 140;
		restoreT.setLayoutData(restoreT_gd);

		btnTimeLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnTimeLine.getSelection() && !restoreT.getText().trim().equals("")) {
					restoreT.setText("");
				}
			}
		});
		btnTimestamp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnTimestamp.getSelection() && !restoreT.getText().trim().equals("")) {
					restoreT.setText("");
				}
			}
		});
		btnTransaction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnTransaction.getSelection() && !restoreT.getText().trim().equals("")) {
					restoreT.setText("");
				}
			}
		});

		Composite com = new Composite(group1, SWT.NONE);
		com.setLayout(new GridLayout(3, false));
		GridData data11 = new GridData(SWT.FILL, SWT.FILL, true, false);
		data11.horizontalSpan = 3;
		com.setLayoutData(data11);
		com.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label databasePath = toolkit.createLabel(com, "数据库目录", SWT.NONE);
		databasePath.setLayoutData(new GridData());

		Text databasePathT = toolkit.createText(com, "", SWT.BORDER | SWT.READ_ONLY);
		databasePathT.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button btnSelect = new Button(com, SWT.PUSH);
		btnSelect.setText("选择...");
		data11 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnSelect.setLayoutData(data11);
		((GridData) btnSelect.getLayoutData()).widthHint = 61;
		btnSelect.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder1);
				String dir = dialog.open();
				if (dir != null) {
					curFolder1 = dialog.getFilterPath();
					databasePathT.setText(dir);
				}
			}
		});

		Composite compositePath = new Composite(group1, SWT.NONE);
		GridLayout layout111 = new GridLayout(5, false);
		compositePath.setLayout(layout111);
		GridData gd11 = new GridData(SWT.FILL, SWT.FILL, true, false);
		compositePath.setLayoutData(gd11);
		compositePath.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Label tablespacePath = toolkit.createLabel(compositePath, "表空间目录", SWT.NONE);
		tablespacePath.setLayoutData(new GridData());

		CCombo tableSpaceC = new CCombo(compositePath, SWT.READ_ONLY | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 250;
		tableSpaceC.setLayoutData(gd);
		for (String path : tablespacelist) {
			tableSpaceC.add(path);
			tablespaceMap.put(path, "");
		}

		Label ref = toolkit.createLabel(compositePath, "映射至", SWT.NONE);
		ref.setLayoutData(new GridData());

		Text tablespaceRefPathT = toolkit.createText(compositePath, "", SWT.BORDER | SWT.READ_ONLY);
		tablespaceRefPathT.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button btnSelect1 = new Button(compositePath, SWT.PUSH);
		btnSelect1.setText("选择...");
		GridData data0 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnSelect1.setLayoutData(data0);
		((GridData) btnSelect1.getLayoutData()).widthHint = 61;
		btnSelect1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				
				if (tableSpaceC.getText().trim().equals("")) {
					MessageDialog.openError(UIUtils.getActiveShell(), "错误", "请先选择表空间目录");
					tableSpaceC.setFocus();
					return;
				}
				
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath(curFolder1);
				String dir = dialog.open();
				if (dir != null) {
					curFolder1 = dialog.getFilterPath();
					tablespaceRefPathT.setText(dir);
					tablespaceMap.put(tableSpaceC.getText(), tablespaceRefPathT.getText());
				}
			}
		});
		tableSpaceC.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String path = tablespaceMap.get(tableSpaceC.getText());
				tablespaceRefPathT.setText(path);
			}
		});

		Group group2 = new Group(form1, SWT.WRAP);
		group2.setText("详细信息");
		group2.setLayout(new GridLayout());
		group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		txtDetail = new StyledText(group2, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		txtDetail.setTextLimit(4000);
		GridData grid1 = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		txtDetail.setLayoutData(grid1);

		form1.setWeights(new int[] { 30, 70 });

		Composite compOpera = new Composite(parent, SWT.NONE);
		compOpera.setLayout(new GridLayout(4, false));
		GridData data111 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data111);
		Label label11 = new Label(compOpera, SWT.None);
		label11.setText("");
		data111 = new GridData(GridData.FILL_HORIZONTAL);
		data111.horizontalSpan = 1;
		label11.setLayoutData(data111);
		compOpera.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label11.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		final Button btnConfirm = toolkit.createButton(compOpera, "确定", SWT.PUSH);
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data111);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!btnTimeLine.getSelection() && !btnTransaction.getSelection() && !btnTimestamp.getSelection()) {
					MessageDialog.openError(UIUtils.getActiveShell(), "错误", "请选择还原项目");
					btnTimeLine.setFocus();
					return;
				}
				if (restoreT.getText().trim().equals("")) {
					MessageDialog.openError(UIUtils.getActiveShell(), "错误", "请输入相应的数值");
					restoreT.setFocus();
					return;
				}
				if (databasePathT.getText().trim().equals("")) {
					MessageDialog.openError(UIUtils.getActiveShell(), "错误", "请选择需要还原的数据库目录");
					databasePathT.setFocus();
					return;
				}
				if (databasePathT.getText().trim().equals(backup.getBackupSetPath())) {
					MessageDialog.openError(UIUtils.getActiveShell(), "错误", "还原的数据库目录不能与备份的数据库相同");
					databasePathT.setFocus();
					return;
				}
				
				for (String path : tablespacelist) {
					String refPath = tablespaceMap.get(path);
					if (refPath == null || refPath.trim().equals("")) {
						MessageDialog.openError(UIUtils.getActiveShell(), "错误", "请为表空间目录选择映射目录");
						tableSpaceC.setText(path);
						tablespaceRefPathT.setText("");
						tablespaceRefPathT.setFocus();
						return;
					}
				}

				commands.clear();
				commands.add(CMDPATH);
				commands.add("restore");
				commands.add("-D");
				commands.add(databasePathT.getText());
				commands.add("-B");
				commands.add(backup.getBackupSetPath());
				if (btnTimeLine.getSelection()) {
					commands.add("--timeline="+restoreT.getText());
				}
				if (btnTimestamp.getSelection()) {
					commands.add("--time=\"" + restoreT.getText() + "\"");
				}
				if (btnTransaction.getSelection()) {
					commands.add("--xid="+restoreT.getText());
				}
				if (btnRecovery.getSelection()) {
					commands.add("--inclusive=true");
				}
				if (backup.getThread() != null) {
					commands.add("-j");
					commands.add(backup.getThread());
				}
				for (String path : tablespacelist) {
					String refPath = tablespaceMap.get(path);
					commands.add("-T");
					commands.add(path + "=" + refPath);
				}
				runnable = new AKBProgressRunnableWithPid() {
					public void run(KBBooleanFlag stopFlag) {
						execCommandStr = execCommand(commands,stopFlag);
					}
				};
				
				new KBProgressDialog(UIUtils.getActiveShell(), "备份还原").run(true, runnable);
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (!"".equals(suss)) {
						if (suss.equals("true")) {
							MessageDialog.openInformation(shell, "提示", "备份还原成功！！！");
							shell.dispose();
						} else {
							MessageDialog.openError(shell, "错误", "还原出错！！！\n" +  (execCommandStr.equals("错误日志： \n") ? "用户终止!" : execCommandStr));
						}
						break;
					}
				}
			}
		});

		final Button btnCanel = toolkit.createButton(compOpera, "取消", SWT.PUSH);
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCanel.setLayoutData(data111);
		((GridData) btnCanel.getLayoutData()).widthHint = 61;

		btnCanel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
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

	private void initData() {
		String path = backup.getBackupSetPath() + File.separator + "backups" + File.separator + backup.getBackupName()
				+ File.separator + "database" + File.separator + "tablespace_map";
		BufferedReader input;
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "utf-8"));
			String c;
			while ((c = input.readLine()) != null) {
				String[] split = c.split(" ");
				if (split.length > 1 && split[1] != null && !split[1].trim().equals("")) {
					tablespacelist.add(split[1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
