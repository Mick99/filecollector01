package filecollector.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import filecollector.util.MyFileUtils;

public class DirectoryPath {
	private static final Logger exc = Logger.getLogger("Exception");

	private Path directoryPath;

	public DirectoryPath() {
	}
	public DirectoryPath(final String directoryPath) throws My_IllegalArgumentException {
		this((directoryPath == null) ? null : (!directoryPath.isEmpty()) ? Paths.get(directoryPath) : null);
	}
	public DirectoryPath(final Path directoryPath) throws My_IllegalArgumentException {
		checkValidDirectoryPath(directoryPath);
		this.directoryPath = directoryPath;
	}
	public Path getDirectoryPath() throws IllegalArgumentException {
		if (!isDirectoryPathNull())
			return directoryPath;
		else
			throw new IllegalArgumentException("Directory path should be not null");
	}
	public void setDirectoryPath(final Path directoryPath) throws My_IllegalArgumentException {
		checkValidDirectoryPath(directoryPath);
		this.directoryPath = directoryPath;
	}
	public boolean isDirectoryPathNull() {
		return directoryPath == null;
	}
	private void checkValidDirectoryPath(final Path pathToCheck) throws My_IllegalArgumentException {
		if (pathToCheck != null) {
			if (!MyFileUtils.isDirectory(pathToCheck)) {
				exc.warn(String.format("'%s' is not a directory!!", pathToCheck.toString()));
				throw new My_IllegalArgumentException(String.format("'%s' is not a directory!!", pathToCheck.toString()));
			}
		} else {
			exc.warn(String.format("Path-obj NULL, make no sense!"));
			throw new My_IllegalArgumentException(String.format("Path-obj NULL, make no sense!"));
		}
	}
	@Override
	public String toString() {
		return (directoryPath != null) ? directoryPath.toString() + " : " + super.toString() : "directoryPath is NULL";
	}
}
