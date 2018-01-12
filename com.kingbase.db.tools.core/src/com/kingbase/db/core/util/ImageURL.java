package com.kingbase.db.core.util;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Duke
 *
 */
public class ImageURL {

	public static final String replication = "icons/DBNavigation_blue.png";
	public static final String tree_servers = "icons/server_this.png";
	public static final String tree_database_enable = "icons/database_enable.png";
	public static final String tree_database_disable = "icons/database_disable.png";
	public static final String tree_publish = "icons/release_16.png";
	public static final String status_publish = "icons/release_32.png";
	public static final String tree_subscriber_enable = "icons/subscribe_enable_16.png";
	public static final String tree_subscriber_disable = "icons/subscribe_disable_16.png";
	public static final String status_subscriber_enable = "icons/subscribe_enable_32.png";
	public static final String status_subscriber_disable = "icons/subscribe_disable_32.png";
	public static final String tree_table = "icons/table.png";
	public static final String tree_schema = "icons/schema.png";
	public static final String right_refresh = "icons/refresh.png";
	public static final String right_add = "icons/create.png";
	public static final String right_update = "icons/edit.png";
	public static final String right_delete = "icons/error_blue1.png";
	public static final String right_sync = "icons/sync.png";// 同步的用触发器的图标
	public static final String right_enable = "icons/ddl.png";
	public static final String right_disable = "icons/event.png";
	public static final String collapseall = "icons/collapseall.gif";
	public static final String grid = "icons/grid.gif";
	public static final String statusShow = "icons/sessions.png";
	public static final String folder = "icons/folder.png";
	public static final String undo = "icons/undo_blue.png";
	public static final String redo = "icons/redo_blue.png";
	public static final String file = "icons/file.png";

	public static Image createImage(String plugin, String imageUrl) {
		ImageDescriptor imageDcr = createImageDescriptor(plugin, imageUrl);
		return imageDcr.createImage();
	}

	public static ImageDescriptor createImageDescriptor(String plugin, String imageUrl) {
		Bundle bundle = Platform.getBundle(plugin);
		URL url = FileLocator.find(bundle, new Path(imageUrl), null);
		ImageDescriptor imageDcr = ImageDescriptor.createFromURL(url);
		return imageDcr;
	}
}
