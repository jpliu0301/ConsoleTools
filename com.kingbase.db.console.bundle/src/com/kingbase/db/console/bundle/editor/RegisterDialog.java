package com.kingbase.db.console.bundle.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 注册服务窗体
 * @author feng
 *
 */
public class RegisterDialog extends Dialog {

	private Shell shell;
	private List<ServiceTableEntity> listEntity;
	private Combo combo;
	private Label txtPrefix;
	private Text txtName;
	private Text txtInstallPath;
	private Text txtDataPath;
	private StyledText txtDesc;
	private ServiceTableEntity entity;
	private String type="new";
	
    public ServiceTableEntity getEntity() {
		return entity;
	}
	public RegisterDialog(Shell parentShell,List<ServiceTableEntity> listEntity)
    {	
        super(parentShell);
        this.shell=parentShell;
		this.listEntity=listEntity;
    }
	
    public RegisterDialog(Shell parentShell,List<ServiceTableEntity> listEntity,ServiceTableEntity entity)
    {	
        super(parentShell);
        this.shell=parentShell;
		this.listEntity=listEntity;
		this.entity=entity;
		this.type="update";
    }
	
	@Override
	protected Point getInitialSize() {
        return new Point(380, 400);
    }
	
    @Override
    protected boolean isResizable()
    {
        return true;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite group = (Composite) super.createDialogArea(parent);
        GridLayout layout= new GridLayout(3,false);
        group.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        group.setLayoutData(gd);

        Label lbType = new Label(group, SWT.NONE);
        lbType.setText("服务类型:");
        lbType.setLayoutData(new GridData());

        combo = new Combo(group, SWT.READ_ONLY | SWT.DROP_DOWN);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan=2;
        combo.setLayoutData(gd);
        combo.add("数据库服务");
        combo.setText("数据库服务");
        
        Label lbPrefix = new Label(group, SWT.NONE);
        lbPrefix.setText("服务前缀:");
        lbPrefix.setLayoutData(new GridData());

        txtPrefix= new Label(group,SWT.NONE);
        txtPrefix.setLayoutData(gd);
        txtPrefix.setText("KbInstance");
        txtPrefix.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        
        Label lbName = new Label(group, SWT.NONE);
        lbName.setText("服务名称:");
        lbName.setLayoutData(new GridData());

        txtName = new Text(group, SWT.BORDER);
        txtName.setLayoutData(gd);
        
        Label lbInstallPath = new Label(group, SWT.NONE);
        lbInstallPath.setText("安装路径:");
        lbInstallPath.setLayoutData(new GridData());

        txtInstallPath = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        txtInstallPath.setLayoutData(gd);
        
        Button btnIn=new Button(group,SWT.NONE);
        btnIn.setText("浏览");
        btnIn.setLayoutData(new GridData());
        btnIn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				String dir = dialog.open();
				if (dir != null) {
					txtInstallPath.setText(dir);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
        
        Label lbDataPath = new Label(group, SWT.NONE);
        lbDataPath.setText("数据目录:");
        lbDataPath.setLayoutData(new GridData());
        
        txtDataPath= new Text(group, SWT.BORDER);
        txtDataPath.setLayoutData(gd);
        
        Button btnData=new Button(group,SWT.NONE);
        btnData.setText("浏览");
        btnData.setLayoutData(new GridData());
        btnData.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				String dir = dialog.open();
				if (dir != null) {
					txtDataPath.setText(dir);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
        
        Label lbDesc = new Label(group, SWT.NONE);
        lbDesc.setText("描述:");
        lbDesc.setLayoutData(new GridData());

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint=130;
        gd.horizontalSpan=2;
        txtDesc= new StyledText(group, SWT.V_SCROLL | SWT.BORDER);
        txtDesc.setTextLimit(4000);
        txtDesc.setLayoutData(gd);
        
        if(type.equals("update")){
        	combo.setText(entity.getType());
        	txtName.setText(entity.getServiceName().substring(10, entity.getServiceName().length()));
        	txtInstallPath.setText(entity.getInstallPath());
        	txtDataPath.setText(entity.getDataPath());
        	txtDesc.setText(entity.getDescribe());
        	txtName.setEnabled(false);
        }
        return group;
    }
    
    

    @Override
    protected void cancelPressed() {
    	super.cancelPressed();
    }
    
    @Override
    protected void okPressed() {
    	if(txtName.getText().equals("")){
    		MessageDialog.openError(shell, "提示", "服务名不能为空！");
			return;
    	}
    	if(txtInstallPath.getText().equals("")){
    		MessageDialog.openError(shell, "提示", "安装路径不能为空！");
			return;
    	}
    	if(txtDataPath.getText().equals("")){
    		MessageDialog.openError(shell, "提示", "数据目录不能为空！");
			return;
    	}
    	if(type.equals("new")){
    		for (int i = 0; i < listEntity.size(); i++) {
    			if(listEntity.get(i).getServiceName().equals(txtPrefix.getText()+txtName.getText())){
    				MessageDialog.openError(shell, "提示", "服务名不能重复！");
    				return;
    			}
    			if(listEntity.get(i).getDataPath().equals(txtDataPath.getText())){
    				MessageDialog.openError(shell, "提示", " 数据目录不能重复！");
    				return;
    			}
    		}
    	}
		boolean isExect=checkPath(txtInstallPath.getText()+"/Server/bin/kingbase");
		if(isExect){
			isExect=checkPath(txtDataPath.getText()+"/kingbase.conf");
			if(!isExect){
				MessageDialog.openError(shell, "提示", txtDataPath.getText()+"/kingbase.conf不存在,请检查数据目录！");
				return;
			}
		}
		else{
			MessageDialog.openError(shell, "提示", txtInstallPath.getText()+"/Server/bin/kingbase不存在,请检查安装路径!");
			return;
		}
    	entity=new ServiceTableEntity();
    	entity.setType(combo.getText());
    	entity.setServiceName(txtPrefix.getText()+txtName.getText());
    	entity.setInstallPath(txtInstallPath.getText());
    	entity.setDataPath(txtDataPath.getText());
    	entity.setDescribe(txtDesc.getText());
    	if(type.equals("new")){
    		entity.setState("未启动");
    	}
    	super.okPressed();
    }
    
    /**
	 * 检查路径存在
	 * @param entity
	 * @return
	 */
	private boolean checkPath(String path) {
		List<String> commands = new ArrayList<String>();
		commands.add("ls");
		commands.add(path);
		ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			int exitValue = process.waitFor();
			if(exitValue==0){
				return true;
			}
			else{
				return false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
}
