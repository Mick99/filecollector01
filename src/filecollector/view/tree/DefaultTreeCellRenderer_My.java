package filecollector.view.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import filecollector.model.filemember.FileMember;
import filecollector.model.filemember.FileSystemMember;

@SuppressWarnings("serial")
public class DefaultTreeCellRenderer_My extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean isSelected, final boolean isExpanded,
			final boolean isLeaf, final int row, final boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
		// Für unser Modell gültig, da es DefaultMutableTreeNode's nutzt
		if (value instanceof DefaultMutableTreeNode) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

			setMetaAttributes(tree, node);
		}

		return this;
	}
	private void setMetaAttributes(final JTree tree, final DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof FileSystemMember) {
			FileSystemMember fsm = (FileSystemMember) node.getUserObject();
			setText(fsm.print());
//			if (fsm.getClass() == FileMember.class) {
//				FileMember fm = (FileMember) fsm;
//				setText(String.format("%05d: %s",fm.getFileSize(), fm.getFileName()));
//				
//			} else {
//				setText(fsm.getFileName());
//			}
		} else {
			setText("NOOOO");
		}
		

	}
}
