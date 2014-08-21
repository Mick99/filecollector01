package filecollector.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {
	// private static final Logger msg = Logger.getLogger("Message");
	// private static final Logger exc = Logger.getLogger("Exception");

	private static Collector self; // Don't use
	private DirectoryMember dirOrigUnsorted;
	private List<DirectoryMember> dirMem; // Wird wohl eher nicht mehr gebraucht
	private EnumMap<ViewSortEnum, List<DirectoryMember>> mapOfDirMem; // but this is more correct
//	private EnumMap<ViewSortEnum, DefaultMutableTreeNode> mapOfTreeNode; // fuer spaeter
	private ViewSortEnum currentView = ViewSortEnum.NONE;
	private DirectoryTreeStructure tree = new DirectoryTreeStructure();

	// Must be convert to List
	public Collector(DirectoryMember root) {
		this.dirOrigUnsorted = root;
		init();
	}
	public Collector(List<DirectoryMember> list) {
		init();
		Collections.sort(list);
		deepCopy(list, ViewSortEnum.ORIG);
	}
	private void init() {
		self = this;
		mapOfDirMem = new EnumMap<>(ViewSortEnum.class);
	}
	@Deprecated
	public static Collector getCollector() {
		return self;
	}
	public List<DirectoryMember> getView(ViewSortEnum vs) {
		if (!mapOfDirMem.containsKey(vs)) {
			deepCopy(mapOfDirMem.get(ViewSortEnum.ORIG), vs);
		}
		return mapOfDirMem.get(vs);
	}
	@Deprecated
	public DirectoryMember getDirMemView(ViewSortEnum vs) {
		return dirMem.get(vs.ordinal());
	}
	private void deepCopy(List<DirectoryMember> source, ViewSortEnum vs) {
		List<DirectoryMember> newList = new ArrayList<>(source.size());
		for (DirectoryMember dm : source) {
			DirectoryMember newDm = new DirectoryMember(dm, vs);
			newList.add(newDm);
		}
		mapOfDirMem.put(vs, newList);
	}
	public MutableTreeNode getDirTreeStructure(ViewSortEnum vs) {
		if (vs == ViewSortEnum.NONE)
			return tree.createEmptyTreeStructure();
		List<DirectoryMember> tmp = getView(vs);
		// Create one root-element ...
		DirectoryMember rootDirMem = tmp.get(0);
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) tree.createRootOfTreeStructure(rootDirMem);
		// ... create root level ...
		List<DirectoryMember> dirPart = getSubDirList(mapOfDirMem.get(vs), rootDirMem);
		List<FileMember> filePart = getFileMemberList(rootDirMem);
		tree.dirListToTreeNode(dirPart, filePart, dmt, vs);
		// ... create level after root level and finish
		// DefaultMutableTreeNode tmpDmt = findTreeNode(dmt, dirPart.get(0));
		for (DirectoryMember dm : dirPart) {
			DefaultMutableTreeNode tmpDmt = findTreeNode(dmt, dm);
			dirPart = getSubDirList(mapOfDirMem.get(vs), dm);
			filePart = getFileMemberList(dm);
			tree.dirListToTreeNode(dirPart, filePart, tmpDmt, vs);
		}
		currentView = vs;
		return dmt;
	}
	private DefaultMutableTreeNode findTreeNode(DefaultMutableTreeNode rootNode, DirectoryMember dm) {
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			TreeNode tn = rootNode.getChildAt(i);
			DefaultMutableTreeNode tmp = (DefaultMutableTreeNode) tn;
			if (dm.equals(tmp.getUserObject()))
				return tmp;
		}
		return null;
	}
	// Later do it a little bit better than that opaque code??
	private List<DirectoryMember> getSubDirList(List<DirectoryMember> source, DirectoryMember parent) {
		List<DirectoryMember> part = new ArrayList<>();
		DirectoryMember nextMember;
		Iterator<DirectoryMember> it = source.listIterator();
		boolean isAdd = false;
		// Find first parent path and ...
		while (it.hasNext() && !isAdd) {
			nextMember = it.next();
			isAdd = nextMember.getPath().getParent().equals(parent.getPath());
			if (isAdd)
				part.add(nextMember);
		}
		// ... find until not parent path ...
		do {
			if (!it.hasNext())
				break;
			nextMember = it.next();
			isAdd = nextMember.getPath().getParent().equals(parent.getPath());
			if (isAdd)
				part.add(nextMember);
		} while (isAdd);
		return part;
	}
	private List<FileMember> getFileMemberList(DirectoryMember parent) {
		List<FileMember> part = new ArrayList<>();
		for (FileSystemMember fs : parent.getDirContent())
			part.add((FileMember) fs);
		return part;
	}
	public void dirListToTreeStructure(DirectoryMember dirMem, MutableTreeNode constructTreeNode) {
		List<DirectoryMember> dirPart = getSubDirList(mapOfDirMem.get(currentView), dirMem);
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) constructTreeNode;
		for (DirectoryMember dm : dirPart) {
			DefaultMutableTreeNode tmpDmt = findTreeNode(dmt, dm);
			dirPart = getSubDirList(mapOfDirMem.get(currentView), dm);
			List<FileMember> filePart = getFileMemberList(dm);
			tree.dirListToTreeNode(dirPart, filePart, tmpDmt, currentView);
		}
	}

	
	public void test() {
		PrintTest p = new PrintTest();
		List<DirectoryMember> l = getView(ViewSortEnum.ORIG);
		p.printList(l);
		// p.printList(dirMem);
	}
	public void test1(String viewSort) {
		ViewSortEnum vs = ViewSortEnum.TEMP_WORK_BEFORE_SORT;
		for (ViewSortEnum v : ViewSortEnum.values()) {
			if (viewSort.equals(v.name())) {
				vs = v;
				break;
			}
		}
		System.out.println(vs.printDescription());
	}
}
