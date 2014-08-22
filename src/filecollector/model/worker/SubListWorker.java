package filecollector.model.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import filecollector.logic.threadpool.IPoolIdentifier;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.model.filemember.DirectoryMember;

public class SubListWorker implements Callable<List<DirectoryMember>> , IPoolIdentifier {

	private List<DirectoryMember> subSource;
	private DirectoryMember parent;
	private PoolIdentifier poolId;
	
	public SubListWorker(List<DirectoryMember> subSource, DirectoryMember parent, PoolIdentifier pid) {
		this.subSource = subSource;
		this.parent = parent;
		this.poolId = pid;
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
	@Override
	public void transferNewIdentifier(PoolIdentifier poolId) {
		this.poolId = poolId;
	}
	@Override
	public String infoStrFALSCH() {
		return this.getClass().getSimpleName() + " :" + poolId;
	}

}
