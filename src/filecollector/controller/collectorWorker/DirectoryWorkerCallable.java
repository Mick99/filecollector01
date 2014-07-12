package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import filecollector.controller.ExecutorSingleton;
import filecollector.controller.IWorkerExecuteCallback;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerCallable extends AbstractDirectoryWorker implements Callable<DirectoryMember> {

	private IWorkerExecuteCallback exeCallback;

	public DirectoryWorkerCallable(final Path dir, IWorkerExecuteCallback exeCallback) {
		super(dir);
		this.exeCallback = exeCallback;
	}
	@Override
	public DirectoryMember call() throws Exception {
		if (directory != null) {
			doProcess();
		}
		return directory;
	}
	@Override
	protected void createNewDirectoryWorker(DirectoryMember dm) {
		// comments look at ..Runnable
		ExecutorSingleton.getInstance().executeWorker(dm);
//		exeCallback.executeWorker(this, dm);

	}
}
