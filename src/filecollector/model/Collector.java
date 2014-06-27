package filecollector.model;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;


import filecollector.controller.DirectoryWorker;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

public class Collector {

	public DirectoryMember root;
	public DirectoryMember current;
	public Collector (Path rootDir) {
		if (!Files.isDirectory (rootDir)) {
			System.out.println ("No Directory.. exit now");
			System.exit(1);
		}
		root = new DirectoryMember (rootDir);
	}
	public void startFirstWorkerThread () {
		DirectoryWorker firstWorker = new DirectoryWorker (root);
		Thread t = new Thread (firstWorker);
		t.start ();
		while (t.isAlive ()) {
			System.out.println ("ALIVE");
		}
	}
	public void print () {
		System.out.println (root.getDirContent ().toString ());
		for (FileSystemMember f : root.getDirContent ()) {
			if (f.getClass () == FileMember.class) {
				FileMember m = (FileMember) f;
				System.out.println (m.getFileSize () + m.getFileName ());
			}
		}
	}
}
