package filecollector.logic.threadpool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.ExecutorsTypeEnum;

/**
 * Helper class designed as singleton to get same instance for different threads.<br>
 * <strong>(! Singleton is only per Classloader distinct !)</strong>
 * 
 * <p>Since 1.5 Singleton as one element enum possible:</p>
 * 
 * <pre>
 * public enum Singleton {
 * 		INSTANCE;
 * 		
 * 		private attr;
 * 		public methode();
 * }
 * </pre>
 * 
 * @author Mick_02
 * 
 */

/*
 *  MW_140724: Derzeit zeige ich nur dass Prinzip, wie es laufen könnte. Erstmal noch alten PoolManager nutzen.
 *   needNew(): 
 *   1. Identifier wird mit Enum + einer nicht benutzten Id erzeugt die hier verwaltet wird.
 *   2. Mit Enum + Id der ExecutorService (newFixed.., newSingle.., usw) erzeugt und von den Threads später benutzt. 
 */
public class PoolManager {
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private static final PoolManager INSTANCE = new PoolManager();
	private Set<ListOfExecutorServices> executorSrvList = new HashSet<>(7);

	// private EnumSet<ExecutorsTypeEnum> executorSrvList = EnumSet.noneOf(ExecutorsTypeEnum.class);

	private PoolManager() {
	}
	public static PoolManager getInstance() {
		return INSTANCE;
	}
	public PoolIdentifier newPool(ExecutorsTypeEnum type) {

		ListOfExecutorServices l = getListService(type);
		if (l == null) // No pool, create empty one
			l = new ListOfExecutorServices(type);
		ExecutorService srv = null;
		srv = createService(type);
		PoolIdentifier newId = null;
		if (l != null && srv != null) {
			newId = l.addExecutorService(type, srv);
			boolean msgBool = executorSrvList.add(l);
			if (msg.isTraceEnabled())
				msg.trace("Add to Set(after)= " + msgBool);
		}
		return newId;
	}
	public ExecutorService usePool(IPoolIdentifier runnable, PoolIdentifier poolIdentifier) {
		runnable.transferNewIdentifier(poolIdentifier);
		ListOfExecutorServices l = getListService(poolIdentifier.getType());
		if (l != null){
			return l.getExecutorService(poolIdentifier);
		}
		return null;
	}
	// Need a boolean result for shutdownAllPools ...
	public void clearPool(PoolIdentifier poolIdentifier) {
		ListOfExecutorServices l = getListService(poolIdentifier.getType());
		if (l != null){
			if (shutdownPool(l.getExecutorService(poolIdentifier))) {
				if (!l.removeElementOfExecutorService(poolIdentifier)) {
					exc.info("Remove failed for PoolIdentifier: "+poolIdentifier.toString());
				}
			}
		}
	}
	private ExecutorService createService(ExecutorsTypeEnum type) {
		ExecutorService tmp = null;
		switch (type) {
		case CACHED:
			tmp = Executors.newCachedThreadPool();
			break;
		case SINGLE:
			tmp = Executors.newSingleThreadExecutor();
			break;
		case MISCALUS:
			tmp = Executors.newFixedThreadPool(2);
			break;

		default:
			String excMessage = String.format("No case block found for: '%s'", type.name());
			exc.info(excMessage);
			// throw new IllegalArgumentException(excMessage);
		}
		return tmp;
	}
	private ListOfExecutorServices getListService(ExecutorsTypeEnum type) {
		ListOfExecutorServices tmp = new ListOfExecutorServices(type);
		boolean containsRes = executorSrvList.contains(tmp);
		if (msg.isTraceEnabled()) {
			String msgMessage = String.format("getListService (%s) contains(before)=%b", type.name(), containsRes);
			msg.trace(msgMessage);
		}
		if (containsRes) {
			Iterator<ListOfExecutorServices> it = executorSrvList.iterator();
			while (it.hasNext()) {
				ListOfExecutorServices srv = it.next();
				boolean equalsRes = srv.equals(tmp);
				if (msg.isTraceEnabled()) {
					String msgMessage = String.format("getListService (%s) equals(before)=%b", type.name(), equalsRes);
					msg.trace(msgMessage);
				}
				if (equalsRes)
					return srv;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return executorSrvList.toString();
	}
	private boolean shutdownPool(ExecutorService es) {
		boolean isShutdown = true;
		es.shutdown();
		msg.debug("shutdownPool");
		try {
			if (!es.awaitTermination(200, TimeUnit.MILLISECONDS)) {
				es.shutdownNow();
				exc.warn("shutdownNow () in 1 sec");
				if (!es.awaitTermination(1, TimeUnit.SECONDS)) {
					exc.error("awaitTermination");
					isShutdown = false;
				}
			}
		} catch (InterruptedException e) {
			isShutdown = false;
			exc.fatal("Who interupt me: ", e);
			es.shutdown();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		return isShutdown;
	}
	// !!! Nicht korrekt impl, isOk muss auf false bleiben wenn einmal gesetzt !!! Nur wg testen
	public void shutdownAllPools() {
		boolean isOk = true;
		// 1. Call shutdown for all ExecutorServices
		for (ListOfExecutorServices l : executorSrvList) {
			for (ElementOfExecutorService el : l.getElementList()) {
				// Need result see above
				clearPool(el.getIdentifier());
			}
		}
		if (isOk) {
			// 2. if for all OK than remove all ElementOfExecutorService
			for (ListOfExecutorServices l : executorSrvList) {
				isOk = l.removeAllElements();
			}
		}
		if (isOk) {
			executorSrvList.clear();
		}
	}
	public void checkAndCleanPools() {
		// TODO MW_140731: Some stuff eg. how long not active, how many runnables in queue, ... 
	}
}
