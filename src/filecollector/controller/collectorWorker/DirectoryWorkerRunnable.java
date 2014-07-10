package filecollector.controller.collectorWorker;


import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends AbstractDirectoryWorker implements Runnable {

	public DirectoryWorkerRunnable (final DirectoryMember directory) {
		super (directory);
	}

	@Override
	public void run () {
		if (directory != null) {
			this.doProcess ();
		}
	}
}
