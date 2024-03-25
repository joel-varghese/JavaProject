package ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import ui.components.GroupPanel;

public class ControlPanel extends GroupPanel {

    public JButton runButton;
    public JButton stepBackButton;
    public JButton stepForwardButton;
    public JButton loadButton;
    public JButton storeButton;


    public ControlPanel() {
        super();
        setOpaque(false);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        runButton = new JButton("Run");
        stepForwardButton = new JButton(">>");
        loadButton = new JButton("Load");
        storeButton = new JButton("Store");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(storeButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(runButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(stepForwardButton, gbc);

    }
}
