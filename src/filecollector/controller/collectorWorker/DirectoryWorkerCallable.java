package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.Callable;

import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerCallable extends DirectoryWorker implements Callable<DirectoryMember> {

	public DirectoryWorkerCallable (DirectoryMember directory) {
		super (directory);
	}
	@Override
	public DirectoryMember call () throws Exception {
		openDirectoryStreamInstance ();
		Iterator<Path> it = null;
		if (isDirStreamOpen)
			it = dirStream.iterator ();
		while (isDirStreamOpen) {
			if (it.hasNext ()) {
				processNextDirectoryEntry (it.next ());
			} else {
				closeDirectoryStreamInstance ();
			}
		}
		return directory;
	}
}
