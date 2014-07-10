package filecollector.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import filecollector.controller.collectorWorker.DirectoryWorkerCallable;
import filecollector.controller.collectorWorker.DirectoryWorkerRunnable;
import filecollector.model.filemember.DirectoryMember;

public class ExecutorSingleton {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	enum WhichExecutor {
		SINGLE, FIXEDPOOL, CACHEDPOOL
	}
	private ExecutorService executorService = null;
	private static ExecutorSingleton instance = null;

	ExecutorSingleton (final WhichExecutor we, final int... threadPoolSize) {
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
		msg.info ("shutdown");
		try {
			if (!executorService.awaitTermination (2, TimeUnit.SECONDS)) {
				executorService.shutdownNow ();
				exc.warn ("shutdownNow");
				if (!executorService.awaitTermination (2, TimeUnit.SECONDS)) {
					exc.error ("awaitTermination");
				}
			}
		} catch (InterruptedException e) {
			executorService.shutdown ();
			// Preserve interrupt status
			Thread.currentThread ().interrupt ();
		}
	}
	// Bsp. Ueberladene Methode um andere Runables auszufuehren... Parameter
	// muss sein!
	public void executeWorker () {
		return;
	}
	public void executeWorker (DirectoryMember directoryMember) {
		DirectoryWorkerRunnable workerRunnable;
		DirectoryWorkerCallable workerCallable;

		switch (TestExecutorEnum.getCurrentEnum ()) {
		case SLEEP_EXECUTOR:
			workerRunnable = new DirectoryWorkerRunnable (directoryMember);
			executeWorker_Sleep (workerRunnable);
			break;
		case FUTURE_GET_EXECUTOR:
			workerRunnable = new DirectoryWorkerRunnable (directoryMember);
			executeWorker_FutureGet (workerRunnable);
			break;
		case CALLABLE_EXECUTOR:
//			workerCallable = new DirectoryWorkerCallable (directoryMember);
//			executeWorker_Callable (workerCallable);
			break;

		default:
			break;
		}
	}
	// private <T extends DirectoryWorker> void executeWorker_Sleep (T
	// directoryWorker)
	private void executeWorker_Sleep (DirectoryWorkerRunnable directoryWorker) {
		executorService.execute (directoryWorker);
	}
	private void executeWorker_FutureGet (DirectoryWorkerRunnable directoryWorker) {
		Future<?> future = executorService.submit ((Runnable) directoryWorker);
		try {
			future.get ();
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}
	private void executeWorker_Callable (DirectoryWorkerCallable directoryWorker) {
		Future<DirectoryMember> future = executorService.submit ((Callable<DirectoryMember>) directoryWorker);
		try {
			/* TODO MW_140705: Have to redesign! dirMember is not really necessary, because DirectoryMember is in 
			 * DirectoryWorker as member attribute defined. 
			 */
			DirectoryMember dirMember = future.get ();
			if (true)
				dirMember = dirMember;
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
