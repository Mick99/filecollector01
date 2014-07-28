package filecollector.logic.threadpool;


public final class PoolExecutorIdentifier {
	private final ExecutorsTypeEnum type;
	private final Integer identifier;

	public PoolExecutorIdentifier(final ExecutorsTypeEnum type,final Integer identifier) {
		this.type = type;
		this.identifier = identifier;
	}
	public ExecutorsTypeEnum getType() {
		return type;
	}
	public Integer getIdentifier() {
		return identifier;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type.name() + "-" + identifier;
	}
}

