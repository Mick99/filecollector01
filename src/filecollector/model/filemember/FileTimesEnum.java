package filecollector.model.filemember;

public enum FileTimesEnum {
	CREATION(0), LASTACCESS(1), LASTMODIFIED(2);
	private int aryIndex;
	private FileTimesEnum(int index) {
		aryIndex = index;
	}
	public int getAryIndex() {
		return aryIndex;
	}

}
