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
	private boolean isFinish = false;
	private DirectoryStream<Path> dirStream;
	
	public DirectoryWorker (DirectoryMember directory) {
		this.directory = directory;
	}
	
	@Override
	public void run () {
		constructDirectoryStreamInstance ();
		Iterator<Path> it = dirStream.iterator ();
		while (!isFinish) {
			while (it.hasNext ()) {
				processNextDirectoryEntry (it.next ());
			}
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

	}

	private void constructDirectoryStreamInstance () {
		try {
			dirStream = Files.newDirectoryStream (directory.getPath ());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
