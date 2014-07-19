package filecollector.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	Path ROOT_DIRECTORY = Paths.get("D:/dummyDirDontUseIt");
	private DirectoryMember root;
	// private DirectoryMember current;
	private DirectoryMember dirSortByDirFirst;

	private DirectoryMember test_sleep;
	private DirectoryMember test_futureGet;
	private DirectoryMember test_callable;

	// Es wird einen anderer Weg eingeschlagen
	@Deprecated
	public Collector(final Path rootDir) {
	}
	// Es wird einen anderer Weg eingeschlagen
	public DirectoryMember getCollectionView_OLD(CollectionViewSelectorEnum vs, Boolean createNewEmptyStruct) {
		switch (vs) {
		case ORIG_UNSORTED:
			if (createNewEmptyStruct) {
				root = new DirectoryMember(ROOT_DIRECTORY);
			}
			return root;
		case SORT_BY_DIR_FIRST:
			if (createNewEmptyStruct) {
				dirSortByDirFirst = new DirectoryMember(ROOT_DIRECTORY);
			}
			return dirSortByDirFirst;
		case TEST_SLEEP:
			test_sleep = new DirectoryMember(ROOT_DIRECTORY);
			return test_sleep;
		case TEST_FUTURE_GET:
			test_futureGet = new DirectoryMember(ROOT_DIRECTORY);
			return test_futureGet;
		case TEST_CALLABLE:
			test_callable = new DirectoryMember(ROOT_DIRECTORY);
			return test_callable;

		default:
			throw new NullPointerException();
		}
	}
	public void printTest() {
		DirectoryMember tmpPrint = null;
		if (root != null) {
			tmpPrint = root;
		} else if (test_sleep != null) {
			tmpPrint = test_sleep;
		} else if (test_futureGet != null) {
			tmpPrint = test_futureGet;
		} else {
			tmpPrint = test_callable;
		}

		if (tmpPrint == null) {
			exc.fatal("Geht gar nich!!!");
			System.exit(3);
		}
		recursivePrint(tmpPrint, 0);
	}
	private void recursivePrint(DirectoryMember dm,int indent) {
		int localIndent = indent;
		Iterator<FileSystemMember> iFsm = dm.getDirContent().iterator();
		while (iFsm.hasNext()) {
			FileSystemMember fileSystemMember = (FileSystemMember) iFsm.next();
			StringBuilder sb = new StringBuilder(fileSystemMember.toPrint());
			for (int i=0; i<localIndent; i++) sb.insert(0, ' ');
			msg.debug(sb.toString());
			if (fileSystemMember.getClass() == DirectoryMember.class) {
				DirectoryMember newDm = (DirectoryMember) fileSystemMember;
				recursivePrint(newDm, ++indent);
			}
		}
	}
}
