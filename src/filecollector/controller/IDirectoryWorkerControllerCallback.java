package filecollector.controller;

import filecollector.model.Collector;

public interface IDirectoryWorkerControllerCallback {
	void finishCollectDirectories(final Collector collector);
}
