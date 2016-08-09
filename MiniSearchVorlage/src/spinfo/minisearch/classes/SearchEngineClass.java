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
import java.util.HashSet;
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
	
	public List<String> indexedWords;
	
	
	
	Preferences prefs = Preferences.getInstance();
	
	private final static String ENCODING = "UTF-8";
		
	
	/**
	 * Constructor of SearchEngineClass - overwrites the default constructor
	 */
	public SearchEngineClass() {
		
	}

	//hallo
	@Override
	public List<IResult> search(String text) {
		// TODO Auto-generated method stub
		System.out.println("\n Suche gestartet: "+ indexedWords.size()+ " " + text);
		
		// Prüfe, ob das eine Phrasensuche ist oder nicht
		if(text.contains("\"")){
			System.out.println("\n Suche ist Phrase ");
		}else{
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
		
		//this.includedFiles = new ArrayList<>();
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
		
		//System.out.println("\n" + includedFiles + includedFiles.size());
		return includedFiles;
	}
	
	
	private List<String> getIndexedTokens(List<File> files) throws IOException {
		
		List<String> words = new ArrayList<String>();
		
		
		for (int i=0; i < files.size(); i++) {
			
			String text = getText(files.get(i), this.ENCODING); //System.out.println(text);		

			// Wir holen eine Instanz eines BreakIterators, der auf Wortebene den
			// Eingabetext "text" tokenisiert und setzen den sprachlichen Kontext.
			BreakIterator iterator = BreakIterator.getWordInstance(Locale.GERMAN);
			iterator.setText(text); // Wir setzen den zu scannenden Text
			int start = iterator.first();
			int end = iterator.next();
			
			while (end != BreakIterator.DONE) {
				String subStringFromText = text.substring(start, end);
					
				words.add(subStringFromText);
					
				start = end;
				end = iterator.next();
			}
		    
		} //System.out.println("\n" + words);		
		
		return words;
	}
	
	
	private static String getText(File file, String encoding)
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
