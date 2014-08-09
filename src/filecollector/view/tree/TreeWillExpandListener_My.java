package filecollector.view.tree;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import filecollector.model.filemember.DirectoryMember;

public class TreeWillExpandListener_My implements TreeWillExpandListener {

	@Override
	public void treeWillCollapse(TreeExpansionEvent arg0) throws ExpandVetoException {
//		System.out.println(arg0.toString() + " My treeWillCollapse");
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent arg0) throws ExpandVetoException {
//		System.out.println(arg0.toString() + " My treeWillExpand");
		TreePath tp = arg0.getPath();
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) tp.getLastPathComponent();
		DirectoryMember dm = (DirectoryMember) dmt.getUserObject();
		System.out.println(dm.toPrint());
		TreeStructure_My.dirListToTreeNode(dm, dmt);
	}

}
