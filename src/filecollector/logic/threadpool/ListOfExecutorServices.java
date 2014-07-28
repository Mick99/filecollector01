package filecollector.logic.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

//public class ListOfExecutorServices {
class ListOfExecutorServices implements Comparable<ExecutorsTypeEnum> {

	private final ExecutorsTypeEnum uniqueType;
	private List<ElementOfExecutorService> usedExecSrv = new LinkedList<>();

	ListOfExecutorServices(final ExecutorsTypeEnum uniqueType) {
		this.uniqueType = uniqueType;
	}
	PoolExecutorIdentifier addExecutorService(ExecutorsTypeEnum type, ExecutorService executorService) {
		ElementOfExecutorService el = new ElementOfExecutorService(type, getUnusedIdentifier(), executorService);
		usedExecSrv.add(el);
		return el.getIdentifier();
	}
	void removeElementOfExecutorService(PoolExecutorIdentifier identifier) {
		// Not impl yet
	}
	List<ElementOfExecutorService> getElementList() {
		return usedExecSrv;
	}
	@Override
	public int hashCode() {
		// hashCode() of enum is distinct
		return uniqueType.hashCode();
	}
	@Override
	public boolean equals(Object arg0) {
		return uniqueType.equals(arg0);
	}
	@Override
	public int compareTo(ExecutorsTypeEnum arg0) {
		return uniqueType.compareTo(arg0);
	}
	private Integer getUnusedIdentifier() {
		int freeNumber = 0;
		for (ElementOfExecutorService el : usedExecSrv) {
			if (el.getIdentifier().equals(freeNumber)) {
				freeNumber++;
			} else {
				break;
			}
		}
		return new Integer(freeNumber);
	}
}

class ElementOfExecutorService {

	private final ExecutorService executorService;
	private final PoolExecutorIdentifier identifier;

	ElementOfExecutorService(ExecutorsTypeEnum type, Integer unusedIdentifier, ExecutorService executorService) {
		identifier = new PoolExecutorIdentifier(type, unusedIdentifier);
		this.executorService = executorService;
	}
	ExecutorService getExecutorService() {
		return executorService;
	}
	PoolExecutorIdentifier getIdentifier() {
		return identifier;
	}
	boolean remove(PoolExecutorIdentifier identifier) {
		// if ExecSrv shutdown() it can removed from List
		return false;
	}
}
