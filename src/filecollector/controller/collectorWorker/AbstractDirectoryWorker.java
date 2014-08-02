package filecollector.controller.collectorWorker;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.IPoolIdentifier;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileAttributesEnum;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;
import filecollector.util.MyFileUtils;

public abstract class AbstractDirectoryWorker implements IPoolIdentifier {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	protected final DirectoryMember directory;
	private DirectoryStream<Path> dirStream;
	private String workerName;
	protected PoolIdentifier poolIdentifier;

	// Runnable constructor
	protected AbstractDirectoryWorker(final DirectoryMember directory) {
		if (checkIfValidDirectory(directory.getPath()))
			this.directory = directory;
		else
			this.directory = null;
	}
	// Callable constructor
	protected AbstractDirectoryWorker(final Path dir) {
		if (checkIfValidDirectory(dir)) {
			directory = new DirectoryMember(dir);
			appendAttributes(directory);
		} else {
			directory = null;
		}
	}
	protected void doProcess() {
		Iterator<Path> it = null;
		if (openDirectoryStreamInstance()) {
			it = dirStream.iterator();
			while (it.hasNext()) {
				processNextDirectoryEntry(it.next());
			}
			closeDirectoryStreamInstance();
		} else {
			// TODO MW_140708: How to handle it? throw, log or nothing
		}
	}
	private boolean checkIfValidDirectory(Path dir) {
		if (MyFileUtils.isDirectory(dir)) {
			return true;
		} else {
			exc.error(String.format("Path parmeter is not a valid directory: '%s'\n", dir.toString()));
			return false;
		}
	}
	private void processNextDirectoryEntry(Path dirEntry) {
		if (Files.isRegularFile(dirEntry)) {
			addFileMember(dirEntry);
			return;
		}
		if (Files.isDirectory(dirEntry)) {
			addDirectoryMemberAndCreateNewWorker(dirEntry);
			return;
		}
	}
	private void addFileMember(final Path dirEntry) {
		FileMember fm = new FileMember(dirEntry);
		appendAttributes(fm);
		directory.addFileSystemMember(fm);
	}
	protected abstract void addDirectoryMemberAndCreateNewWorker(final Path dirEntry);

	private void appendAttributes(FileMember fm) {
		DosFileAttributes dosFileAttr = getFileAttributes(fm.getPath());
		if (dosFileAttr != null) {
			fm.setFileSize(dosFileAttr.size());
			appendFileTimesAndFlags(fm, dosFileAttr);
		}
	}
	protected void appendAttributes(DirectoryMember dm) {
		DosFileAttributes dosFileAttr = getFileAttributes(dm.getPath());
		if (dosFileAttr != null) {
			appendFileTimesAndFlags(dm, dosFileAttr);
		}
	}
	private DosFileAttributes getFileAttributes(Path pathToReadAttr) {
		try {
			return Files.readAttributes(pathToReadAttr, DosFileAttributes.class);
		} catch (IOException e) {
			exc.warn("Files.readAttributes", e);
			return null;
		}
	}
	private void appendFileTimesAndFlags(FileSystemMember fsm, DosFileAttributes dfa) {
		fsm.new FileTimes(dfa.creationTime(), dfa.lastAccessTime(), dfa.lastModifiedTime());
		appendFileFlags(fsm, dfa);
	}
	private void appendFileFlags(FileSystemMember fsm, DosFileAttributes dfa) {
		Set<FileAttributesEnum> tmp = new HashSet<>();
		if (dfa.isReadOnly())
			tmp.add(FileAttributesEnum.READONLY_DOSATTR);
		if (dfa.isHidden())
			tmp.add(FileAttributesEnum.HIDDEN_DOSATTR);
		if (dfa.isSystem())
			tmp.add(FileAttributesEnum.SYSTEM_DOSATTR);
		if (dfa.isArchive())
			tmp.add(FileAttributesEnum.ARCHIVE_DOSATTR);
		if (!tmp.isEmpty())
			fsm.setFileAttributes(EnumSet.copyOf(tmp));
	}
	private boolean openDirectoryStreamInstance() {
		try {
			dirStream = Files.newDirectoryStream(directory.getPath());
			int cw = WorkerCounter.createWorker();
			int wi = WorkerCounter.getWorkerId();
			workerName = "Worker [ " + wi + " ]";
			msg.trace("Create worker count " + cw + " : " + workerName);
			return true;
		} catch (IOException e) {
			// TODO MW_140710: Take a closer look. log e, path (e.g. security violation...)
			exc.error(String.format("Files.newDirectoryStream is not a valid directory: '%s'\n", directory.getPath().toString()));
			exc.error(e.getMessage(), e);
			return false;
		}
	}
	private void closeDirectoryStreamInstance() {
		try {
			if (dirStream != null)
				dirStream.close();
		} catch (IOException e) {
			// ignore
		} finally {
			int rw = WorkerCounter.releaseWorker();
			msg.debug("Release " + rw + " for " + workerName);
		}
	}
	@Override
	public void transferNewIdentifier(PoolIdentifier poolId) {
		poolIdentifier = poolId.newIdentifier();
	}
}
