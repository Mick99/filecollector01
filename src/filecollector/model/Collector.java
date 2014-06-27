package filecollector.model;

import java.nio.file.Files;
import java.nio.file.Path;

import filecollector.controller.DirectoryWorker;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;


public class Collector {

	public final DirectoryMember root;
	public DirectoryMember current;
	public FileMember f; 
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
	}
	public void print () {
		System.out.println (root.getDirContent ().toString ());
	}
}
