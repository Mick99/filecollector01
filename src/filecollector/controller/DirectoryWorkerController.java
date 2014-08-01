package filecollector.controller;

import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter;
import filecollector.logic.threadpool.ExecutorsTypeEnum;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;

public class DirectoryWorkerController {

	private IDirectoryWorkerCallback viewCtrlCallback;
	
	public DirectoryWorkerController(IDirectoryWorkerCallback viewCtrlCallback) {
		this.viewCtrlCallback = viewCtrlCallback;
	}
	public void startCollect(DirectoryPath dirPath) {
		// Start DirCollStarter as Call- or Runnable
//		PoolManager.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newFixedThreadPool(2));
//		PoolManager_OLD.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newSingleThreadExecutor());
		PoolIdentifier poolId;
		if ((poolId = PoolManager.getInstance().isPoolAvailable(ExecutorsTypeEnum.SINGLE)) == null)
			poolId = PoolManager.getInstance().newPool(ExecutorsTypeEnum.SINGLE);
		DirectoryCollectorStarter dcs;
		try {
			dcs = new DirectoryCollectorStarter(dirPath.getDirectoryPath());
//			PoolManager_OLD.getInstance().getMiscalus().execute(dcs);
			PoolManager.getInstance().usePool(dcs, poolId).execute(dcs);
		} catch (NullPointerException | My_IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewCtrlCallback.finishCollect(); // TEST, nicht die richtige Stelle
	}
}
