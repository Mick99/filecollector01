package filecollector.model.filemember;

import java.nio.file.Path;

public class FileMember extends FileSystemMember {
	private long fileSize;

	public FileMember(final Path path) {
		super(path);
	}
	/**
	 * Copy-constructor for deep object copy.
	 * @param original: Object
	 */
	public FileMember(final FileMember original) {
		super(original);
		this.fileSize = original.fileSize;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public String toPrint() {
		return String.format("%05d: %s",getFileSize(), getFileName());
//		return Long.toString(fileSize) + " :" + getFileName();
	}
}