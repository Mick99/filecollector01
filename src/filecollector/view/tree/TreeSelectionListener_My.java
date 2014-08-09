package filecollector.view.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class TreeSelectionListener_My implements TreeSelectionListener {

	@Override
	public void valueChanged(TreeSelectionEvent selectEvent) {
//		System.out.println("valueChanged");
//		System.out.println(selectEvent.toString());
		TreePath path = selectEvent.getPath();
		Object comp = path.getLastPathComponent();
		String name = comp.getClass().getSimpleName();
		
		System.out.format("%s : [ %s ]   %s%n", path, name, selectEvent.toString());		

	}

}
