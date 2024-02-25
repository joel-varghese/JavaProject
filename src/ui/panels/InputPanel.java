package ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ui.panels.registers.GeneralRegisterPanel;
import ui.panels.registers.IndexRegisterPanel;
import ui.panels.registers.MiscRegisterPanel;

public class InputPanel extends JPanel {
    
    public GeneralRegisterPanel generalRegisterPanel;

    public IndexRegisterPanel indexRegisterPanel;

    public MiscRegisterPanel miscRegisterPanel;

    public InputPanel() {
        super();
        setOpaque(false);
        setLayout(new GridBagLayout());

        generalRegisterPanel = new GeneralRegisterPanel();
        indexRegisterPanel = new IndexRegisterPanel();
        miscRegisterPanel = new MiscRegisterPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        add(generalRegisterPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(indexRegisterPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        add(miscRegisterPanel, gbc);
    }

}
