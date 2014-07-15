package filecollector.controller;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import filecollector.controller.ExecutorSingleton;
import filecollector.controller.ExecutorSingleton.WhichExecutor;
import filecollector.controller.collectorWorker.WorkerCounter;
import filecollector.logic.PoolManager;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;
import filecollector.util.SleepUtils;

public class MainController {

	private static final Logger msg = Logger.getLogger("Message");
//	private static final Logger exc = Logger.getLogger("Exception");

	private Collector collector;

	public void entryApplikation (String[] args) {

		collector = new Collector (Paths.get (args[0]));
//		new ExecutorSingleton (WhichExecutor.CACHEDPOOL);
		PoolManager.getInstance().setPool((ThreadPoolExecutor) Executors.newCachedThreadPool());

		// startFirstWorkerThread ();
		// callExecutor ();

		// Differnz zur Zeitmessung
		long endTime = 0;
		long startTime = System.currentTimeMillis ();

		long futureDiffTime = 0;

		// Only for tests set Enum to get current test to run...
//		TestExecutorEnum.setCurrentEnum (TestExecutorEnum.FUTURE_GET_EXECUTOR);
//		startTime = System.currentTimeMillis ();
//		testCallExecutor ();
//		endTime = System.currentTimeMillis ();
//		futureDiffTime = endTime - startTime;

		// Only for tests set Enum to get current test to run...
//		TestExecutorEnum.setCurrentEnum (TestExecutorEnum.FUTURE_GET_EXECUTOR);
//		startTime = System.currentTimeMillis ();
//		testCallExecutor ();
		
//		newCallExecutor_FutureGet();
		newCallExecutor_Callable();
		endTime = System.currentTimeMillis ();
		long sleepDiffTime = endTime - startTime;


		PoolManager.getInstance().clearPoolWorker();
//		ExecutorSingleton.getInstance ().shutdownExecutor ();
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
	private void testCallExecutor () {
		switch (TestExecutorEnum.getCurrentEnum ()) {
		case SLEEP_EXECUTOR:
			callExecutor_Sleep ();
			break;
		case FUTURE_GET_EXECUTOR:
			callExecutor_FutureGet ();
			break;
		case CALLABLE_EXECUTOR:
			callExecutor_Callable ();

			break;

		default:
			break;
		}
	}

	private void callExecutor_FutureGet () {
		// ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL);
		// executor.executeWorker (collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET));
		// executor.shutdownExecutor ();
		ExecutorSingleton.getInstance ().executeWorker (
				collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET, true));
	}

	private void callExecutor_Sleep () {
		// ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL);
		// executor.executeWorker (collector.getCollectionView (CollectionViewSelectorEnum.TEST_SLEEP));
		ExecutorSingleton.getInstance ().executeWorker (
				collector.getCollectionView (CollectionViewSelectorEnum.TEST_SLEEP, true));
		/**
		 * !!! Das ist nicht immer SICHER, allWorkerFinish kann 0 sein obwohl noch weitere Verzeichnissse da sind. Wird
		 * nur ueber sleep 10 entschaerft, kann mit sleep 0 geprueft werden !!!
		 */

		do {
			SleepUtils.safeSleep (TimeUnit.MILLISECONDS, 10);
			msg.trace ("ALIVE");
		} while (!WorkerCounter.allWorkerFinish ());
		// executor.shutdownExecutor ();
	}
	private void callExecutor_Callable () {
		ExecutorSingleton.getInstance ().executeWorker (
				collector.getCollectionView (CollectionViewSelectorEnum.TEST_CALLABLE, true));
	}
}
