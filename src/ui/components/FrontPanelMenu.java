package ui.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class FrontPanelMenu extends JMenuBar {

    // File Menu
    public JMenu fileMenu;

    public JMenuItem fileLoadROM, fileOpenLog;

    // Action Menu
    public JMenu actionMenu;

    public JMenuItem actionReset;

    // View Menu
    public JMenu viewMenu;

    public JMenuItem viewConsole, viewMemory;

    public FrontPanelMenu() {
        super();

        fileMenu = new JMenu("File");
        fileLoadROM = new JMenuItem("Load ROM");
        fileOpenLog = new JMenuItem("Open Log");
        fileMenu.add(fileLoadROM);
        fileMenu.addSeparator();
        fileMenu.add(fileOpenLog);

        actionMenu = new JMenu("Action");
        actionReset = new JMenuItem("Reset");
        actionMenu.add(actionReset);

        viewMenu = new JMenu("View");
        viewConsole = new JMenuItem("Console");
        viewMenu.add(viewConsole);

        add(fileMenu);
        add(actionMenu);
        add(viewMenu);
    }

}
