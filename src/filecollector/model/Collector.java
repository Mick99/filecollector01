package filecollector.model;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;


import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {

	Logger log = Logger.getLogger ("MW_Level"); // Collector.class.getSimpleName ()

	private DirectoryMember root;
	private DirectoryMember current;
	
	public Collector (Path rootDir) {
		if (!Files.isDirectory (rootDir)) {
			log.error ("No Directory.. exit now");
			System.exit(1);
		}
		root = new DirectoryMember (rootDir);
	}
	public DirectoryMember getCollectionView (CollectionViewSelectorEnum vs) {
		DirectoryMember tmp = null;
		// At the present time only root 
		switch (vs) {
		case ORIG_UNSORTED:
			tmp = root;
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
		log.debug (root.getDirContent ().toString ());
		for (FileSystemMember f : root.getDirContent ()) {
			if (f.getClass () == FileMember.class) {
				FileMember m = (FileMember) f;
				log.warn (m.getFileSize () + m.getFileName ());
			}
			if (f.getClass () == DirectoryMember.class) {
				DirectoryMember m = (DirectoryMember) f;
				log.warn (m.getPath ().toString ());
			}
		}
	}
}
