package filecollector.logic.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ListOfExecutorServices {
	// class ListOfExecutorServices implements Comparable<ListOfExecutorServices> {

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
	// List<ElementOfExecutorService> getElementList() {
	// return usedExecSrv;
	// }
	ExecutorsTypeEnum getUniqueType() {
		return uniqueType;
	}
	@Override
	public int hashCode() {
		int res = hash_3();
//		System.out.println("hash= " + Integer.toHexString(res));
		return res;
	}
	@Override
	public boolean equals(Object obj) {
		boolean res = equals_3(obj);
//		System.out.println("equals= " + res);
		return res;
	}
	private int hash_3() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueType == null) ? 0 : uniqueType.hashCode());
		return result;
	}
	private boolean equals_3(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		final ListOfExecutorServices otherList = (ListOfExecutorServices) obj;
		return this.getUniqueType().equals(otherList.getUniqueType());
	}
	private int compareTo_My(ListOfExecutorServices otherList) {
		// Only equals compare is needful, other (< or >) will return always -1
		boolean compareResult = (this.uniqueType == otherList.uniqueType) && (this.usedExecSrv == otherList.usedExecSrv);
		if (compareResult)
			return 0;
		else
			return -1;
	}
	private Integer getUnusedIdentifier() {
		int freeNumber = 0;
		for (ElementOfExecutorService el : usedExecSrv) {
			if (el.getIdentifier().getIdentifier() == freeNumber) {
				freeNumber++;
			} else {
				break;
			}
		}
		return new Integer(freeNumber);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ElementOfExecutorService e : usedExecSrv) {
			sb.append(e.toString() + "\n");
		}
		return sb.toString();
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
	@Override
	public String toString() {
		return "(" + identifier.getType().name() + " : " + identifier.getIdentifier().toString() + ")";
	}
}
