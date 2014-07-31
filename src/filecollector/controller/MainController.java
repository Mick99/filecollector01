package filecollector.controller;

import java.nio.file.Paths;

import org.apache.log4j.Logger;

import filecollector.logic.PoolManager_OLD;
import filecollector.logic.threadpool.PoolManager;
import filecollector.util.MyFileUtils;

public class MainController {
	public static Boolean exitApp = false;
	public static RunOrCallableEnum runOrCallableEnum = RunOrCallableEnum.RUNNABLE;

//	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");


	public void entryApplikation (String[] args) {
//		if (!MyFileUtils.isDirectory(Paths.get (args[0])))
//			System.exit(1);
		ViewController vc = new ViewController();
		// Bis key stop or quit signaliesiert
		while (!exitApp) {
			try {
				Thread.sleep(3000);
//				PoolManager_OLD.getInstance().checkAndCleanPools();
				PoolManager.getInstance().checkAndCleanPools();
			} catch (InterruptedException e) {
				exc.warn("MainController Interupted", e);
				Thread.currentThread().interrupt();
			}
		}
//		PoolManager_OLD.getInstance().shutdownAllPools();
		PoolManager.getInstance().shutdownAllPools();
	}
}
