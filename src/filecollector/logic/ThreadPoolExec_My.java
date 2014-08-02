package filecollector.logic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * MW_140714:
 * Das Prinzip was verfolgt werden soll ist, fuer WorkerThread mit der selben Aufgabe einen fest Zugeordneten
 * Pool zur Verfuegung zu stellen, der dauerhaft oder wenn keine Aufgaben mehr notwendig sind in wieder zu
 * entfernen. Als erste Idee kommt entweder TreeSet (mit compareTo(), hashCode() u. equals()) oder aber ein
 * enum zum Einsatz. 
 * Damit ich allerdings erstmal weiter komme wird der PoolManager-class mit nur EINEM ThreadPoolExecutor gecodet.
 * 
 */
public class ThreadPoolExec_My extends ThreadPoolExecutor {

	public static final BlockingQueue<Runnable> MY_QUEUE = new LinkedBlockingQueue<>();

	// Simple predefined params... MY_QUEUE noch etwas unklar...
	public ThreadPoolExec_My(String nameToFindSameWorkerPool) {
		this(3, 5, 10, TimeUnit.SECONDS, MY_QUEUE);
	}
	public ThreadPoolExec_My(int corePoolSize, int maximumPoolSize, long keepAlive, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAlive, unit, workQueue);
		// Auto-generated constructor stub
	}

}
