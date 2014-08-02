package filecollector.controller;

import filecollector.model.DirectoryPath;
import filecollector.view.MainFrame;

public class ViewController implements IGuiCallback, IDirectoryWorkerCallback {

	private MainFrame mf;
	private DirectoryWorkerController dirWorkerCtrl;

	public ViewController() {
		mf = new MainFrame(this);
		mf.createMainFrame();
		dirWorkerCtrl = new DirectoryWorkerController(this);
	}
	public void repaintMainFrame() {
		// not used
	}
	public void closeMainFrame() {
		mf.closeMainFrame();
	}
	@Override
	public void sendInput(String input) {
		// not used
	}
	@Override
	public void startCollect(DirectoryPath dirPath) {
		dirWorkerCtrl.startCollect(dirPath);
	}
	@Override
	public void finishCollect() {
		// Wenn alle Threads fertig sind, hier melden...
	}
}
