package filecollector.model.filemember;

public class DirectoryMemberSize implements IMemberSize {
	/**
	 * Sum of file sizes without sub directory's
	 */
	private Long capacitySize;
	/**
	 * Sum of file and sub directory's sizes
	 */
	private Long cumulatedCapacitySize;

	DirectoryMemberSize() {
		capacitySize = new Long(0);
		cumulatedCapacitySize = new Long(0);
	}
	/**
	 * Only for DirectoryMember CopyCtor 
	 * @param original: DirectoryMember
	 */
	DirectoryMemberSize(DirectoryMember original) {
		this.capacitySize = new Long(original.getDirSize().getCapacitySize());
		this.cumulatedCapacitySize = new Long(original.getDirSize().getCumulatedCapacitySize());
	}
	public Long getCapacitySize() {
		return capacitySize;
	}
	public Long getCumulatedCapacitySize() {
		return cumulatedCapacitySize;
	}
	public void setCapacitySize(Long size) {
		if (size != null)
			capacitySize = new Long(size);
	}
	public void setCumulatedCapacitySize(Long size) {
		if (size != null)
			cumulatedCapacitySize = new Long(size);
	}
	@Override
	public Long getSize() {
		return getCapacitySize();
	}
	@Override
	public String print() {
		return String.format("%06d", getSize());
	}
}
