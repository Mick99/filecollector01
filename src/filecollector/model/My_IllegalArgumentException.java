package filecollector.model;

public class My_IllegalArgumentException extends Exception {
	// From interface Serializable
	private static final long serialVersionUID = 1L;
	
	public My_IllegalArgumentException() {
		super();
	}
	public My_IllegalArgumentException(String message) {
		super(message);
	}
}
