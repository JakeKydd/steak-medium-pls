package spinfo.minisearch;

import java.io.File;

import javax.swing.JOptionPane;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.gui.SearchEngineFrame;
import spinfo.minisearch.interfaces.ISearchEngine;
import spinfo.minisearch.util.Preferences;
import spinfo.minisearch.classes.SearchEngineClass;

public abstract class Main implements ISearchEngine {

	/**
	 * Start-Methode der Suchmaschine.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		System.out.println("Starte Programm...");
		ISearchEngine searchEngine = getSearchEngine();
		if (searchEngine == null) {
			System.err
					.println("Sie müssen zunächst eine neue Instanz Ihrer ISearchEngine-Implementation");
			System.err
					.println("in der Methode getSearchEngine() in der Klasse Main zurückgeben!");
			System.err.println("Programm wird abgebrochen!");
			System.exit(0);
		}
		SearchEngineFrame frame = SearchEngineFrame.getInstance();
		System.out.println("Verbinde GUI mit Suchmaschine");
		frame.connectSearchEngine(searchEngine);
		frame.setTitle(searchEngine.getProgramName());
		frame.setVisible(true);
		frame.startInfiniteProgressBar();
		try {
			boolean load = Preferences.getInstance().autoLoadDatabase();
			System.out.println("Datenbank soll" + (load ? "" : " nicht")
					+ " automatisch geladen werden");
			searchEngine.start(new File("index"), load);
			System.out.println("Programmstart erfolgreich!");
		} catch (SearchEngineException e) {
			System.out
					.println("Beim Programmstart ist ein Fehler aufgetreten:");
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Achtung",
					JOptionPane.INFORMATION_MESSAGE);
		}
		frame.stopProgressBar();
	}

	/**
	 * Diese Methode müssen Sie anpassen, damit Ihre Suchmaschine verwendet
	 * wird.
	 * 
	 * @return die Instanz von {@link ISearchEngine}, die im Programm verwendet
	 *         wird
	 */
	public static ISearchEngine getSearchEngine() {		 
		
		
		return new SearchEngineClass();
		//return null;
	}

}
