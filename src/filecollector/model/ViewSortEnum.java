package filecollector.model;

public enum ViewSortEnum {
	ORIG_UNSORTED("Original as constructed"), SORT_BY_DIR_FIRST("Natrual sorted but directories first"), SORT_BY_FILE_FIRST(
			"Natrual sorted but files first"), TEMP_WORK_BEFORE_SORT("Temporary working copy before add members to SORT..");

	private String description;

	private ViewSortEnum(String description) {
		this.description = description;
	}
	public String printDescription() {
		return description;
	}
}
