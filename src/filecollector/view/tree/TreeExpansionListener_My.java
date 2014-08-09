package filecollector.view.tree;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

public class TreeExpansionListener_My implements TreeExpansionListener {

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {
//		System.out.println(arg0.toString() + " My treeCollapsed");
	}

	@Override
	public void treeExpanded(TreeExpansionEvent arg0) {
//		System.out.println(arg0.toString() + " My treeExpanded");
	}

}
