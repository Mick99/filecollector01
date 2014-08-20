package filecollector.model;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import filecollector.model.ViewSortEnum;
import filecollector.model.Collector;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class DirectoryTreeStructure {

	public DirectoryTreeStructure() {
		// TODO Auto-generated constructor stub
	}
	MutableTreeNode createRootOfTreeStructure(DirectoryMember dm) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dm);
		return root;
	}
	void dirListToTreeNode(List<DirectoryMember> dm, List<FileMember> fm, MutableTreeNode constructTreeNode, ViewSortEnum vs) {
		List<FileSystemMember> allMember = new ArrayList<>();
		if (vs == ViewSortEnum.SORT_BY_FILE_FIRST) {
			allMember.addAll(fm);
			allMember.addAll(dm);
		} else {
			allMember.addAll(dm);
			allMember.addAll(fm);
		}
		int insertPos = 0;
		for (FileSystemMember fsm : allMember) {
			constructTreeNode.insert(new DefaultMutableTreeNode(fsm), insertPos++);
		}
	}
	MutableTreeNode createEmptyTreeStructure() {
		// Wurzelknoten erstellen
		FileMember empty = new FileMember(Paths.get("D:/--Empty--"));
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(empty);

		return rootNode;
	}

	public void dirListToTreeNode_OLD(DirectoryMember dm, MutableTreeNode constructTreeNode) {
		ListIterator<FileSystemMember> it = dm.getDirContent().listIterator();
		int nextInsertNode = 0;
		while (it.hasNext()) {
			FileSystemMember fileSystemMember = (FileSystemMember) it.next();
			if (fileSystemMember.getClass() == DirectoryMember.class) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) constructTreeNode.getChildAt(nextInsertNode);
				createOneLevelTreeNode((DirectoryMember) fileSystemMember, node);
			}
			nextInsertNode++;
		}
	}
	public MutableTreeNode startTreeStructure() {
		// Once create first "Root-Node"...
		DirectoryMember dirMem = Collector.getCollector().getDirMemView(ViewSortEnum.ORIG);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dirMem);
		createOneLevelTreeNode(dirMem, root);
		// After that create second level nodes and finish, because all collapsed
		dirListToTreeNode_OLD(dirMem, root);
		return root;
	}
	private void createOneLevelTreeNode(DirectoryMember dm, MutableTreeNode constructTreeNode) {
		int insertPos = 0;
		for (FileSystemMember fsm : dm.getDirContent()) {
			constructTreeNode.insert(new DefaultMutableTreeNode(fsm), insertPos++);
		}
	}

}
