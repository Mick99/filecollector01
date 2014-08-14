package filecollector.logic.differentThreadsCollection;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import filecollector.controller.MainController;
import filecollector.controller.RunOrCallableEnum;
import filecollector.controller.collectorWorker.DirectoryAttributeAppendOnce;
import filecollector.controller.collectorWorker.WorkerExecutor;
import filecollector.logic.threadpool.ExecutorsTypeEnum;
import filecollector.logic.threadpool.IPoolIdentifier;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.Collector;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;
import filecollector.model.filemember.DirectoryMember;

public class DirectoryCollectorStarter extends Thread implements IPoolIdentifier {
	private static final Logger msg = Logger.getLogger("Message");

	private DirectoryPath directoryPath;
	private PoolIdentifier poolIdentifier;
	private IDirectoryWorkerControllerCallback directoryWorkerController;

	// No Default constructor
	public DirectoryCollectorStarter(Path dirPath, IDirectoryWorkerControllerCallback dirWorkerController) throws My_IllegalArgumentException {
		directoryPath = new DirectoryPath(dirPath);
		directoryWorkerController = dirWorkerController;
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
		PoolIdentifier poolId = PoolManager.getInstance().newPool(ExecutorsTypeEnum.CACHED);
		msg.debug("PoolManager: " + PoolManager.getInstance().toString());
		if (MainController.runOrCallableEnum != null) {
			if (MainController.runOrCallableEnum == RunOrCallableEnum.RUNNABLE)
				newCallExecutor_FutureGet(poolId);
			if (MainController.runOrCallableEnum == RunOrCallableEnum.CALLABLE)
				newCallExecutor_Callable(poolId);
		}
		PoolManager.getInstance().clearPool(poolId);
		directoryWorkerController.finishCollectDirectories();
	}
	private void newCallExecutor_FutureGet(PoolIdentifier poolId) {
		WorkerExecutor workerExecutor = new WorkerExecutor();
		DirectoryMember dirMember = new DirectoryMember(directoryPath.getDirectoryPath());
		new DirectoryAttributeAppendOnce(dirMember);
		workerExecutor.executeWorker(dirMember, poolId);
		new Collector(dirMember);
	}
	private void newCallExecutor_Callable(PoolIdentifier poolId) {
		List<DirectoryMember> dmList = new ArrayList<>(100);
		WorkerExecutor workerExecutor = new WorkerExecutor(dmList);
		workerExecutor.executeWorker(directoryPath.getDirectoryPath(), poolId);
		new Collector(dmList);
	}
	@Override
	public String toString() {
		return directoryPath.toString() + " :: " + poolIdentifier.toString();
	}
	@Override
	public void transferNewIdentifier(PoolIdentifier poolId) {
		poolIdentifier = poolId.newIdentifier();
	}
	@Override
	public String infoStrFALSCH() {
		return String.format("Start from Dir=%s , %s", directoryPath.getDirectoryPath().toString(), this.getClass().getSimpleName());
	}
}
