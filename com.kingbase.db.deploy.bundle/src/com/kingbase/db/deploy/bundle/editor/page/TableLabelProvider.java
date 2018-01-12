package com.kingbase.db.deploy.bundle.editor.page;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.kingbase.db.deploy.bundle.model.tree.TableNodeEntity;

public class TableLabelProvider extends LabelProvider implements
ITableLabelProvider {

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableNodeEntity entity = (TableNodeEntity) element;
		if (columnIndex == 0) {
			return entity.getLibrary();
		} else if (columnIndex == 1) {
			return entity.getPhysicalMachine();
		} else if (columnIndex == 2) {
			return entity.getNodeType();
		} else if (columnIndex == 3) {
			return entity.getListenerAddress();
		}  else if (columnIndex == 4) {
			return entity.getListenerPost();
		} 

		return element.toString();
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}
