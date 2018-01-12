/**
 * 
 */
package com.kingbase.db.deploy.bundle.graphical.parts;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.SWT;

import com.kingbase.db.deploy.bundle.graphical.model.DeployConnection;

/**
 * @author jpliu
 *
 */
public class ConnectLineEditorPart extends AbstractConnectionEditPart{
	protected IFigure createFigure() {
		DeployConnection model = (DeployConnection) getModel();

		PolylineConnection connection = new PolylineConnection();
		connection.setLineWidth(2);
		if (model.getStatus() != null && model.getStatus().equals("up")) {

			connection.setForegroundColor(ColorConstants.lightGreen);
			connection.setToolTip(new Label("up")); 
		} else if (model.getStatus() != null && model.getStatus().equals("down")) {

			connection.setForegroundColor(ColorConstants.red);
			connection.setToolTip(new Label("down"));
		}else if (model.getStatus() != null && model.getStatus().equals("unused")) {
			connection.setToolTip(new Label("unused"));
			connection.setForegroundColor(ColorConstants.darkBlue);
		}else if (model.getStatus() != null && model.getStatus().equals("waitting")) {
			connection.setToolTip(new Label("waitting"));
			connection.setForegroundColor(ColorConstants.darkGreen);
		}else{
			connection.setForegroundColor(ColorConstants.black);
		}

		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setConnectionRouter(null);
		connectionLayer.setAntialias(SWT.ON);

		connection.setTargetDecoration(new PolygonDecoration());
		connection.setConnectionRouter(new BendpointConnectionRouter());
		return connection;
	}
	@Override
	protected void createEditPolicies() {
	}
	
}
