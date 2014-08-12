package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
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
		init();
	}
	private void init()	{
		dirContent = new ArrayList<FileSystemMember>();
	}
	/**
	 * Copy-constructor for deep object copy.
	 * @param original: Object
	 */
	/*TODO MW_140812: Bei Copy-Ctor sollte ich wohl besser private init Methode schreiben, 
	 * um die Ablaeufe des Standard-ctor nicht nochmal zu coden und bei Anpassungen am Verhalten den Copy-Ctor zu vergessen. 
	 * Denn this(Path) darf kann ich in abgeleiteten Klassen nicht mehr aufrufen, dass geht nur in der Basisklasse.
	 * 
	 */
	public DirectoryMember(final DirectoryMember original) {
		super(original);
		init();
		this.capacitySize = original.capacitySize;
		this.cumulatedCapacitySize = original.cumulatedCapacitySize;
		// copy FileSystemMember
		Iterator<FileSystemMember> it = original.getDirContent().listIterator();
		while (it.hasNext()) {
			FileSystemMember origMember = (FileSystemMember) it.next();
			if (origMember.getClass() == FileMember.class) {
				FileMember orig = (FileMember) origMember;
				new FileMember(orig);
			}
			if (origMember.getClass() == DirectoryMember.class) {
				DirectoryMember orig = (DirectoryMember) origMember;
				new DirectoryMember(orig);
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
	public String toPrint() {
		return getPath().getFileName().toString();
//		return getABSOLUTE_PATH_NAME();
	}
}
