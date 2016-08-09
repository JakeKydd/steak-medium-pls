package spinfo.minisearch.interfaces;

import java.io.File;
import java.util.Map;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.gui.SearchEngineFrame;


/**
 * Dieses Interface repräsentiert ein Dokument, das bei einer Suche gefunden
 * wurde, mitsamt der Vorkommen der gesuchten Wörter oder Phrasen. 
 * Es wird benötigt, um Suchergebnisse in der graphischen Benutzeroberfläche
 * darzustellen (sowohl für die Listen- wie auch für die Detaildarstellung):
 * Ihre Implementation von {@link ISearchEngine} muss eine Liste von Implementationen
 * von {@link IResult} zurückgeben, wenn die Suche gestartet wird.
 * 
 * @see ISearchEngine#search(String)
 * @see SearchEngineFrame#search(String)
 */
public interface IResult extends Comparable<IResult>{

	/**
	 * Gibt den absoluten Pfad zu einem gefundenen Dokument zurück.
	 * @see File#getAbsolutePath()
	 * @return den absoluten Pfad zum gefundenen Dokument
	 */
	String getAbsoluteDocumentPath();

	/**
	 * Gibt die Anzahl der Vorkommen des Suchbegriffs zurück,
	 * bzw. die Summe der Anzahlen aller Suchbegriffe, falls mehr
	 * als ein Begriff gesucht wurde.
	 * @return die Anzahl der Vorkommen der Suchbegriffe
	 */
	int getNumberOfOccurrences();
	
	/**
	 * Gibt den Zeitpunkt der letzten Änderung des Dokuments
	 * zurück.
	 * @see File#lastModified()
	 * @return den Zeitpunkt der letzten Änderung
	 */
	long getLastModified();
	
	/**
	 * Gibt die Größe des Dokuments (Anzahl der bytes) zurück.
	 * @see File#length()
	 * @return die Größe des Dokuments
	 */
	long getSize();

	/**
	 * Gibt den Inhalt des Dokuments als {@link String} zurück.
	 * <strong>Achtung:</strong> Dieser String muss identisch zu 
	 * dem String sein, der während der Indexierung benutzt wurde, 
	 * da andernfalls die Positionsangaben der zu markierenden
	 * Fundstellen ({@link IResult}{@link #getHighlightedPositions()})
	 * nicht funktioniert (bzw. von Ihnen viel unnötige Arbeit verlangt).
	 * @throws SearchEngineException falls ein Fehler aufgetreten ist.
	 * @return Den Inhalt des Dokuments
	 */
	String getContent() throws SearchEngineException;

	/**
	 * Gibt die Fundstellen der Suchbegriffe zurück. Als Key wird die 
	 * Position des ersten zu markierenden Buchstabens erwartet, als
	 * Value die Position hinter dem letzten zu markierenden Buchstaben. 
	 * @return eine Map mit Position & Länge von Fundstellen
	 */
	Map<Integer, Integer> getHighlightedPositions();

}
