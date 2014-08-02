package filecollector.controller;

import org.apache.log4j.Logger;

import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter;
import filecollector.logic.differentThreadsCollection.IDirectoryWorkerControllerCallback;
import filecollector.logic.threadpool.ExecutorsTypeEnum;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;

public class DirectoryWorkerController implements IDirectoryWorkerControllerCallback {
	private static final Logger exc = Logger.getLogger("Exception");

	private IDirectoryWorkerCallback viewCtrlCallback;

	public DirectoryWorkerController(IDirectoryWorkerCallback viewCtrlCallback) {
		this.viewCtrlCallback = viewCtrlCallback;
	}
	public void startCollect(DirectoryPath dirPath) {
		// Start DirCollStarter as Call- or Runnable
		PoolIdentifier poolId;
		if ((poolId = PoolManager.getInstance().isPoolAvailable(ExecutorsTypeEnum.SINGLE)) == null)
			poolId = PoolManager.getInstance().newPool(ExecutorsTypeEnum.SINGLE);
		try {
			DirectoryCollectorStarter dcs = new DirectoryCollectorStarter(dirPath.getDirectoryPath(), this);
			PoolManager.getInstance().usePool(dcs, poolId).execute(dcs);
		} catch (NullPointerException | My_IllegalArgumentException e) {
			exc.error("Try to start collect", e);
		}
	}
	@Override
	public void finishCollectDirectories() {
		viewCtrlCallback.finishCollect();
	}
}
