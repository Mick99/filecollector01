package filecollector.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
/**
 * Helper class designed as singleton to get same instance for different threads
 *  
 * @author Mick_02
 *
 */
public final class PoolManager {
	private static final Logger exc = Logger.getLogger ("Exception");

	private static final PoolManager INSTANCE = new PoolManager ();
	// TODO MW_140712: Idea? Both as collection with name or number to have for different WorkerThreads his own executor pool.  
	private ThreadPoolExecutor pool;
	private ScheduledThreadPoolExecutor scheduled;
	
	private PoolManager () {
	}
	public static PoolManager getInstance () {
		return INSTANCE;
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
	public void clearPoolWorker () {
		if (shutdownPool (pool))
			pool = null;
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
	public void clearScheduledWorker () {
		if (shutdownPool (scheduled))
			scheduled = null;
	}
	private boolean shutdownPool (ExecutorService es) {
		boolean isShutdown = true;
		es.shutdown ();
		try {
			if (!es.awaitTermination (20, TimeUnit.MILLISECONDS)) {
				es.shutdownNow ();
				exc.warn ("shutdownNow () in 1 sec");
				if (!es.awaitTermination (1, TimeUnit.SECONDS)) {
					exc.error ("awaitTermination");
					isShutdown = false;
				}
			}
		} catch (InterruptedException e) {
			isShutdown = false;
			exc.fatal ("Who interupt me: ", e);
			es.shutdown ();
			// Preserve interrupt status
			Thread.currentThread ().interrupt ();
		}
		return isShutdown;
	}
}
