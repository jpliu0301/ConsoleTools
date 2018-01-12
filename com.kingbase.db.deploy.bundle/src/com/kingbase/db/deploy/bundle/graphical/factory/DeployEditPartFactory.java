package com.kingbase.db.deploy.bundle.graphical.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.pentaho.di.graphical.editpart.NodeLabelEditPart;
import org.pentaho.di.graphical.editpart.ProcessContainerPart;
import org.pentaho.di.graphical.model.AbstractActivityNode;

import com.kingbase.db.deploy.bundle.graphical.model.DeployConnection;
import com.kingbase.db.deploy.bundle.graphical.model.DeployContentsMasterModel;
import com.kingbase.db.deploy.bundle.graphical.model.DeployContentsModel;
import com.kingbase.db.deploy.bundle.graphical.parts.DeployContetntsPart;
import com.kingbase.db.deploy.bundle.graphical.parts.ConnectLineEditorPart;

public class DeployEditPartFactory implements EditPartFactory{

	@Override
	public EditPart createEditPart(EditPart arg0, Object model) {
		EditPart part = null;
		if ((model instanceof DeployContentsModel)||(model instanceof DeployContentsMasterModel) ) {
			part = new ProcessContainerPart();
		} else if (model instanceof DeployConnection) {
			part = new ConnectLineEditorPart();
		} else if (model instanceof AbstractActivityNode) {
			part = new DeployContetntsPart();
		} else {
			part = new NodeLabelEditPart();
		}

		part.setModel(model);
		return part;
	}

}
