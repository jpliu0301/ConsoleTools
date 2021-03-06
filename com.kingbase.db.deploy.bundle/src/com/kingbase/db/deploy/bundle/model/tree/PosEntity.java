package com.kingbase.db.deploy.bundle.model.tree;

import java.util.List;

public class PosEntity {

	private String name;
	private List<TableNodeEntity> listDb;
	private String dbzipPath="";
	private String dbzipName="";
	private Boolean isDbBase=true;
	private String max_wal = "";
	private String max_standby_arc = "";
	private String wal_keep = "";
	private String max_standby_str = "";
	private String replication = "";
	private String wal_receiver = "";
	private String hot_standby = "";
	private String posPath="";
	private String listenerPort="";
	private String dbUser="";
	private String dbPassword="";
	private boolean insensitive=true;
	private List<KeyValueEntity> listKey1;
	
	public String getDbzipName() {
		return dbzipName;
	}
	public void setDbzipName(String dbzipName) {
		this.dbzipName = dbzipName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TableNodeEntity> getListDb() {
		return listDb;
	}
	public void setListDb(List<TableNodeEntity> listDb) {
		this.listDb = listDb;
	}
	public String getDbzipPath() {
		return dbzipPath;
	}
	public void setDbzipPath(String dbzipPath) {
		this.dbzipPath = dbzipPath;
	}
	public Boolean getIsDbBase() {
		return isDbBase;
	}
	public void setIsDbBase(Boolean isDbBase) {
		this.isDbBase = isDbBase;
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
	public String getPosPath() {
		return posPath;
	}
	public void setPosPath(String posPath) {
		this.posPath = posPath;
	}
	public List<KeyValueEntity> getListKey1() {
		return listKey1;
	}
	public void setListKey1(List<KeyValueEntity> listKey1) {
		this.listKey1 = listKey1;
	}
	public String getListenerPort() {
		return listenerPort;
	}
	public void setListenerPort(String listenerPort) {
		this.listenerPort = listenerPort;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public boolean isInsensitive() {
		return insensitive;
	}
	public void setInsensitive(boolean insensitive) {
		this.insensitive = insensitive;
	}
	
}
