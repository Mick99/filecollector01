package filecollector.model.filemember;

import java.nio.file.Path;

public class FileMember extends FileSystemMember {
	private long fileSize;

	public FileMember(final Path path) {
		super(path);
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public String toPrint() {
		return Long.toString(fileSize) + " :" + getFileName();
	}
}