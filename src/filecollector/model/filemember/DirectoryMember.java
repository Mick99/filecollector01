package filecollector.model.filemember;

import java.nio.file.Path;

public class DirectoryMember extends FileSystemMember {
	// Sum of file sizes without subdirectory's
	private long capacitySize;
	// Sum of file and subdirectory's sizes
	private long cumulatedCapacitySize;

	public DirectoryMember (final Path path) {
		super (path);
	}
}
