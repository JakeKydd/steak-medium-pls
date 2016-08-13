/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spinfo.minisearch.gui;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import spinfo.minisearch.Main;
import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.interfaces.ISearchEngine;
import spinfo.minisearch.util.Preferences;


/**
 * Diese Klasse implementiert die Methoden von {@link AbstractSearchEngineFrame},
 * die die graphische Oberfläche mit der Logik der Suchmaschine verbindet.
 * 
 * Sie realisiert u.a. das "Singleton"-Design Pattern, wodurch Sie in Ihrem Code
 * an beliebiger Stelle auf das Objekt, das das Fenster der Suchmaschine repräsentiert,
 * zugreifen können.
 * http://de.wikipedia.org/wiki/Singleton_(Entwurfsmuster)
 */
public class SearchEngineFrame extends AbstractSearchEngineFrame {
	
	private static final long serialVersionUID = -6236346898142323671L;
	
	////////// Implementation des Singleton-Designpatterns /////////////////
	
	private static SearchEngineFrame singleton;
	
	/**
	 * Ein privater Konstruktor, weil nur ein Objekt dieser Klasse
	 * instantiiert werden darf. Dies geschieht mit Hilfe der Methode
	 * {@link SearchEngineFrame#getInstance()} im Hauptprogramm {@link Main}.
	 */
	private SearchEngineFrame() {
		singleton = this;
	}
	
	/**
	 * Mit Hilfe dieser Methode erhalten Sie Zugriff auf das Singleton-Objekt,
	 * das die graphische Oberfläche des Programms implementiert. So können Sie
	 * bspw. die Methoden, die für die Darstellung des Fortschrittsbalkens
	 * zuständig sind (z.B. {@link SearchEngineFrame#setMaximumWork(int)} oder
	 * {@link SearchEngineFrame#setWorkDone(int)}), aufrufen.
	 * @return die Singleton-Instanz von SearchEngineFrame
	 */
	public static SearchEngineFrame getInstance() {
		if(singleton == null) {
			singleton = new SearchEngineFrame();
		}
		return singleton;
	}
		
	///////////////////////////////////////////////////////
	

	/**
	 * Verbindet die Suchmaschine mit der GUI, so dass anschließend
	 * Benutzer-Interaktionen an Ihre {@link ISearchEngine}-Implementation
	 * weitergeleitet werden können. Diese Methode wird vom Hauptprogramm
	 * aufgerufen - dort müssen Sie das Programm an Ihre Implementation 
	 * anpassen.
	 */
	public void connectSearchEngine(ISearchEngine engine) {
		this.searchEngine = engine;
	}
    
    protected void setResults(List<IResult> documents) {
    	
       super.resultsTable.setModel(new ResultsTableModel(documents));
       if(documents.size() == 0) {
    	   super.summaryLabel.setText("Ihre Suche lieferte keine Ergebnisse");
       } else {
    	   super.summaryLabel.setText("Ihre Suche lieferte " + documents.size() + " Ergebnisse");
    	   
       }
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Benutzer auf die
     * "Suchen"-Taste klickt oder die Return-Taste drückt, während
     * das Suchfeld aktiviert ist. Sie ruft die Methode 
     * {@link ISearchEngine#search(String)} auf und übergibt den
     * Inhalt des Suchfeldes an diese Methode.
     * <strong>Achtung:</strong> Die Methode wird in einem eigenen
     * Thread aufgerufen, damit die GUI nicht blockiert wird. Beachten
     * Sie die Hinweise zum Thema Threads in der Aufgabenstellung.
     */
	@Override
	protected void search(final String text) {
		Thread searchThread = new Thread() {
			
			public void run() {
				List<IResult> results = searchEngine.search(text);
				setResults(results);
				oldSelection = -2;
			}
			
		};
		searchThread.start();
		
	}

	/**
     * Diese Methode wird aufgerufen, wenn der Benutzer ein Update
     * des Index startet. Sie ruft die Methode 
     * {@link ISearchEngine#updateAll()} auf.
     * <strong>Achtung:</strong> Die Methode wird in einem eigenen
     * Thread aufgerufen, damit die GUI nicht blockiert wird. Beachten
     * Sie die Hinweise zum Thema Threads in der Aufgabenstellung.
     */
	@Override
	protected void update() {
		Thread updateThread = new Thread() {
			
			public void run() {
				searchEngine.updateAll();
			}
			
		};
		updateThread.start();
		
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer eines der Dokumente,
	 * die für eine Suchanfrage gefunden wurden, selektiert. Sie aktualisiert
	 * die Detailanzeige für das Dokument (Anzeige des Dokumenttextes und Hervorherbung
	 * der Suchwörter).
	 */
	@Override
	public void setDetails(IResult details) {
		try {
			resultDetails.setEditable(false);
			resultDetails.setDoubleBuffered(true);
			Document blank = new DefaultStyledDocument();
			resultDetails.setDocument(blank);
			Document newDoc = new DefaultStyledDocument();
			newDoc.insertString(0, details.getContent(), null);
			resultDetails.setDocument(newDoc);
			hightlightText(details);
		} catch (Exception e) {
			super.resultDetails.setText("");
			JOptionPane.showConfirmDialog(this, "Message: "+e.getMessage(), "Ein Fehler ist aufgetreten", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	/**
	 * Hilfsmethode, die dafür sorgt, dass die Fundstellen eines Suchwortes in
	 * der Detailanzeige hervorgehoben werden.
	 * @param details
	 */
	private void hightlightText(IResult details) {
		Map<Integer, Integer> positions = details.getHighlightedPositions();
		StyledDocument doc = (StyledDocument) resultDetails.getDocument();
		Style defaultAttributes = doc.getStyle(StyleContext.DEFAULT_STYLE);
		Style highlightAttribute = doc.addStyle("highlighted", defaultAttributes);
	    StyleConstants.setBackground(highlightAttribute, Preferences.getInstance().getColor());
		doc.setCharacterAttributes(0, doc.getLength(), defaultAttributes, true);
		for (Integer start : positions.keySet()) {
			Integer end = positions.get(start) - start + 1;
			doc.setCharacterAttributes(start, end, highlightAttribute, true);
		}
	}
	
	

	private int oldSelection = -2;
	
	@Override
	protected void resultsTableSelectionChanged(int selection) {
		if(selection == oldSelection) {
			return;
		}
		oldSelection = selection;
		if(selection == -1) {
			super.resultDetails.setText("");
			return;
		}
		ResultsTableModel model = (ResultsTableModel) super.resultsTable.getModel();
		IResult selected = model.getDocument(selection);
		setDetails(selected);
	}
	
	
	
	//////////////////// Fortschrittsbalken /////////////////

	
	/**
	 * Setzt das Maximum der "Arbeit", die in einer lang arbeitenden Methode
	 * (wie z.B. der Erstellung des Index) absolviert werden muss. Es bietet
	 * sich bspw. an, dieser Methode die Anzahl aller Dateien, die indexiert
	 * werden müssen, zu übergeben. Anschließend kann die Methode 
	 * {@link SearchEngineFrame#setWorkDone(int)} jedesmal aufgerufen werden,
	 * wenn eine Datei indexiert wurde, wobei die Anzahl der bereits indexierten
	 * Dateien übergeben wird. 
	 * 
	 * @see JProgressBar
	 * 
	 */
	@Override
	public void setMaximumWork(int maxWork) {
		progressBar.setIndeterminate(false);
		super.progressBar.setMaximum(maxWork);
	}

	/**
	 * Aktualisiert die Fortschrittsleiste. Die übergebene Zahl muss größer als
	 * 0 sein. Ist sie größer oder gleich der maximalen Größe, die zuvor mit 
	 * {@link SearchEngineFrame#setMaximumWork(int)} gesetzt wurde, verschwindet
	 * der Fotschrittsbalken wieder.
	 * @see JProgressBar
	 */
	@Override
	public void setWorkDone(final int workDone) {
		progressBar.setIndeterminate(false);
		progressBar.setValue(workDone);
		if(progressBar.getMaximum() <= workDone) {
			progressBar.setValue(0);
		}
	}
	
	/**
	 * Startet die "unendliche" Fortschrittsleiste, was bspw. dann sinnvoll ist,
	 * wenn das Maximum nicht berechnet werden kann. Das Programm signalisiert
	 * in diesem Fall, dass es beschäftigt ist, bis die Methode 
	 * {@link SearchEngineFrame#stopProgressBar()} aufgerufen wird. Sinnvoll
	 * bspw. dann, wenn der Index der Suchmaschine eingelesen wird.
	 */
	public void startInfiniteProgressBar() {
		progressBar.setIndeterminate(true);
	}
	
	/**
	 * Sorgt dafür, dass die Fortschrittsleiste zurückgesetzt/gestoppt wird.
	 */
	public void stopProgressBar() {
		progressBar.setValue(progressBar.getMaximum()+1);
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
	}
	

}
