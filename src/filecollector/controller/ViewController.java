package filecollector.controller;

import filecollector.model.DirectoryPath;
import filecollector.view.MainFrame;

public class ViewController implements IGuiCallback, IDirectoryWorkerCallback {

//	private final TextView textView = new TextView();
	private boolean isShowMenu = true;
	private MainFrame mf;
	private DirectoryWorkerController dirWorkerCtrl;
	
	
	public ViewController() {
		mf = new MainFrame(this);
		mf.createMainFrame();
		dirWorkerCtrl = new DirectoryWorkerController(this);
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
		dirWorkerCtrl.startCollect(dirPath);
	}
	@Override
	public void finishCollect() {
		// Wenn alle Threads fertig sind, hier melden...
		System.out.println("FFFFFIIIIIIIIIIIIIIINNNNNNNNNNNNNIIIIIIIIIIIIIISSSSSSSHHHHH");
		
	}
}
