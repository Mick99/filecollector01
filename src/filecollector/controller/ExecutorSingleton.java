package filecollector.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import filecollector.controller.collectorWorker.DirectoryWorker;


public class ExecutorSingleton {
	Logger log = Logger.getLogger ("MW_Level"); // MainController.class.getSimpleName ()
	
	enum WhichExecutor {SINGLE, FIXEDPOOL, CACHEDPOOL}
	
	private ExecutorService executorService = null;
	private static ExecutorSingleton instance = null;
	
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
		if (executorService != null)
			instance = this;
	}
	public static ExecutorSingleton getInstance () {
		if (instance == null) {
			throw new NullPointerException ("instance cannot be null?");
		}
		return instance;
	}
	public void shutdownExecutor () {
		executorService.shutdown ();
		try {
			if (!executorService.awaitTermination (2, TimeUnit.SECONDS)) {
				executorService.shutdownNow ();
				if (!executorService.awaitTermination (2, TimeUnit.SECONDS)) {
					log.error ("awaitTermination");
				}
			}
		} catch (InterruptedException e) {
			executorService.shutdown ();
			// Preserve interrupt status
			Thread.currentThread ().interrupt ();
		}
	}
	// Bsp. Ueberladene Methode um andere Runables auszufuehren... Parameter muss sein!
	public void executeWorker () {
		return;
	}
	public void executeWorker (DirectoryWorker directoryWorker) {
		switch (TestExecutorEnum.getCurrentEnum ()) {
		case SLEEP_EXECUTOR:
			executeWorker_Sleep (directoryWorker);
			break;
		case FUTURE_GET_EXECUTOR:
			executeWorker_FutureGet (directoryWorker);
			break;
		case CALLABLE_EXECUTOR:
			
			break;

		default:
			break;
		}
	}
	private void executeWorker_Sleep (DirectoryWorker directoryWorker) {
		executorService.execute (directoryWorker);
	}
	private void executeWorker_FutureGet (DirectoryWorker directoryWorker) {
		Future<?> future = executorService.submit (directoryWorker);
		try {
			future.get ();
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
