package filecollector.model;

import java.nio.file.Files;
import java.nio.file.Path;


import filecollector.controller.DirectoryWorker;
import filecollector.controller.WorkerCounter;
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
		while (!WorkerCounter.allWorkerFinish ()) {
			try {
				Thread.sleep (4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			if (f.getClass () == DirectoryMember.class) {
				DirectoryMember m = (DirectoryMember) f;
				System.out.println (m.getPath ().toString ());
			}
		}
	}
}
