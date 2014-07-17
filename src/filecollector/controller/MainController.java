package filecollector.controller;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import filecollector.logic.PoolManager;
import filecollector.logic.differentThreadsCollection.CollectorStarter;
import filecollector.logic.differentThreadsCollection.KeyboardInput;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;
import filecollector.util.MyFileUtils;

public class MainController {

	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");


	public void entryApplikation (String[] args) {
		if (!MyFileUtils.isDirectory(Paths.get (args[0])))
			System.exit(1);
		PoolManager.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newFixedThreadPool(2));
		KeyboardInput key = new KeyboardInput();
		CollectorStarter cs = new CollectorStarter();
		PoolManager.getInstance().getMiscalus().execute(key);
//		PoolManager.getInstance().getMiscalus().execute(cs);
		// Bis key stop or quit signaliesiert
		while (key.isNotQuit()) {
			try {
				Thread.sleep(4000);
//				System.out.println("Main");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				exc.warn("MainController Interupted", e);
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		PoolManager.getInstance().clearMiscalus();


	}
}
