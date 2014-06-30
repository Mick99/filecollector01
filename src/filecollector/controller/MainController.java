package filecollector.controller;

import java.nio.file.Paths;

import org.apache.log4j.Logger;

import filecollector.model.CollectionViewSelector;
import filecollector.model.Collector;

public class MainController {

	Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName ()
	
	private Collector collector;
	
	public void entryApplikation (String[] args) {
		
		collector = new Collector (Paths.get (args[0]));
		startFirstWorkerThread ();
		collector.printTest ();

	}
	private void startFirstWorkerThread () {
		DirectoryWorker firstWorker = new DirectoryWorker (collector.getCollectionView (CollectionViewSelector.ORIG_UNSORTED));
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
