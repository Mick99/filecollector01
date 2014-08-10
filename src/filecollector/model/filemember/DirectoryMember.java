package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class DirectoryMember extends FileSystemMember {
	/**
	 * List of sub directory content
	 */
	private List<FileSystemMember> dirContent;
	/**
	 * Sum of file sizes without sub directory's
	 */
	private long capacitySize = -1L;
	/**
	 * Sum of file and sub directory's sizes
	 */
	private long cumulatedCapacitySize = -1L;

	public DirectoryMember(final Path path) {
		super(path);
		dirContent = new ArrayList<FileSystemMember>();
	}
	public void addFileSystemMember(FileSystemMember fileSystemMember) {
		dirContent.add(fileSystemMember);

	}
	public List<FileSystemMember> getDirContent() {
		return dirContent;
	}
	@Override
	public String toPrint() {
		return getABSOLUTE_PATH_NAME();
	}
}
