package filecollector.controller;

import java.nio.file.Paths;

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
		callExecutor ();
		collector.printTest ();

	}
	private void callExecutor () {
		DirectoryWorker dw = new DirectoryWorker (collector.getCollectionView (CollectionViewSelectorEnum.ORIG_UNSORTED));
		ExecutorSingleton executor = new ExecutorSingleton (WhichExecutor.FIXEDPOOL, 20);
		executor.executeWorker (dw);
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
