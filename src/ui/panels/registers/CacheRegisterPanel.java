package ui.panels.registers;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.CachePanel;
import ui.components.CacheTextField;

public class CacheRegisterPanel extends CachePanel {
    
    public CacheTextField cache;

    public CacheTextField printer;

    public CacheTextField console;

    public JButton submitCacheButton;

    public CacheRegisterPanel(){
        super();
        setLayout(new GridBagLayout());

        cache = new CacheTextField("Cache Content",16);

        printer = new CacheTextField("Printer",10);

        console = new CacheTextField("Console Input",2);

        submitCacheButton = new JButton("Submit");


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

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(submitCacheButton, gbc);
    }

}
