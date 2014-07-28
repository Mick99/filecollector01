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
//	private static final Logger msg = Logger.getLogger("Message");
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
	public PoolExecutorIdentifier needNew(ExecutorsTypeEnum type) {

		ListOfExecutorServices l = getListService(type);
		if (l == null) // No pool, create empty one
			l = new ListOfExecutorServices(type);
		ExecutorService srv = null;
		srv = createService(type);
		PoolExecutorIdentifier newId = null;
		if (l != null && srv != null) {
			newId = l.addExecutorService(type, srv);
			executorSrvList.add(l);
		}
		return newId;
	}
	public PoolExecutorIdentifier usePool(PoolExecutorIdentifier poolIdentifier, Runnable runnable) {
		
		return new PoolExecutorIdentifier(poolIdentifier.getType(), poolIdentifier.getIdentifier());
	}
	private ExecutorService createService(ExecutorsTypeEnum type) {
		ExecutorService tmp = null;
		switch (type) {
		case POOL:
			tmp = Executors.newCachedThreadPool();
			break;
		case SINGLE:
			tmp = Executors.newSingleThreadExecutor();
			break;
		case MISCALUS:
			tmp = Executors.newFixedThreadPool(4);
			break;

		default:
			String excMessage = String.format("No case block found for: '%s'", type.name());
			exc.info(excMessage);
//			throw new IllegalArgumentException(excMessage);
		}
		return tmp;
	}

	private ListOfExecutorServices getListService(ExecutorsTypeEnum type) {
		
//		if (executorSrvList.contains(type)) {
			Iterator<ListOfExecutorServices> it = executorSrvList.iterator();
			while (it.hasNext()) {
				ListOfExecutorServices srv = it.next();
				if (srv.getUniqueType().equals(type))
					return srv;
			}
//		}
		return null;
	}
	@Override
	public String toString() {
		return executorSrvList.toString();
	}
}
