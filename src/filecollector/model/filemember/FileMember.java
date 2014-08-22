package filecollector.model.filemember;

import java.nio.file.Path;

public class FileMember extends FileSystemMember {
	private FileMemberSize fileSize;

	public FileMember(final Path path) {
		super(path);
		fileSize = new FileMemberSize();
	}
	/**
	 * Copy-constructor for deep object copy.
	 * @param original: Object
	 */
	public FileMember(final FileMember original) {
		super(original);
		fileSize = new FileMemberSize(original);
	}
	public FileMemberSize getFileSize() {
		return fileSize;
	}
	@Override
	public String print() {
		return String.format("%s: %s   [%3$tF %3$tT]", getFileSize().print(), getFileName(), getFileTimes().getDaylightZoneOffsetTime(FileTimesEnum.LASTMODIFIED).getTime());
	}
}