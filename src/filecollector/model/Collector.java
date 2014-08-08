package filecollector.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {
	// private static final Logger msg = Logger.getLogger("Message");
	// private static final Logger exc = Logger.getLogger("Exception");

	private static Collector self;
	private DirectoryMember dirOrigUnsorted;
	private DirectoryMember dirSortByDirFirst;
	private DirectoryMember dirSortByFileFirst;

	public Collector(DirectoryMember root) {
		this.dirOrigUnsorted = root;
		self = this;
	}
	// Copy from List to DirecdtoryMember structure
	public Collector(List<DirectoryMember> list) {
		dirOrigUnsorted = makeListToDirectoryMemberStructure(list);
		self = this;
	}
	public static Collector getCollector() {
		return self;
	}
	public DirectoryMember getCollectionView(CollectionViewSelectorEnum vs) {
		switch (vs) {
		case ORIG_UNSORTED:
			return dirOrigUnsorted;
		case SORT_BY_DIR_FIRST:
			return dirSortByDirFirst;
		case SORT_BY_FILE_FIRST:
			return dirSortByFileFirst;
		default:
			throw new NullPointerException();
		}
	}
	public MutableTreeNode newRootNodeStructure() {
        // Wurzelknoten erstellen 
        MutableTreeNode rootNode = new DefaultMutableTreeNode(dirOrigUnsorted.getFileName());
        recursiveDirectoryLevel(dirOrigUnsorted, rootNode, 2);
		
		return rootNode;
	}
	private void recursiveDirectoryLevel(final DirectoryMember dm, MutableTreeNode resultTreeNode, int level) {
		int j=0;
		if (level > 0) {
			Iterator<FileSystemMember> iFsm = dm.getDirContent().iterator();
			while (iFsm.hasNext()) {
				FileSystemMember fileSystemMember = (FileSystemMember) iFsm.next();
				DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(fileSystemMember.getFileName());
				resultTreeNode.insert(tmp, j++);
				if (fileSystemMember.getClass() == DirectoryMember.class) {
					DirectoryMember newDm = (DirectoryMember) fileSystemMember;
					recursiveDirectoryLevel(newDm, resultTreeNode, --level);
				}
			}
		}
	}
	// TODO MW_140802: Eher mist, sollte leichter gehen??? Dazu DirMem.., FileMem.. equals,... have to impl
	private DirectoryMember makeListToDirectoryMemberStructure(List<DirectoryMember> list) {
		MyDirectoryMember_Comparator comp = new MyDirectoryMember_Comparator();
		Collections.sort(list, comp);
		return makeDirectoryMemberStructure(list);
	}
	private DirectoryMember makeDirectoryMemberStructure(List<DirectoryMember> sortedList) {
		DirectoryMember first = sortedList.get(0);
		Iterator<DirectoryMember> it = sortedList.listIterator(1);
		while (it.hasNext()) {
			DirectoryMember dm = (DirectoryMember) it.next();
			addToDirMember(dm.getPath().getParent(), dm, sortedList);
		}
		return first;
	}
	private void addToDirMember(Path parentPath, DirectoryMember addToParent, List<DirectoryMember> sortedList) {
		for (DirectoryMember d : sortedList) {
			if (d.getPath().equals(parentPath)) {
				d.addFileSystemMember(addToParent);
			}
		}
	}
}
