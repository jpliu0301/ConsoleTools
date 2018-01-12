package com.kingbase.db.deploy.bundle.editor.page;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.kingbase.db.deploy.bundle.model.tree.KeyValueEntity;

public class KeyValueProvider extends LabelProvider implements
ITableLabelProvider {

	@Override
	public String getColumnText(Object element, int columnIndex) {
		KeyValueEntity entity = (KeyValueEntity) element;
		if (columnIndex == 0) {
			return entity.getKey();
		} else if (columnIndex == 1) {
			return entity.getValue();
		}
		return element.toString();
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}