package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.filemember.DirectoryMember;

public class WorkerExecutor implements IWorkerExecuteCallback {
	private static final Logger exc = Logger.getLogger("Exception");

	public List<DirectoryMember> resultCallable;

	public WorkerExecutor() {
	}
	public WorkerExecutor(List<DirectoryMember> resultList) {
		resultCallable = resultList;
	}
	@Override
	public void executeWorker(DirectoryMember dirMember, PoolIdentifier poolId) {
		DirectoryWorkerRunnable workerRunnable = new DirectoryWorkerRunnable(dirMember, this);
		executeWorker_FutureGet(workerRunnable, poolId);
	}
	@Override
	public void executeWorker(Path dir, PoolIdentifier poolId) {
		DirectoryWorkerCallable workerCallable = new DirectoryWorkerCallable(dir, this);
		executeWorker_Callable(workerCallable, poolId);

	}
	private void executeWorker_FutureGet(DirectoryWorkerRunnable directoryWorker, PoolIdentifier poolId) {
		Future<?> future = PoolManager.getInstance().usePool(directoryWorker, poolId).submit(directoryWorker);
		try {
			future.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			exc.info("FuturGet Interrupt", e);
		} catch (ExecutionException e) {
			exc.error("FuturGet Execution", e);
		}
	}
	private void executeWorker_Callable(DirectoryWorkerCallable directoryWorker, PoolIdentifier poolId) {
		Future<DirectoryMember> future = PoolManager.getInstance().usePool(directoryWorker, poolId).submit(directoryWorker);
		try {
			DirectoryMember dirMember = future.get();
			if (dirMember != null)
				resultCallable.add(dirMember);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			exc.info("Callable Interrupt", e);
		} catch (ExecutionException e) {
			exc.error("Callable Execution", e);
		}
	}
}
