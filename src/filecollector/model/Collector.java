package filecollector.model;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;


import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {

	Logger log = Logger.getLogger ("MW_Level"); // Collector.class.getSimpleName ()

	private final Path rootDirectory;
	private DirectoryMember root;
	private DirectoryMember current;
	private DirectoryMember test_sleep;
	private DirectoryMember test_futureGet;
	private DirectoryMember test_callable;
	
	public Collector (final Path rootDir) {
		if (!Files.isDirectory (rootDir)) {
			log.error ("No Directory.. exit now");
			System.exit(1);
		}
		rootDirectory = rootDir;
	}
	public DirectoryMember getCollectionView (CollectionViewSelectorEnum vs) {
		DirectoryMember tmp = null;
		switch (vs) {
		case ORIG_UNSORTED:
			root = new DirectoryMember (rootDirectory);
			tmp = root;
			break;
		case TEST_SLEEP:
			test_sleep = new DirectoryMember (rootDirectory);
			tmp = test_sleep;
			break;
		case TEST_FUTURE_GET:
			test_futureGet = new DirectoryMember (rootDirectory);
			tmp = test_futureGet;
			break;
		case TEST_CALLABLE:
			test_callable = new DirectoryMember (rootDirectory);
			tmp = test_callable;
			break;

		default:
			break;
		}
		if (tmp == null) {
			log.error ("NULL is nich wirklich gut???");
			throw new NullPointerException ();
		}
		return tmp;
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
			log.fatal ("Geht gar nich!!!");
			System.exit (3);
		}
		
		log.debug (tmpPrint.getDirContent ().toString ());
		for (FileSystemMember f : tmpPrint.getDirContent ()) {
			if (f.getClass () == FileMember.class) {
				FileMember m = (FileMember) f;
				log.warn (m.getFileSize () + m.getFileName ());
			}
			if (f.getClass () == DirectoryMember.class) {
				DirectoryMember m = (DirectoryMember) f;
				log.warn (m.getPath ().toString ());
				if (m.getPath ().endsWith ("FirstOrdner")) {
					DirectoryMember subDir = (DirectoryMember) f;
					for (FileSystemMember f1 : subDir.getDirContent ()) {
						FileMember m1 = (FileMember) f1;
						log.warn (m1.getFileSize () + m1.getFileName ());
					}
				}
			}
		}
	}
}
