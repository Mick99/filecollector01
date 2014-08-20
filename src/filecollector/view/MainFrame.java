package filecollector.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import filecollector.controller.IGuiCallback;
import filecollector.controller.MainController;
import filecollector.controller.RunOrCallableEnum;
import filecollector.model.Collector;
import filecollector.model.DirectoryPath;
import filecollector.model.My_IllegalArgumentException;
import filecollector.model.ViewSortEnum;
import filecollector.view.tree.DefaultTreeCellRenderer_My;
import filecollector.view.tree.DefaultTreeModel_My;
import filecollector.view.tree.TreeExpansionListener_My;
import filecollector.view.tree.TreeModelListener_My;
import filecollector.view.tree.TreeSelectionListener_My;
import filecollector.view.tree.TreeStructure_My;
import filecollector.view.tree.TreeWillExpandListener_My;

public class MainFrame {

	private JFrame frame;
	private ExitListener exitListener;
	private JTextArea textArea;
	private JCheckBox runOrCall;
	private JScrollPane dirScrollPane;
	private JTree dirTree;
	private JButton startCollect;
	private JTextField directoryInput;

	private String frameTitle = "File Collector :";
	private TreeModel dirTreeModel;
	private IGuiCallback viewCtrlCallback;

	public MainFrame(IGuiCallback viewCtrlCallback) {
		this.viewCtrlCallback = viewCtrlCallback;
	}
	public void createMainFrame() {
		frame = new JFrame(frameTitle);
		exitListener = new ExitListener(frame);
		frame.addWindowListener(exitListener);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Das Hauptfenster wird in drei Bereiche unterteilt
		frame.add(createToolBarPanel(), BorderLayout.NORTH);
		frame.add(createCenterPanel(), BorderLayout.CENTER);
		frame.add(createInputPanel(), BorderLayout.SOUTH);

		frame.setSize(700, 350);
		frame.setVisible(true);
	}
	public void closeMainFrame() {
		exitListener.closeApp();
	}
	public void newDirectoryStructure() {
		startCollect.setVisible(true);
		Color colorOld = startCollect.getBackground();
		Boolean toogle = new Boolean(false);
		for (int i = 0; i < 7; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// ignore
			}
			if (toogle) {
				startCollect.setBackground(Color.RED);
				toogle = Boolean.FALSE;
			} else {
				startCollect.setBackground(Color.GREEN);
				toogle = Boolean.TRUE;
			}
		}
		startCollect.setBackground(colorOld);
		frame.setTitle(String.format("%s Path= %s", frameTitle, directoryInput.getText()));
	}
	private JPanel createToolBarPanel() {
		// Zwei Toolbars erzeugen
		final JToolBar zoomToolBar = new JToolBar();
		zoomToolBar.add(new JButton("+"));
		zoomToolBar.add(new JButton("-"));
		zoomToolBar.addSeparator();
		zoomToolBar.add(new JButton("100%"));

		final JToolBar skipToolBar = new JToolBar();
		skipToolBar.add(new JButton("|<-"));
		skipToolBar.add(new JButton("<<"));
		skipToolBar.add(new JButton(">>"));
		skipToolBar.add(new JButton("->|"));
		skipToolBar.addSeparator();

		// TODO MW_140819: Schau dir mal Model dazu an, wg Enum und nicht Strings
		final JComboBox<String> viewSortSelector = new JComboBox<>();
		for (ViewSortEnum v : ViewSortEnum.values())
			viewSortSelector.addItem(v.name());
		viewSortSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<String> selectedChoice = (JComboBox<String>) event.getSource();
				String s = (String) selectedChoice.getSelectedItem();
				// System.out.print(s);
				// Collector.getCollector().test1(s);

				// Nicht sehr gluecklich String nach Enum
				ViewSortEnum vs = ViewSortEnum.TEMP_WORK_BEFORE_SORT;
				for (ViewSortEnum v : ViewSortEnum.values()) {
					if (s.equals(v.name())) {
						vs = v;
						break;
					}
				}
				DefaultTreeModel_My model = (DefaultTreeModel_My) dirTree.getModel();
				// MutableTreeNode newRoot = TreeStructure_My.startTreeStructure_TEST();
				MutableTreeNode newRoot = viewCtrlCallback.getDirTreeStructure(vs);
				model.setRoot(newRoot);
				model.reload(newRoot);
			}
		});

		// Ausrichtung LEFT ist wichtig, da die Toolbars sonst mittig sind.
		final JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolbarPanel.add(zoomToolBar);
		toolbarPanel.add(skipToolBar);
		toolbarPanel.add(viewSortSelector);

		// JPanel f�r Toolbar und Separator
		final JPanel compoundPanel = new JPanel(new BorderLayout());
		compoundPanel.add(toolbarPanel, BorderLayout.NORTH);
		compoundPanel.add(new JSeparator(), BorderLayout.SOUTH);

		return compoundPanel;
	}
	private JComponent createCenterPanel() {
		// dirTree = buildJTree(TreeStructure_My.createEmptyTreeStructure());
		dirTree = buildJTree(viewCtrlCallback.getDirTreeStructure(ViewSortEnum.NONE));
		dirScrollPane = new JScrollPane(dirTree);
		final JSplitPane splitPane = new JSplitPane();
		textArea = new JTextArea("TextArea etwas breiter");
		splitPane.setLeftComponent(textArea);
		splitPane.setRightComponent(dirScrollPane);
		return splitPane;
	}
	private JTree buildJTree(MutableTreeNode rootNodeAttr) {
		dirTreeModel = new DefaultTreeModel_My(rootNodeAttr);
		JTree newDirectoryTree = new JTree(dirTreeModel);
		// TreeCellRenderer
		newDirectoryTree.setCellRenderer(new DefaultTreeCellRenderer_My());
		// Model Listener
		dirTreeModel.addTreeModelListener(new TreeModelListener_My());
		// Selection Listener
		newDirectoryTree.addTreeSelectionListener(new TreeSelectionListener_My());
		// Selection WillExpand
		newDirectoryTree.addTreeWillExpandListener(new TreeWillExpandListener_My(viewCtrlCallback));
		// Selection Listener
		newDirectoryTree.addTreeExpansionListener(new TreeExpansionListener_My());

		return newDirectoryTree;
	}
	private JPanel createInputPanel() {
		directoryInput = new JTextField("d:/test2", 30);
		Font font = new Font(Font.DIALOG_INPUT, Font.ITALIC, 14);
		directoryInput.setFont(font);
		final JButton sendInput = new JButton("Send input");
		sendInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String in = directoryInput.getText();
				textArea.append("\n" + in);
				DefaultTreeModel_My model = (DefaultTreeModel_My) dirTree.getModel();
				// MutableTreeNode newRoot = TreeStructure_My.createDemoTree();
				MutableTreeNode newRoot = TreeStructure_My.startTreeStructure_TEST();
				model.setRoot(newRoot);
				model.reload(newRoot);
			}
		});
		startCollect = new JButton("START");
		startCollect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					DirectoryPath dir = new DirectoryPath(directoryInput.getText());
					viewCtrlCallback.startCollect(dir);
					startCollect.setVisible(false);
				} catch (My_IllegalArgumentException e) {
					JOptionPane.showMessageDialog(frame, "Not a valid directory path", "Wrong Dir", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		runOrCall = new JCheckBox();
		runOrCall.setText(MainController.runOrCallableEnum.getText());
		runOrCall.setSelected(MainController.runOrCallableEnum.getRunOrCall());
		runOrCall.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainController.runOrCallableEnum == RunOrCallableEnum.RUNNABLE) {
					MainController.runOrCallableEnum = RunOrCallableEnum.CALLABLE;
				} else {
					MainController.runOrCallableEnum = RunOrCallableEnum.RUNNABLE;
				}
				runOrCall.setText(MainController.runOrCallableEnum.getText());
				runOrCall.setSelected(MainController.runOrCallableEnum.getRunOrCall());
			}
		});
		final JButton testCopyCtor = new JButton("testCopyCtor");
		testCopyCtor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// Collector.getCollector().testCopyCtor();
				Collector.getCollector().test();
			}
		});

		final JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(sendInput);
		inputPanel.add(directoryInput);
		inputPanel.add(startCollect);
		inputPanel.add(runOrCall);
		inputPanel.add(testCopyCtor);

		// JPanel f�r Toolbar und Separator
		final JPanel compoundPanel = new JPanel(new BorderLayout());
		compoundPanel.add(new JSeparator(), BorderLayout.NORTH);
		compoundPanel.add(inputPanel, BorderLayout.SOUTH);

		return compoundPanel;
	}
	// private JPanel createStatusBarPanel() {
	// final JPanel statusbarPanel = new JPanel();
	// statusbarPanel.setLayout(new BorderLayout());
	//
	// final JPanel leftPanel = new JPanel();
	// final JPanel rightPanel = new JPanel();
	//
	// // final JLabel info1 = new JLabel("Left aligned info");
	// final JTextField directoryInput = new JTextField("Directory Input:");
	// final JLabel info2 = new JLabel("Right aligned info");
	// leftPanel.add(directoryInput);
	// rightPanel.add(info2);
	//
	// statusbarPanel.add(new JSeparator(), BorderLayout.NORTH);
	// statusbarPanel.add(leftPanel, BorderLayout.WEST);
	// statusbarPanel.add(rightPanel, BorderLayout.EAST);
	//
	// return statusbarPanel;
	// }
}
