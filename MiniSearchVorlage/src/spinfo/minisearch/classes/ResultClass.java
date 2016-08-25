package spinfo.minisearch.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.util.Preferences;

public class ResultClass implements IResult{

	private static final long serialVersionUID = -4984707036913993459L;
	
	private static ResultClass instance;
	
		
	private File filename;
	
	private String search;
	
	private String text_inhalt;
	
	/**
	 * constructor with parameter filename
	 * @param filename
	 */
	public ResultClass(File filename) {
		this.filename = filename;		
	}

	// empty constructor
	public ResultClass() {		
	}
	
	/**
	 * Singleton-Instanz
	 * @return die Singleton-Instanz von {@link ResultClass}
	 */
	public static ResultClass getInstance() {
		if(instance == null) {
			instance = new ResultClass();
		}
		return instance;
	}
	
	/**
	 * getFilename
	 * Gibt den Dateinamen zurück
	 * @return  filename
	 */
	public File getFilename() {
		return filename;
	}
	/**
	 * getSearchKeyword
	 * Gibt das Suchwort zurück
	 * @return  search
	 */
	public String getSearchKeyword() {
		return search;
	}
	/**
	 * getTextInhalt
	 * Gibt den gefundenen Inhalt zurück
	 * @return  text_inhalt
	 */
	public String getTextInhalt() {
		return text_inhalt;
	}

	/**
	 * setFilename
	 * weist der Variablen filename einen Wert zu
	 * @param filename Dateiname
	 */
	public void setFilename(File filename) {
		this.filename = filename;
	}
	/**
	 * setSearchKeyword
	 * weist der Variablen search einen Wert zu
	 * @param search Suchterm
	 */
	public void setSearchKeyword(String search) {
		this.search = search;
	}
	/**
	 * setTextInhalt
	 * weist der Variablen text_inhalt den Inhalt der Textdatei zu
	 * @param text_inhalt Textueller Inhalt
	 */
	public void setTextInhalt(String text_inhalt) {
		this.text_inhalt = text_inhalt;
	}

	@Override
	public int compareTo(IResult arg0) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public int getNumberOfOccurrences() {
		// TODO Auto-generated method stub
		Integer Occurence = 0;
		
		try {
			
			String txt_inhalt = getText(this.filename,PredefinedVars.ENCODING);
			String[] splitted = txt_inhalt.toLowerCase().split("\\s+");
			for (int i=0; i < splitted.length; i++) {
					
				if (splitted[i].contains(this.search.toLowerCase())) {
					Occurence++;				
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Occurence;
	}

	@Override
	public String getContent() throws SearchEngineException {
		// TODO Auto-generated method stub
		
		this.text_inhalt = null;
		String txt_inhalt = null;
		
		try {
			this.text_inhalt = getText(this.filename, PredefinedVars.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new SearchEngineException("Encoding nicht unterstützt!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		txt_inhalt = this.text_inhalt;
		return txt_inhalt;
	}

	@Override
	public Map<Integer, Integer> getHighlightedPositions() {
		// TODO Auto-generated method stub		
		List<Integer> MarkBeginn = new ArrayList<>();
		List<Integer> MarkEnd = new ArrayList<>();
		
						
		for (int i = -1; (i = this.text_inhalt.toLowerCase().indexOf(this.search.toLowerCase(), i + 1)) != -1; ) {
		    MarkBeginn.add(i);
		    MarkEnd.add(i + this.search.toLowerCase().length()-1);		   
		}  

		Map<Integer, Integer> Highlights = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < MarkBeginn.size(); i++) {			
				Highlights.put(MarkBeginn.get(i),MarkEnd.get(i));			
		}
		return Highlights;
	}

	@Override
	public String getAbsoluteDocumentPath() {
		
		return this.filename.getAbsolutePath();
		
	}

	@Override
	public long getLastModified() {
		
		return this.filename.lastModified();
	}

	@Override
	public long getSize() {
		
		return this.filename.length();
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
