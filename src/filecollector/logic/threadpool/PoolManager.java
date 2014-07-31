package filecollector.logic.threadpool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.ExecutorsTypeEnum;

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
	private Integer id = 0;
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
}
