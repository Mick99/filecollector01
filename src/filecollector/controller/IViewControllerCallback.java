package filecollector.controller;

import filecollector.model.DirectoryPath;

public interface IViewControllerCallback {

	void sendInput(String input);
	void startCollect(DirectoryPath dirPath);
}
