package spinfo.minisearch.gui;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Hilfsklasse, die {@link LookAndFeelInfo}-Objekte im Einstellungs-
 * Dialog des Programms als {@link JLabel} rendert, so dass die {@link JComboBox},
 * in der sich ein "Look and Feel" auswählen lässt, dargestellt werden kann.
 *
 */
public class LafRenderer implements ListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		LookAndFeelInfo info = (LookAndFeelInfo) value;
		return new JLabel(info.getName());
	}

}
