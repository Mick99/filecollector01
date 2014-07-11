package filecollector.controller.collectorWorker;


import filecollector.controller.ExecutorSingleton;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends AbstractDirectoryWorker implements Runnable {

	public DirectoryWorkerRunnable (final DirectoryMember directory) {
		super (directory);
	}
	@Override
	public void run () {
		if (directory != null) {
			doProcess ();
		}
	}
	@Override
	public void createNewDirectoryWorker (DirectoryMember dm) {
		// TODO MW_140711: Have to impl a eg. WorkerHelperSingleton to get correct type instance. ExeSgl maybe old.
		ExecutorSingleton.getInstance ().executeWorker (dm);
	}
}
