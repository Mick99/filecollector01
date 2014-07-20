package filecollector.controller;

import filecollector.model.DirectoryPath;
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
	}
}
