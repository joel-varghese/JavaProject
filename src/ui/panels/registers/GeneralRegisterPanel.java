package ui.panels.registers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.GroupPanel;
import ui.components.RegisterTextField;

public class GeneralRegisterPanel extends GroupPanel {

    public RegisterTextField r0;

    public RegisterTextField r1;

    public RegisterTextField r2;

    public RegisterTextField r3;

    public GeneralRegisterPanel() {
        super();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        r0 = new RegisterTextField("GPR 0: ");

        r1 = new RegisterTextField("GPR 1: ");

        r2 = new RegisterTextField("GPR 2: ");

        r3 = new RegisterTextField("GPR 3: ");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(r0, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(r1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(r2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(r3, gbc);
        
    }
    
}
