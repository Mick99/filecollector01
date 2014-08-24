package filecollector.model.filemember;


public class FileMemberSize implements IMemberSize {
	private Long size; 
	
	FileMemberSize() {
		size  = new Long(0);
	}
	FileMemberSize(FileMember original) {
		size  = new Long(original.getFileSize().getSize());
	}
	public void setSize(Long size) {
		if (size != null)
			this.size = new Long(size);
	}
	@Override
	public Long getSize() {
		return size;
	}
	@Override
	public String print() {
		return String.format("%05d", getSize());
	}
}
