package spinfo.minisearch.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import spinfo.minisearch.Main;
import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.interfaces.ISearchEngine;
import spinfo.minisearch.util.Preferences;

/**
 * Diese Klasse enthält von uns als relevant angesehenen Tests, die Ihre Suchmaschine
 * sämtlich bestehen muss, damit die Hausarbeit als bestanden gewertet werden kann. 
 * Ihre Suchmaschine besteht die Tests, wenn Sie diese Klasse als JUnit-Test auführen 
 * und der Balken im JUnit-View (öffnet sich automatisch) grün bleibt. 
 *
 */
public class MiniSearchTests {	
	
	private ISearchEngine getSearchEngine(){
		return Main.getSearchEngine();
	}
	
	private File testIndex = new File("test_index");
	private File testTexts = new File("test_texts");
	
	@Before
	public void beforeTest() {
		Preferences.getInstance().setDirectories(testTexts);
	}	
		
	/**
	 * Testet die Hinzufügung eines Ordners zum Suchmaschinenindex.
	 * Überprüft anhand einer einfachen Suchanfrage, ob alle Dateien 
	 * in den Index aufgenommen wurden.
	 */
	@Test
	public void testDirectoryUpload() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.start(testIndex, false);
		searchEngy.update(addedDirectories, null);
		List<IResult> searchResults = searchEngy.search("Test");
		// Da Test in 5 Texten vorkommt (nur in text6 nicht), 
		// muss die Result-Liste 5 Elemente enthalten:
		assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + searchResults.size() + ", müsste aber 5 sein!", searchResults.size()==5);
	}
	
	/**
	 * Testet das Löschen eines Ordners aus dem Suchmaschinenindex.
	 * Überprüft das anhand einer einfachen Suchanfrage.
	 */
	@Test
	public void testDirectoryDeletion() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		searchEngy.start(testIndex, false);
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(new File("test_texts/Innerer_Ordner"));
		addedDirectories.add(new File("test_texts/Zweiter_innerer_Ordner"));
			searchEngy.update(addedDirectories, null);
		//Noch muss "Wort" zweimal vorhanden sein (text5 und text6):
		List<IResult> searchResults = searchEngy.search("Wort");		
		assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + searchResults.size() + ", müsste aber 2 sein!", searchResults.size()==2);
		
		List<File> removedDirectories = new Vector<File>();
		removedDirectories.add(new File("test_texts/Zweiter_innerer_Ordner"));
		try {
			searchEngy.update(null, removedDirectories);
		} 
		catch (SearchEngineException e) {
			e.printStackTrace();
		}
		//Jetzt darf "Wort" nur noch einmal gefunden werden:
		searchResults = searchEngy.search("Wort");		
		assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + searchResults.size() + ", müsste aber 1 sein!", searchResults.size()==1);
	}
	
	/**
	 * Testet die Reaktion der Suchmaschine auf einen veralteten Index
	 * (eine Datei wurde aus einem Verzeichnis gelöscht, das unter 
	 * Kontrolle des Index steht, der wurde aber nicht aktualisiert).
	 * Erwartet wird eine (sprechende) Exception
	 */
	@Test(expected = SearchEngineException.class)
	public void testIndexOutOfSync() throws SearchEngineException, IOException{
		//Erstellen eines temporären Textes
		String fileName = "text_temp.txt";
		File tempFile = new File(testTexts,fileName);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
		out.write("Dies hier ist ein temporärer Text in einer temporären Datei. 1");
		out.close();
		
		//Erzeugen des Indexes mit dem temporären Text
		ISearchEngine searchEngy = getSearchEngine();
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.start(testIndex, false);
		searchEngy.update(addedDirectories, null);
		
		//Löschen des temporären Textes aus dem indizierten Ordner
		tempFile.delete();
		List<IResult> results = searchEngy.search("temporärer");
		
		//Provozieren der Exception (Das Wort wurde im Index gefunden, der Text ist aber inzwischen gelöscht)
		results.get(0).getContent();		
	}
	
	
	/**
	 * Wie der vorherige Test, aber mit updated Index. Darf keinen 
	 * Fehler mehr schmeissen, sondern das gelöschte Dokument nicht
	 * mehr finden.
	 * @throws SearchEngineException 
	 * @throws IOException 
	 */
	@Test
	public void testRemoveFileFromIndexedDirectory() throws SearchEngineException, IOException{
		//Erstellen eines temporären Textes
		String fileName = "text_temp.txt";
		File tempFile = new File(testTexts,fileName);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
		out.write("Dies hier ist ein temporärer Text in einer temporären Datei. 2");
		out.close();
		
		//Erzeugen des Indexes mit dem temporären Text
		ISearchEngine searchEngy = getSearchEngine();
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.start(testIndex, false);
		searchEngy.update(addedDirectories, null);
		
		//Löschen des temporären Textes aus dem indizierten Ordner
		tempFile.delete();
		
		//Unterschied zum Test oben: Der Index wird aktualisiert
		searchEngy.updateAll();		
		
		//Provozieren der Exception (Das Wort wurde im Index gefunden, der Text ist aber inzwischen gelöscht)
		List<IResult> results = searchEngy.search("temporärer");
		assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + results.size() + ", müsste aber 0 sein, weil \"temporärer\" nur im gelöschten Text vorkam!", results.size()==0);				
	}
	
	
	/**
	 * Testet, ob Dateien, die nach der Indizierung in den indizierten Ordner aufgenommen werden,
	 * nach einem updateAll() im Index gelandet sind.
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws SearchEngineException 
	 * 
	 */
	@Test
	public void testAddFileToIndexedDirectory() throws UnsupportedEncodingException, FileNotFoundException, SearchEngineException{
		File tempFile = null;
		try {
			//Erzeugen des Indexes mit dem temporären Text
			ISearchEngine searchEngy = getSearchEngine();
			List<File> addedDirectories = new Vector<File>();
			addedDirectories.add(testTexts);
			searchEngy.start(testIndex, false);
			searchEngy.update(addedDirectories, null);
			
			//Erstellen eines temporären Textes
			String fileName = "text_temp.txt";
			tempFile = new File(testTexts,fileName);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
			out.write("Dies hier nachträglich eingefügter Text. Hier steht das Wort yyy drin.");
			out.close();
			
			List<IResult> results = searchEngy.search("yyy");
			assertTrue("Die Liste der gefundenen Ergebnisse darf nicht null sein!", results != null);
			assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + results.size() + ", müsste aber 0 sein, weil \"yyy\" noch nicht indiziert sein kann!", results.size()==0);				

			//Unterschied zum Test oben: Der Index wird aktualisiert
			searchEngy.updateAll();		
			
			results = searchEngy.search("yyy");
			assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + results.size() + ", müsste aber 1 sein, weil \"yyy\" durch das Update im Index gelandet sein müsste!", results.size()==1);
		} finally{
			//Löschen des temporären Textes aus dem indizierten Ordner
			tempFile.delete();
		}
	}
	
	/**
	 * Testet, ob Texte, die nach Erstellung des Indexes verändert wurden, 
	 * nach einem updateAll() aktualisiert sind.
	 * @throws SearchEngineException
	 * @throws IOException
	 */
	@Test
	public void testUpdatedText() throws SearchEngineException, Exception{
		File tempFile = null;
		try {
			//Erstellen eines temporären Textes
			String fileName = "text_temp.txt";
			tempFile = new File(testTexts,fileName);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
			out.write("Dies hier ist ein temporärer Text in einer temporären Datei, der gleich verändert wird. ");
			out.close();
			System.out.println(tempFile.lastModified());
			
			//Erzeugen des Indexes mit dem temporären Text
			ISearchEngine searchEngy = getSearchEngine();
			List<File> addedDirectories = new Vector<File>();
			addedDirectories.add(testTexts);
			searchEngy.start(testIndex, false);
			searchEngy.update(addedDirectories, null);
			
			//Verändern des temporären Textes aus dem indizierten Ordner
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
			out.write("Dies hier ist ein neuer temporärer Text in einer temporären Datei, der jetzt verändert ist und das Wort zzz enthält. ");
			out.close();
			
			//Um wirklich sicher zu gehen, dass sich der Zeitpunkt der
			//letzten Änderung sich wirklich verändert
			tempFile.setLastModified(tempFile.lastModified()+1000);
			System.out.println(tempFile.lastModified());
			
			//Noch wird zzz nicht gefunden:
			List<IResult> results = searchEngy.search("zzz");
			assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + results.size() + ", müsste aber 0 sein, weil \"zzz\" noch nicht im Index stehen kann!", results.size()==0);	
			
			//Der Index wird aktualisiert
			searchEngy.updateAll();
			
			//jetzt müsste zzz gefunden werden:
			results = searchEngy.search("zzz");
			assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + results.size() + ", müsste aber 1 sein, weil \"zzz\" durch das Update im Index gelandet sein müsste", results.size()==1);
		} 
		catch(Exception e){
			throw e;
		}
		finally{				
				//Zum Schluss: Löschen der temporären Datei
				tempFile.delete();
		}
	}
	
	/**
	 * Testet, ob der Index gelöscht werden kann.
	 * @throws Exception
	 */
	@Test
	public void testIndexDeletion() throws Exception {
		if(!testIndex.exists()) {
			testIndex.mkdirs();
			File dummyFile = new File(testIndex, "dummy.txt");
			dummyFile.createNewFile();
		}
		ISearchEngine engine = getSearchEngine();
		engine.start(testIndex, false);
		engine.deleteIndex();
		assertTrue("Das Index-Verzeichnis " + testIndex.getAbsolutePath() + " hätte gelöscht werden müssen!", !testIndex.exists());
	}
	
	
	/**
	 * Testet, ob sich der Index importieren (deserialisieren) lässt.
	 * Testet zugleich (als notwendige Voraussetzung), ob sich der
	 * Index exportieren lässt.
	 */
	@Test
	public void testImportIndex() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		File indexDir = new File("test_index");
		searchEngy.start(indexDir, false);
		searchEngy.deleteIndex();
		assertTrue("Das index-Verzeichnis " + indexDir.getAbsolutePath() + " wurde nicht gelöscht!", !indexDir.exists());
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(new File("test_texts"));
		searchEngy.update(addedDirectories, null);
		searchEngy.stop();
		assertTrue(indexDir.exists());
		assertTrue(indexDir.listFiles().length > 0);
		searchEngy = getSearchEngine();
		//Eine "frische" SearchEngine kann nichts finden
		List<IResult> searchResults = searchEngy.search("Test");
		assertTrue("Es wurden " + searchResults.size() + " Ergebnisse gefunden - erwartet wurden 0!", searchResults.size()==0);
		searchEngy.start(indexDir, true);
		//eine gestartete SearchEngine hat ihren Index durch
		//die Serialisierung bekommen 
		searchResults = searchEngy.search("Test");
		assertTrue("Die Anzahl der gefundenen Ergebnisse ist " + searchResults.size() + ", müsste aber 5 sein!", searchResults.size()==5);
	}
	
	/**
	 * Testet, ob die Positionen von gefundenen Wörtern im Text
	 * korrekt geliefert werden.
	 */
	@Test
	public void testTokenPositions() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(new File("test_texts/Innerer_Ordner"));
		searchEngy.start(testIndex, false);
		searchEngy.update(addedDirectories, null);
		List<IResult> searchResults = searchEngy.search("Dies");
		IResult firstR = searchResults.get(0);
		Map<Integer,Integer> positions = firstR.getHighlightedPositions();
		Set<Integer> startPos = positions.keySet();
		assertTrue("Anzahl der Fundstellen muss 1 sein, ist aber " + startPos.size() + "!", startPos.size()==1);
		for (Integer sp : startPos) {
			assertTrue("Anfang des Wortes muss 0 sein, ist aber " + sp + "!", sp.equals(0));
			assertTrue(positions.get(sp).equals(3));
		}		
		searchResults = searchEngy.search("weiterer");
		firstR = searchResults.get(0);
		positions = firstR.getHighlightedPositions();
		startPos = positions.keySet();
		assertTrue("Anzahl der Fundstellen muss 1 sein, ist aber " + startPos.size() + "!", startPos.size()==1);
		for (Integer sp : startPos) {
			assertTrue("Anfang des Wortes muss 4 sein, ist aber " + sp + "!", sp.equals(4));
			assertTrue(positions.get(sp).equals(11));
		}
	}
	
	/**
	 * Testet, ob die exakte Phrasensuche (Suchanfragen mit "") 
	 * korrekte Ergebnisse liefert.
	 */
	@Test
	public void testExactPhrasesSearch() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		searchEngy.start(testIndex, false);
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.update(addedDirectories, null);
		List<IResult> searchResults = searchEngy.search("gleicher Wortfolgen");
		// Ohne Phrasensuche müssten zwei Ergebnisse geliefert werden: 
		// text1 und text2 
		assertTrue("Anzahl der Fundstellen muss 2 sein, ist aber " + searchResults.size(), searchResults.size()==2);
		searchResults = searchEngy.search("\"gleicher Wortfolgen\"");
		// Mit Phrasensuche darf nur noch text2 geliefert werden. 
		assertTrue("Anzahl der Fundstellen muss 1 sein, ist aber " + searchResults.size(), searchResults.size()==1);
	}	
	
	/**
	 * Testet, ob die Ergebnisse korrekt sortiert sind.
	 */
	@Test
	public void testIsCorrectSorted() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		searchEngy.start(testIndex, false);
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.update(addedDirectories, null);
		List<IResult> searchResults = searchEngy.search("gleicher Wortfolgen");
		// text2 muss vor text1 in der Liste stehen, 
		// da dort die Terme öfter vorkommen. 
		assertTrue("Anzahl der Fundstellen muss 2 sein, ist aber " + searchResults.size(), searchResults.size()==2);
		int topResultTokenCount = searchResults.get(0).getHighlightedPositions().size();
		int secondResultTokenCount = searchResults.get(1).getHighlightedPositions().size();
		assertTrue(topResultTokenCount > secondResultTokenCount);		
	}
	
	/**
	 * Testet durch unterschiedliche Anfagen, ob der Index
	 * korrekt erstellt wurde.
	 */
	@Test
	public void testCorrectTokenizing() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		searchEngy.start(testIndex, false);
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.update(addedDirectories, null);
		
		//Ein Test, ob Wörter mit Umlauten gefunden werden:
		List<IResult> searchResults = searchEngy.search("vernünftig");
		assertTrue("Es hätten Vorkommen gefunden werden müssen, die Liste ist aber leer!", !searchResults.isEmpty());
		
		//Ein Test, ob Wörter auch gefunden werden, wenn Sie klein geschrieben gesucht werden,
		//obwohl sie nur groß geschrieben in den Texten vorkommen: 
		searchResults = searchEngy.search("wiederkehr");
		assertTrue("Es hätten Vorkommen gefunden werden müssen, die Liste ist aber leer!", !searchResults.isEmpty());
		
		
		// text5 endet mit einem Buchstaben. Letzte Wörter werden 
		// beim Tokenisieren gerne vergessen, deshalb dieser Text
		searchResults = searchEngy.search("endet");
		assertTrue("Es hätten Vorkommen gefunden werden müssen, die Liste ist aber leer!", !searchResults.isEmpty());
		
		// Wörter, die nicht vorkommen, sollen auch nicht gefunden
		// werden. Wenn nichts gefunden wird, soll einfach nichts
		// zurückgegeben werden und nicht etwas eine Exception 
		// geworfen werden (NullPointer ist hier sehr beliebt)
		searchResults = searchEngy.search("garantiertNichtDrin");
		assertTrue("Es hätten keine Vorkommen gefunden werden dürfen, die Liste enthält aber " + searchResults.size() + " Vorkommen!", searchResults.isEmpty());
	}
	
	/**
	 * Testet, ob nur Text-Dateien indexiert wurden, indem
	 * nach einem String gesucht wird, der ausschließlich in einer
	 * Nicht-Text-Datei im Indexverzeichnis enthalten ist.
	 * @throws SearchEngineException
	 */
	@Test
	public void testTextFileSelection() throws SearchEngineException {
		ISearchEngine searchEngy = getSearchEngine();
		searchEngy.start(testIndex, false);
		List<File> addedDirectories = new Vector<File>();
		addedDirectories.add(testTexts);
		searchEngy.update(addedDirectories, null);
		List<IResult> searchResults = searchEngy.search("gnurx");
		assertTrue("Das Programm indexiert auch Nicht-Text-Dateien!",searchResults.size() == 0);
	}
	

}
