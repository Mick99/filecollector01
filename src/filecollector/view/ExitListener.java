package filecollector.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import filecollector.controller.MainController;

/**
 * Beispielklasse eines WindowListeners. Es wird eine WindowAdapter genutzt, um R�ckfrage beim Schliessen eines Fensters ztu halten. <br>
 * <b>Neu in Auflage 2</b>
 * 
 * @author Michael Inden
 * 
 *         Copyright 2012 by Michael Inden
 */
public final class ExitListener extends WindowAdapter {
	private final JFrame parentFrame;

	public ExitListener(final JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
	public void closeApp() {
		final int answer = JOptionPane.showConfirmDialog(parentFrame, "Wollen Sie das Programm beenden?");
		if (answer == JOptionPane.YES_OPTION) {
			MainController.exitApp = true;
			// Fenster unsichtbar machen und (bei Bedarf) Ressourcen freigeben
			parentFrame.setVisible(false);
			parentFrame.dispose();

			// Programmende forcieren
			// System.exit(0);
		}
	}
	@Override
	public void windowClosing(final WindowEvent event) {
		closeApp();
	}
}