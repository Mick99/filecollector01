package filecollector.model.filemember;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import filecollector.util.HashUtils;

/**
 * Only absolute paths are allowed. No relative paths like "./../Test"
 * 
 * @author Mick_02
 * 
 */
public abstract class FileSystemMember implements Comparable<FileSystemMember> {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private Path path;
	private Path path_Absolute; // if we switch to relative path, right now it isn't
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf(FileAttributesEnum.class);
	private FileTimes fileTimes = null;

	// Inner class only for FileTimes
	public class FileTimes implements Comparable<FileTimes> {
		private FileTime[] creAccModTime = new FileTime[3];

		FileTimes(FileTime creationTime, FileTime lastAccessTime, FileTime lastModifiedTime) {
			creAccModTime[FileTimesEnum.CREATION.getAryIndex()] = creationTime;
			creAccModTime[FileTimesEnum.LASTACCESS.getAryIndex()] = lastAccessTime;
			creAccModTime[FileTimesEnum.LASTMODIFIED.getAryIndex()] = lastModifiedTime;
		}
		/**
		 * Copy-constructor for deep object copy.
		 * 
		 * @param original
		 *            : Object
		 */
		FileTimes(final FileTimes original) {
			this(original.getFileTime(FileTimesEnum.CREATION), original.getFileTime(FileTimesEnum.LASTACCESS), original
					.getFileTime(FileTimesEnum.LASTMODIFIED));
		}
		public FileTime getFileTime(FileTimesEnum fte) {
			return creAccModTime[fte.getAryIndex()];
		}
		public GregorianCalendar getDaylightZoneOffsetTime(FileTimesEnum fte) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.clear();
				gc.setTimeInMillis(getFileTime(fte).toMillis() + Calendar.ZONE_OFFSET + Calendar.DST_OFFSET);
			return gc;
		}
		@Override
		public int hashCode() {
			// MW_140811: Ersetzt durch HashUtils.
			// final int prime = 113;
			// int res = 1;
			// // res = prime * res + getOuterType().hashCode(); !Rekursion, nicht möglich was Eclipse generiert
			// for (FileTime t : creAccModTime) {
			// res = prime * res + ((t == null) ? 0 : t.hashCode());
			// }
			// return res;
			int hash = 31;
			for (FileTime t : creAccModTime) {
				hash = HashUtils.calcHashCode(hash, (t == null) ? 0 : t.hashCode());
			}
			if (msg.isTraceEnabled()) {
				String msgMessage = String.format("this(%s) hashCode=%s", toString(), Integer.toHexString(hash));
				msg.trace(msgMessage);
			}
			return hash;
		}
		@Override
		public boolean equals(Object obj) {
			boolean equ = isEqual(obj);
			if (msg.isTraceEnabled()) {
				String msgMessage = String.format("this(%s) equals=%b", toString(), equ);
				msg.trace(msgMessage);
			}
			return equ;
		}
		private boolean isEqual(Object obj) {
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
			// javadocs for Comparable: Note that null is not an instance of any class, and e.compareTo(null) should throw a NullPointerException even
			// though e.equals(null) returns false.
			if (other == null) {
				String s = "FileTime.compareTo('null'): Null is not an instance of any class and not compareable!";
				NullPointerException e = new NullPointerException(s);
				exc.warn(s, e);
				throw e;
			}
			return this.getFileTime(FileTimesEnum.LASTMODIFIED).compareTo(other.getFileTime(FileTimesEnum.LASTMODIFIED));
		}
		// Only generated by Eclipse(->Source), because its a hidden attribute and not useful!
		// private FileSystemMember getOuterType() {
		// return FileSystemMember.this;
		// }
	}

	protected FileSystemMember(final Path path) {
		if (path.isAbsolute()) {
			this.path = path;
			this.path_Absolute = path.toAbsolutePath();
		} else {
			exc.warn(String.format("Only absolute paths are allowed %s:\n", path.toString()), new IllegalArgumentException());
		}
	}
	/**
	 * Copy-constructor for deep object copy.
	 * 
	 * @param original
	 *            : Object
	 */
	protected FileSystemMember(final FileSystemMember original) {
		this(Paths.get(original.getPath().toString()));
		fileAttributes = EnumSet.copyOf(original.fileAttributes);
		fileTimes = new FileTimes(original.getFileTimes());
	}
	public Path getPath() {
		return path;
	}
	protected void setPath(Path path) {
		this.path = path;
	}
	public void setPath_FORTEST() {
		String newP = path.toString() + "_New";
		setPath(Paths.get(newP));
	}
	public EnumSet<FileAttributesEnum> getFileAttributes() {
		return fileAttributes;
	}
	public void setFileAttributes(EnumSet<FileAttributesEnum> fileAttributes) {
		this.fileAttributes = fileAttributes;
	}
	public FileTimes getFileTimes() {
		if (fileTimes == null) {
			// TODO MW_140813: Erst mal Dummy-FileTime auf 1.1.1970 setzen...
			FileTime dum = FileTime.fromMillis(0);
			fileTimes = new FileTimes(dum, dum, dum);
		}
		return fileTimes;
	}
	public void setFileTimes(DosFileAttributes dfa) {
		fileTimes = new FileTimes(dfa.creationTime(), dfa.lastAccessTime(), dfa.lastModifiedTime());
	}
	/**
	 * Attention only JUnit Tests !</br> </br> setFileTimes_OnlyForJUnitTests(..)
	 * 
	 */
	public void setFileTimes_OnlyForJUnitTests(FileTime testCreation, FileTime testAccess, FileTime testModified) {
		fileTimes = new FileTimes(testCreation, testAccess, testModified);
	}
	public String getFileName() {
		return (path.getFileName()  == null) ? "" : path.getFileName().toString();
	}
	public abstract Long getSize();
	
	public abstract String print();
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " : " + path.toString();
	}
	@Override
	public int hashCode() {
		// MW_140811: Ersetzt durch HashUtils.
		// final int prime = 113;
		// int res = 1;
		// res = prime * res + path.hashCode();
		// res = prime * res + fileAttributes.hashCode();
		// //
		// res = prime * res + ((fileTimes == null) ? 0 : fileTimes.hashCode());
		// return res;
		int hash = 17; // Start value
		hash = HashUtils.calcHashCode(hash, path.hashCode());
		hash = HashUtils.calcHashCode(hash, fileAttributes.hashCode());
		hash = HashUtils.calcHashCode(hash, (fileTimes == null) ? 0 : fileTimes.hashCode());
		if (msg.isTraceEnabled()) {
			String msgMessage = String.format("this(%s) hashCode=%s", toString(), Integer.toHexString(hash));
			msg.trace(msgMessage);
		}
		return hash;
	}
	@Override
	public boolean equals(Object obj) {
		boolean equ = isEqual(obj);
		if (msg.isTraceEnabled()) {
			String msgMessage = String.format("this(%s) equals=%b", toString(), equ);
			msg.trace(msgMessage);
		}
		return equ;
	}
	private boolean isEqual(Object obj) {
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
		// javadocs for Comparable: Note that null is not an instance of any class, and e.compareTo(null) should throw a NullPointerException even
		// though e.equals(null) returns false.
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
