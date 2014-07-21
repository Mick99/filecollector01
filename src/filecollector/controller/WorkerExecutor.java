package filecollector.controller;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import filecollector.controller.collectorWorker.DirectoryWorkerCallable;
import filecollector.controller.collectorWorker.DirectoryWorkerRunnable;
import filecollector.logic.PoolManager;
import filecollector.model.filemember.DirectoryMember;

public class WorkerExecutor implements IWorkerExecuteCallback {

	public List<DirectoryMember> resultCallable = new ArrayList<>();
	
	@Override
	public void executeWorker(DirectoryMember dirMember) {
		System.out.println("Runnable");
		DirectoryWorkerRunnable workerRunnable = new DirectoryWorkerRunnable (dirMember,this);
		executeWorker_FutureGet (workerRunnable);
	}
	@Override
	public void executeWorker(Path dir) {
		System.out.println("Callable");
		DirectoryWorkerCallable workerCallable = new DirectoryWorkerCallable (dir, this);
		executeWorker_Callable (workerCallable);
		
	}
	private void executeWorker_FutureGet (DirectoryWorkerRunnable directoryWorker) {
		Future<?> future = PoolManager.getInstance().getPool().submit ((Runnable) directoryWorker);
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
		Future<DirectoryMember> future = PoolManager.getInstance().getPool().submit ((Callable<DirectoryMember>) directoryWorker);
		try {
			/* TODO MW_140705: Have to redesign! dirMember is not really necessary, because DirectoryMember is in 
			 * DirectoryWorker as member attribute defined. 
			 */
			DirectoryMember dirMember = future.get ();
			if (dirMember != null)
				resultCallable.add(dirMember);
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
