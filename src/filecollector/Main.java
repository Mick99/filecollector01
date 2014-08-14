package filecollector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import filecollector.controller.MainController;

public class Main {
	/*
	 * Log4j als sysout missbraucht Levels TRACE < DEBUG < INFO < WARN < ERROR < FATAL
	 */
	// static Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName ()
	// private static final Logger log = Logger.getRootLogger();
	private static final long RELOAD = 60000L;
	private static final Logger msg = Logger.getLogger("Message");
	// private static final Logger exc = Logger.getLogger("Exception");

	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch("config/Log4j.properties", RELOAD);
		msg.warn("Start");
		MainController mc = new MainController();
		mc.entryApplikation(args);
	}
}
