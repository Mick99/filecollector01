package filecollector.controller;

import javax.swing.tree.MutableTreeNode;

import filecollector.model.DirectoryPath;
import filecollector.model.ViewSortEnum;
import filecollector.model.filemember.DirectoryMember;

public interface IGuiCallback {

	void sendInput(String input);
	void startCollect(DirectoryPath dirPath);
	// Methods for TreeModel...
	MutableTreeNode getDirTreeStructure(ViewSortEnum vs);
	void dirListToTreeStructure(DirectoryMember dm, MutableTreeNode constructTreeNode);
}
