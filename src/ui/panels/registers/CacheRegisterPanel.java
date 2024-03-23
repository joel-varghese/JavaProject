package ui.panels.registers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.GroupPanel;
import ui.components.CachePanel;
import ui.components.CacheTextField;

public class CacheRegisterPanel extends CachePanel {
    
    public CacheTextField cache;

    public CacheTextField printer;

    public CacheTextField console;

    public CacheRegisterPanel(){
        super();
        setLayout(new GridBagLayout());

        cache = new CacheTextField("Cache Content",20);

        printer = new CacheTextField("Printer",10);

        console = new CacheTextField("Console Input",2);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(cache, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(printer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(console, gbc);
    }

}
