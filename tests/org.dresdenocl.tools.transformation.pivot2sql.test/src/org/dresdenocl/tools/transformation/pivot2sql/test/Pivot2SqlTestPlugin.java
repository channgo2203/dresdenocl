package org.dresdenocl.tools.transformation.pivot2sql.test;

import org.apache.log4j.Logger;
import org.dresdenocl.logging.LoggingPlugin;
import org.dresdenocl.metamodels.ecore.EcoreMetamodelPlugin;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * <p>
 * The activator class controls the plug-in life cycle.
 * </p>
 * 
 * @author Bjoern Freitag
 */
public class Pivot2SqlTestPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String ID =
			"org.dresdenocl.tools.transformation.pivot2sql.test"; //$NON-NLS-1$

	/** The shared instance. */
	private static Pivot2SqlTestPlugin plugin;

	/**
	 * <p>
	 * Creates a new {@link EcoreMetamodelPlugin}.
	 * </p>
	 */
	public Pivot2SqlTestPlugin() {

		plugin = this;
	}

	/**
	 * <p>
	 * Returns the shared instance.
	 * </p>
	 * 
	 * @return the shared instance
	 */
	public static Pivot2SqlTestPlugin getDefault() {

		return plugin;
	}

	/**
	 * <p>
	 * Facade method for the classes in this plug-in that hides the dependency
	 * from the <code>org.dresdenocl.logging</code> plug-in.
	 * </p>
	 * 
	 * @param clazz
	 *          The {@link Class} to return the {@link Logger} for.
	 * 
	 * @return A log4j {@link Logger}> instance.
	 * 
	 * @generated NOT
	 */
	public static Logger getLogger(Class<?> clazz) {

		return LoggingPlugin.getLogManager(plugin).getLogger(clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);

		/* configure custom logging properties. */
		LoggingPlugin.configureDefaultLogging(plugin);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;

		super.stop(context);
	}
}