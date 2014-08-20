package filecollector.controller;

import filecollector.model.Collector;

public interface IDirectoryWorkerCallback {

	void finishCollect(final Collector collector);
}
