package filecollector.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import filecollector.logic.threadpool.ExecutorsTypeEnum;
import filecollector.logic.threadpool.PoolIdentifier;
import filecollector.logic.threadpool.PoolManager;
import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;
import filecollector.model.worker.SubListWorker;

public class Collector {
	// private static final Logger msg = Logger.getLogger("Message");
	private static final Logger exc = Logger.getLogger("Exception");

	private static Collector self; // Don't use
	private DirectoryMember dirOrigUnsorted;
	private List<DirectoryMember> dirMem; // Wird wohl eher nicht mehr gebraucht
	private EnumMap<ViewSortEnum, List<DirectoryMember>> mapOfDirMem; // but this is more correct
	// private EnumMap<ViewSortEnum, DefaultMutableTreeNode> mapOfTreeNode; // fuer spaeter
	private ViewSortEnum currentView = ViewSortEnum.NONE;
	private DirectoryTreeStructure tree = new DirectoryTreeStructure();

	// Must be convert to List
	public Collector(DirectoryMember root) {
		this.dirOrigUnsorted = root;
		init();
	}
	public Collector(List<DirectoryMember> list) {
		init();
		Collections.sort(list);
		deepCopy(list, ViewSortEnum.ORIG);
	}
	private void init() {
		self = this;
		mapOfDirMem = new EnumMap<>(ViewSortEnum.class);
	}
	@Deprecated
	public static Collector getCollector() {
		return self;
	}
	public List<DirectoryMember> getView(ViewSortEnum vs) {
		if (!mapOfDirMem.containsKey(vs)) {
			deepCopy(mapOfDirMem.get(ViewSortEnum.ORIG), vs);
		}
		return mapOfDirMem.get(vs);
	}
	@Deprecated
	public DirectoryMember getDirMemView(ViewSortEnum vs) {
		return dirMem.get(vs.ordinal());
	}
	private void deepCopy(List<DirectoryMember> source, ViewSortEnum vs) {
		List<DirectoryMember> newList = new ArrayList<>(source.size());
		for (DirectoryMember dm : source) {
			DirectoryMember newDm = new DirectoryMember(dm);
			newList.add(newDm);
		}
		mapOfDirMem.put(vs, newList);
	}
	public MutableTreeNode getDirTreeStructure(ViewSortEnum vs) {
		if (vs == ViewSortEnum.NONE)
			return tree.createEmptyTreeStructure();
		List<DirectoryMember> tmp = getView(vs);
		// Create one root-element ...
		DirectoryMember rootDirMem = tmp.get(0);
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) tree.createRootOfTreeStructure(rootDirMem);
		// ... create root level ...
		List<DirectoryMember> dirPart = getSubDirListWorker(mapOfDirMem.get(vs), rootDirMem);
		List<FileMember> filePart = getFileMemberList(rootDirMem);
		tree.dirListToTreeNode(dirPart, filePart, dmt, vs);
		// ... create level after root level and finish
		for (DirectoryMember dm : dirPart) {
			DefaultMutableTreeNode tmpDmt = findTreeNode(dmt, dm);
			dirPart = getSubDirListWorker(mapOfDirMem.get(vs), dm);
			filePart = getFileMemberList(dm);
			tree.dirListToTreeNode(dirPart, filePart, tmpDmt, vs);
		}
		currentView = vs;
		return dmt;
	}
	private DefaultMutableTreeNode findTreeNode(DefaultMutableTreeNode rootNode, DirectoryMember dm) {
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			TreeNode tn = rootNode.getChildAt(i);
			DefaultMutableTreeNode tmp = (DefaultMutableTreeNode) tn;
			if (dm.equals(tmp.getUserObject()))
				return tmp;
		}
		return null;
	}
	private List<FileMember> getFileMemberList(DirectoryMember parent) {
		List<FileMember> part = new ArrayList<>();
		for (FileSystemMember fs : parent.getDirContent())
			part.add((FileMember) fs);
		return part;
	}
	public void dirListToTreeStructure(DirectoryMember dirMem, MutableTreeNode constructTreeNode) {
		List<DirectoryMember> dirPart = getSubDirListWorker(mapOfDirMem.get(currentView), dirMem);
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) constructTreeNode;
		for (DirectoryMember dm : dirPart) {
			DefaultMutableTreeNode tmpDmt = findTreeNode(dmt, dm);
			dirPart = getSubDirListWorker(mapOfDirMem.get(currentView), dm);
			List<FileMember> filePart = getFileMemberList(dm);
			tree.dirListToTreeNode(dirPart, filePart, tmpDmt, currentView);
		}
	}
	private List<DirectoryMember> getSubDirListWorker(List<DirectoryMember> source, DirectoryMember parent) {
		List<DirectoryMember> result = new ArrayList<>();
		// Split source list
		PoolIdentifier poolId;
		if ((poolId = PoolManager.getInstance().isPoolAvailable(ExecutorsTypeEnum.MISCALUS)) == null)
			poolId = PoolManager.getInstance().newPool(ExecutorsTypeEnum.MISCALUS);
		int maxSize = source.size();
		int offset = calculateOffset(maxSize);
		List<Future<List<DirectoryMember>>> futureList = new ArrayList<>();
		for (int fromIndex = 0; fromIndex < maxSize; fromIndex += offset) {
			SubListWorker worker = new SubListWorker(source.subList(fromIndex, (fromIndex + offset > maxSize) ? maxSize : fromIndex + offset), parent, poolId);
			Future<List<DirectoryMember>> submit = PoolManager.getInstance().usePool(worker, poolId).submit(worker);
			futureList.add(submit);
		}
		for (Future<List<DirectoryMember>> future : futureList) {
			try {
				List<DirectoryMember> dirMember = future.get();
				if (dirMember != null)
					result.addAll(dirMember);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				exc.info("Callable Interrupt", e);
			} catch (ExecutionException e) {
				exc.error("Callable Execution", e);
			}
		}
		return result;
	}
	// Soll 9 Threads nicht ueberschreiten.
	private int calculateOffset(int maxSize) {
		int offset = 8;
		int threads = 0;
		do {
			offset *= 2;
			threads = maxSize / offset;
		} while (threads > 8);
		return offset;
	}

	
	public void test() {
		PrintTest p = new PrintTest();
		List<DirectoryMember> l = getView(ViewSortEnum.ORIG);
		p.printList(l);
		// p.printList(dirMem);
	}
	public void test1(String viewSort) {
		ViewSortEnum vs = ViewSortEnum.TEMP_WORK_BEFORE_SORT;
		for (ViewSortEnum v : ViewSortEnum.values()) {
			if (viewSort.equals(v.name())) {
				vs = v;
				break;
			}
		}
		System.out.println(vs.printDescription());
	}
}
