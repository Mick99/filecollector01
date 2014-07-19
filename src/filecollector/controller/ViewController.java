package filecollector.controller;

import filecollector.view.MainFrame;

public class ViewController implements IViewControllerCallback {

//	private final TextView textView = new TextView();
	boolean isShowMenu = true;
	MainFrame mf = new MainFrame();
	
	
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
}
