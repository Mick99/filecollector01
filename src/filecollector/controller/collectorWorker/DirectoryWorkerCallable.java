package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.concurrent.Callable;

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
}
