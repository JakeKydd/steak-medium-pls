package spinfo.minisearch.interfaces;

import java.io.File;
import java.util.List;

import spinfo.minisearch.exeption.SearchEngineException;


/**
 * Schnittstelle zwischen Benutzeroberfläche und Ihrem Programm: Sie müssen eine eigene
 * Implementation von {@link ISearchEngine} anfertigen und in der main-Methode des
 * Programms instantiieren, damit das Programm diese anschließend verwendet.<br>
 * Dieses Interface bündelt die Methoden, die aus Sicht der GUI benötigt werden -
 * es ist jedoch nicht sinnvoll, sämtliche Programmlogik ausschließlich in
 * einer Klasse zu implementieren - beachten Sie die diesbezüglichen Anmerkungen
 * in der Dokumentation.
 *
 */
public interface ISearchEngine {

	/**
	 * Suchmethode - der übergebene String soll in allen Texten gesucht werden. Als
	 * Rückgabe wird eine Liste von Dokumenten & darin enthaltenen Fundstellen erwartet,
	 * die im Interface {@link IResult} definiert sind. Beachten Sie, dass der "Such-String"
	 * <code>text</code> aus mehr als einem Wort bestehen kann und evtl. Anführungszeichen
	 * enthält, die interpretiert werden müssen.
	 * @param text
	 * @return Die Liste der gefundenen Dokumente. Darf nicht null sein.
	 */
	List<IResult> search(String text);

	/**
	 * Diese Methode synchronisiert den Index mit dem Dateisystem. 
	 * Dabei wird folgendes überprüft:
	 * <ul>
	 *  <li>Sind alle indizierten Dateien noch vorhanden?
	 *  	Ansonsten werden sie aus dem Index gelöscht.</li>
	 *  <li>Sind neue (Text-)Dateien hinzugekommen?
	 *  	Ist das der Fall, werden sie in den Index aufgenommen.</li>
	 *  <li>Sind indexierte Textdateien geändert worden)
	 *  	Ist das der Fall, werden sie im Index aktualisiert.</li>
	 * </ul> 
	 */
	void updateAll();

	/**
	 * Wird aufgerufen, wenn im Einstellungs-Dialog Änderungen vorgenommen wurden und der
	 * Dialog mit "OK" bestätigt wurde. Die übergebenen Listen sind niemals <code>null</code>,
	 * sondern evtl. - falls keine Verzeichnisse hinzugefügt und/oder entfernt wurden - leer.
	 * @param addedDirectories Liste der neu hinzugefügten Verzeichnisse.
	 * @param removedDirectories Liste der entfernten Verzeichnisse.
	 * @throws SearchEngineException
	 */
	void update(List<File> addedDirectories, List<File> removedDirectories) throws SearchEngineException;

	/**
	 * Gibt den Namen des Programms zurück, der im "Über dieses Programm"-Dialog
	 * angezeigt wird.
	 * @return den Namen des Programms
	 */
	String getProgramName();

	/**
	 * Gibt den Text der Nachricht zurück, die im "Über dieses Programm"-Dialog
	 * angezeigt wird.
	 * @return die "Über dieses Programm"-Nachricht.
	 */
	String getAboutMessage();

	/**
	 * Diese Methode wird aufgerufen, wenn das Programm (oder eine der JUnit-Testmethoden)
	 * gestartet wird. Es wird das Verzeichnis übergeben, in dem der Index gespeichert wird 
	 * oder werden soll, sowie ein boolescher Wert, der angibt, ob der Index geladen werden
	 * soll oder nicht. Beachten Sie, dass das Index-Verzeichnis evtl. nicht existiert und
	 * erst angelegt werden muss!
	 * @param indexDir Das Index-Verzeichnis
	 * @param deserializeIndex Soll der Index geladen werden?
	 * @throws SearchEngineException
	 */
	void start(File indexDir, boolean deserializeIndex) throws SearchEngineException;

	/**
	 * Diese Methode wird aufgerufen, wenn das Programm beendet wird. In diesem Fall soll
	 * der Index gespeichert werden.
	 * @throws SearchEngineException
	 */
	void stop() throws SearchEngineException;

	/**
	 * Wird diese Methode aufgerufen, muss das Index-Verzeichnis inkl. evtl. darin enthaltener
	 * Dateien gelöscht werden.
	 * @throws SearchEngineException
	 */
	void deleteIndex() throws SearchEngineException;
}
