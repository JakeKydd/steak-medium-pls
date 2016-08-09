package spinfo.minisearch.exeption;

import java.io.IOException;

import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.interfaces.ISearchEngine;
import spinfo.minisearch.tests.MiniSearchTests;

/**
 * Standard-Exception, die von {@link ISearchEngine} und {@link IResult} benutzt wird.
 * Tritt in Ihrem Programm ein Fehler auf, können Sie diese Klasse verwenden, um eine
 * {@link SearchEngineException} zu werfen, die dann in der GUI dargestellt wird (Bspw. dann,
 * wenn der Index nicht mehr aktuell ist und auf eine Datei zugegriffen wird, die
 * nicht mehr existiert).
 * @see IResult#getContent() als Beispiel
 * @see MiniSearchTests#testIndexOutOfSync() als Test, der das Werfen einer solchen Exception provoziert.
 */
public class SearchEngineException extends Exception {

	private static final long serialVersionUID = -1355005810897939255L;

	/**
	 * Der übergebene String dient als Fehlerbeschreibung.
	 * @param message Die Fehlerbeschreibung
	 * @see Exception#Exception(String)
	 */
	public SearchEngineException(String message) {
		super(message);
	}

	/**
	 * Wie {@link SearchEngineException#SearchEngineException(String)}, aber
	 * mit zusätzlich übergebener Exception, die den Grund für den Fehler
	 * spezifiziert (z.B. eine abgefangene {@link IOException}).
	 * @param message Die Fehlerbeschreibung
	 * @param cause Grund des Fehlers
	 * @see Exception#Exception(String, Throwable)
	 */
	public SearchEngineException(String message, Throwable cause) {
		super(message, cause);
	}

}
