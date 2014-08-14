package filecollector.controller.collectorWorker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileAttributesEnum;
import filecollector.model.filemember.FileSystemMember;

//TODO MW_140813: Das erste DirMem hat kein appendAttributes(..) muß wahrscheinlich kuenstlich nachgetragen werden.
// Code ist aus AbstractDir.. kopiert, nicht gut?

public class DirectoryAttributeAppendOnce {

	public DirectoryAttributeAppendOnce(DirectoryMember dm) {
		DosFileAttributes dosFileAttr = getFileAttributes(dm.getPath());
		if (dosFileAttr != null) {
			appendFileTimesAndFlags(dm, dosFileAttr);
		}
	}

	private DosFileAttributes getFileAttributes(Path pathToReadAttr) {
		try {
			return Files.readAttributes(pathToReadAttr, DosFileAttributes.class);
		} catch (IOException e) {
//			exc.warn("Files.readAttributes", e);
			return null;
		}
	}
	private void appendFileTimesAndFlags(FileSystemMember fsm, DosFileAttributes dfa) {
//		fsm.new FileTimes(dfa.creationTime(), dfa.lastAccessTime(), dfa.lastModifiedTime());
		fsm.setFileTimes(dfa);
		appendFileFlags(fsm, dfa);
	}
	private void appendFileFlags(FileSystemMember fsm, DosFileAttributes dfa) {
		Set<FileAttributesEnum> tmp = new HashSet<>();
		if (dfa.isReadOnly())
			tmp.add(FileAttributesEnum.READONLY_DOSATTR);
		if (dfa.isHidden())
			tmp.add(FileAttributesEnum.HIDDEN_DOSATTR);
		if (dfa.isSystem())
			tmp.add(FileAttributesEnum.SYSTEM_DOSATTR);
		if (dfa.isArchive())
			tmp.add(FileAttributesEnum.ARCHIVE_DOSATTR);
		if (!tmp.isEmpty())
			fsm.setFileAttributes(EnumSet.copyOf(tmp));
	}

}
