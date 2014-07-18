package filecollector.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import filecollector.util.MyFileUtils;

public class DirectoryPath {
	private static final Logger exc = Logger.getLogger("Exception");

	private Path DIRECTORY_PATH;

	public DirectoryPath() {
	}
	public DirectoryPath(final String directoryPath) throws IllegalArgumentException {
		if (directoryPath != null) {
			Path tmp = Paths.get(directoryPath);
			if (checkValidDirectoryPath(tmp))
				DIRECTORY_PATH = tmp;
		}
		if (isPathNull())
			throw new IllegalArgumentException("Directory path should be not null");
	}
	public DirectoryPath(final Path directoryPath) throws IllegalArgumentException {
		if (checkValidDirectoryPath(directoryPath))
			DIRECTORY_PATH = directoryPath;
		if (isPathNull())
			throw new IllegalArgumentException("Directory path should be not null");
	}
	public Path getDIRECTORY_PATH() throws IllegalArgumentException {
		if (!isPathNull())
			return DIRECTORY_PATH;
		else
			throw new IllegalArgumentException("Directory path should be not null");
	}
	public void setDIRECTORY_PATH(final Path directoryPath) throws IllegalArgumentException {
		if (checkValidDirectoryPath(directoryPath))
			DIRECTORY_PATH = directoryPath;
		if (isPathNull())
			throw new IllegalArgumentException("Directory path should be not null");
	}
	private boolean isPathNull () {
		return DIRECTORY_PATH == null;
	}
	private boolean checkValidDirectoryPath(final Path pathToCheck) {
		if (pathToCheck != null) {
			if (!MyFileUtils.isDirectory(pathToCheck)) {
				System.out.println(String.format("'%s' is not a directory!!", pathToCheck.toString())); //exc.warn
				return false;
			}
		} else {
			exc.warn(String.format("Path-obj NULL, make no sense!"));
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return DIRECTORY_PATH.toString() + " : " + super.toString();
	}
}
