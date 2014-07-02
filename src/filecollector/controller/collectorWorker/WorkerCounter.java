package filecollector.controller.collectorWorker;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * WorkerCounter use AtomicInteger may be synchronize not necessary?
 * 
 * @author Mick_02
 *
 */
public final class WorkerCounter {
	private static AtomicInteger count = new AtomicInteger ();
	private static AtomicInteger workerId = new AtomicInteger ();

	public static int createWorker () {
		return count.incrementAndGet ();
	}
	public static int releaseWorker () {
		return count.getAndDecrement ();
		
	}
	public static boolean allWorkerFinish () {
		return (count.intValue () == 0) ? true : false;
	}
	public static int getWorkerId () {
		return workerId.incrementAndGet ();
	}
}
