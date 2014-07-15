package filecollector.model.filemember;

import java.nio.file.Path;

public class FileMember extends FileSystemMember {
	private final String FILE_NAME;
	private long fileSize;

	public FileMember (final Path path) {
		super (path);
		this.FILE_NAME = path.getFileName ().toString ();
	}
	public long getFileSize () {
		return fileSize;
	}
	public void setFileSize (long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFILE_NAME () {
		return FILE_NAME;
	}
	@Override
	public String toPrint() {
		return Long.toString(fileSize) + ":" + FILE_NAME;
	}
}