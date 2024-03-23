package ui.panels.other_inputs;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.GroupPanel;
import ui.components.BinaryTextField;
import ui.components.OctalTextField;

public class OctalBinaryPanel extends GroupPanel {

    public BinaryTextField binary;

    public OctalTextField octal;

    public OctalBinaryPanel() {
        super();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        binary = new BinaryTextField("Binary: ");

        octal = new OctalTextField("Octal: ", binary);


        gbc.gridx = 0;
        gbc.gridy = 0;
        add(octal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(binary, gbc);
    }
    
}