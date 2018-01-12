package com.kingbase.db.tools;

import java.util.ArrayList;
import java.util.List;

public class HtmlModel {

	private String name="";
	private String url="";
	
	private List<HtmlModel> listModel=new ArrayList<HtmlModel>();
	public List<HtmlModel> getListModel() {
		return listModel;
	}
	public void setListModel(List<HtmlModel> listModel) {
		this.listModel = listModel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
