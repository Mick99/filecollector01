package filecollector.util;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Small selection of usefully static helper methods...
 *  
 * @author Mick_02
 *
 */
public final class MyFileUtils {

	// No constructor 
	private MyFileUtils () {}  
	
	public static boolean isDirectory (final Path dir) {
        if (dir == null)
            throw new IllegalArgumentException("Passed Path must not be null!");
		return (Files.isDirectory (dir)) ? true : false;
	}
}
