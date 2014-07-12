package filecollector.controller;

import filecollector.controller.collectorWorker.DirectoryWorkerCallable;
import filecollector.controller.collectorWorker.DirectoryWorkerRunnable;
import filecollector.model.filemember.DirectoryMember;

public interface IWorkerExecuteCallback {

	void executeWorker(DirectoryWorkerRunnable curWorkerType, DirectoryMember dirMember);
	void executeWorker(DirectoryWorkerCallable curWorkerType, DirectoryMember dirMember);
}
