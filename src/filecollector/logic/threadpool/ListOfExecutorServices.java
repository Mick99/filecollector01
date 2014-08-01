package filecollector.logic.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

class ListOfExecutorServices {
	// class ListOfExecutorServices implements Comparable<ListOfExecutorServices> {
	private static final Logger msg = Logger.getLogger("Message");
	// private static final Logger exc = Logger.getLogger("Exception");

	private final ExecutorsTypeEnum uniqueType;
	private List<ElementOfExecutorService> usedExecSrv = new LinkedList<>();

	ListOfExecutorServices(final ExecutorsTypeEnum uniqueType) {
		this.uniqueType = uniqueType;
	}
	PoolIdentifier addExecutorService(ExecutorsTypeEnum type, ExecutorService executorService) {
		ElementOfExecutorService el = new ElementOfExecutorService(type, getUnusedIdentifier(), executorService);
		usedExecSrv.add(el);
		return el.getIdentifier();
	}
	ExecutorService getExecutorService(PoolIdentifier poolId) {
		ElementOfExecutorService el = getElement(poolId);
		if (el == null) {
			throw new IllegalArgumentException("PoolIdentifier not found: " + poolId.getType() + poolId.getIdentifier());
		}
		return el.getExecutorService();
	}
	PoolIdentifier getFirstPoolIdentifier(ExecutorsTypeEnum type) {
		try {
			ElementOfExecutorService el = usedExecSrv.get(0);
			return el.getIdentifier();
		} catch (IndexOutOfBoundsException e) {
			// Do nothing
		}
		return null;
	}
	boolean removeElementOfExecutorService(PoolIdentifier poolId) {
		ElementOfExecutorService el = getElement(poolId);
		if (el == null) {
			throw new IllegalArgumentException("PoolIdentifier not found: " + poolId.getType() + poolId.getIdentifier());
		}
		return usedExecSrv.remove(el);
	}
	boolean removeAllElements() {
		return usedExecSrv.removeAll(usedExecSrv);
	}
	List<ElementOfExecutorService> getElementList() {
		return usedExecSrv;
	}
	ExecutorsTypeEnum getUniqueType() {
		return uniqueType;
	}
	@Override
	public int hashCode() {
		int res = hash_3();
		if (msg.isTraceEnabled()) {
			String msgMessage = String.format("this(%s) hashCode=%s", this.toString(), Integer.toHexString(res));
			msg.trace(msgMessage);
		}
		return res;
	}
	@Override
	public boolean equals(Object obj) {
		boolean res = equals_3(obj);
		if (msg.isTraceEnabled()) {
			String msgMessage = String.format("this(%s) equals=%b", this.toString(), res);
			msg.trace(msgMessage);
		}
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
	private ElementOfExecutorService getElement(PoolIdentifier poolId) {
		for (ElementOfExecutorService el : usedExecSrv) {
			if (el.getIdentifier().equals(poolId)) {
				return el;
			}
		}
		return null;
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
			sb.append(e.toString());
		}
		return "ListOf: "+uniqueType.name()+" ("+ sb.toString()+")";
	}
}

class ElementOfExecutorService {

	private final ExecutorService executorService;
	private final PoolIdentifier identifier;

	ElementOfExecutorService(ExecutorsTypeEnum type, Integer unusedIdentifier, ExecutorService executorService) {
		identifier = new PoolIdentifier(type, unusedIdentifier);
		this.executorService = executorService;
	}
	ExecutorService getExecutorService() {
		return executorService;
	}
	PoolIdentifier getIdentifier() {
		return identifier;
	}
	boolean remove(PoolIdentifier identifier) {
		// if ExecSrv shutdown() it can removed from List
		// set null for final not possible
		return false;
	}
	@Override
	public String toString() {
//		return "(" + identifier.getType().name() + " : " + identifier.getIdentifier().toString() + ")";
		return identifier.toString()+" ";
	}
}
