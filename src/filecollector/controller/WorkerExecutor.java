package filecollector.controller;

import filecollector.controller.collectorWorker.DirectoryWorkerCallable;
import filecollector.controller.collectorWorker.DirectoryWorkerRunnable;
import filecollector.model.filemember.DirectoryMember;

public class WorkerExecutor implements IWorkerExecuteCallback {

	@Override
	public void executeWorker(DirectoryWorkerRunnable curWorkerType, DirectoryMember dirMember) {
		// TODO Auto-generated method stub
		System.out.println("Runnable");

	}

	@Override
	public void executeWorker(DirectoryWorkerCallable curWorkerType, DirectoryMember dirMember) {
		// TODO Auto-generated method stub
		System.out.println("Callable");

	}

}
