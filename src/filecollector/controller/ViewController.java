package filecollector.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import filecollector.logic.PoolManager;
import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter;
import filecollector.logic.differentThreadsCollection.DirectoryCollectorStarter.WorkerType;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;
import filecollector.view.MainFrame;

public class ViewController implements IViewControllerCallback {

//	private final TextView textView = new TextView();
	boolean isShowMenu = true;
	MainFrame mf = new MainFrame(this);
	
	
	public ViewController() {
		mf.createMainFrame();
	}
	public void showMainFrame() {
		
	}
	public void closeMainFrame() {
		mf.closeMainFrame();
	}
	@Override
	public void sendInput(String input) {
//		isShowMenu = true;
//		if (input.length() == 1) {
//			if (input.equals("n")) {
//				textView.showInput();
//				isShowMenu = false;
//			}
//		} else {
//			String newDir = new String(input);
//			System.out.println("newDir: " + newDir);
//		}
//		showMenu();
	}
	@Override
	public void startCollect(DirectoryPath dirPath) {
		// Start DirCollStarter as Call- or Runnable
		PoolManager.getInstance().setMiscalus((ThreadPoolExecutor) Executors.newFixedThreadPool(2));
		DirectoryCollectorStarter dcs;
		try {
			dcs = new DirectoryCollectorStarter(dirPath.getDirectoryPath(), WorkerType.RUNNABLE);
			PoolManager.getInstance().getMiscalus().execute(dcs);
		} catch (NullPointerException | My_IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
