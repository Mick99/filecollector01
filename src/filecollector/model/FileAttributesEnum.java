package filecollector.model;

/**
 * Based on java.io.File "Method Summary"
 * 
 * @author Mick_02
 *
 */
public enum FileAttributesEnum {
	CAN_EXECUTE ("Executable"),
	CAN_READ ("Readable"),
	CAN_WRITE ("Writeable"),
	IS_HIDDEN ("Hidden");
	
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
