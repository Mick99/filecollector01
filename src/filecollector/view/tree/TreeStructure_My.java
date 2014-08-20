package filecollector.view.tree;

import java.nio.file.Paths;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import filecollector.model.ViewSortEnum;
import filecollector.model.Collector;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public final class TreeStructure_My {

	private TreeStructure_My() {
	}
	public static MutableTreeNode startTreeStructure_TEST() {
		// Once create first "Root-Node"...
		DirectoryMember dirMem = Collector.getCollector().getDirMemView(ViewSortEnum.ORIG);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dirMem);
		createOneLevelTreeNode(dirMem, root);
		// After that create second level nodes and finish, because all collapsed
		dirListToTreeNode(dirMem, root);
		return root;
	}
	public static void dirListToTreeNode(DirectoryMember dm, MutableTreeNode constructTreeNode) {
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
	private static void createOneLevelTreeNode(DirectoryMember dm, MutableTreeNode constructTreeNode) {
		int insertPos = 0;
		for (FileSystemMember fsm : dm.getDirContent()) {
			constructTreeNode.insert(new DefaultMutableTreeNode(fsm), insertPos++);
		}
	}
	public static MutableTreeNode createEmptyTreeStructure() {
		// Wurzelknoten erstellen
		FileMember empty = new FileMember(Paths.get("D:/--Empty--"));
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(empty);

		return rootNode;
	}
	public static MutableTreeNode createDemoTree() {
		// Wurzelknoten erstellen
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");

		// Knoten für erste Ebene erstellen
		final MutableTreeNode firstChild = new DefaultMutableTreeNode("First");
		final MutableTreeNode secondChild = new DefaultMutableTreeNode("Second");
		final MutableTreeNode thirdChild = new DefaultMutableTreeNode("Third");

		// Erste Ebene mit Wurzelknoten verbinden -- DefaultMutableTreeNode.add()
		rootNode.add(firstChild);
		rootNode.add(secondChild);
		rootNode.add(thirdChild);

		// Zweite Ebene erstellen und einfügen -- MutableTreeNode.insert()
		secondChild.insert(new DefaultMutableTreeNode("2.1"), 0);
		secondChild.insert(new DefaultMutableTreeNode("2.2"), 1);

		return rootNode;
	}
}
