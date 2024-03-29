package ui.components;

import java.io.*; 
import java.util.*; 

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OctalTextField extends JPanel {

    private JLabel label;
    public JTextField textField;
    public BinaryTextField otherTextField;

    public OctalTextField(String label, BinaryTextField binaryTextField) { 
        
        super();
        setLayout(new GridBagLayout());

        this.label = new JLabel(label);
        this.textField = new JTextField(8);
        this.textField.setHorizontalAlignment(SwingConstants.RIGHT);
        this.otherTextField = binaryTextField;


        set((char) 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(this.label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(this.textField, gbc);

        // Add action listener to the text field
        this.textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOtherTextField();
            }
        });
    }

    // public char get() {
    public int get() {
        try {
            if (textField.getText().startsWith("0x")) {
                // return (char) Integer.parseInt(textField.getText().substring(2), 16);
                // return Integer.parseInt(textField.getText().substring(2), 16);
                return Integer.parseInt(textField.getText().substring(2));
            } else {
                // return (char) Integer.parseInt(textField.getText(), 16);
                // return Integer.parseInt(textField.getText(), 16);
                return Integer.parseInt(textField.getText());

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
    // public void set(char value) {
    public void set(int value) {
        // textField.setText(String.format("00%s",Integer.toBinaryString((int) value)));
        textField.setText(String.format("00%s",Integer.toBinaryString(value)));
    }


    // Method to update the other text field when this one changes
    public void updateOtherTextField() {
        // Get the value from this text field
        // char value = get();
        int octalValue = get();
        int intValue = octalToInteger(Integer.toString(octalValue));
        
        // Update the other text field with the new value
        // Here, you can replace "otherTextField" with the reference to the other OctalBinaryTextField
        otherTextField.set(intValue);
    }

    public static int octalToInteger(String octal) {
        // Validate octal input
        if (!octal.matches("[0-7]+")) {
            throw new IllegalArgumentException("Input is not a valid octal number.");
        }

        // Parse octal string and convert to integer
        int result = 0;
        for (int i = 0; i < octal.length(); i++) {
            char digitChar = octal.charAt(i);
            int digit = Character.getNumericValue(digitChar);
            result = result * 8 + digit; // Multiply by 8 for each digit position
        }
        return result;
    }

}

