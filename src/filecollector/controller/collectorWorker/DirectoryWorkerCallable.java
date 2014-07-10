package filecollector.controller.collectorWorker;

import java.util.concurrent.Callable;

import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerCallable extends AbstractDirectoryWorker implements Callable<DirectoryMember> {

	public DirectoryWorkerCallable (DirectoryMember directory) {
		super (directory);
	}
	@Override
	public DirectoryMember call () throws Exception {
		doProcess ();
		// TODO MW_140710: Noch nicht ganz klar wie... return directory;
		return null;
	}
}
