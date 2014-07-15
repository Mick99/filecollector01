package filecollector.controller.collectorWorker;

import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

import filecollector.controller.ExecutorSingleton;
import filecollector.controller.IWorkerExecuteCallback;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryWorkerRunnable extends AbstractDirectoryWorker implements Runnable {

	private IWorkerExecuteCallback exeCallback;

//	public DirectoryWorkerRunnable(final DirectoryMember directory) {
	public DirectoryWorkerRunnable(final DirectoryMember directory, final IWorkerExecuteCallback exeCallback) {
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
	protected void addDirectoryMemberAndCreateNewWorker (final Path dirEntry) {
		DirectoryMember dm = new DirectoryMember (dirEntry);
		appendAttributes(dm);
		directory.addFileSystemMember (dm);
		exeCallback.executeWorker(dm);
	}
}
