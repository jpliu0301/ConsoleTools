package com.kingbase.db.deploy.bundle.model.tree;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class TableNodeModifier implements ICellModifier {

	public final static String[] PROP_NAME = { "library", "physicalMachine", "nodeType",
			"listenerAddress","listenerPost" };
	
	
	public final static String[] LIBRARY = { "主库", "备库", "主分发","备分发"};
	
	public final static String[] ONELIBRARY = { "主库", "备库"};
	
	public final static String[] TWOLIBRARY = { "主分发","备分发"};


	
	private String[] phymac = new String[]{};

	public final static String[] NODETYPE = { "主backend", "备backend", "主pool","备pool"};

	public final static String[] ONENODETYPE = { "主backend", "备backend"};
	
	public final static String[] TWONODETYPE = {"主pool","备pool"};


	private TableViewer tv;

	public TableNodeModifier(TableViewer tv,String[] phymac) {
		this.tv = tv;
		this.phymac=phymac;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {

		TableNodeEntity p = (TableNodeEntity) element;
		if (property.equals(PROP_NAME[0])) {
			for (int i = 0; i < LIBRARY.length; i++) {
				if(p.getLibrary().equals(LIBRARY[i])){
					return i;
				}
			}
		}else if (property.equals(PROP_NAME[1])) {
			for (int i = 0; i < phymac.length; i++) {
				if(p.getPhysicalMachine().equals(phymac[i])){
					return i;
				}
			}
		} else if (property.equals(PROP_NAME[2])) {
			for (int i = 0; i < NODETYPE.length; i++) {
				if(p.getNodeType().equals(NODETYPE[i])){
					return i;
				}
			}
		}else if (property.equals(PROP_NAME[3])) {
			return p.getListenerAddress();
		}else if (property.equals(PROP_NAME[4])) {
			return p.getListenerPost();
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem) element;

		TableNodeEntity p = (TableNodeEntity) item.getData();

		if (property.equals(PROP_NAME[0])) {
			p.setLibrary(LIBRARY[(int) value]);
		} else if (property.equals(PROP_NAME[1])) {
			p.setPhysicalMachine(phymac[(int) value]);
		}else if (property.equals(PROP_NAME[2])) {
			p.setNodeType(NODETYPE[(int) value]);
		}else if (property.equals(PROP_NAME[3])) {
			p.setListenerAddress((String)value);
		}else if (property.equals(PROP_NAME[4])) {
			p.setListenerPost((String)value);
		}
		tv.update(p, null);
	}
}
