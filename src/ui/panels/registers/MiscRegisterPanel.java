package ui.panels.registers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.GroupPanel;
import ui.components.RegisterTextField;
import javax.swing.JCheckBox;

public class MiscRegisterPanel extends GroupPanel {

    public RegisterTextField pc;
    // public JCheckBox pcCheckbox;

    public RegisterTextField mar;
    // public JCheckBox marCheckbox;

    public RegisterTextField mbr;
    // public JCheckBox mbrCheckbox;


    public MiscRegisterPanel() {
        super();
        setLayout(new GridBagLayout());

        pc = new RegisterTextField("PC: ");
        // pcCheckbox = new JCheckBox("PC");

        mar = new RegisterTextField("MAR: ");
        // marCheckbox = new JCheckBox("MAR");

        mbr = new RegisterTextField("MBR: ");
        // mbrCheckbox = new JCheckBox("MBR");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(pc, gbc);

        // gbc.gridx = 1;
        // gbc.gridy = 0;
        // add(pcCheckbox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(mar, gbc);

        // gbc.gridx = 1;
        // gbc.gridy = 1;
        // add(marCheckbox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(mbr, gbc);

        // gbc.gridx = 1;
        // gbc.gridy = 2;
        // add(mbrCheckbox, gbc);
    }

}
