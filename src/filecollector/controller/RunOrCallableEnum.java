package filecollector.controller;

public enum RunOrCallableEnum {
	RUNNABLE("Runnable", false), CALLABLE("Callable", true);

	private String text;
	private Boolean runOrCall;

	private RunOrCallableEnum(String text, Boolean runOrCall) {
		this.text = text;
		this.runOrCall = runOrCall;
	}
	public String getText() {
		return text;
	}
	public Boolean getRunOrCall() {
		return runOrCall;
	}
}
