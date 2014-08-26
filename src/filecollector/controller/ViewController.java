package filecollector.controller;

import java.util.List;

import javax.swing.tree.MutableTreeNode;

import filecollector.model.Collector;
import filecollector.model.DirectoryPath;
import filecollector.model.ViewSortEnum;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember;
import filecollector.view.MainFrame;

public class ViewController implements IGuiCallback, IDirectoryWorkerCallback {

	private MainFrame mf;
	private Collector collector;
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
	public void finishCollect(Collector collector) {
		// Wenn alle Threads fertig sind, hier melden...
		this.collector = collector;
		mf.newDirectoryStructure();
	}
	@Override
	public MutableTreeNode getDirTreeStructure(ViewSortEnum vs) {
		if (collector == null)
			return null;
		return collector.getDirTreeStructure(vs);
	}
	@Override
	public void dirListToTreeStructure(DirectoryMember dm, MutableTreeNode constructTreeNode) {
		// collector.dirListToTreeStructure(dm, constructTreeNode);
		collector.dirListToTreeStructure(dm, constructTreeNode);
	}
	@Override
	public List<FileSystemMember> getTableView() {
		return collector.getTableView();
	}
	@Override
	public List<FileSystemMember> removeFromTableView(MutableTreeNode removeChildsFromTreeNode) {
		return collector.removeFromTableView(removeChildsFromTreeNode);
	}
}
