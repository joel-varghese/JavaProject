package ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import ui.components.GroupPanel;

public class ControlPanel extends GroupPanel {

    public JButton runButton;

    public JButton stepBackButton;

    public JButton stepForwardButton;

    public ControlPanel() {
        super();
        setOpaque(false);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        runButton = new JButton("Run");
        stepForwardButton = new JButton(">>");

        gbc.gridx = 2;
        gbc.gridy = 0;
        add(runButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        add(stepForwardButton, gbc);

    }
}
