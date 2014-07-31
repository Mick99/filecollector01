package filecollector.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember;

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
		
		self = this;
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
	public static Collector getCollector() {
		return self;
	}
}
