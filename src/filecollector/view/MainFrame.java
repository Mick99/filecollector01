package filecollector.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;

public class MainFrame {

	private JFrame frame;
	private ExitListener exitListener;
	private JTextArea textArea;
	
	public void createMainFrame() {
		frame = new JFrame("File Collector");
		exitListener = new ExitListener(frame);
		frame.addWindowListener(exitListener);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Das Hauptfenster wird in drei Bereiche unterteilt 
        frame.add(createToolBarPanel(), BorderLayout.NORTH);
        frame.add(createCenterPanel(), BorderLayout.CENTER);
        frame.add(createInputPanel(), BorderLayout.SOUTH);

        frame.setSize(600, 300);
        frame.setVisible(true);
		
	}
	public void closeMainFrame() {
		exitListener.closeApp();
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

        // Ausrichtung LEFT ist wichtig, da die Toolbars sonst mittig sind. 
        final JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.add(zoomToolBar);
        toolbarPanel.add(skipToolBar);

        // JPanel f�r Toolbar und Separator 
        final JPanel compoundPanel = new JPanel(new BorderLayout());
        compoundPanel.add(toolbarPanel, BorderLayout.NORTH);
        compoundPanel.add(new JSeparator(), BorderLayout.SOUTH);

        return compoundPanel;
		
	}
	private JComponent createCenterPanel() {
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JScrollPane(new JTree()));
        textArea = new JTextArea("TextArea");
        splitPane.setRightComponent(textArea);
        return splitPane;
	}
	private JPanel createInputPanel() {
		final JTextField directoryInput = new JTextField("D:/test2", 30);
		Font font = new Font(Font.DIALOG_INPUT, Font.ITALIC, 14);
		directoryInput.setFont(font);
		final JButton sendInput = new JButton("Send input");
		sendInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String in = directoryInput.getText();
				textArea.append("\n" + in);
				
			}
		});
		
		final JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(sendInput);
		inputPanel.add(directoryInput);
        // JPanel f�r Toolbar und Separator 
        final JPanel compoundPanel = new JPanel(new BorderLayout());
        compoundPanel.add(new JSeparator(), BorderLayout.NORTH);
        compoundPanel.add(inputPanel, BorderLayout.SOUTH);
        
        return compoundPanel;
	}
	private JPanel createStatusBarPanel() {
        final JPanel statusbarPanel = new JPanel();
        statusbarPanel.setLayout(new BorderLayout());

        final JPanel leftPanel = new JPanel();
        final JPanel rightPanel = new JPanel();

//        final JLabel info1 = new JLabel("Left aligned info");
        final JTextField directoryInput = new JTextField("Directory Input:");
        final JLabel info2 = new JLabel("Right aligned info");
        leftPanel.add(directoryInput);
        rightPanel.add(info2);

        statusbarPanel.add(new JSeparator(), BorderLayout.NORTH);
        statusbarPanel.add(leftPanel, BorderLayout.WEST);
        statusbarPanel.add(rightPanel, BorderLayout.EAST);

        return statusbarPanel;
	}
}
