package filecollector.view.tree;

import javax.swing.JTable;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import filecollector.controller.IGuiCallback;
import filecollector.model.Collector;
import filecollector.model.filemember.DirectoryMember;
import filecollector.view.table.FileSystemTableModel;

public class TreeWillExpandListener_My implements TreeWillExpandListener {

	private IGuiCallback viewCtrlCallback;
	private JTable jTable;

	public TreeWillExpandListener_My(IGuiCallback viewCtrlCallback, JTable jTable) {
		this.viewCtrlCallback = viewCtrlCallback;
		this.jTable = jTable;
	}
	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
//		System.out.println(arg0.toString() + " My treeWillCollapse");
		TreePath tp = event.getPath();
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) tp.getLastPathComponent();
		// Table
		FileSystemTableModel newTableModel = new FileSystemTableModel(viewCtrlCallback.removeFromTableView(dmt));
		jTable.setModel(newTableModel);

	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
//		System.out.println(arg0.toString() + " My treeWillExpand");
		TreePath tp = event.getPath();
		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) tp.getLastPathComponent();
		DirectoryMember dm = (DirectoryMember) dmt.getUserObject();
//		System.out.println(dm.toPrint());
		
//		TreeStructure_My.dirListToTreeNode(dm, dmt);
		viewCtrlCallback.dirListToTreeStructure(dm, dmt);
		// Table
		FileSystemTableModel newTableModel = new FileSystemTableModel(viewCtrlCallback.getTableView());
		jTable.setModel(newTableModel);
	}

}
