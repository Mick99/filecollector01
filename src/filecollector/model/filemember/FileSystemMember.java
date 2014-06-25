package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.EnumSet;

public class FileSystemMember {
	private final String canonicalPathName;
	// Path from nio
	private final Path path;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf (FileAttributesEnum.class);
	
	public FileSystemMember (final Path path) {
		this.path  = path;
		this.canonicalPathName = path.toString ();
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

	Path getPath () {
		return path;
	}
	
}
