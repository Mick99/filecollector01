package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import filecollector.model.ViewSortEnum;


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
		init();
	}
	private void init()	{
		dirContent = new ArrayList<FileSystemMember>();
	}
	/**
	 * List Copy-constructor for deep object copy with natural ordering.
	 * @param original: DirectoryMember
	 */
	/*TODO MW_140812: Bei Copy-Ctor sollte ich wohl besser private init Methode schreiben, 
	 * um die Ablaeufe des Standard-ctor nicht nochmal zu coden und bei Anpassungen am Verhalten den Copy-Ctor zu vergessen. 
	 * Denn this(Path) darf kann ich in abgeleiteten Klassen nicht mehr aufrufen, dass geht nur in der Basisklasse.
	 * 
	 */
	public DirectoryMember(final DirectoryMember original, ViewSortEnum vs) {
		super(original);
		init();
		this.capacitySize = original.capacitySize;
		this.cumulatedCapacitySize = original.cumulatedCapacitySize;
		// copy FileMember
		List<FileSystemMember> tmp = original.getDirContent();
		if (vs == ViewSortEnum.ORIG)
			Collections.sort(tmp);
		Iterator<FileSystemMember> it = tmp.listIterator();
		while (it.hasNext()) {
			FileSystemMember origMember = (FileSystemMember) it.next();
			if (origMember.getClass() == FileMember.class) {
				FileMember orig = (FileMember) origMember;
				addFileSystemMember(new FileMember(orig));
			}
			//TODO MW_140818 DEL: Only useful for explorer directory structure 
//			if (origMember.getClass() == DirectoryMember.class) {
//				DirectoryMember orig = (DirectoryMember) origMember;
//				addFileSystemMember(new DirectoryMember(orig));
//			}
		}
	}
	public void addFileSystemMember(FileSystemMember fileSystemMember) {
		dirContent.add(fileSystemMember);
	}
	public List<FileSystemMember> getDirContent() {
		return dirContent;
	}
	@Override
	public String toPrint() {
		return String.format("%s   [%2$tF %2$tT]", getPath().getFileName().toString(), getFileTimes().getDaylightZoneOffsetTime(FileTimesEnum.LASTMODIFIED).getTime());
	}
}
