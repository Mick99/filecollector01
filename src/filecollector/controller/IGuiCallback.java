package filecollector.controller;

import java.util.List;

import javax.swing.tree.MutableTreeNode;

import filecollector.model.DirectoryPath;
import filecollector.model.ViewSortEnum;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember;

public interface IGuiCallback {

	void sendInput(String input);
	void startCollect(DirectoryPath dirPath);
	// Methods for TreeModel...
	MutableTreeNode getDirTreeStructure(ViewSortEnum vs);
	void dirListToTreeStructure(DirectoryMember dm, MutableTreeNode constructTreeNode);
	// Methods for TableModel..
	List<FileSystemMember> getTableView();
	List<FileSystemMember> removeFromTableView(MutableTreeNode removeChildsFromTreeNode);
	
}
