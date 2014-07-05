package filecollector.controller;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import filecollector.controller.collectorWorker.DirectoryWorker;
import filecollector.controller.collectorWorker.DirectoryWorkerRunnable;
import filecollector.controller.ExecutorSingleton;
import filecollector.controller.ExecutorSingleton.WhichExecutor;
import filecollector.controller.collectorWorker.WorkerCounter;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;
import filecollector.util.SleepUtils;

public class MainController {

	Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName
												// ()

	private Collector collector;

	public void entryApplikation (String[] args) {

		collector = new Collector (Paths.get (args[0]));
		// startFirstWorkerThread ();
		// callExecutor ();

		// Differnz zur Zeitmessung
		long endTime = 0;
		long startTime = System.currentTimeMillis ();

		long futureDiffTime = 0;
		/*
		 * // Only for tests set Enum to get current test to run...
		 * TestExecutorEnum.setCurrentEnum
		 * (TestExecutorEnum.FUTURE_GET_EXECUTOR); startTime =
		 * System.currentTimeMillis (); testCallExecutor (); endTime =
		 * System.currentTimeMillis (); long futureDiffTime = endTime -
		 * startTime;
		 */
		// Only for tests set Enum to get current test to run...
		TestExecutorEnum.setCurrentEnum (TestExecutorEnum.SLEEP_EXECUTOR);
		startTime = System.currentTimeMillis ();
		testCallExecutor ();
		endTime = System.currentTimeMillis ();
		long sleepDiffTime = endTime - startTime;

		log.error ("Sleep DIFF(ms) : " + sleepDiffTime);
		log.error ("FutureGet DIFF(ms) : " + futureDiffTime);
		collector.printTest ();

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

			break;

		default:
			break;
		}
	}

	private void callExecutor_FutureGet () {
		DirectoryWorker dw = new DirectoryWorker (
				collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET));
		ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL);

		executor.executeWorker (dw);
		executor.shutdownExecutor ();
	}

	private void callExecutor_Sleep () {
		DirectoryWorkerRunnable dwr = new DirectoryWorkerRunnable (
				collector.getCollectionView (CollectionViewSelectorEnum.TEST_SLEEP));
		ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL);
		executor.executeWorker (dwr);
		/**
		 * !!! Das ist nicht immer SICHER, allWorkerFinish kann 0 sein obwohl
		 * noch weitere Verzeichnissse da sind. Wird nur ueber sleep 10
		 * entschaerft, kann mit sleep 0 geprueft werden !!!
		 */

		do {
			SleepUtils.safeSleep (TimeUnit.MILLISECONDS, 10);
			log.info ("ALIVE");
		} while (!WorkerCounter.allWorkerFinish ());
		executor.shutdownExecutor ();
	}
}
