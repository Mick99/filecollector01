package filecollector.controller;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import filecollector.logic.PoolManager;
import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter;
import filecollector.logic.differentThreadsCollection.KeyboardInput;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;
import filecollector.util.MyFileUtils;
import filecollector.view.MainFrame;

public class MainController {
	public static Boolean exitApp = false;

	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");


	public void entryApplikation (String[] args) {
		if (!MyFileUtils.isDirectory(Paths.get (args[0])))
			System.exit(1);
		ViewController vc = new ViewController();
		PoolManager.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newFixedThreadPool(2));
		DirectoryCollectorStarter dcs = new DirectoryCollectorStarter();
//		PoolManager.getInstance().getMiscalus().execute(dcs);
		// Bis key stop or quit signaliesiert
		while (!exitApp) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				exc.warn("MainController Interupted", e);
				Thread.currentThread().interrupt();
			}
		}
		PoolManager.getInstance().clearMiscalus();
		


	}
}
