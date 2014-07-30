package filecollector.logic.threadpool;

public final class PoolIdentifier {
	private final ExecutorsTypeEnum type;
	private final Integer identifier;

	public PoolIdentifier(final ExecutorsTypeEnum type, final Integer identifier) {
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
		return type.name() + "-" + identifier;
	}
}
