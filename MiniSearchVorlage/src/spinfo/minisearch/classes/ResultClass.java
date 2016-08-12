package spinfo.minisearch.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.util.Preferences;

public class ResultClass implements IResult{

	private static final long serialVersionUID = -4984707036913993459L;
	
	private static ResultClass instance;
	
	private File FileSelected = null;
		
	private File filename;
	
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
	 * setFilename
	 * weist der Variablen filename einen Wert zu
	 * @param filename Dateiname
	 */
	public void setFilename(File filename) {
		this.filename = filename;
	}

	@Override
	public int compareTo(IResult arg0) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public int getNumberOfOccurrences() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getContent() throws SearchEngineException {
		// TODO Auto-generated method stub
		
		String txt_inhalt = null;
		
		try {
			txt_inhalt = getText(FileSelected, PredefinedVars.ENCODING);
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
		return txt_inhalt;
	}

	@Override
	public Map<Integer, Integer> getHighlightedPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAbsoluteDocumentPath() {
		// This will change to the file object given by the IResult Object
		//File file = FileSelected;

		//System.out.println("Absoluter Pfad : " + file.getAbsolutePath());
		//return FileSelected.getAbsolutePath();
		return this.filename.getAbsolutePath();
		
	}

	@Override
	public long getLastModified() {
		// TODO Auto-generated method stub
		// This will change to the file object given by the IResult Object
		//File file = FileSelected;

		//System.out.println("Zuletzt geändert : " + file.lastModified());
		//return file.lastModified();
		return this.filename.lastModified();
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		// This will change to the file object given by the IResult Object
		//File file = FileSelected;

		//System.out.println("Datengröße in bytes : " + file.length());
		//return file.length();
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
