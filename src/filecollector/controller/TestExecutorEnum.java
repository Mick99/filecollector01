package filecollector.controller;

public enum TestExecutorEnum {
	SLEEP_EXECUTOR, FUTURE_GET_EXECUTOR, CALLABLE_EXECUTOR;

	private static TestExecutorEnum currentEnum = null;

	public static TestExecutorEnum getCurrentEnum () {
		return currentEnum;
	}

	public static void setCurrentEnum (TestExecutorEnum currentEnum) {
		TestExecutorEnum.currentEnum = currentEnum;
	}
}
