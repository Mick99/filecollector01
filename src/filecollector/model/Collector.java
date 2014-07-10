package filecollector.model;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;
import filecollector.util.MyFileUtils;

public class Collector {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private final Path ROOT_DIRECTORY; // TODO DEL: Kann eigentlich wech. War vorher der Start-Pfad um root zu fuellen.
	private DirectoryMember root;
//	private DirectoryMember current;
	private DirectoryMember dirSortByDirFirst;
	
	private DirectoryMember test_sleep;
	private DirectoryMember test_futureGet;
	private DirectoryMember test_callable;

	// Es wird einen anderer Weg eingeschlagen
	@Deprecated
	public Collector (final Path rootDir) {
		if (!MyFileUtils.isDirectory (rootDir)) {
			exc.fatal ("No Directory.. exit now");
			System.exit (1);
		}
		ROOT_DIRECTORY = rootDir;
	}
	// Es wird einen anderer Weg eingeschlagen
	public DirectoryMember getCollectionView (CollectionViewSelectorEnum vs, Boolean createNewEmptyStruct) {
		switch (vs) {
		case ORIG_UNSORTED:
			if (createNewEmptyStruct) {
				root = new DirectoryMember (ROOT_DIRECTORY);
			}
			return root;
		case SORT_BY_DIR_FIRST:
			if (createNewEmptyStruct) {
				dirSortByDirFirst = new DirectoryMember (ROOT_DIRECTORY);
			}
			return dirSortByDirFirst;
		case TEST_SLEEP:
			test_sleep = new DirectoryMember (ROOT_DIRECTORY);
			return test_sleep;
		case TEST_FUTURE_GET:
			test_futureGet = new DirectoryMember (ROOT_DIRECTORY);
			return test_futureGet;
		case TEST_CALLABLE:
			test_callable = new DirectoryMember (ROOT_DIRECTORY);
			return test_callable;

		default:
			throw new NullPointerException ();
		}
	}
	public void printTest () {
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
			exc.fatal ("Geht gar nich!!!");
			System.exit (3);
		}

		for (FileSystemMember f : tmpPrint.getDirContent ()) {
			if (f.getClass () == FileMember.class) {
				FileMember m = (FileMember) f;
				msg.debug (m.getFileSize () + m.getFILE_NAME ());
			}
			if (f.getClass () == DirectoryMember.class) {
				DirectoryMember m = (DirectoryMember) f;
				msg.debug (m.getPath ().toString ());
				if (m.getPath ().endsWith ("FirstOrdner")) {
					DirectoryMember subDir = (DirectoryMember) f;
					for (FileSystemMember f1 : subDir.getDirContent ()) {
						FileMember m1 = (FileMember) f1;
						msg.debug (m1.getFileSize () + m1.getFILE_NAME ());
					}
				}
			}
		}
	}
}
