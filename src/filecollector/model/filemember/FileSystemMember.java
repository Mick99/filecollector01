package filecollector.model.filemember;

import java.nio.file.Path;
import java.util.EnumSet;

import org.apache.log4j.Logger;

/**
 * Only absolute paths are allowed. No relative paths like "./../Test"
 * 
 * @author Mick_02
 * 
 */
public class FileSystemMember {
//	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private final String ABSOLUTE_PATH_NAME;
	private Path path;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf (FileAttributesEnum.class);

	protected FileSystemMember (final Path path) {
		// Inside "if else" through final not possible
		this.ABSOLUTE_PATH_NAME = path.toString ();
		if (path.isAbsolute ()) {
			this.path = path;
		} else {
			exc.warn (String.format ("Only absolute paths are allowed %s:\n", path.toString ()),
					new IllegalArgumentException ());
		}
	}

	public String getABSOLUTE_PATH_NAME () {
		return ABSOLUTE_PATH_NAME;
	}
	public Path getPath () {
		return path;
	}
	protected void setPath (Path path) {
		this.path = path;
	}
	protected EnumSet<FileAttributesEnum> getFileAttributes () {
		return fileAttributes;
	}
	protected void setFileAttributes (EnumSet<FileAttributesEnum> fileAttributes) {
		this.fileAttributes = fileAttributes;
	}
}
