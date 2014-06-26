package filecollector.model.filemember;

import java.nio.file.Path;

public class FileMember extends FileSystemMember {
	private final String fileName;
	private long fileSize;
	
	public FileMember (final Path path) {
		super (path);
		this.fileName = path.getFileName ().toString (); 
	}

	public long getFileSize () {
		return fileSize;
	}

	public void setFileSize (long fileSize) {
		this.fileSize = fileSize;
	}
	

}