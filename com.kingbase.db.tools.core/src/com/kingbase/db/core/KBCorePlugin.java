/**
 * 
 */
package com.kingbase.db.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author duke
 *
 */
public class KBCorePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.kingbase.db.core"; //$NON-NLS-1$

	private static KBCorePlugin plugin;

	public KBCorePlugin() {
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

	public static KBCorePlugin getDefault() {
		return plugin;
	}
}
