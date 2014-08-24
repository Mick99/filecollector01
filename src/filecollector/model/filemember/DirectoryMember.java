package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DirectoryMember extends FileSystemMember {
	/**
	 * List of sub directory content
	 */
	private List<FileSystemMember> dirContent;
	private DirectoryMemberSize dirSize;

	public DirectoryMember(final Path path) {
		super(path);
		dirSize = new DirectoryMemberSize();
		init();
	}
	private void init()	{
		dirContent = new ArrayList<FileSystemMember>();
	}
	/**
	 * List Copy-constructor for deep object copy with natural ordering.
	 * @param original: DirectoryMember
	 */
	/* MW_140812: Bei Copy-Ctor sollte ich wohl besser private init Methode schreiben, 
	 * um die Ablaeufe des Standard-ctor nicht nochmal zu coden und bei Anpassungen am Verhalten den Copy-Ctor zu vergessen. 
	 * Denn this(Path) darf kann ich in abgeleiteten Klassen nicht mehr aufrufen, dass geht nur in der Basisklasse.
	 * 
	 */
	public DirectoryMember(final DirectoryMember original) {
		super(original);
		init();
		dirSize = new DirectoryMemberSize(original);
		// copy FileMembers
		Iterator<FileSystemMember> it = original.getDirContent().listIterator();
		while (it.hasNext()) {
			FileSystemMember origMember = (FileSystemMember) it.next();
			if (origMember.getClass() == FileMember.class) {
				FileMember orig = (FileMember) origMember;
				addFileSystemMember(new FileMember(orig));
			}
		}
	}
	public void addFileSystemMember(FileSystemMember fileSystemMember) {
		dirContent.add(fileSystemMember);
	}
	public List<FileSystemMember> getDirContent() {
		return dirContent;
	}
	@Override
	public String print() {
		// Special handling e.g. 'd:\' getFilname() result is null 
		String fname = new String();
		if (getPath().getFileName() != null) {
			fname = getPath().getFileName().toString(); 
		} else {
			fname = getPath().toString(); 
		}
		return String.format("%s   [%2$tF %2$tT] >>> %3$s", fname, getFileTimes().getDaylightZoneOffsetTime(FileTimesEnum.LASTMODIFIED).getTime(), getDirSize().print());
	}
	public DirectoryMemberSize getDirSize() {
		return dirSize;
	}
	@Override
	public Long getSize() {
		return dirSize.getSize();
	}
}
