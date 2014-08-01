package filecollector.controller.collectorWorker;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import filecollector.logic.PoolManager_OLD;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.filemember.DirectoryMember;

public class WorkerExecutor implements IWorkerExecuteCallback {

	public List<DirectoryMember> resultCallable;
	
	public WorkerExecutor() {
	}
	public WorkerExecutor(List<DirectoryMember> resultList) {
		resultCallable = resultList;
	}
	@Override
	public void executeWorker(DirectoryMember dirMember, PoolIdentifier poolId) {
		System.out.println("Runnable");
		DirectoryWorkerRunnable workerRunnable = new DirectoryWorkerRunnable (dirMember,this);
		executeWorker_FutureGet (workerRunnable, poolId);
	}
	@Override
	public void executeWorker(Path dir, PoolIdentifier poolId) {
		System.out.println("Callable");
		DirectoryWorkerCallable workerCallable = new DirectoryWorkerCallable (dir, this);
		executeWorker_Callable (workerCallable, poolId);
		
	}
	private void executeWorker_FutureGet (DirectoryWorkerRunnable directoryWorker, PoolIdentifier poolId) {
//		Future<?> future = PoolManager_OLD.getInstance().getPool().submit ((Runnable) directoryWorker);
		Future<?> future = PoolManager.getInstance().usePool(directoryWorker, poolId).submit(directoryWorker);
		try {
			future.get ();
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}
	private void executeWorker_Callable (DirectoryWorkerCallable directoryWorker, PoolIdentifier poolId) {
//		Future<DirectoryMember> future = PoolManager_OLD.getInstance().getPool().submit ((Callable<DirectoryMember>) directoryWorker);
		Future<DirectoryMember> future = PoolManager.getInstance().usePool(directoryWorker, poolId).submit(directoryWorker);
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
