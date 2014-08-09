package filecollector.model.filemember;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

import org.apache.log4j.Logger;

/**
 * Only absolute paths are allowed. No relative paths like "./../Test"
 * 
 * @author Mick_02
 * 
 */
public abstract class FileSystemMember {
	// private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private final String ABSOLUTE_PATH_NAME;
	private Path path;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf(FileAttributesEnum.class);
	private FileTimes fileTimes = null;

	// Inner class only for FileTimes
	public class FileTimes {
		private FileTime creationTime = null;
		private FileTime lastAccessTime = null;
		private FileTime lastModifiedTime = null;

		public FileTimes(FileTime creationTime, FileTime lastAccessTime, FileTime lastModifiedTime) {
			this.creationTime = creationTime;
			this.lastAccessTime = lastAccessTime;
			this.lastModifiedTime = lastModifiedTime;
		}
		public FileTime getCreationTime() {
			return creationTime;
		}
		public FileTime getLastAccessTime() {
			return lastAccessTime;
		}
		public FileTime getLastModifiedTime() {
			return lastModifiedTime;
		}
	}

	protected FileSystemMember(final Path path) {
		// Inside "if else" through final not possible
		if (path.isAbsolute()) {
			this.path = path;
		} else {
			exc.warn(String.format("Only absolute paths are allowed %s:\n", path.toString()), new IllegalArgumentException());
		}
		this.ABSOLUTE_PATH_NAME = path.toString();
	}
	public String getABSOLUTE_PATH_NAME() {
		return ABSOLUTE_PATH_NAME;
	}
	public Path getPath() {
		return path;
	}
	protected void setPath(Path path) {
		this.path = path;
	}
	public EnumSet<FileAttributesEnum> getFileAttributes() {
		return fileAttributes;
	}
	public void setFileAttributes(EnumSet<FileAttributesEnum> fileAttributes) {
		this.fileAttributes = fileAttributes;
	}
	public FileTimes getFileTimes() {
		return fileTimes;
	}
	public void setFileTimes(FileTimes fileTimes) {
		this.fileTimes = fileTimes;
	}
	public String getFileName() {
		return path.getFileName().toString();
	}
	public abstract String toPrint();
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" : "+path.toString();
	}
}
