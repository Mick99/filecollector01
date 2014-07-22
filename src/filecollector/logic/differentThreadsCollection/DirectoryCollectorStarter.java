package filecollector.logic.differentThreadsCollection;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


import filecollector.controller.collectorWorker.WorkerExecutor;
import filecollector.logic.PoolManager;
import filecollector.model.Collector;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryCollectorStarter extends Thread {

	public enum WorkerType {
		RUNNABLE, CALLABLE;
	}
	private Collector collector;
	private DirectoryPath directoryPath;
	private WorkerType workerType;

	// No Default constructor
	public DirectoryCollectorStarter(Path dirPath, WorkerType workerType) throws My_IllegalArgumentException {
		directoryPath = new DirectoryPath(dirPath);
		this.workerType = workerType;
	}
	// May be not useful
	public DirectoryCollectorStarter(String dirString) throws My_IllegalArgumentException {
		directoryPath = new DirectoryPath(dirString);
	}
	public DirectoryPath dummyTEST() {
		return directoryPath;
	}
	@Override
	public void run() {
//		collector = new Collector ();
		PoolManager.getInstance().setPool((ThreadPoolExecutor) Executors.newCachedThreadPool());
		if (workerType != null) {
			if (workerType == WorkerType.RUNNABLE) 
				newCallExecutor_FutureGet();
			if (workerType == WorkerType.CALLABLE)
				newCallExecutor_Callable();
		}
		PoolManager.getInstance().clearPoolWorker();
	}
	private void newCallExecutor_FutureGet () {
//		collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET, true));
		WorkerExecutor workerExecutor = new WorkerExecutor();
		DirectoryMember dirMember = new DirectoryMember(directoryPath.getDirectoryPath());
		workerExecutor.executeWorker(dirMember);
		System.out.println("newCallExecutor_FutureGet");
		Collector col = new Collector(dirMember);
		col.printTest();
	}
	private void newCallExecutor_Callable () {
		WorkerExecutor workerExecutor = new WorkerExecutor();
//		workerExecutor.executeWorker(collector.getROOT_DIRECTORY());
		System.out.println("Callable finish");
	}
	@Override
	public String toString() {
		return directoryPath.toString() + " :: " + super.toString();
	}
}
