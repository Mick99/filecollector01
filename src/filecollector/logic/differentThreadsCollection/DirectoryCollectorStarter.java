package filecollector.logic.differentThreadsCollection;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


import filecollector.controller.MainController;
import filecollector.controller.RunOrCallableEnum;
import filecollector.controller.collectorWorker.WorkerExecutor;
import filecollector.logic.PoolManager_OLD;
import filecollector.logic.threadpool.ExecutorsTypeEnum;
import filecollector.logic.threadpool.IPoolIdentifier;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.CollectionViewSelectorEnum;
import filecollector.model.Collector;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;
import filecollector.model.PrintTest;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryCollectorStarter extends Thread implements IPoolIdentifier {

	private Collector collector;
	private DirectoryPath directoryPath;
	private PoolIdentifier poolIdentifier;

	// No Default constructor
	public DirectoryCollectorStarter(Path dirPath) throws My_IllegalArgumentException {
		directoryPath = new DirectoryPath(dirPath);
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
//		PoolManager_OLD.getInstance().setPool((ThreadPoolExecutor) Executors.newCachedThreadPool());
		PoolIdentifier poolId = PoolManager.getInstance().newPool(ExecutorsTypeEnum.CACHED);
		System.out.println(PoolManager.getInstance().toString());
		if (MainController.runOrCallableEnum != null) {
			if (MainController.runOrCallableEnum == RunOrCallableEnum.RUNNABLE) 
				newCallExecutor_FutureGet(poolId);
			if (MainController.runOrCallableEnum == RunOrCallableEnum.CALLABLE)
				newCallExecutor_Callable(poolId);
		}
//		PoolManager_OLD.getInstance().clearPoolWorker();
		PoolManager.getInstance().clearPool(poolId);
	}
	private void newCallExecutor_FutureGet (PoolIdentifier poolId) {
//		collector.getCollectionView (CollectionViewSelectorEnum.TEST_FUTURE_GET, true));
		WorkerExecutor workerExecutor = new WorkerExecutor();
		DirectoryMember dirMember = new DirectoryMember(directoryPath.getDirectoryPath());
		workerExecutor.executeWorker(dirMember, poolId);
		System.out.println("FutureGet finish");
		// Only for TEST
		new Collector(dirMember);
		PrintTest p = new PrintTest();
		p.printTest(Collector.getCollector().getCollectionView(CollectionViewSelectorEnum.ORIG_UNSORTED));
	}
	private void newCallExecutor_Callable (PoolIdentifier poolId) {
		List<DirectoryMember> dmList = new ArrayList<>(100);
		WorkerExecutor workerExecutor = new WorkerExecutor(dmList);
		workerExecutor.executeWorker(directoryPath.getDirectoryPath(), poolId);
		System.out.println("Callable finish");
		// Only for TEST
		new Collector(dmList);
		
//		PrintTest p = new PrintTest();
//		p.printTest(Collector.getCollector().getCollectionView(CollectionViewSelectorEnum.ORIG_UNSORTED));
	}
	@Override
	public String toString() {
		return directoryPath.toString()+" :: "+poolIdentifier.toString();
	}
	@Override
	public void transferNewIdentifier(PoolIdentifier poolId) {
		poolIdentifier = poolId.newIdentifier();
	}
}
