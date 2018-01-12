package com.kingbase.db.deploy.bundle.model.tree;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.pentaho.di.viewer.CTableTreeNode;
import org.pentaho.di.viewer.ITreeProvider;

import com.kingbase.db.deploy.bundle.KBDeployCore;
import com.kingbase.db.core.util.ImageURL;

/**
 * 读写分离，主备同步信息
 * 
 * @author feng
 *
 */
public class CReadWriteEntity extends CTableTreeNode implements ITreeProvider {

	
	public PosEntity posEntity;
	private PoolEntity poolEntity;


	public PosEntity getPosEntity() {
		return posEntity;
	}

	public void setPosEntity(PosEntity posEntity) {
		this.posEntity = posEntity;
	}

	public PoolEntity getPoolEntity() {
		return poolEntity;
	}

	public void setPoolEntity(PoolEntity poolEntity) {
		this.poolEntity = poolEntity;
	}
	
	private String zStrConfig = "";
	private String bStrConfig = "";
	private List<TableNodeEntity> list;
	private boolean isVip = false;
	private String vip = "";
	private String copyForm = "同步";
	private String max_wal = "";
	private String max_standby_arc = "";
	private String wal_keep = "";
	private String max_standby_str = "";
	private String replication = "";
	private String wal_receiver = "";
	private String hot_standby = "";

	private String initThread = "32";
	private String max_pool = "4";
	private String poolUser = "SYSTEM";
	private String poolPass = "123456";
	private String baseUser = "kingbase";
	private String basePass = "123456";

	public String getInitThread() {
		return initThread;
	}

	public void setInitThread(String initThread) {
		this.initThread = initThread;
	}

	public String getMax_pool() {
		return max_pool;
	}

	public void setMax_pool(String max_pool) {
		this.max_pool = max_pool;
	}

	public String getPoolUser() {
		return poolUser;
	}

	public void setPoolUser(String poolUser) {
		this.poolUser = poolUser;
	}

	public String getPoolPass() {
		return poolPass;
	}

	public void setPoolPass(String poolPass) {
		this.poolPass = poolPass;
	}

	public String getBaseUser() {
		return baseUser;
	}

	public void setBaseUser(String baseUser) {
		this.baseUser = baseUser;
	}

	public String getBasePass() {
		return basePass;
	}

	public void setBasePass(String basePass) {
		this.basePass = basePass;
	}

	public String getzStrConfig() {
		return zStrConfig;
	}

	public void setzStrConfig(String zStrConfig) {
		this.zStrConfig = zStrConfig;
	}

	public String getbStrConfig() {
		return bStrConfig;
	}

	public void setbStrConfig(String bStrConfig) {
		this.bStrConfig = bStrConfig;
	}

	public List<TableNodeEntity> getList() {
		return list;
	}

	public void setList(List<TableNodeEntity> list) {
		this.list = list;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getCopyForm() {
		return copyForm;
	}

	public void setCopyForm(String copyForm) {
		this.copyForm = copyForm;
	}

	public String getMax_wal() {
		return max_wal;
	}

	public void setMax_wal(String max_wal) {
		this.max_wal = max_wal;
	}

	public String getMax_standby_arc() {
		return max_standby_arc;
	}

	public void setMax_standby_arc(String max_standby_arc) {
		this.max_standby_arc = max_standby_arc;
	}

	public String getWal_keep() {
		return wal_keep;
	}

	public void setWal_keep(String wal_keep) {
		this.wal_keep = wal_keep;
	}

	public String getMax_standby_str() {
		return max_standby_str;
	}

	public void setMax_standby_str(String max_standby_str) {
		this.max_standby_str = max_standby_str;
	}

	public String getReplication() {
		return replication;
	}

	public void setReplication(String replication) {
		this.replication = replication;
	}

	public String getWal_receiver() {
		return wal_receiver;
	}

	public void setWal_receiver(String wal_receiver) {
		this.wal_receiver = wal_receiver;
	}

	public String getHot_standby() {
		return hot_standby;
	}

	public void setHot_standby(String hot_standby) {
		this.hot_standby = hot_standby;
	}

	@Override
	public Image getImage(Object arg0) {
		return ImageURL.createImage(KBDeployCore.PLUGIN_ID, ImageURL.replication);

	}

	@Override
	public String getText(Object arg0) {
		return super.getName();
	}
}
