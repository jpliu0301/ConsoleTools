package com.kingbase.db.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import com.kingbase.db.core.KBCorePlugin;

/**
 * 
 * @author Duke
 *
 */
public class PlatformUtil {

	/**
	 * 打开向导
	 * 
	 * @param wizard
	 * @param selection
	 * @param shell
	 */
	public static void openWizard(IWizard wizard, ISelection selection, Shell shell) {
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		}

		if (wizard instanceof INewWizard) {
			((INewWizard) wizard).init(PlatformUI.getWorkbench(), selectionToPass);
		}

		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
	}

	/**
	 * 获得插件下指定路径的文件对象
	 * 
	 * @param path
	 * @return
	 */
	public static File getConfigurationFile(String path) {
		return getConfigurationFile(KBCorePlugin.PLUGIN_ID, path);
	}

	public static File getConfigurationFile(String pluginId, String path) {
		Bundle bundle = Platform.getBundle(pluginId);
		URL fullPathString = BundleUtility.find(bundle, path);
		URL localURL = null;
		try {
			if (fullPathString == null) {
				File newfile = new File(path);
				if (!newfile.exists()) {
					newfile.createNewFile();
				}
				return newfile;
			}
			localURL = Platform.asLocalURL(fullPathString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(localURL.getFile());
		return file;
	}

	/**
	 * 获得工作区路径
	 * 
	 * @return
	 */
	public static String getWorkspaceLocation() {
		return getWorkspaceRoot().getLocation().toString();
	}

	/**
	 * 获得工作区
	 * 
	 * @return
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * 打开编辑器
	 * 
	 * @param editInput
	 * @param editorId
	 */
	public static IEditorPart openEditor(IEditorInput editInput, String editorId) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activityPage = window.getActivePage();
		try {
			IEditorPart editor = activityPage.findEditor(editInput);
			if (editor != null) {
				activityPage.bringToTop(editor);
				return null;
			} else {
				editor = activityPage.openEditor(editInput, editorId);
				return editor;
			}

		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 关闭已经打开的编辑器
	 * 
	 * @param editInput 通过打开的input找到对应的编辑器，删除节点的同时关闭编辑器
	 * @param isSave
	 * @return
	 */
	public static boolean closeEditor(IEditorInput editInput, boolean isSave) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activityPage = window.getActivePage();
		IEditorPart editor = activityPage.findEditor(editInput);
		if (editor != null) {
			return activityPage.closeEditor(editor, isSave);
		} else {
			return true;
		}
	}

	public static IProject getProject(String projName) {
		IProject project = getWorkspaceRoot().getProject(projName);
		try {
			if (!project.exists()) {
				project.create(null);
				IPath path =null;

				IFile file = (IFile) project.getFile("release.xml");
				if (file == null || !file.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "release.xml");
					file = getWorkspaceRoot().getFile(path);
					if (!file.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());

						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("data-sources");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				IFile file1 = (IFile) project.getFile("subscribe.xml");
				if (file1 == null || !file1.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "subscribe.xml");
					file1 = getWorkspaceRoot().getFile(path);
					if (!file1.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());
						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("data-sources");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				project.refreshLocal(2, null);
			}
			if (!project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return project;
	}

	public static IProject getColonyProject(String projName) {
		IProject project = getWorkspaceRoot().getProject(projName);
		try {
			if (!project.exists()) {
				project.create(null);

				IPath path = new Path(project.getLocation().toOSString() + File.separator + "cnode");
				IFolder folderNode = (IFolder) project.findMember(path);
				if (folderNode == null) {
					FileUtil.createDirectory(path.toOSString());
					folderNode = project.getFolder("cnode");
				}

				path = new Path(project.getLocation().toOSString() + File.separator + "masterstand");
				IFolder folderMaster = (IFolder) project.findMember(path);
				if (folderMaster == null) {
					FileUtil.createDirectory(path.toOSString());
					folderMaster = project.getFolder("masterstand");
				}

				path = new Path(project.getLocation().toOSString() + File.separator + "readwrite");
				IFolder folderRead = (IFolder) project.findMember(path);
				if (folderRead == null) {
					FileUtil.createDirectory(path.toOSString());
					folderRead = project.getFolder("readwrite");
				}

				IFile file = (IFile) folderNode.getFile("node.xml");
				if (file == null || !file.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "cnode/cnode.xml");
					file = getWorkspaceRoot().getFile(path);
					if (!file.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());

						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("cnodes");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				IFile file1 = (IFile) folderMaster.getFile("master.xml");
				if (file1 == null || !file1.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "masterstand/master.xml");
					file1 = getWorkspaceRoot().getFile(path);
					if (!file1.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());
						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("masters");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				IFile file2 = (IFile) folderMaster.getFile("read.xml");
				if (file2 == null || !file2.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "readwrite/read.xml");
					file2 = getWorkspaceRoot().getFile(path);
					if (!file2.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());
						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("reads");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				project.refreshLocal(2, null);
			}
			if (!project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return project;
	}
	public static IProject getConsoleProject(String projName) {
		IProject project = getWorkspaceRoot().getProject(projName);
		try {
			if (!project.exists()) {
				project.create(null);

				IPath path = new Path(project.getLocation().toOSString() + File.separator + "backupSet");
				IFolder folderNode = (IFolder) project.findMember(path);
				if (folderNode == null) {
					FileUtil.createDirectory(path.toOSString());
					folderNode = project.getFolder("backupSet");
				}

				IFile file = (IFile) folderNode.getFile("backupSet.xml");
				if (file == null || !file.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "backupSet/backupSet.xml");
					file = getWorkspaceRoot().getFile(path);
					if (!file.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());

						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("backupSets");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				
				IPath path1 = new Path(project.getLocation().toOSString() + File.separator + "ioTuning");
				IFolder folderNode1 = (IFolder) project.findMember(path1);
				if (folderNode1 == null) {
					FileUtil.createDirectory(path1.toOSString());
					folderNode1 = project.getFolder("ioTuning");
				}
				
				IFile file1 = (IFile) folderNode1.getFile("ioTuning.xml");
				if (file1 == null || !file1.exists()) {
					path1 = new Path(project.getLocation().toOSString() + File.separator + "ioTuning/ioTuning.xml");
					file1 = getWorkspaceRoot().getFile(path1);
					if (!file1.exists()) {
						File fileCon = new File(path1.toString());
						FileUtil.createFile(path1.toString());
						
						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("ioTunings");
						
						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {
							
						}
					}
				}


				
				IPath pathService = new Path(project.getLocation().toOSString() + File.separator + "serviceManagement");
				IFolder folderNodeService = (IFolder) project.findMember(pathService);
				if (folderNodeService == null) {
					FileUtil.createDirectory(pathService.toOSString());
					folderNodeService = project.getFolder("serviceManagement");
				}
				IFile fileService = (IFile) folderNodeService.getFile("serviceManagement.xml");
				if (fileService == null || !fileService.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "serviceManagement/serviceManagement.xml");
					file = getWorkspaceRoot().getFile(path);
					if (!file.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());

						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("serviceManagement");// 根节点

						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {

						}
					}
				}
				
				IPath pathService1 = new Path(project.getLocation().toOSString() + File.separator + "logAnalysis");
				IFolder folderNodeService1 = (IFolder) project.findMember(pathService1);
				if (folderNodeService1 == null) {
					FileUtil.createDirectory(pathService1.toOSString());
					folderNodeService1 = project.getFolder("logAnalysis");
				}
				IFile fileService1 = (IFile) folderNodeService1.getFile("logAnalysis.xml");
				if (fileService1 == null || !fileService1.exists()) {
					path = new Path(project.getLocation().toOSString() + File.separator + "logAnalysis/logAnalysis.xml");
					file = getWorkspaceRoot().getFile(path);
					if (!file.exists()) {
						File fileCon = new File(path.toString());
						FileUtil.createFile(path.toString());
						
						Document document = DocumentHelper.createDocument();
						Element root = document.addElement("logAnalysis");// 根节点
						
						try {
							XMLWriter output = new XMLWriter(new FileWriter(fileCon));
							output.write(document);
							output.close();
						} catch (IOException e) {
							
						}
					}
				}
				

				project.refreshLocal(2, null);
			}
			if (!project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return project;
	}

}
