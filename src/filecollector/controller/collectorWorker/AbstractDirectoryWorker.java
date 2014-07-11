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
import filecollector.util.MyFileUtils;

public abstract class AbstractDirectoryWorker {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	protected final DirectoryMember directory;
	private DirectoryStream<Path> dirStream;
	private String workerName;

	protected AbstractDirectoryWorker (final DirectoryMember directory) {
		if (checkIfValidDirectory (directory.getPath ()))
			this.directory = directory;
		else
			this.directory = null;
	}
	protected AbstractDirectoryWorker (final Path dir) {
		if (checkIfValidDirectory (dir))
			directory = new DirectoryMember (dir);
		else
			directory = null;
	}
	protected void doProcess () {
		Iterator<Path> it = null;
		if (openDirectoryStreamInstance ()) {
			it = dirStream.iterator ();
			while (it.hasNext ()) {
				processNextDirectoryEntry (it.next ());
			}
			closeDirectoryStreamInstance ();
		} else {
			// TODO MW_140708: How to handle it? throw, log or nothing
		}
	}
	private boolean checkIfValidDirectory (Path dir) {
		if (MyFileUtils.isDirectory (dir)) {
			return true;
		} else {
			exc.error (String.format ("Path parmeter is not a valid directory: '%s'\n", dir.toString ()));
			return false;
		}
	}
	private void processNextDirectoryEntry (Path dirEntry) {
		DosFileAttributes dosFileAttributes = null;
		try {
			dosFileAttributes = Files.readAttributes (dirEntry, DosFileAttributes.class);
		} catch (IOException e) {
			exc.warn ("Files.readAttributes", e);
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
	protected abstract void createNewDirectoryWorker (DirectoryMember dm);
	private boolean openDirectoryStreamInstance () {
		try {
			dirStream = Files.newDirectoryStream (directory.getPath ());
			int cw = WorkerCounter.createWorker ();
			int wi = WorkerCounter.getWorkerId ();
			workerName = "Worker [ " + wi + " ]";
			msg.trace ("Create worker count " + cw + " : " + workerName);
			return true;
		} catch (IOException e) {
			// TODO MW_140710: Take a closer look. log e, path (e.g. security violation...)
			exc.error (String.format ("Files.newDirectoryStream is not a valid directory: '%s'\n", directory.getPath ().toString ()));
			exc.error (e.getMessage (), e);
			return false;
		}
	}
	private void closeDirectoryStreamInstance () {
		try {
			if (dirStream != null)
				dirStream.close ();
		} catch (IOException e) {
			// ignore
		} finally {
			int rw = WorkerCounter.releaseWorker ();
			msg.debug ("Release " + rw + " for " + workerName);
		}
	}
}
