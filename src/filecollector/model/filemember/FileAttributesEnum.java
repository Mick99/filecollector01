package filecollector.model.filemember;

/**
 * Based on java.nio.Attribute "Method Summary"; java.nio.Files is not useful as you can see in FileAttTest-class
 * 
 * @author Mick_02
 * 
 */
public enum FileAttributesEnum {
	READONLY_DOSATTR("readonly Flag from DosFileAttributeView"), HIDDEN_DOSATTR("hidden Flag from DosFileAttributeView"),
	SYSTEM_DOSATTR("system Flag from DosFileAttributeView"), ARCHIVE_DOSATTR("archive Flag from DosFileAttributeView");

	private final String attributeDescription;

	private FileAttributesEnum (String attr) {
		attributeDescription = attr;
	}
	public String printAttributeDescription () {
		return attributeDescription;
	}
	@Override
	public String toString () {
		return super.toString () + " " + attributeDescription;
	}
}
