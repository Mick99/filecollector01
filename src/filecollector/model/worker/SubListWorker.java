package filecollector.model.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import filecollector.logic.threadpool.IWorker;
import filecollector.model.filemember.DirectoryMember;

public class SubListWorker implements Callable<List<DirectoryMember>> , IWorker {

	private List<DirectoryMember> subSource;
	private DirectoryMember parent;
	
	public SubListWorker(List<DirectoryMember> subSource, DirectoryMember parent) {
		this.subSource = subSource;
		this.parent = parent;
	}
	@Override
	public List<DirectoryMember> call() throws Exception {
		List<DirectoryMember> part = new ArrayList<>();
		int parentLevel = parent.getPath().getNameCount();
		for (DirectoryMember dm : subSource) {
			if (dm.getPath().getNameCount() == parentLevel+1) {
				if (dm.getPath().getParent().equals(parent.getPath())) {
					part.add(dm);
				}
			}
		}
		return part;
	}
}
