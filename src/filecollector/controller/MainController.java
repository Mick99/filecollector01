package filecollector.controller;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.PoolManager;

public class MainController {
	public static Boolean exitApp = false;
	public static RunOrCallableEnum runOrCallableEnum = RunOrCallableEnum.CALLABLE;

	// private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	public void entryApplikation(String[] args) {
		new ViewController();
		while (!exitApp) {
			try {
				Thread.sleep(3000);
				PoolManager.getInstance().checkAndCleanPools();
			} catch (InterruptedException e) {
				exc.warn("MainController Interupted", e);
				Thread.currentThread().interrupt();
			}
		}
		PoolManager.getInstance().shutdownAllPools();
	}
}
