/**
 * 
 */
package com.kingbase.db.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Duke
 *
 */
public class FileUtil {

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
		} else {
			File[] fx = file.listFiles();
			for (int i = 0; i < fx.length; i++) {
				delete(fx[i]);
			}
		}
		file.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void delete(String filePath) {
		Path path = Paths.get(filePath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件复制
	 * 
	 * @param sourcePath
	 * @param targetPath
	 */
	public static void copyFile(String sourcePath, String targetPath) {
		Path source = Paths.get(sourcePath);
		Path target = Paths.get(targetPath);
		try {
			Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新建文件
	 * 
	 * @param filePath
	 */
	public static void createFile(String filePath) {
		Path path = Paths.get(filePath);
		try {
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新建目录/文件夹
	 * 
	 * @param filePath
	 */
	public static void createDirectory(String filePath) {
		Path path = Paths.get(filePath);
		try {
			if (!Files.exists(path)) {
				Files.createDirectory(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean exist(String filePath) {
		Path path = Paths.get(filePath);
		return Files.exists(path);
	}
}
