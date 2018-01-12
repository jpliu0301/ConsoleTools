package com.kingbase.db.deploy.bundle.views;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jpliu
 *
 */
public class LogManagerDeploy {

	public static Logger createLogger(int logId, String logFile) {
		start(logId, logFile);
		return LoggerFactory.getLogger("" + logId);
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	protected static void start(int logId, String logFile) {
		String logID = "" + logId;
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		Layout layout = PatternLayout.createLayout("%-d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %m%n", null, config, null, null, true, false, null,
				null);
		Appender appender = FileAppender.createAppender(logFile, "true", "false", logID, null,
				"true", "true", null, layout, null, null, null, config);
		appender.start();
		config.addAppender(appender);
		AppenderRef ref = AppenderRef.createAppenderRef(logID, null, null);
		AppenderRef[] refs = new AppenderRef[] { ref };
		LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.ALL, logID, "true", refs,
				null, config, null);
		loggerConfig.addAppender(appender, null, null);
		config.addLogger(logID, loggerConfig);
		ctx.updateLoggers();
	}

	public static void stop(int logId) {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		String logID = "" + logId;
		config.getAppender(logID).stop();
		config.getLoggerConfig(logID).removeAppender(logID);
		config.removeLogger(logID);
		ctx.updateLoggers();
	}

	private static LoggerContext getContext(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

}
