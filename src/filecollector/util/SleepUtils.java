package filecollector.util;

import java.util.concurrent.TimeUnit;

/**
 * Sleep utility class to send automatic new interrupt.
 * 
 * @author Michael Inden (Der Weg zum Java-Profi)
 * 
 */
public final class SleepUtils {

	public static void safeSleep (final TimeUnit timeUnit, final long duration) {
		safeSleep (timeUnit.toMillis (duration));
	}
	public static void safeSleep (final long durationInMilliSecs) {
		try {
			Thread.sleep (durationInMilliSecs);
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
		}
	}
	private SleepUtils () {}
}
