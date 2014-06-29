package filecollector.controller;



import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Iterator;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;

public class DirectoryWorker implements Runnable {
	
	private final DirectoryMember directory;
	private DirectoryStream<Path> dirStream;
	private String workerName;
	private boolean isFinish = false;
	private boolean isDirStreamOpen = false;
	
	public DirectoryWorker (DirectoryMember directory) {
		this.directory = directory;
	}
	@Override
	public void run () {
		openDirectoryStreamInstance ();
		Iterator<Path> it = null;
		if (isDirStreamOpen)
			it = dirStream.iterator ();
		while (!isFinish) {
			while (isDirStreamOpen) {
				if (it.hasNext ()) {
					processNextDirectoryEntry (it.next ());
				} else {
					closeDirectoryStreamInstance ();
				}
			}
			try {
				Thread.sleep (8);
				allWorkerFinish ();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void allWorkerFinish () {
		if (WorkerCounter.allWorkerFinish ()) {
			System.out.println ("Finish " + workerName);
			isFinish = true;
		} else {
			System.out.println ("Wait " + workerName);
		}
		
	}
	private void processNextDirectoryEntry (Path dirEntry) {
		DosFileAttributes dosFileAttributes = null;
		try {
			dosFileAttributes = Files.readAttributes (dirEntry, DosFileAttributes.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if (Files.isRegularFile (dirEntry)) {
			addFileMember (dirEntry, dosFileAttributes);
			return;
		}
		if (Files.isDirectory (dirEntry)) {
			addDirectoryMember (dirEntry, dosFileAttributes);
			return;
		}
	}
	private void addFileMember (final Path dirEntry, final DosFileAttributes dosFileAttr) {
		FileMember fm = new FileMember (dirEntry);
		fm.setFileSize (dosFileAttr.size ());
		directory.addFileSystemMember (fm);
	}
	private void addDirectoryMember (final Path dirEntry, final DosFileAttributes dosFileAttr) {
		DirectoryMember dm = new DirectoryMember (dirEntry);
		directory.addFileSystemMember (dm);
		createNewDirectoryWorker (dm);
	}
	private void createNewDirectoryWorker (DirectoryMember dm) {
		DirectoryWorker newDirectoryWorkerThread = new DirectoryWorker (dm);
		Thread t1 = new Thread (newDirectoryWorkerThread);
		t1.start ();
		
	}

	private void openDirectoryStreamInstance () {
		try {
			dirStream = Files.newDirectoryStream (directory.getPath ());
			isDirStreamOpen = true;
			int tmp = WorkerCounter.createWorker ();
			workerName = "Worker [ " + WorkerCounter.getWorkerId () + " ]";
			System.out.println ("Create worker count " + tmp + " : " + workerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isFinish = true;
		}
	}
	private void closeDirectoryStreamInstance () {
		try {
			if (dirStream != null)
				dirStream.close ();
			isDirStreamOpen = false;
			int tmp = WorkerCounter.releaseWorker ();
			System.out.println ("Release " + tmp + " for " + workerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
