package ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ui.panels.registers.GeneralRegisterPanel;
import ui.panels.registers.IndexRegisterPanel;
import ui.panels.registers.MiscRegisterPanel;
import ui.panels.other_inputs.OctalBinaryPanel;

public class InputPanel extends JPanel {

    public OctalBinaryPanel octalBinaryPanel;
    public GeneralRegisterPanel generalRegisterPanel;
    public IndexRegisterPanel indexRegisterPanel;
    public MiscRegisterPanel miscRegisterPanel;

    public InputPanel() {
        super();
        setOpaque(false);
        setLayout(new GridBagLayout());

        octalBinaryPanel = new OctalBinaryPanel();
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

        gbc.gridx = 3;
        gbc.gridy = 0;
        add(octalBinaryPanel, gbc);
    }

}
