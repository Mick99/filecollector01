package filecollector.model;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import filecollector.model.ViewSortEnum;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class DirectoryTreeStructure {

	DirectoryTreeStructure() {
	}
	MutableTreeNode createRootOfTreeStructure(DirectoryMember dm) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dm);
		return root;
	}
	void dirListToTreeNode(List<DirectoryMember> dm, List<FileMember> fm, MutableTreeNode constructTreeNode, ViewSortEnum vs) {
		List<FileSystemMember> allMember = selectionToShow(dm, fm, vs);
		int insertPos = 0;
		for (FileSystemMember fsm : allMember) {
			constructTreeNode.insert(new DefaultMutableTreeNode(fsm), insertPos++);
		}
	}
	MutableTreeNode createEmptyTreeStructure() {
		FileMember empty = new FileMember(Paths.get("D:/--Empty--"));
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(empty);
		return rootNode;
	}
	private List<FileSystemMember> selectionToShow(List<DirectoryMember> dm, List<FileMember> fm, ViewSortEnum vs) {
		List<FileSystemMember> allMember = new ArrayList<>();
		if (vs == ViewSortEnum.FILE_FIRST || vs == ViewSortEnum.DIR_ONLY) {
			if (vs == ViewSortEnum.FILE_FIRST) {
				allMember.addAll(fm);
				allMember.addAll(dm);
			} else {
				allMember.addAll(dm);
			}
		} else {
			allMember.addAll(dm);
			allMember.addAll(fm);
		}
		return allMember;
	}
}
