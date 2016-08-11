package spinfo.minisearch.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.*;
import spinfo.minisearch.util.Preferences;
import spinfo.minisearch.gui.*;
import spinfo.minisearch.exeption.*;

public class SearchEngineClass implements ISearchEngine {
	
	private File indexDir = null;
	
	public List<File> includedFiles = new ArrayList<>();
	
	//public List<String> indexedWords;
	public HashMap<String, File> indexedWords;
	
	
	Preferences prefs = Preferences.getInstance();	
			
	
	/**
	 * Constructor of SearchEngineClass - overwrites the default constructor
	 */
	public SearchEngineClass() {
		
	}

	
	@Override
	public List<IResult> search(String text) {
		// TODO Auto-generated method stub
		System.out.println("\n Suche gestartet: "+ indexedWords.size() + " " + text);
		
		// Prüfe, ob das eine Phrasensuche ist oder nicht
		if(text.contains("\"")){
			System.out.println("\n Suche ist Phrase ");
			
		} else {
			// Hello 
			// Split each word from textfield {@link AbstractSearchEngineFrame#searchField} , use blank spaces 
			// as delimiter
			// @see: http://stackoverflow.com/questions/7899525/how-to-split-a-string-by-space			
			String[] splitted = text.split("\\s+");
			for(int i=0; i < splitted.length; i++) {
				System.out.println(splitted[i]);
				
			}
			System.out.println("\n Suche ist Keyword ");
			
		}
		return null;
	}

	@Override
	public void updateAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(List<File> addedDirectories, List<File> removedDirectories) throws SearchEngineException {
		
				
	}

	/**
	 * Liefert den Namen des Programms zurück im Menü unter "über dieses Programm"
	 * @return  Name des Programms
	 */
	@Override
	public String getProgramName() {		
		
		return this.getClass().getSimpleName();
	}

	/**
	 * Gibt den Text zurück, der in der About-Message unter "über dieses Programm" ausgegeben wird
	 * @return  AboutMessage-Text
	 */
	@Override
	public String getAboutMessage() {
		
		return PredefinedVars.ABOUT_MESSAGE;
	}

	
	/**
	 * Methode wird aufgerufen, wenn das Programm gestartet wird. Es wird das Index-Verzeichnis
	 * übergeben, in dem alle Dateien des Index enthalten sind, und ein boolescher Wert, der angibt,
	 * ob der Index gespeichert werden soll oder nicht.
	 * @param indexDir Das Index-Verzeichnis
	 * @param deserializeIndex Soll der Index geladen werden?
	 */
	@Override
	public void start(File indexDir, boolean deserializeIndex) throws SearchEngineException {
		
		// aktuelle Directories aus den Settings		
		List<File> directories = prefs.getDirectories();
		
		List<File> files = new ArrayList<File>();
		String filepath = "";
		
		
		//Prüfe, ob Verzeichnis nicht leer(null) ist
		if (directories != null) {
			for (File datei : directories) {
				
				filepath = datei.getAbsolutePath();
				this.indexDir = new File(filepath);	
				
				files = getFilesFromIndex(includedFiles,datei);
				
			}
			
			//Alle Duplikate und leere Elemente aus der Liste rauswerfen 
			Set<File> DD = new HashSet<>();
			DD.addAll(files);
			files.clear();
			files.addAll(DD); 
			System.out.println("files size "+files.size());
			
			try {
				this.indexedWords = getIndexedTokens(files);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			printStackTrace("Es sind keine Index-Verzeichnisse gesetzt.");
		}		 
			
		
	}
	

	@Override
	public void stop() throws SearchEngineException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteIndex() throws SearchEngineException {
		
		this.indexDir = null;
		this.includedFiles.clear();
	}
	
	
	// ----------------------  additional methods for SearchEngineClass ------------------------------//
	
	private void printStackTrace(String excMessage) {
		System.err.println(excMessage);		
	}
	
	/**
	 * Diese Methode durchsucht das Index-Verzeichnis und liefert alle
	 * Dateien, die darin vorkommen zurück als Liste. Die Ordner werden rekursiv durchsucht.
	 * @param  directory  Index-Verzeichnis
	 * @return includedFiles  alle Dateien die im Index-Verzeichnis sind
	 */
	public List<File> getFilesFromIndex(List<File> includedFiles,File directory) {
		
		File[] files = directory.listFiles();
				
		if (files != null) {
			for (int i = 0; i < files.length; i++) {				
				
				if (files[i].isDirectory()) {					 
					// recursive call
					getFilesFromIndex(includedFiles,files[i]);			
					
				}
				
				if (files[i].getName().endsWith(".txt")) {			
					this.includedFiles.add(files[i]);
					System.out.println("\n" + files[i].getName() +" added. ");
				}				
				
			}				
			
		}	
		
		
		return includedFiles;
	}
	
	
	private HashMap<String, File> getIndexedTokens(List<File> files) throws IOException {
		
		//List<String> words = new ArrayList<String>();
		HashMap<String, File> words = new HashMap<>();
		
		
		for (int i=0; i < files.size(); i++) {
			
			String text = getText(files.get(i), PredefinedVars.ENCODING);		

			// Eine Instanz des BreakIterators, der auf Wortebene den
			// Text tokenisiert und den sprachlichen Kontext setzt.
			BreakIterator iterator = BreakIterator.getWordInstance(Locale.GERMAN);
			iterator.setText(text); 
			int start = iterator.first();
			int end = iterator.next();
			
			while (end != BreakIterator.DONE) {
				String subStringFromText = text.substring(start, end);
					
				//words.add(subStringFromText);
				words.put(subStringFromText, files.get(i));
					
				start = end;
				end = iterator.next();
			}
		} 	
		for (String key : words.keySet()) {
			System.out.println("Key:" + key + "\n Value: " + words.get(key));
			
		}
		
				
		return words;
	}
	
	/**
	 * Mit der Methode getText wird der Inhalt einer Datei ausgelesen
	 * 
	 * @param file   Dateiname
	 * @param encoding  
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getText(File file, String encoding)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		
		//mit dem BufferedReader die Datei durchlaufen
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding));
		StringBuffer stringBuffer = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			stringBuffer.append(line).append(System.lineSeparator());
		}
		br.close();
		return stringBuffer.toString();
	}

}
