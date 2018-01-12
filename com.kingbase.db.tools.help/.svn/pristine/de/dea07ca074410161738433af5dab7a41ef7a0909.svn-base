package com.kingbase.db.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtil {

	static String filePath = null;
	static String fileName = "";
	

	XmlUtil(String filePath) {
		XmlUtil.filePath = filePath;
	}
	public void writeXml(List<HtmlModel> list){

		File tocPath = new File(filePath + "//toc");
		if (! tocPath.exists()) {
			tocPath.mkdirs();
		}
		
		Document pluginDoc = DocumentHelper.createDocument();
		Element tocRoot = pluginDoc.addElement("plugin").addElement("extension").addAttribute("point", "org.eclipse.help.toc");
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).getName();
			fileName = list.get(i).getUrl().substring(0, list.get(i).getUrl().lastIndexOf("."));
			fileName =  "toc" + fileName.substring(0,  1).toUpperCase() + fileName.substring(1).toLowerCase() + ".xml";

			tocRoot.addElement("toc").addAttribute("file", "toc/" + fileName).addAttribute("primary", "true");
			File fileLocal = new File(filePath + "//toc//" + fileName);
			if (!fileLocal.exists()) {
				try {
					fileLocal.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("toc");
			root.addAttribute("label", list.get(i).getName());
			root.addAttribute("topic", "html/" + list.get(i).getUrl());
			wXml(root, list.get(i).getListModel());
			try {
				XMLWriter output = new XMLWriter(new FileWriter(fileLocal),xmlFormat());
				output.write(document);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File pluginPath = new File( filePath + "//plugin.xml");
		if (!pluginPath.exists()) {
			try {
				pluginPath.createNewFile();
				FileWriter fw = new FileWriter(pluginPath);
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				fw.write("<?eclipse version=\"3.4\"?>\n");
				fw.flush();
				fw.close();
				XMLWriter output = new XMLWriter(new FileWriter(pluginPath, true),xmlFormat());
				output.write(pluginDoc);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void wXml(Element element, List<HtmlModel> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName() != "") {
				Element element1 = element.addElement("topic");
				element1.addAttribute("label",list.get(i).getName());
				element1.addAttribute("href", "html/"+list.get(i).getUrl());
				wXml(element1, list.get(i).getListModel());
			} else {
				wXml(element, list.get(i).getListModel());
			}
		}
	}

	public static OutputFormat xmlFormat() {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf8");
		format.setSuppressDeclaration(true);
		format.setIndent(true);
		format.setIndent("  ");
		format.setNewlines(true);
		return format;
	}
}
