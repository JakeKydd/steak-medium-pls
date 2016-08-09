package spinfo.minisearch.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import spinfo.minisearch.gui.Settings;

/**
 * Diese Klasse verwaltet die Einstellungen, die im Settings-Dialog {@link Settings} vorgenommen
 * werden können. Die Einstellungen werden in der Datei "minisearch.ini" gespeichert, die ggf.
 * neu angelegt wird.
 * {@link Preferences}  realisiert das "Singleton"-Design Pattern, wodurch Sie in Ihrem Code
 * an beliebiger Stelle auf die Einstellungen zugreifen können - siehe auch
 * http://de.wikipedia.org/wiki/Singleton_(Entwurfsmuster)
 * 
 */
public class Preferences {
	
	/**
	 * Singleton-Variable
	 */
	private static Preferences instance;
	
	private static final String AUTO_LOAD_DB = "auto.load.database";
	private static final String SEARCH_DIRECTORIES = "search.directories";
	private static final String COLOR = "highlight.color";
	
	private Preferences() {
		try {
			loadProperties();
		} catch (IOException x) {
			
		}
	}
	
	/**
	 * Zugriff auf die Singleton-Preferences-Instanz durch eine statische Methode.
	 * Existiert noch kein Preferences-Objekt, wird ein neues erzeugt. Bei weiteren
	 * Aufrufen der Methode wird stets dieses Objekt zurückgegeben.
	 * @return die Singleton-Instanz von {@link Preferences}
	 */
	public static Preferences getInstance() {
		if(instance == null) {
			instance = new Preferences();
		}
		return instance;
	}
	
	private Properties properties = new Properties();
	
	private File propsFile = new File("minisearch.ini");
	
	private void loadProperties() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propsFile));
		properties.load(bis);
		bis.close();
	}
	
	public void storeProperties() throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(propsFile));
		properties.store(bos, "Auto-generated properties, do not modify!");
		bos.close();
	}
	
	/**
	 * Lädt die zuletzt gespeicherten Einstellungen.
	 */
	public void resetProperties() {
		try {
			loadProperties();
		} catch (IOException x) {
		}
	}
	
	/**
	 * Gibt zurück, ob die Datenbank beim Starten automatisch geladen werden soll oder nicht.
	 * Default-Wert ist false.
	 * @return true oder false
	 */
	public boolean autoLoadDatabase() {
		String value = properties.getProperty(AUTO_LOAD_DB, "false");
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * Gibt die Liste der Verzeichnisse, die indexiert werden sollen, zurück. Default-Wert
	 * ist das Verzeichnis "texts" im Programmverzeichnis.
	 * @return Liste der Verzeichnisse
	 */
	public List<File> getDirectories() {
		String value = properties.getProperty(SEARCH_DIRECTORIES, "texts");
		String[] fileNames = value.split(";");
		List<File> files = new ArrayList<File>();
		for(int i = 0; i < fileNames.length; i++) {
			files.add(new File(fileNames[i]));
		}
		return files;
	}
	
	/**
	 * Diese Methode wird von der Klasse {@link Settings} aufgerufen.
	 * @param value
	 */
	public void setAutoLoadDatabase(boolean value) {
		properties.setProperty(AUTO_LOAD_DB, Boolean.toString(value));
	}
	
	/**
	 * Setzt die Liste der Verzeichnisse, die indexiert werden sollen.
	 * Diese Methode wird von der Klasse {@link Settings} aufgerufen.
	 * @param directories
	 */
	public void setDirectories(File... directories) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < directories.length; i++) {
			buffer.append(directories[i].getAbsolutePath());
			if(i < directories.length-1) {
				buffer.append(";");
			}
		}
		properties.setProperty(SEARCH_DIRECTORIES, buffer+"");
	}

	/**
	 * Gibt die Farbe, die für die Markierung von Fundstellen
	 * genutzt wird, zurück. Default ist {@link Color#yellow}.
	 * @return Farbe für markierte Textstellen
	 */
	public Color getColor() {
		String colorString = properties.getProperty(COLOR);
		if(colorString == null) {
			return Color.yellow;
		}
		return new Color(Integer.parseInt(colorString));
	}

	/**
	 * Setzt die Farbe, mit der Markierungen unterlegt
	 * werden sollen. Wird von der Klasse {@link Settings} 
	 * aufgerufen.
	 * @param newColor
	 */
	public void setColor(Color newColor) {
		properties.setProperty(COLOR, newColor.getRGB()+"");
	}
	

}
