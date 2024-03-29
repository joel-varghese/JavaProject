package ui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class CacheTextFieldDeprecated extends JPanel {

    private JLabel label;

    public JTextField textField;

    public CacheTextFieldDeprecated(String label,int rows) {
        super();
        setLayout(new GridBagLayout());


        this.label = new JLabel(label);
        this.textField = new JTextField(9);
        //this.textField.setLineWrap(true);
        //this.textField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(this.textField);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        set((char) 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(this.label, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(this.textField, gbc);

    // Getter method to access the JTextArea from outside
    // public JTextArea getTextArea() {
    //     return textArea;
    // }
    }

    public char get() {
        try {

            if (textField.getText().startsWith("0x")) {
                return (char) Integer.parseInt(textField.getText().substring(2), 16);
            } else {
                return (char) Integer.parseInt(textField.getText(), 16);
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
