package filecollector.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;

public class Collector {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	Path ROOT_DIRECTORY = Paths.get("D:/dummyDirDontUseIt");
	private static Collector self;
	private DirectoryMember dirOrigUnsorted;
	// private DirectoryMember current;
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
	// Eher mist, sollte leichter gehen??? Dazu DirMem.. equals, compareTo.. impl
	private DirectoryMember makeListToDirectoryMemberStructure(List<DirectoryMember> list) {
//		List<DirectoryMember> sortedList = Collections.sort(list); // Hier muß DirectoryMember Comperable sein
		// Sortier erstmal selbst
//		List<DirectoryMember> sortedList = mySort(list);
//		System.out.println("Vor Sort");
		PrintTest p = new PrintTest();
//		p.printList(list);
		MyDirectoryMember_Comparator comp = new MyDirectoryMember_Comparator();
		Collections.sort(list, comp);
		System.out.println("Nach Sort");
//		p.printList(list);
		p.printTest(makeDirectoryMemberStructure(list));
	
		return null;
		
	}
	private DirectoryMember makeDirectoryMemberStructure(List<DirectoryMember> sortedList) {
		DirectoryMember first = sortedList.get(0);
		Iterator<DirectoryMember> it = sortedList.listIterator(1);
//		int curLevel = first.getPath().getNameCount();
//		DirectoryMember curDir = first;
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
