package filecollector.controller.collectorWorker;

import filecollector.controller.ExecutorSingleton;
import filecollector.controller.IWorkerExecuteCallback;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends AbstractDirectoryWorker implements Runnable {

	private IWorkerExecuteCallback exeCallback;

	public DirectoryWorkerRunnable(final DirectoryMember directory) {
//	public DirectoryWorkerRunnable(final DirectoryMember directory, final IWorkerExecuteCallback exeCallback) {
		super(directory);
		this.exeCallback = exeCallback;
	}
	@Override
	public void run() {
		if (directory != null) {
			doProcess();
		}
	}
	@Override
	public void createNewDirectoryWorker(DirectoryMember dm) {
		// TODO MW_140711: Have to impl a eg. WorkerHelperSingleton to get correct type instance. ExeSgl maybe old.
		ExecutorSingleton.getInstance().executeWorker(dm);
		// TODO MW_140712: Above problem solved via Callback interface from new to design class "WorkerExecutor.
//		exeCallback.executeWorker(this, dm);
	}
}
