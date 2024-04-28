package ui.panels.registers;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui.components.CachePanel;
import ui.components.CacheTextField;

public class DeviceRegisterPanel extends CachePanel {
    
    public CacheTextField paragraphContent;

    public CacheTextField deviceOutput;

    public CacheTextField deviceInput;

    public JButton submitDeviceButton;

    public DeviceRegisterPanel(){
        super();
        setLayout(new GridBagLayout());

        paragraphContent = new CacheTextField("Paragraph Content",6);

        deviceOutput = new CacheTextField("Device Output", 2);

        deviceInput = new CacheTextField("Device Input",2);

        submitDeviceButton = new JButton("Submit");


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(paragraphContent, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(deviceOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(deviceInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(submitDeviceButton, gbc);
    }

}
