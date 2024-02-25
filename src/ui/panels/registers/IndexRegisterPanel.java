package ui.panels.registers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.GroupPanel;
import ui.components.RegisterTextField;

public class IndexRegisterPanel extends GroupPanel {
    
    public RegisterTextField x1;

    public RegisterTextField x2;

    public RegisterTextField x3;

    public IndexRegisterPanel(){
        super();
        setLayout(new GridBagLayout());

        x1 = new RegisterTextField("X1: ");

        x2 = new RegisterTextField("X2: ");

        x3 = new RegisterTextField("X3: ");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(x1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(x2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(x3, gbc);
    }

}
