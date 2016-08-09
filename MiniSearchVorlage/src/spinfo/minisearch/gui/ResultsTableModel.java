package spinfo.minisearch.gui;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import spinfo.minisearch.interfaces.IResult;

/**
 * Eine {@link TableModel}-Implementation für die Tabelle, in der die Dokumente, die
 * den Suchbegriff enthalten, angezeigt werden.
 * 
 */
public class ResultsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 8305912357104764241L;

	/**
	 * Liste der gefundenen Dokumente
	 */
	private List<IResult> documents;
	
	/**
	 * Zur Darstellung der letzten Änderung einer Datei benötigtes {@link DateFormat}.
	 */
	private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	/**
	 * Initialisierung mit der Liste der gefundenen Dokumente
	 * @param documents
	 */
	public ResultsTableModel(List<IResult> documents) {
		this.documents = documents;
	}

	/**
	 * Anzahl der Spalten (immer 4) wird zurückgegeben.
	 */
	public int getColumnCount() {
		return 4;
	}

	/**
	 * Anzahl der Zeilen - entspricht der Anzahl der Dokumente, die im
	 * Konstruktor übergeben wurde.
	 */
	public int getRowCount() {
		if(documents == null) return 0;
		return documents.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		IResult doc = documents.get(rowIndex);
		switch(columnIndex) {
		case 0: return doc.getAbsoluteDocumentPath();
		case 1: return doc.getNumberOfOccurrences();
		case 2: return doc.getSize();
		case 3: Date d = new Date(doc.getLastModified()); return df.format(d);
		default: return null;
		}
	}

	/**
	 * Gibt die Spaltenüberschriften ("Dokument", "Fundstellen" usw.)
	 * zurück.
	 */
	@Override
	public String getColumnName(int column) {
		switch(column) {
			case 0: return "Dokument";
			case 1: return "Fundstellen";
			case 2: return "Größe";
			case 3: return "Letzte Änderung";
			default: return null;
		}
	}

	/**
	 * Gibt das grade ausgewählte Dokument zurück. Der Index
	 * <code>selection</code> muss innerhalb des gültigen Bereichs liegen.
	 * @param selection Index des gewünschten Dokuments
	 * @return das Dokument
	 */
	public IResult getDocument(int selection) {
		return documents.get(selection);
	}

}
