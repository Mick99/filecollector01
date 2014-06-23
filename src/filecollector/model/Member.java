package filecollector.model;

import java.util.EnumSet;

public class Member {
	private String fullPathName;
	// File length or sum of files inside directory
	private long capacity;
	private EnumSet<FileAttributesEnum> fileAttributes = EnumSet.noneOf (FileAttributesEnum.class);
	
}
