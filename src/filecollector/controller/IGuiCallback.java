package filecollector.controller;

import filecollector.model.DirectoryPath;

public interface IGuiCallback {

	void sendInput(String input);
	void startCollect(DirectoryPath dirPath);
}
