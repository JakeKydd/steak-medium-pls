package spinfo.minisearch.gui;

import java.io.File;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Ein {@link TableModel}, dass einer {@link JTable}-Tabelle dazu dient,
 * eine Liste von {@link File}-Objekten als Model zu verwenden.
 * Intern wird eine {@link List}-Instanz verwendet, um die relevanten
 * Anfragen weiterzuleiten. Wird die Liste modifiziert, genügt ein Aufruf
 * von {@link FileTableModel#update()}, um das Model zu aktualisieren.
 * 
 * Diese Klasse wird verwendet, um die Liste der Verzeichnisse, die
 * indexiert werden sollen, darzustellen.
 * 
 */
public class FileTableModel extends AbstractListModel {

	private static final long serialVersionUID = -7663512598995388336L;

	private List<File> files;

	public FileTableModel(List<File> files) {
		this.files = files;
	}

	public Object getElementAt(int index) {
		return files.get(index);
	}

	public int getSize() {
		return files.size();
	}

	public void update() {
		super.fireIntervalAdded(this, 0, files.size());
	}
	
	
}
