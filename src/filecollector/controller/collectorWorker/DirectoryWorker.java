package filecollector.controller.collectorWorker;



import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Iterator;

import org.apache.log4j.Logger;

import filecollector.controller.ExecutorSingleton;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;

public class DirectoryWorker {
	Logger log = Logger.getLogger ("MW_Level"); // DirectoryWorker.class.getSimpleName ()
	
	protected final DirectoryMember directory;
	protected DirectoryStream<Path> dirStream;
	private String workerName;
	protected boolean isDirStreamOpen = false;
	
	public DirectoryWorker (DirectoryMember directory) {
		this.directory = directory;
	}
	protected void processNextDirectoryEntry (Path dirEntry) {
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
		ExecutorSingleton.getInstance ().executeWorker (newDirectoryWorkerThread);		
	}
	protected void openDirectoryStreamInstance () {
		try {
			dirStream = Files.newDirectoryStream (directory.getPath ());
			isDirStreamOpen = true;
			int tmp = WorkerCounter.createWorker ();
			workerName = "Worker [ " + WorkerCounter.getWorkerId () + " ]";
			log.info ("Create worker count " + tmp + " : " + workerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void closeDirectoryStreamInstance () {
		try {
			if (dirStream != null)
				dirStream.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			isDirStreamOpen = false;
			int tmp = WorkerCounter.releaseWorker ();
			log.warn ("Release " + tmp + " for " + workerName);
		}
	}
}
