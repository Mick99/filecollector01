package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import filecollector.controller.ExecutorSingleton;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerCallable extends AbstractDirectoryWorker implements Callable<DirectoryMember> {

	public DirectoryWorkerCallable (final Path dir) {
		super (dir);
	}
	@Override
	public DirectoryMember call () throws Exception {
		if (directory != null) {
			doProcess ();
		}
		return directory;
	}
	@Override
	protected void createNewDirectoryWorker (DirectoryMember dm) {
		// TODO MW_140711: Have to impl a eg. WorkerHelperSingleton to get correct type instance. ExeSgl maybe old.
		ExecutorSingleton.getInstance ().executeWorker (dm);
	}
}
