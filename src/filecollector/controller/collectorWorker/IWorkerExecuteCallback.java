package filecollector.controller.collectorWorker;

import java.nio.file.Path;

import filecollector.model.filemember.DirectoryMember;

public interface IWorkerExecuteCallback {

	void executeWorker(DirectoryMember dirMember);
	void executeWorker(Path dir);
}
