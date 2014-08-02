package filecollector.model;

public enum CollectionViewSelectorEnum {
	ORIG_UNSORTED("Original as constructed"), SORT_BY_DIR_FIRST("Natrual sorted but directories first"), SORT_BY_FILE_FIRST(
			"Natrual sorted but files first");

	private String description;

	private CollectionViewSelectorEnum(String description) {
		this.description = description;
	}
	public String printDescription() {
		return description;
	}
}
