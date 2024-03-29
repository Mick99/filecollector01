package filecollector.logic;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;


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
@SuppressWarnings("unused")
public final class PoolManager_OLD {
	//MW_140721: Erstmal nur simpel.
	public enum PoolExecutorType {
		POOL, SCHEDULED, MISCALUS;
	}
	private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private static final PoolManager_OLD INSTANCE = new PoolManager_OLD();
	// MW_140712: Idea? Both as collection with name or number to have own executor pool for different WorkerThreads.
//	private Set<ThreadPoolExec_My> poolSet;
	private ThreadPoolExecutor pool;
	private ScheduledThreadPoolExecutor scheduled;
	private ThreadPoolExecutor miscalus;

	private PoolManager_OLD() {
	}
	public static PoolManager_OLD getInstance() {
		return INSTANCE;
	}
	public void checkAndCleanPools() {
		/*MW_140721: Pruefen ob pools gewisse Zeit inaktiv sind und gegebenenfalls runter fahren?
		 * Erstmal periodisch im MainController aufrufen.
		 * 
		 */
	}
	public void shutdownAllPools() {
		if (scheduled != null)
			clearScheduledWorker();
		if (pool != null)
		clearPoolWorker();
		if (miscalus != null)
		clearMiscalus();
	}
	public ThreadPoolExecutor getMiscalus() {
		return miscalus;
	}
	public boolean setMiscalus(ThreadPoolExecutor miscalus) {
		if (this.miscalus == null) {
			this.miscalus = miscalus;
			return true;
		} // else do nothing
		return false;
	}
	public void clearMiscalus() {
		if (shutdownPool(miscalus)) {
			if (miscalus.isTerminated())
				miscalus = null;
		}
	}
	public ThreadPoolExecutor getPool() {
		return pool;
	}
	public synchronized boolean setPool(ThreadPoolExecutor pool) {
		if (this.pool == null) {
			this.pool = pool;
			return true;
		} // else do nothing
		return false;
	}
	public void clearPoolWorker() {
		if (shutdownPool(pool)) {
			if (pool.isTerminated())
				pool = null;
		}
	}
	public boolean isEmptyPoolAvailable() {
		return (pool.getActiveCount() == 0) ? true : false;
	}
	public ScheduledThreadPoolExecutor getScheduled() {
		return scheduled;
	}
	public synchronized boolean setScheduled(ScheduledThreadPoolExecutor scheduled) {
		if (this.scheduled == null) {
			this.scheduled = scheduled;
			return true;
		} // else do nothing
		return false;
	}
	public void clearScheduledWorker() {
		if (shutdownPool(scheduled))
			scheduled = null;
	}
	private boolean shutdownPool(ExecutorService es) {
		boolean isShutdown = true;
		es.shutdown();
		msg.debug("shutdownPool");
		try {
			if (!es.awaitTermination(20, TimeUnit.MILLISECONDS)) {
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
}
