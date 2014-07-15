package filecollector.controller;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import filecollector.logic.PoolManager;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;

public class MainController {

	private static final Logger msg = Logger.getLogger("Message");
//	private static final Logger exc = Logger.getLogger("Exception");

	private Collector collector;

	public void entryApplikation (String[] args) {

		collector = new Collector (Paths.get (args[0]));
		PoolManager.getInstance().setPool((ThreadPoolExecutor) Executors.newCachedThreadPool());

		// Differnz zur Zeitmessung
		long endTime = 0;
		long startTime = System.currentTimeMillis ();

		long futureDiffTime = 0;

		
		newCallExecutor_FutureGet();
//		newCallExecutor_Callable();
		endTime = System.currentTimeMillis ();
		long sleepDiffTime = endTime - startTime;


		PoolManager.getInstance().clearPoolWorker();
		msg.info ("Sleep DIFF(ms) : " + sleepDiffTime);
		msg.info ("FutureGet DIFF(ms) : " + futureDiffTime);
		collector.printTest ();
	}
	private void newCallExecutor_FutureGet () {
//		collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET, true));
		WorkerExecutor workerExecutor = new WorkerExecutor();
		workerExecutor.executeWorker(collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET, true));
	}
	private void newCallExecutor_Callable () {
		WorkerExecutor workerExecutor = new WorkerExecutor();
		workerExecutor.executeWorker(collector.getROOT_DIRECTORY());
		System.out.println("Callable finish");
	}
}
