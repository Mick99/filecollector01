package filecollector.model;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import filecollector.util.MyFileUtils;

public class DirectoryPath {
	private static final Logger exc = Logger.getLogger("Exception");

	private Path directoryPath;

	// No Default constructor
	public DirectoryPath(final String directoryPath) throws My_IllegalArgumentException {
		this.directoryPath = checkValidDirectoryPath(checkStringDirectoryPath(directoryPath));
	}
	public DirectoryPath(final Path directoryPath) throws My_IllegalArgumentException {
		this.directoryPath = checkValidDirectoryPath(directoryPath);
	}
	public Path getDirectoryPath() throws NullPointerException {
		if (!isDirectoryPathNull())
			return directoryPath;
		else
			throw new NullPointerException("Path-obj should be not null");
	}
	public void setDirectoryPath(final Path directoryPath) throws My_IllegalArgumentException {
		checkValidDirectoryPath(directoryPath);
		this.directoryPath = directoryPath;
	}
	public boolean isDirectoryPathNull() {
		return directoryPath == null;
	}
	@Override
	public String toString() {
		return (directoryPath != null) ? directoryPath.toString() + " : " + super.toString() : "directoryPath is NULL";
	}
	private Path checkValidDirectoryPath(final Path pathToCheck) throws My_IllegalArgumentException {
		if (pathToCheck != null) {
			if (!MyFileUtils.isDirectory(pathToCheck)) {
				String excMessage = String.format("'%s' is not a directory!!", pathToCheck.toString());
				exc.warn(excMessage);
				throw new My_IllegalArgumentException(excMessage);
			}
		} else {
			String excMessage = "Path-obj NULL, make no sense!";
			exc.warn(excMessage);
			throw new My_IllegalArgumentException(excMessage);
		}
		return pathToCheck;
	}
	private Path checkStringDirectoryPath(final String directoryPath) throws My_IllegalArgumentException {
		Path tmp = null;
		if (directoryPath != null) {
			if (!directoryPath.isEmpty()) {
				try {
					tmp = Paths.get(directoryPath);
				} catch (InvalidPathException e) {
					String excMessage = String.format("Paths.get('%s') exception", directoryPath);
					exc.warn(excMessage, e);
					throw new My_IllegalArgumentException(excMessage, e);
				}
			} else {
				String excMessage = String.format("Empty String-obj: '%s'", directoryPath);
				exc.warn(excMessage);
				throw new My_IllegalArgumentException(excMessage);
			}
		} else {
			String excMessage = "String-obj NULL, make no sense!";
			exc.warn(excMessage);
			throw new My_IllegalArgumentException(excMessage);
		}
		return tmp;
	}
}
