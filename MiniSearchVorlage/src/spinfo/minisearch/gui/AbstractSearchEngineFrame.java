/*
 * AbstractSearchEngineFrame.java
 *
 * Created on 12. Juni 2008, 16:44
 */

package spinfo.minisearch.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.IResult;
import spinfo.minisearch.interfaces.ISearchEngine;

/**
 * 
 * Hauptfenster der graphischen Oberfläche, das mit dem GUI-Editor von Netbeans
 * erstellt und anschließend neu strukturiert wurde, damit es halbwegs übersichtlich
 * ist. Sie müssen sich den Code dieser Klasse nicht ansehen, dürfen es aber natürlich
 * gerne.
 * Da diese Klasse bereits sehr viel Code enthält, der für die GUI enötigt wird,
 * sind die "eigentlich wichtigen" Methoden, die mit Ihren Klassen interagieren,
 * hier nur abstrakt definiert. Die Implementation dieser Methoden finden Sie in
 * der Klasse {@link SearchEngineFrame}.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractSearchEngineFrame extends javax.swing.JFrame {
   
   private ImageIcon cutIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/editcut.png"));
   private ImageIcon copyIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/editcopy.png"));
   private ImageIcon pasteIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/editpaste.png"));
   private ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/editdelete.png"));
   private ImageIcon searchIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/search.png"));
   private ImageIcon prefsIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/configure.png"));
   private ImageIcon updateIcon = new ImageIcon(getClass().getResource("/spinfo/minisearch/gui/icons/db_update.png"));
   

   private JMenuItem aboutItem;
   private JMenuItem configureItem;
   private JMenuItem copyItem;
   private JMenuItem cutItem;
   private JMenuItem deleteItem;
   private JLabel detailsLabel;
   private JLabel dokumentsLabel;
   private JMenu editMenu;
   private JMenu fileMenu;
   private JMenu helpMenu;
   private JButton prefsButton;
   private JButton cutButton;
   private JButton copyButton;
   private JButton pasteButton;
   private JButton deleteButton;
   private JButton updateButton;
   private JSeparator jSeparator1;
   private JToolBar.Separator jSeparator2;
   private JToolBar.Separator jSeparator3;
   private JToolBar toolbar;
   private JMenuItem javaDocItem;
   private JMenuBar menuBar;
   private JMenuItem pasteItem;
   protected JProgressBar progressBar;
   private JMenuItem quitItem;
   protected JTextPane resultDetails;
   private JPanel resultDetailsPanel;
   private JScrollPane resultDetailsScrollPane;
   protected JTable resultsTable;
   private JPanel resultsTablePanel;
   private JScrollPane resultsTableScrollPane;
   private JButton searchButton;
   protected JTextField searchField;
   private JPanel searchPanel;
   private JMenuItem selectAllItem;
   private JSeparator separator;
   private JSplitPane splitPane;
   protected JLabel summaryLabel;
   private JMenuItem updateNowItem;
      
   private Browser browser;
   
   protected ISearchEngine searchEngine;
   
   ///////////////////////////// Schnittstelle zur Anwendungslogik /////////////////////////////////

   
   protected abstract void search(String text);
   
   protected abstract void update();
   
   public abstract void setDetails(IResult details);
   
   protected abstract void setResults(List<IResult> documents);
   
   public abstract void setMaximumWork(int maxWork);
   
   public abstract void setWorkDone(int workDone);
   
   protected abstract void resultsTableSelectionChanged(int selection);
   
   public abstract void startInfiniteProgressBar();
	
   public abstract void stopProgressBar();
	
   private JTextComponent focusComponent = null;
   
   public void setShowToolBarButtonText(boolean display) {
   
   		JButton[] buttons = new JButton[] {prefsButton, cutButton, copyButton, pasteButton, deleteButton, updateButton};
   		for (JButton button : buttons) {
   			if(display) {
   				button.setText(button.getAction().getValue(Action.NAME).toString());
			} else {
				button.setText(null);
   		}
   	}
   }
   
   
/////////////////// Actions: /////////////////////
       
   // Actions repräsentieren Aktionen, die z.B. hinter der Aktivierung eines
   // Buttons oder eines Menupunktes stehen können. Der Vorteil gegenüber 
   // ActionListenern besteht darin, dass eine Action für mehrere GUI-Elemente
   // benutzt werden kann (hier: Toolbar und Menus), was den Programmcode
   // etwas vereinfacht.
   
    private Action cutAction = new AbstractAction("Ausschneiden", cutIcon) {

        public void actionPerformed(ActionEvent e) {
           searchField.cut();
        }

        
    };
    
    private Action copyAction = new AbstractAction("Kopieren", copyIcon) {

        public void actionPerformed(ActionEvent e) {
        	focusComponent.copy();
        }

        
    };
    
     private Action pasteAction = new AbstractAction("Einfügen", pasteIcon) {

        public void actionPerformed(ActionEvent e) {
            searchField.paste();
        }

        
    };
    
    private Action deleteAction = new AbstractAction("Löschen", deleteIcon) {

        public void actionPerformed(ActionEvent e) {
          searchField.replaceSelection("");
        }

        
    };
    
    private Action selectAllAction = new AbstractAction("Alles auswählen") {

        public void actionPerformed(ActionEvent e) {
          focusComponent.selectAll();
        }

        
    };
    
    private Action searchAction = new AbstractAction("Suchen", searchIcon) {

        public void actionPerformed(ActionEvent e) {
           search(searchField.getText());
        }

        
    };
    
    private Action prefsAction = new AbstractAction("Einstellungen", prefsIcon) {

        public void actionPerformed(ActionEvent e) {
           Settings settings = new Settings(AbstractSearchEngineFrame.this);
           settings.setVisible(true);
           if(settings.getResult() == Settings.OK) {
               List<File> added = settings.getAddedDirs();
               List<File> removed = settings.getRemovedDirs();
               if(added.size() > 0 || removed.size() > 0) {
            	   try{
            		   searchEngine.update(added, removed);
            	   }
            	   catch (SearchEngineException e1) {
           				JOptionPane.showMessageDialog(AbstractSearchEngineFrame.this, e1.getMessage(), "Fehler beim Update", JOptionPane.INFORMATION_MESSAGE);
           		   }
               }
           }
        }

        
    };
    
    private Action quitAction = new AbstractAction("Beenden") {

		public void actionPerformed(ActionEvent e) {
			exit();
			System.exit(0);
		}
    	
    };
    
    private Action updateAction = new AbstractAction("Index neu aufbauen", updateIcon) {

        public void actionPerformed(ActionEvent e) {
           update();
        }

    };

    
/////////////////// Konstruktion der graphischen Oberfläche ///////////////////////////
    
    

    public AbstractSearchEngineFrame() {
        initComponents();
    }
   
    @SuppressWarnings("unchecked")
   private void initComponents() {

    	// langweilige Konstruktor-Aufrufe:
    	createObjectInstances();
    	
    	// Was passiert, wenn das Fenster geschlossen wird?
        setWindowBehaviour();

        // Erzeugung der Toolbar mit Buttons für Copy, Paste usw.:
        createToolbar();
        // Erzeugt die Menuleiste mit allen Menus, MenuItems usw.
        createMenuBar();
        // Erzeugung des Bereichs, in dem das Feld zur Suchbegriffeingabe, Suchbutton etc.
        // enthalten ist
        createSearchPanel();
        // Erzeugung des Bereichs, der die Liste mit gefundenen Dokumenten
        // anzeigt
        createResultListArea();
        // Erzeugung des Bereichs, in dem ein gefundenes Dokument angezeigt wird:
        createDetailsArea();
   
        // Zuweisung der einzelnen Bereiche 
        splitPane.setDividerLocation(200);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setTopComponent(resultsTablePanel);
        splitPane.setBottomComponent(resultDetailsPanel);

        // Layout (Automatisch von Netbeans generiert):
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        //.add(20, 20, 20)
                        .add(toolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(searchPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(toolbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(searchPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        focusComponent = searchField;
        
    }

	private void setWindowBehaviour() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Window-Listener, der dafür sorgt, dass beim Schließen des
        // Fensters die exit-Methode aufgerufen wird.
        this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				exit();
			}
        	
        });
	}

	private void createDetailsArea() {
		detailsLabel.setText("Details:");
        resultDetails.setEditable(false);
        resultDetailsScrollPane.setViewportView(resultDetails);
        
        // Wird die Detailanzeige aktiviert oder deaktiviert, müssen die Edit-Actions
        // aktualisiert werden
        resultDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            	focusComponent = resultDetails;
            	updateEditActions();
            }

			public void focusLost(FocusEvent e) {
				focusComponent = resultDetails;
				updateEditActions();
			}
                        
        });
        
        // Ändert sich die Auswahl des Textes in der Detailsanzeige, müssen die Edit-Actions
        // aktualisiert werden
        resultDetails.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent evt) {
            	updateEditActions();
            }
        });
        
        

        org.jdesktop.layout.GroupLayout resultDetailsPanelLayout = new org.jdesktop.layout.GroupLayout(resultDetailsPanel);
        resultDetailsPanel.setLayout(resultDetailsPanelLayout);
        resultDetailsPanelLayout.setHorizontalGroup(
            resultDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultDetailsPanelLayout.createSequentialGroup()
                .add(detailsLabel)
                .addContainerGap(530, Short.MAX_VALUE))
            .add(resultDetailsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );
        resultDetailsPanelLayout.setVerticalGroup(
            resultDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultDetailsPanelLayout.createSequentialGroup()
                .add(detailsLabel)
                .add(4, 4, 4)
                .add(resultDetailsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
        );
	}

	private void createResultListArea() {
		dokumentsLabel.setText("Dokumente:");

        resultsTableScrollPane.setViewportView(resultsTable);
        
        // Listener, der auf die Änderungen in der Auswahl von Suchergebnissen (Dokumenten)
        // reagiert (und dafür sorgt, dass die Details-Anzeige aktualisiert wird).
        resultsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				int selection = resultsTable.getSelectedRow();
				resultsTableSelectionChanged(selection);
			}

			
        });

        org.jdesktop.layout.GroupLayout resultsTablePanelLayout = new org.jdesktop.layout.GroupLayout(resultsTablePanel);
        resultsTablePanel.setLayout(resultsTablePanelLayout);
        resultsTablePanelLayout.setHorizontalGroup(
            resultsTablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsTablePanelLayout.createSequentialGroup()
                .add(dokumentsLabel)
                .addContainerGap())
            .add(resultsTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        // Wird die Tabelle mit gefundenen Dokumenten aktiviert, müssen die Edit-Actions
        // deaktiviert werden
        resultsTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            	focusComponent = null;
            	updateEditActions();
            }

			public void focusLost(FocusEvent e) {
				
			}
                        
        });
        resultsTablePanelLayout.setVerticalGroup(
            resultsTablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsTablePanelLayout.createSequentialGroup()
                .add(dokumentsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resultsTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
        );
	}

	private void createSearchPanel() {
		searchField.setText("Suchbegriff eingeben");
        
        // FocusListener: Wird aktiviert, wenn das Suchfeld aktiviert oder deaktiviert wird.
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            	focusComponent = searchField;
            	updateEditActions();
            }

			public void focusLost(FocusEvent e) {
				focusComponent = searchField;
				updateEditActions();
			}
                        
        });
        
        // Listener, der informiert wird, wenn sich die Auswahl im Suchfeld ändert,
        // und ggf. die Edit-Actions aktiviert oder deaktiviert
        searchField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent evt) {
            	updateEditActions();
            }
        });
        
        // Keylistener für das Feld zur Suchbegriffseingabe, der bei Betätigung 
        // der Return-Taste die search-Methode aufruft und die Suche startet. 
        searchField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					search(searchField.getText());
				}
			}

			public void keyTyped(KeyEvent e) {}
        	
        });
        
        
        searchButton.setAction(searchAction);
        searchButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            	focusComponent = resultDetails;
            	updateEditActions();
            }

 			public void focusLost(FocusEvent e) {
 				focusComponent = resultDetails;
 				updateEditActions();
 			}
                        
        });

         org.jdesktop.layout.GroupLayout searchPanelLayout = new org.jdesktop.layout.GroupLayout(searchPanel);
         searchPanel.setLayout(searchPanelLayout);
         searchPanelLayout.setHorizontalGroup(
             searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
             .add(searchPanelLayout.createSequentialGroup()
                 .addContainerGap()
                 .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                     .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                     .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                     .add(searchPanelLayout.createSequentialGroup()
                         .add(searchField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                         .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                         .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                         .addContainerGap())
                     .add(searchPanelLayout.createSequentialGroup()
                         .add(summaryLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                         .add(118, 118, 118))))
         );
         searchPanelLayout.setVerticalGroup(
             searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
             .add(searchPanelLayout.createSequentialGroup()
                 .add(58, 58, 58)
                 .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                     .add(searchField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                     .add(searchButton))
                 .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                 .add(summaryLabel)
                 .add(18, 18, 18)
                 .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                 .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                 .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                 .addContainerGap())
         );
	}

	private void createMenuBar() {
		fileMenu.setText("Datei");

        updateNowItem.setAction(updateAction);
        fileMenu.add(updateNowItem);

      
        fileMenu.add(separator);

        quitItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitItem.setAction(quitAction);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        editMenu.setText("Bearbeiten");

        cutItem.setAction(cutAction);
        editMenu.add(cutItem);

        copyItem.setAction(copyAction);
        editMenu.add(copyItem);

        pasteItem.setAction(pasteAction);
        editMenu.add(pasteItem);

        deleteItem.setAction(deleteAction);
        editMenu.add(deleteItem);

        selectAllItem.setAction(selectAllAction);
        editMenu.add(selectAllItem);
        editMenu.add(jSeparator1);

        configureItem.setAction(prefsAction);
        editMenu.add(configureItem);

        menuBar.add(editMenu);

        helpMenu.setText("Hilfe");

        aboutItem.setText("Über dieses Programm");
        
        aboutItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(AbstractSearchEngineFrame.this, searchEngine.getAboutMessage(), "Über " + searchEngine.getProgramName(), JOptionPane.INFORMATION_MESSAGE);
			}
        	
        });
        
        helpMenu.add(aboutItem);

        javaDocItem.setText("Javadoc anzeigen...");
        helpMenu.add(javaDocItem);

        javaDocItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Point myLoc = getLocation();
				if(browser == null) browser = new Browser(searchEngine.getProgramName());
				browser.setLocation(myLoc.x + 100, myLoc.y + 100);
				browser.setVisible(true);
			}
        	
        });
        
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
	}

	private void createToolbar() {
		toolbar.setRollover(false);
		toolbar.setOpaque(false);
		toolbar.setFloatable(false);
        prefsButton.setAction(prefsAction);
        prefsButton.setFocusable(false);
        prefsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        prefsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(prefsButton);
        toolbar.add(jSeparator2);
        prefsButton.setToolTipText(prefsButton.getAction().getValue(Action.NAME).toString());
       
        cutButton.setAction(cutAction);
        cutButton.setFocusable(false);
        cutButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cutButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cutButton.setToolTipText(cutButton.getAction().getValue(Action.NAME).toString());
        toolbar.add(cutButton);

        copyButton.setAction(copyAction);
        copyButton.setFocusable(false);
        copyButton.setHorizontalTextPosition(SwingConstants.CENTER);
        copyButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        copyButton.setToolTipText(copyButton.getAction().getValue(Action.NAME).toString());
        toolbar.add(copyButton);

        pasteButton.setAction(pasteAction);
        pasteButton.setFocusable(false);
        pasteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pasteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pasteButton.setToolTipText(pasteButton.getAction().getValue(Action.NAME).toString());
        toolbar.add(pasteButton);

        deleteButton.setAction(deleteAction);
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteButton.setToolTipText(deleteButton.getAction().getValue(Action.NAME).toString());
        toolbar.add(deleteButton);
        toolbar.add(jSeparator3);

        updateButton.setAction(updateAction);
        updateButton.setFocusable(false);
        updateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        updateButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        updateButton.setToolTipText(updateButton.getAction().getValue(Action.NAME).toString());
        toolbar.add(updateButton);
        setShowToolBarButtonText(false);
	}

	private void createObjectInstances() {
		// Erzeugung der Benötigten GUI-Komponenten:
        toolbar = new JToolBar();
        prefsButton = new JButton();
        jSeparator2 = new JToolBar.Separator();
        cutButton = new JButton();
        copyButton = new JButton();
        pasteButton = new JButton();
        deleteButton = new JButton();
        jSeparator3 = new JToolBar.Separator();
        updateButton = new JButton();
        searchPanel = new JPanel();
        searchField = new JTextField();
        summaryLabel = new JLabel();
        splitPane = new JSplitPane();
        resultsTablePanel = new JPanel();
        dokumentsLabel = new JLabel();
        resultsTableScrollPane = new JScrollPane();
        resultsTable = new JTable();
        resultDetailsPanel = new JPanel();
        detailsLabel = new JLabel();
        resultDetailsScrollPane = new JScrollPane();
        resultDetails = new JTextPane();
        searchButton = new JButton();
        progressBar = new JProgressBar();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        updateNowItem = new JMenuItem();
        separator = new JSeparator();
        quitItem = new JMenuItem();
        editMenu = new JMenu();
        cutItem = new JMenuItem();
        copyItem = new JMenuItem();
        pasteItem = new JMenuItem();
        deleteItem = new JMenuItem();
        selectAllItem = new JMenuItem();
        jSeparator1 = new JSeparator();
        configureItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutItem = new JMenuItem();
        javaDocItem = new JMenuItem();
	}

    
    
/////////////////// GUI-Interaktion /////////////////////
    
    /**
     * Sorgt dafür, dass die Edit-Buttons (Kopieren, Ausschneiden usw) aktualisiert werden. 
     */
    private void updateEditActions() {
    	if(focusComponent == null) {
    		cutAction.setEnabled(false);
    		copyAction.setEnabled(false);
    		pasteAction.setEnabled(false);
    		deleteAction.setEnabled(false);
    		selectAllAction.setEnabled(false);
    		return;
    	}
    	if(focusComponent == searchField) {
    	     cutAction.setEnabled(searchField.getSelectedText() != null);
    	     copyAction.setEnabled(searchField.getSelectedText() != null);
    	     pasteAction.setEnabled(true);
    	     deleteAction.setEnabled(searchField.getSelectedText() != null);
    	     selectAllAction.setEnabled(true);
    	} else {
    		 boolean enabled = resultDetails.getSelectedText() != null;
    	     cutAction.setEnabled(false);
    	     copyAction.setEnabled(enabled);
    	     pasteAction.setEnabled(false);
    	     deleteAction.setEnabled(false);
    	     selectAllAction.setEnabled(enabled);
    	}
       
    }
    
  
    /**
     * Ruft die stop-Methode der Suchmaschine auf
     */
    private void exit() {
		try {
			searchEngine.stop();
		} catch (SearchEngineException e1) {
			JOptionPane.showMessageDialog(AbstractSearchEngineFrame.this, e1.getMessage(), "Fehler beim Herunterfahren der Suchmaschine", JOptionPane.ERROR_MESSAGE);
		}
	}
}
