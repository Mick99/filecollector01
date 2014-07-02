package filecollector.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import filecollector.controller.collectorWorker.DirectoryWorker;


public class ExecutorSingleton {
	Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName ()
	
	enum WhichExecutor {SINGLE, FIXEDPOOL, CACHEDPOOL}
	
	private ExecutorService executorService = null;
	private static ExecutorSingleton instance = null;
	
	public static ExecutorSingleton getInstance () {
		if (instance == null) {
			throw new NullPointerException ("instance cannot be null?");
		}
		return instance;
	}
	ExecutorSingleton (final WhichExecutor we,final int ... threadPoolSize) {
		
		switch (we) {
		case SINGLE:
			executorService = Executors.newSingleThreadExecutor ();
			break;
		case FIXEDPOOL:
			if (threadPoolSize.length > 0) {
				executorService = Executors.newFixedThreadPool (threadPoolSize[0]);
			}
			break;
		case CACHEDPOOL:
			executorService = Executors.newCachedThreadPool ();
			break;

		default:
			break;
		}
		instance = this;
	}
	public void executeWorker (DirectoryWorker directoryWorker) {
		Boolean isFinish = false;
		Future<Boolean> future = executorService.submit (directoryWorker, isFinish);
		try {
			isFinish = future.get ();
			log.error ("Future finish");
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Bsp. Ueberladene Methode um andere Runables auszufuehren... Parameter muss sein!
	public void executeWorker () {
		return;
	}
}
