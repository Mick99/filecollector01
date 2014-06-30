package filecollector.controller;

import java.nio.file.Paths;

import filecollector.model.Collector;

public class MainController {

	public void entryApplikation (String[] args) {
		
		Collector c = new Collector (Paths.get (args[0]));
		c.startFirstWorkerThread ();
		c.printTest ();

	}
}
