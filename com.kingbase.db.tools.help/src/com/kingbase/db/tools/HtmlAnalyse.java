package com.kingbase.db.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlAnalyse {

	private static String indexPath = null;
	private static XmlUtil xmlUtil;
	private static String resPath = null;
	private static String encoding = "utf-8";
	
	public static void main(String[] args) {
		
		if(args.length != 2 && args.length != 3) {
			System.out.println("usage:");
			System.out.println("    HtmlAnalyse htmlPath resultPath [encoding]");
			System.exit(1);
		}
		
		indexPath = args[0];
		resPath = args[1];
		if (args.length == 3) {
			encoding = args[2];
		}
		
		File file=new File(indexPath,"index.html");
		List<HtmlModel> list=new ArrayList<HtmlModel>();
		getHtmlModel(file, list, encoding);
		
		xmlUtil = new XmlUtil(resPath);
		xmlUtil.writeXml(list);
		System.out.println("finish");
	}


	public static void getHtmlModel(File file,List<HtmlModel> list, String encoding){
		Document doc;
		try {
			doc = Jsoup.parse(file, encoding);
			Elements elementDl=doc.getElementsByTag("dt");
			HtmlModel model=null;
			for (int i = 0; i < elementDl.size(); i++) {
				if(elementDl.get(i).nextElementSibling()!=null){
					if(elementDl.get(i).nextElementSibling().tagName().equals("dd")||elementDl.get(i).lastElementSibling().tagName().equals("dd")){
						model=new HtmlModel();
						Elements elementA=elementDl.get(i).getElementsByTag("a");
						if(elementA.size()>0){
							String url=elementA.get(0).attr("href");
							if(url.contains(".html")&&!url.contains("html#")){
								String name=elementDl.get(i).text();
								model.setName(name);
								model.setUrl(url);
								list.add(model);
							}

						}

					}
					else {
						elementDl.get(i).parents();
						if(elementDl.get(i).text()!=""&&elementDl.get(i).getElementsByTag("a").size()>0){
							Elements elementA=elementDl.get(i).getElementsByTag("a");
							if(elementA.size()>0){
								String url=elementA.get(0).attr("href");
								HtmlModel model1=new HtmlModel();
								String name=elementDl.get(i).text();
								model1.setName(name);
								model1.setUrl(url);
								if(url.contains(".html")&&!url.contains("html#")){
									File fileChile=new File(indexPath,url);
									getHtmlModel(fileChile, model1.getListModel(), encoding);
								}
								if(model==null){
									model=new HtmlModel();
									model.getListModel().add(model1);
									list.add(model);
								}
								else{
									model.getListModel().add(model1);
								}

							}

						}

					}
				}
				else{
					if(elementDl.get(i).tagName().equals("dt")){
						Elements elementA=elementDl.get(i).getElementsByTag("a");
						if(elementA.size()>0){
							String url=elementA.get(0).attr("href");
							String urlText=elementA.get(0).text();
							if(!url.equals("bookindex.html")&&!urlText.equals("Index")){
								HtmlModel model1=new HtmlModel();
								String name=elementDl.get(i).text();
								model1.setName(name);
								model1.setUrl(url);
								if(url.contains(".html")&&!url.contains("html#")){
									File fileChile=new File(indexPath,url);
									getHtmlModel(fileChile, model1.getListModel(), encoding);
								}
								if(model==null){
									model=new HtmlModel();
									model.getListModel().add(model1);
									list.add(model);
								}
								else{
									model.getListModel().add(model1);
								}
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
