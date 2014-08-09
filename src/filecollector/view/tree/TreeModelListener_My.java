package filecollector.view.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class TreeModelListener_My implements TreeModelListener {

	@Override
	public void treeNodesChanged(final TreeModelEvent e) {
		System.out.println("My treeNodesChanged");
	}

	@Override
	public void treeNodesInserted(final TreeModelEvent e) {
		System.out.println("My treeNodesInserted");
	}

	@Override
	public void treeNodesRemoved(final TreeModelEvent e) {
		System.out.println("My treeNodesRemoved");
	}

	@Override
	public void treeStructureChanged(final TreeModelEvent e) {
//		System.out.println("My treeStructureChanged");
		System.out.println("My treeStructureChanged: "+e.toString());
	}

}
