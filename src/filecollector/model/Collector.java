package filecollector.model;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;


import filecollector.controller.DirectoryWorker;
import filecollector.controller.WorkerCounter;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {

	static Logger log = Logger.getLogger ("MW_Log4j");

	public DirectoryMember root;
	public DirectoryMember current;
	
	public Collector (Path rootDir) {
		if (!Files.isDirectory (rootDir)) {
			log.error ("No Directory.. exit now");
			System.exit(1);
		}
		root = new DirectoryMember (rootDir);
	}
	public void startFirstWorkerThread () {
		DirectoryWorker firstWorker = new DirectoryWorker (root);
		Thread t = new Thread (firstWorker);
		t.start ();
		do {
			try {
				Thread.sleep (10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info ("ALIVE");
		} while (!WorkerCounter.allWorkerFinish ());
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
