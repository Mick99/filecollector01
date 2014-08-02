package filecollector.logic.threadpool;

public final class PoolIdentifier {
	private final ExecutorsTypeEnum type;
	private final Integer identifier;

	PoolIdentifier(final ExecutorsTypeEnum type, final Integer identifier) {
		this.type = type;
		this.identifier = identifier;
	}
	public ExecutorsTypeEnum getType() {
		return type;
	}
	public Integer getIdentifier() {
		return identifier;
	}
	public PoolIdentifier newIdentifier() {
		return new PoolIdentifier(this.type, this.identifier);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		final PoolIdentifier p = (PoolIdentifier) obj;
		return (this.type.equals(p.type)) && (this.identifier.equals(p.identifier));
	}
	@Override
	public String toString() {
		return type.name() + "-" + identifier + " => " + super.toString();
	}
}
