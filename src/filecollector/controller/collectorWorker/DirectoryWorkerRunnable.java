package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.util.Iterator;

import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends DirectoryWorker implements Runnable {

	public DirectoryWorkerRunnable (DirectoryMember directory) {
		super (directory);
	}

	@Override
	public void run () {
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
	}
}
