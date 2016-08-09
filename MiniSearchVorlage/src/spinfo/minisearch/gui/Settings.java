/*
 * Settings.java
 *
 * Created on 16. Juni 2008, 12:36
 */

package spinfo.minisearch.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import spinfo.minisearch.util.Preferences;

/**
 * Einstellungsdialog des Programms.
 */
public class Settings extends javax.swing.JDialog {
	
	private static final long serialVersionUID = -1687125291752031235L;
	
	private List<File> files = new Vector<File>();
	
	private List<File> oldSelection = new Vector<File>();
	
	private FileTableModel model;
	
	public static final int OK = 1, CANCEL = -1;
	
	private int result = CANCEL;
	
	private JFrame mainFrame;
	
    /** Creates new form Settings */
    public Settings(JFrame parent) {
    	super(parent, "Einstellungen", true);
    	 this.mainFrame = parent;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "serial" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	UIManager.installLookAndFeel("Napkin", NapkinLookAndFeel.class.getName());
        tabbedPane = new javax.swing.JTabbedPane();
        directories = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        general = new javax.swing.JPanel();
        loadDbButton = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        lafCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        colorButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        model = new FileTableModel(files);
        jTable1.setModel(model);
        jTable1.setCellRenderer(new DefaultListCellRenderer() {

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setText((((File)value).getAbsolutePath()));
				return label;
			}
        	
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Hinzufügen");
        jButton1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);
				int choice = fileChooser.showOpenDialog(Settings.this);
				if(choice == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser.getSelectedFiles();
					for (File f : files) {
						if(!Settings.this.files.contains(f)) {
							Settings.this.files.add(f);
						}
					}
					model.update();
				}
			}
        	
        });

        jButton2.setText("Entfernen");
        jButton2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO: Etwas gehackt...
				int[] rows = jTable1.getSelectedIndices();
				List<File> toRemove = new ArrayList<File>();
				for (int row : rows) {
					try {
						toRemove.add(files.get(row));
					} catch (RuntimeException e1) {
					}
				}
				files.removeAll(toRemove);
				model.update();
			}
        	
        });

        org.jdesktop.layout.GroupLayout directoriesLayout = new org.jdesktop.layout.GroupLayout(directories);
        directories.setLayout(directoriesLayout);
        directoriesLayout.setHorizontalGroup(
            directoriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(directoriesLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(directoriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        directoriesLayout.setVerticalGroup(
            directoriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(directoriesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2)
                .addContainerGap(248, Short.MAX_VALUE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Verzeichnisse", directories);

        loadDbButton.setText("Datenbank beim Start automatisch laden");
        loadDbButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDbButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Look and Feel:");

        lafCombo.setModel(new LafComboBoxModel());
        lafCombo.setRenderer(new LafRenderer());
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if(info.getClassName().equals(UIManager.getSystemLookAndFeelClassName())) {
				lafCombo.setSelectedItem(info);
				break;
			}
		}
        
        lafCombo.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					LookAndFeelInfo info = (LookAndFeelInfo) e.getItem();
					try {
						UIManager.setLookAndFeel(info.getClassName());
						SwingUtilities.updateComponentTreeUI(mainFrame);
						SwingUtilities.updateComponentTreeUI(Settings.this);
						mainFrame.pack();
						Settings.this.pack();
					} catch (Exception x) {
						x.printStackTrace();
					} 
				}
			}
        	
        });
        
        jLabel2.setText("Hervorgehobener Text:");

        colorButton.setText("Auswählen");
        colorButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(Settings.this, "Farbe auswählen", Preferences.getInstance().getColor());
				if(newColor != null) {
					Preferences.getInstance().setColor(newColor);
				}
			}
        	
        });

        org.jdesktop.layout.GroupLayout generalLayout = new org.jdesktop.layout.GroupLayout(general);
        general.setLayout(generalLayout);
        generalLayout.setHorizontalGroup(
            generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(generalLayout.createSequentialGroup()
                .addContainerGap()
                .add(generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(loadDbButton)
                    .add(generalLayout.createSequentialGroup()
                        .add(generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 126, Short.MAX_VALUE)
                        .add(generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(colorButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lafCombo, 0, 144, Short.MAX_VALUE))))
                .addContainerGap(202, Short.MAX_VALUE))
        );
        generalLayout.setVerticalGroup(
            generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(generalLayout.createSequentialGroup()
                .add(17, 17, 17)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(loadDbButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(16, 16, 16)
                .add(generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(lafCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(generalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(colorButton))
                .addContainerGap(153, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Allgemein", general);

        applyButton.setText("Übernehmen");
        applyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					Preferences.getInstance().setAutoLoadDatabase(loadDbButton.isSelected());
					File[] f = new File[files.size()];
					files.toArray(f);
					Preferences.getInstance().setDirectories(f);
					Preferences.getInstance().storeProperties();
					result = OK;
					setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        	
        });

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(408, Short.MAX_VALUE)
                .add(cancelButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(applyButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(applyButton)
                    .add(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void loadDbButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDbButtonActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_loadDbButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
	setVisible(false);
}//GEN-LAST:event_cancelButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton colorButton;
    private javax.swing.JPanel directories;
    private javax.swing.JPanel general;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList jTable1;
    private javax.swing.JComboBox lafCombo;
    private javax.swing.JCheckBox loadDbButton;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    @Override
	public void setVisible(boolean visible) {
    	if(visible) {
    		Preferences.getInstance().resetProperties();
    		oldSelection.addAll(Preferences.getInstance().getDirectories());
    		loadDbButton.setSelected(Preferences.getInstance().autoLoadDatabase());
    		files.addAll(Preferences.getInstance().getDirectories());
    		model.update();
    	}
		super.setVisible(visible);
	}
    
    public List<File> getAddedDirs() {
    	List<File> toReturn = new Vector<File>();
    	toReturn.addAll(files);
    	toReturn.removeAll(oldSelection);
    	return toReturn;
    }
    
    public List<File> getRemovedDirs() {
    	List<File> toReturn = new Vector<File>();
    	toReturn.addAll(oldSelection);
    	toReturn.removeAll(files); System.out.println(files);
    	return toReturn;
    }

	public int getResult() {
		return result;
	}

    
}
