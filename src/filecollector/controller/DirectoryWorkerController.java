package filecollector.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import filecollector.logic.PoolManager_OLD;
import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter;
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
		PoolManager_OLD.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newSingleThreadExecutor());
		DirectoryCollectorStarter dcs;
		try {
			dcs = new DirectoryCollectorStarter(dirPath.getDirectoryPath());
			PoolManager_OLD.getInstance().getMiscalus().execute(dcs);
		} catch (NullPointerException | My_IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewCtrlCallback.finishCollect(); // TEST, nicht die richtige Stelle
	}
}
