package ui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class RegisterTextField extends JPanel {

    private JLabel label;

    public JTextField textField;

    public RegisterTextField(String label) {
        super();
        setLayout(new GridBagLayout());

        this.label = new JLabel(label);
        this.textField = new JTextField(8);
        this.textField.setHorizontalAlignment(SwingConstants.RIGHT);

        set((char) 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(this.label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(this.textField, gbc);
    }

    public char get() {
        try {
            if (textField.getText().startsWith("0x")) {
                return (char) Integer.parseInt(textField.getText().substring(2), 2);
            } else {
                return (char) Integer.parseInt(textField.getText(), 2);
            }

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Sets the value of the register text field.
     * 
     * @param value The value to set the register to
     */
    public void set(char value) {
        textField.setText(String.format("00%s",Integer.toBinaryString((int) value)));
    }
}
