package com.kingbase.db.console.bundle.editor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import com.kingbase.db.console.bundle.model.tree.LogAnalysis;
import com.kingbase.db.console.bundle.model.tree.LogAnalysisEntity;
import com.kingbase.db.console.bundle.views.ConsoleView;
import com.kingbase.db.core.editorinput.DataBaseInput;
import com.kingbase.db.core.util.IKBProgressRunnable;
import com.kingbase.db.core.util.KBBooleanFlag;
import com.kingbase.db.core.util.KBProgressDialog;
import com.kingbase.db.core.util.UIUtils;

public class CreateLogAnalysisEditor extends EditorPart {

	public static final String ID = "com.kingbase.db.console.bundle.editor.CreateLogAnalysisEditor";
	private DataBaseInput input;
	private Text txtName;
	private TableViewer tv;
	private List<LogAnalysisEntity> listLog = new ArrayList<LogAnalysisEntity>();
	private LogAnalysis node;
	private String error="";
	private List<LogAnalysisEntity> listBase = new ArrayList<LogAnalysisEntity>();
	private Button btnConfirm;
	private String result;
	private Connection sourceCon;

	public CreateLogAnalysisEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		this.input = (DataBaseInput) input;
		this.node = (LogAnalysis) this.input.getNode();
		if (node.getAddress() == null) {
			return;
		}
		initListLog();
		if (node.getConnection() != null) {
			sourceCon = node.getConnection();
		} else {

			sourceCon = ConsoleView.getConnection(node);
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
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Group group1 = new Group(parent, SWT.NONE);
		group1.setText("报告信息");
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
		lbName.setText("报告名称:");
		lbName.setLayoutData(new GridData());

		txtName = new Text(comp, SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 200;
		txtName.setLayoutData(data);
		SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
		String str = sdf.format(new Date());
		txtName.setText(str);

		Label lbhj = new Label(comp, SWT.NONE);
		lbhj.setText("环境检测:");
		lbhj.setLayoutData(new GridData());

		Table table2 = new Table(comp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table2.setHeaderVisible(true);
		table2.setLinesVisible(true);
		String[] s = new String[] { "类型", "描述", "期望值", "当前值", "检查结果" };
		for (int i = 0; i < s.length; i++) {
			TableColumn column = new TableColumn(table2, SWT.NONE | SWT.CENTER);
			column.setText(s[i]);
			column.setWidth(150);
		}
		tv = new TableViewer(table2);
		tv.setContentProvider(new ContentProvider());
		tv.setLabelProvider(new TableLogAnalyProvider());
		tv.setInput(listLog);
		
		Composite composLabel =new Composite(group1, SWT.NONE);
		composLabel.setLayout(new GridLayout(2, false));
		composLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label lbValueName=new Label(composLabel,SWT.NONE);
		lbValueName.setText("期望值复制:");
		Text txtValue = new Text(composLabel,SWT.READ_ONLY);
		txtValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composLabel.setVisible(false);

		table2.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj=e.item.getData();
				if(obj!=null){
					if(obj instanceof LogAnalysisEntity){
						composLabel.setVisible(true);
						String desc=((LogAnalysisEntity)obj).getDesc();
						String value="";
						if(desc.equals("log_line_prefix")||desc.equals("log_error_verbosity")||desc.equals("lc_messages")){
							value=(desc+"='"+((LogAnalysisEntity)obj).getExpectValue()+"'");
						}
						else{
							value=(desc+"="+((LogAnalysisEntity)obj).getExpectValue());
						}
						txtValue.setText(value);
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		Composite compOpera = new Composite(parent, SWT.NONE);
		compOpera.setLayout(new GridLayout(4, false));
		GridData data111 = new GridData(GridData.FILL_HORIZONTAL);
		compOpera.setLayoutData(data111);
		Label label111 = new Label(compOpera, SWT.None);
		label111.setText("");
		data111 = new GridData(GridData.FILL_HORIZONTAL);
		data111.horizontalSpan = 1;
		label111.setLayoutData(data111);
		compOpera.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label111.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Button btnCheck = new Button(compOpera, SWT.PUSH);
		btnCheck.setText("重新检查");
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnCheck.setLayoutData(data);
		btnCheck.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				listBase.clear();
				listBase.addAll(listLog);

				if (sourceCon == null) {
					MessageDialog.openWarning(UIUtils.getActiveShell(), "提示", "服务器连接不正确！请检查");
					return;
				}
				String mess2 = executeDDL(sourceCon, "show log_destination");
				if (mess2.equals("csvlog")) {
					String mess3 = executeDDL(sourceCon, "select log_analyse.csv_check()");
					if (mess3.equals("true")) {
						listBase.get(0).setCurrentValue("已安装");
						listBase.get(0).setResult("通过");
						listBase.get(1).setCurrentValue("csvlog");
					} else {
						listBase.get(0).setCurrentValue("未安装");
						listBase.get(0).setResult("不通过");
						listBase.get(1).setCurrentValue("csvlog");
					}
				} else if (mess2.equals("stderr")) {
					listBase.get(0).setCurrentValue("未安装");
					listBase.get(0).setResult("通过");
					listBase.get(1).setCurrentValue("stderr");
					String mess4 = executeDDL(sourceCon, "show log_line_prefix");
					if (mess4.equals(listBase.get(2).getExpectValue())) {
						listBase.get(2).setCurrentValue(mess4);
						listBase.get(2).setResult("通过");
					} else {
						listBase.get(2).setCurrentValue(mess4);
						listBase.get(2).setResult("不通过");
					}
				}
				for (int i = 0; i < listBase.size(); i++) {
					if (i > 2) {
						String mess5 = executeDDL(sourceCon, "show " + listBase.get(i).getDesc());
						if (mess5.equals(listBase.get(i).getExpectValue())) {
							listBase.get(i).setCurrentValue(mess5);
							listBase.get(i).setResult("通过");
						} else {
							listBase.get(i).setCurrentValue(mess5);
							listBase.get(i).setResult("不通过");
						}
					}
				}
				tv.setInput(listBase);
				tv.refresh();
				int index = 0;
				for (int i = 0; i < listBase.size(); i++) {
					if (listBase.get(i).getResult().equals("不通过")) {
						index = 1;
						break;
					}
				}
				if (index == 0) {
					btnConfirm.setEnabled(true);
				}
				if (error != "") {
					MessageDialog.openInformation(UIUtils.getActiveShell(), "错误", error);
				}
			}
		});

		btnConfirm = new Button(compOpera, SWT.PUSH);
		btnConfirm.setText("确定");
		btnConfirm.setEnabled(false);
		data111 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnConfirm.setLayoutData(data111);
		((GridData) btnConfirm.getLayoutData()).widthHint = 61;
		btnConfirm.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				listBase.clear();
				listBase.addAll(listLog);

				String txtNameT = txtName.getText();
				if (txtNameT.equals("")) {
					MessageDialog.openInformation(UIUtils.getActiveShell(), "提示", "报告名称不能为空!");
					return;
				}

				if (sourceCon == null) {
					MessageDialog.openWarning(UIUtils.getActiveShell(), "提示", "服务器连接不正确！请检查");
					return;
				}
				
				new KBProgressDialog(UIUtils.getActiveShell(), "生成日志").run(false, new IKBProgressRunnable() {
					public void run(KBBooleanFlag stopFlag) {

						result = executeDDL(sourceCon, "select log_analyse.log_snapshot('" + txtNameT + "')");
						stopFlag.setFlag(true);
					}
				});
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (!"".equals(result) && result != null) {
						if (result.equals("true")) {

							LogAnalysis.getChilds(node);
							input.getTreeView().refresh();
							MessageDialog.openInformation(UIUtils.getActiveShell(), "提示", "生成报告成功!");
							UIUtils.closeEditor(CreateLogAnalysisEditor.this);
						} else {
							MessageDialog.openInformation(UIUtils.getActiveShell(), "提示", "生成报告失败!");
						}
						break;
					}
					if(error!=""){
						MessageDialog.openInformation(UIUtils.getActiveShell(), "提示", "生成报告失败!"+error);
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
				UIUtils.closeEditor(CreateLogAnalysisEditor.this);
			}
		});

	}

	protected String executeDDL(Connection sourceCon, String sql) {

		error = "";
		String mess = "";
		try {
			DatabaseMetaData metaData = sourceCon.getMetaData();
			String schemaTerm = metaData.getSchemaTerm();
			if (!schemaTerm.equals("")) { //$NON-NLS-1$
				Statement stm = sourceCon.createStatement();
				ResultSet resultSet = null;
				resultSet = stm.executeQuery(sql); // $NON-NLS-1$
				while (resultSet.next()) {
					return resultSet.getObject(1).toString();
				}
				if (resultSet != null) {
					resultSet.close();
				}
				stm.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			error = e.getMessage();
			if(e.getMessage().equals("This connection has been closed.")){
				error="此连接已关闭,请重新打开编辑器！";
			}
		}
		return mess;
	}

	@Override
	public boolean isDirty() {
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
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void initListLog() {
		listLog.add(new LogAnalysisEntity("1", "安装阶段", "Text::CSV_XS", "可选", "", ""));
		listLog.add(new LogAnalysisEntity("2", "参数设置", "log_destination", "csvlog或者stderr", "", ""));
		listLog.add(new LogAnalysisEntity("3", "参数设置", "log_line_prefix", "%t [%p]: [%l-1]", "", ""));
		listLog.add(new LogAnalysisEntity("4", "参数设置", "logging_collector", "on", "", ""));
		listLog.add(new LogAnalysisEntity("5", "参数设置", "log_min_duration_statement", "-1", "", ""));
		listLog.add(new LogAnalysisEntity("6", "参数设置", "log_duration", "on", "", ""));
		listLog.add(new LogAnalysisEntity("7", "参数设置", "log_checkpoints", "on", "", ""));
		listLog.add(new LogAnalysisEntity("8", "参数设置", "log_connections", "on", "", ""));
		listLog.add(new LogAnalysisEntity("9", "参数设置", "log_disconnections", "on", "", ""));
		listLog.add(new LogAnalysisEntity("10", "参数设置", "log_lock_waits", "on", "", ""));
		listLog.add(new LogAnalysisEntity("11", "参数设置", "log_temp_files", "0", "", ""));
		listLog.add(new LogAnalysisEntity("12", "参数设置", "log_autovacuum_min_duration", "0", "", ""));
		listLog.add(new LogAnalysisEntity("13", "参数设置", "log_error_verbosity", "default", "", ""));
		listLog.add(new LogAnalysisEntity("14", "参数设置", "lc_messages", "C", "", ""));
	}

	class TableLogAnalyProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			LogAnalysisEntity entity = (LogAnalysisEntity) element;
			switch (columnIndex) {
			case 0:
				return entity.getType();
			case 1:
				return entity.getDesc();
			case 2:
				return entity.getExpectValue();
			case 3:
				return entity.getCurrentValue();
			case 4:
				return entity.getResult();
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
}
