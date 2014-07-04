package filecollector.controller;

import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;

import filecollector.controller.collectorWorker.DirectoryWorker;
import filecollector.controller.ExecutorSingleton;
import filecollector.controller.ExecutorSingleton.WhichExecutor;
import filecollector.controller.collectorWorker.WorkerCounter;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;

public class MainController {

	Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName ()
	
	private Collector collector;
	
	public void entryApplikation (String[] args) {
		
		collector = new Collector (Paths.get (args[0]));
//		startFirstWorkerThread ();
//		callExecutor ();
		
		// Differnz zur Zeitmessung
		long endTime = 0;
		long startTime = System.currentTimeMillis ();
		
		// Only for tests set Enum to get current test to run...
		TestExecutorEnum.setCurrentEnum (TestExecutorEnum.FUTURE_GET_EXECUTOR); 
		startTime = System.currentTimeMillis ();
		testCallExecutor ();
		endTime = System.currentTimeMillis ();
		long futureDiffTime = endTime - startTime;
		
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
		DirectoryWorker dw = new DirectoryWorker (collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET));
		ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL); // FIXEDPOOL, 40
		executor.executeWorker (dw);
		executor.shutdownExecutor ();
	}
	private void callExecutor_Sleep () {
		DirectoryWorker dw = new DirectoryWorker (collector.getCollectionView (CollectionViewSelectorEnum.TEST_SLEEP));
		ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.CACHEDPOOL); // FIXEDPOOL, 40
		executor.executeWorker (dw);
		/**
		 * !!! Das ist nicht immer SICHER, allWorkerFinish kann 0 sein obwohl noch weitere Verzeichnissse da sind.
		 * Wird nur ueber sleep 10 entschaerft, kann mit sleep 0 geprueft werden !!!
		 */
		do {
			try {
				Thread.sleep (10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info ("ALIVE");
		} while (!WorkerCounter.allWorkerFinish ());
		executor.shutdownExecutor ();
	}
	private void startFirstWorkerThread () {
		DirectoryWorker firstWorker = new DirectoryWorker (collector.getCollectionView (CollectionViewSelectorEnum.ORIG_UNSORTED));
		Thread t = new Thread (firstWorker);
		t.start ();
		/**
		 * !!! Das ist nicht immer SICHER, allWorkerFinish kann 0 sein obwohl noch weitere Verzeichnissse da sind.
		 * Wird nur ueber sleep 10 entschaerft, kann mit sleep 0 geprueft werden !!!
		 */
		do {
			try {
				Thread.sleep (10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info ("ALIVE");
		} while (!WorkerCounter.allWorkerFinish ());
	}

}
