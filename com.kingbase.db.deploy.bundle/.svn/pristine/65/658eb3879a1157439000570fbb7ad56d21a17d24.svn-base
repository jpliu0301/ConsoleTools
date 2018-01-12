package com.kingbase.db.deploy.bundle.model.tree;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class KeyValueModifier implements ICellModifier {

	public final static String[] KEY_VALUE= { "key", "value"};
	
	private TableViewer tv;
	
	public KeyValueModifier(TableViewer tv){
		this.tv=tv;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		if (property.equals(KEY_VALUE[0])) {
			return false;
		}
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		KeyValueEntity p = (KeyValueEntity) element;
		if (property.equals(KEY_VALUE[0])) {
			return p.getKey();
		}else if (property.equals(KEY_VALUE[1])) {
			return p.getValue();
		} 
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem) element;

		KeyValueEntity p = (KeyValueEntity) item.getData();

		if (property.equals(KEY_VALUE[0])) {
			p.setKey(value.toString());
		} else if (property.equals(KEY_VALUE[1])) {
			p.setValue(value.toString());
		}
		tv.update(p, null);

	}

}
