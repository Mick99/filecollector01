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
public abstract class FileSystemMember implements Comparable<FileSystemMember> {
	// private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private final String ABSOLUTE_PATH_NAME;
	private Path path;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf(FileAttributesEnum.class);
	private FileTimes fileTimes = null;

	// Inner class only for FileTimes
	public class FileTimes implements Comparable<FileTimes>{
		private FileTime[] creAccModTime = new FileTime[3];

		public FileTimes(FileTime creationTime, FileTime lastAccessTime, FileTime lastModifiedTime) {
			creAccModTime[0] = creationTime;
			creAccModTime[1] = lastAccessTime;
			creAccModTime[2] = lastModifiedTime;
		}
		public FileTime getCreationTime() {
			return creAccModTime[0];
		}
		public FileTime getLastAccessTime() {
			return creAccModTime[1];
		}
		public FileTime getLastModifiedTime() {
			return creAccModTime[2];
		}
		@Override
		public int hashCode() {
			final int prime = 37;
			int res = 1;
			res = prime * res + getOuterType().hashCode();
			for (FileTime t : creAccModTime) {
				res = prime * res + ((t == null) ? 0 : t.hashCode());
			}
			return res;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			if (getClass() != obj.getClass())
				return false;
			FileTimes other = (FileTimes) obj;
			return compareTo(other) == 0;
		}
		// Compare only lastModifiedTime, other must impl separate compare methods
		@Override
		public int compareTo(FileTimes other) {
			// javadocs for Comparable: Note that null is not an instance of any class, and e.compareTo(null) should throw a NullPointerException even though e.equals(null) returns false.
			if (other == null) {
				String s = "FileTime.compareTo('null'): Null is not an instance of any class and not compareable!";
				NullPointerException e = new NullPointerException(s);
				exc.warn(s, e);
				throw e;
			}
			return this.getLastModifiedTime().compareTo(other.getLastModifiedTime());
		}
		private FileSystemMember getOuterType() {
			return FileSystemMember.this;
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
	@Override
	public int hashCode() {
		final int prime = 37;
		int res = 1;
		res = prime * res + path.hashCode();
		res = prime * res + fileAttributes.hashCode();
		res = prime * res + fileTimes.hashCode();
		return res;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		FileSystemMember other = (FileSystemMember) obj;
		return compareTo(other) == 0;
	}
	@Override
	public int compareTo(FileSystemMember other) {
		// javadocs for Comparable: Note that null is not an instance of any class, and e.compareTo(null) should throw a NullPointerException even though e.equals(null) returns false.
		if (other == null) {
			String s = "FileSystemMember.compareTo('null'): Null is not an instance of any class and not compareable!";
			NullPointerException e = new NullPointerException(s);
			exc.warn(s, e);
			throw e;
		}
		// Most important ordering is path attribute
		int res = getPath().compareTo(other.getPath());
		if (res == 0) {
			res = getFileTimes().compareTo(other.getFileTimes());
		}
		return res;
	}
}
