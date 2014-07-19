package filecollector.logic.differentThreadsCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.util.Scanner;

import org.apache.log4j.Logger;

import filecollector.controller.IViewControllerCallback;
import filecollector.util.StreamUtils;

// Reading chars from console have to press enter- or return key. Another way is to use GUI with KeyListener see 'Getch.java'.
public class KeyboardInput extends Thread {
	private static final Logger exc = Logger.getLogger("Exception");

	private IViewControllerCallback textViewController;
	private boolean hasQuitNormal = false;
	private String lastInput;
	private int character;

	public KeyboardInput() {
		this.setDaemon(true); // Cancel thread if JVM exit
		this.setName("ConsoleInput");
	}
	public KeyboardInput(IViewControllerCallback textViewController) {
		this();
		this.textViewController = textViewController;
	}
	@Override
	public void run() {
		LineReaderInput keyReader = new LineReaderInput();
		keyReader.readerTest();
	}
	public boolean isNotQuit() {
		return !hasQuitNormal;
	}

	class LineReaderInput {

		private void readerTest() {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			if (in != null) {
				while (!hasQuitNormal) {
					try {
						lastInput = in.readLine();
					} catch (IOException e) {
						exc.info("readLine exception, why?", e);
					}
					if (lastInput.equalsIgnoreCase("x")) {
						hasQuitNormal = true;
					}
					textViewController.sendInput(lastInput);
				}
			}
			StreamUtils.safeClose(in);
		}
	}

	; // Nur damit der Abstand bleibt, wegen Format loescht alles bis auf eine Zeile...

	class ScannerInput_NOTUSED {
		private void scannerTest() {
			System.out.println("keyf");
			Scanner in = new Scanner(System.in);
			if (in != null) {
				while (!hasQuitNormal) {
					System.out.println("vor hasNext");
					if (in.hasNext()) {
						lastInput = in.next();
						// in.next();// maybe flush
					}
					// SleepUtils.safeSleep(TimeUnit.MILLISECONDS, 200);
					System.out.println("key=" + lastInput);
					// hasQuitNormal = true;
					// System.out.println(hasQuitNormal);
					if (lastInput.equalsIgnoreCase("x")) {
						hasQuitNormal = true;
						System.out.println(hasQuitNormal);

					}
				}
			}

		}
	}
}
