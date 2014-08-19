package filecollector.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private List<DirectoryMember> dirMem; // Wird wohl eher nicht mehr gebraucht
	private Map<ViewSortEnum, List<DirectoryMember>> mapOfDirMem;
	
	public Collector(DirectoryMember root) {
		this.dirOrigUnsorted = root;
		init();
	}
	public Collector(List<DirectoryMember> list) {
		init();
//		dirMem = list;
		deepCopy(list, ViewSortEnum.ORIG_UNSORTED);
//		sort(mapOfDirMem.get(ViewSortEnum.SORT_BY_FILE_FIRST));
	}
	private void init() {
		self = this;
		mapOfDirMem = new HashMap<>();
	}
	public static Collector getCollector() {
		return self;
	}
	public List<DirectoryMember> getView(ViewSortEnum vs) {
		if (mapOfDirMem.get(vs.ordinal()) == null) {
			deepCopy(mapOfDirMem.get(ViewSortEnum.ORIG_UNSORTED), vs);
		}
		return mapOfDirMem.get(vs.ordinal());
	}
	public DirectoryMember getDirMemView(ViewSortEnum vs) {
		return dirMem.get(vs.ordinal());
	}
	private void deepCopy(List<DirectoryMember> source, ViewSortEnum vs) {
		List<DirectoryMember> newList = new ArrayList<>(source.size());
		if (vs == ViewSortEnum.SORT_BY_DIR_FIRST || vs == ViewSortEnum.SORT_BY_FILE_FIRST)
			Collections.sort(source);
		for(DirectoryMember dm : source) {
			DirectoryMember newDm = new DirectoryMember(dm, vs);
			newList.add(newDm);
		}
		mapOfDirMem.put(vs, newList);
	}

	
	
	public void test() {
		PrintTest p = new PrintTest();
//		p.printList(mapOfDirMem.get(ViewSortEnum.ORIG_UNSORTED));
		p.printList(dirMem);
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
	

	
	
	// Copy-Ctor test
	private DirectoryMember testCopyOfOrig;
	
	public void testCopyCtor() {
		testCopyOfOrig = new DirectoryMember(dirOrigUnsorted, ViewSortEnum.ORIG_UNSORTED);
		for (FileSystemMember fsm : testCopyOfOrig.getDirContent()) {
			fsm.setPath_FORTEST();
		}
		testCompareBUTNotEquals(dirOrigUnsorted, testCopyOfOrig);
	}
	private void testCompareBUTNotEquals(DirectoryMember orig, DirectoryMember copy) {
		Iterator<FileSystemMember> origIt = orig.getDirContent().iterator();
		Iterator<FileSystemMember> copyIt = copy.getDirContent().iterator();
		while (origIt.hasNext()) {
			FileSystemMember fileOrig = (FileSystemMember) origIt.next();
			if (copyIt.hasNext()) {
				FileSystemMember fileCopy = (FileSystemMember) copyIt.next();
				equCompareHash(fileOrig, fileCopy);
				if ((fileOrig.getClass() == DirectoryMember.class) && (fileCopy.getClass() == DirectoryMember.class)) {
					DirectoryMember newOrig = (DirectoryMember) fileOrig;
					DirectoryMember newCopy = (DirectoryMember) fileCopy;
					testCompareBUTNotEquals(newOrig, newCopy);
				}
			} else {
				System.out.println("copy Next?");
			}
		}
	}
	private void equCompareHash(FileSystemMember orig, FileSystemMember copy) {
		if (orig == copy) System.out.println("NOOO");
		if (orig.hashCode() != copy.hashCode()) {
			System.out.format("hash?");
		}
		if (!orig.equals(copy)) {
			System.out.format("equal?");
		}
		int comp = orig.compareTo(copy); 
		if (comp != 0) {
//			System.out.format("compareTo?");
		}
		System.out.format("compTo = %d", comp);
		System.out.format("%norig hash: %d  %s%n", orig.hashCode(), orig.getFileName());
		System.out.format("copy hash: %d  %s%n", copy.hashCode(), copy.getFileName());
		
		System.out.format("compTo Times=%d%n",orig.getFileTimes().compareTo(copy.getFileTimes()));
//		System.out.println("equCompareHash");
	}
	// Copy-Ctor test

	// TODO MW_140810: !!! To DELETE, nur wg. Recursion erst mal noch lassen !!!
	public MutableTreeNode newRootNodeStructure() {
		// Wurzelknoten erstellen
		MutableTreeNode rootNode = new DefaultMutableTreeNode(dirOrigUnsorted.getFileName());
		recursiveDirectoryLevel(dirOrigUnsorted, rootNode, 2);

		return rootNode;
	}
	private void recursiveDirectoryLevel(final DirectoryMember dm, MutableTreeNode resultTreeNode, int level) {
		if (level > 0) {
			Iterator<FileSystemMember> iFsm = dm.getDirContent().iterator();
			while (iFsm.hasNext()) {
				FileSystemMember fileSystemMember = (FileSystemMember) iFsm.next();
				// DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(fileSystemMember.getFileName());
				// resultTreeNode.insert(tmp, j++);
				if (fileSystemMember.getClass() == DirectoryMember.class) {
					DirectoryMember newDm = (DirectoryMember) fileSystemMember;
					recursiveDirectoryLevel(newDm, resultTreeNode, --level);
				}
			}
		}
	}
	// MW_140810: !!! To DELETE, nur wg. Recursion erst mal noch lassen !!!
}
