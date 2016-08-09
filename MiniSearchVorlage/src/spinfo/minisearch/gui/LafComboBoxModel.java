package spinfo.minisearch.gui;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListDataListener;

/**
 * Ein {@link ComboBoxModel} für das Popup-Menu ({@link JComboBox})
 * des Einstellungs-Dialogs, das die vorhandenen "Look and Feel"-Installationen
 * als Daten bereithält.
 *
 */
public class LafComboBoxModel implements ComboBoxModel {
	
	private LookAndFeelInfo[] lookAndFeels;
	private LookAndFeelInfo selected;
	
	
	public LafComboBoxModel() {
		this.lookAndFeels = UIManager.getInstalledLookAndFeels();
	}

	public Object getSelectedItem() {
		return selected;
	}

	public void setSelectedItem(Object anItem) {
		for (LookAndFeelInfo info : lookAndFeels) {
			if(info.equals(anItem)) {
				selected = info;
				break;
			}
		}
	}

	public void addListDataListener(ListDataListener l) {}

	public Object getElementAt(int index) {
		return lookAndFeels[index];
	}

	public int getSize() {
		return lookAndFeels.length;
	}

	public void removeListDataListener(ListDataListener l) {}

}
