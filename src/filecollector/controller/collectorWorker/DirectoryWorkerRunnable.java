package filecollector.controller.collectorWorker;


import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends AbstractDirectoryWorker implements Runnable {

	public DirectoryWorkerRunnable (DirectoryMember directory) {
		super (directory);
	}

	@Override
	public void run () {
		this.doProcess ();
	}
}
