package filecollector.model;

public enum ViewSortEnum {
	NONE("Ohhh nooo ;-)"), ORIG("Original as constructed"), DIR_FIRST("Natrual sorted but directories first"), FILE_FIRST(
			"Natrual sorted but files first"), DIR_ONLY("Show only directories"), TEMP_WORK_BEFORE_SORT("Temporary working copy");

	private String description;

	private ViewSortEnum(String description) {
		this.description = description;
	}
	public String printDescription() {
		return description;
	}
}
