package filecollector.model;

import java.nio.file.Path;
import java.util.EnumSet;

public class Member {
	private final String canonicalPathName;
	// Path from nio
	private Path path;
	// File length or sum of files inside directory
	private long capacity = -1L;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf (FileAttributesEnum.class);
	
	public Member (String canonicalPathName) {
		this.canonicalPathName = canonicalPathName;
	}

	long getCapacity () {
		return capacity;
	}

	void setCapacity (long capacity) {
		this.capacity = capacity;
	}

	EnumSet<FileAttributesEnum> getFileAttributes () {
		return fileAttributes;
	}

	void setFileAttributes (EnumSet<FileAttributesEnum> fileAttributes) {
		this.fileAttributes = fileAttributes;
	}

	String getCanonicalPathName () {
		return canonicalPathName;
	}
	
}
