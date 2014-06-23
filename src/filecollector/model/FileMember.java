package filecollector.model;

public class FileMember extends Member {
	private String fileName;
	
	public FileMember (final String canonicalPathName) {
		super (canonicalPathName);
	}
}