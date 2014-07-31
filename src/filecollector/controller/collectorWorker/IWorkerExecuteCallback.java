package filecollector.controller.collectorWorker;

import java.nio.file.Path;

import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.model.filemember.DirectoryMember;

public interface IWorkerExecuteCallback {

	void executeWorker(DirectoryMember dirMember, PoolIdentifier poolId);
	void executeWorker(Path dir, PoolIdentifier poolId);
}
