/**
 * 
 */
package com.kingbase.db.deploy.bundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author duke
 *
 */
public class KBDeployCore extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "com.kingbase.db.deploy.bundle";

	private static KBDeployCore plugin;

	public KBDeployCore() {
		super();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static KBDeployCore getDefault() {
		return plugin;
	}

}
