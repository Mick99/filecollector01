package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.Callable;

import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerCallable extends AbstractDirectoryWorker implements Callable<DirectoryMember> {

	public DirectoryWorkerCallable (DirectoryMember directory) {
		super (directory);
	}
	@Override
	public DirectoryMember call () throws Exception {
		doProcess ();
		return directory;
	}
}
