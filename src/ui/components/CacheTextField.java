package ui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class CacheTextField extends JPanel {

    private JLabel label;

    public JTextArea textArea;

    public CacheTextField(String label,int rows) {
        super();
        setLayout(new GridBagLayout());

        this.label = new JLabel(label);
        this.textArea = new JTextArea(rows,30);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        set((char) 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(this.label, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(this.textArea, gbc);
    }

    public char get() {
        try {

            if (textArea.getText().startsWith("0x")) {
                return (char) Integer.parseInt(textArea.getText().substring(2), 16);
            } else {
                return (char) Integer.parseInt(textArea.getText(), 16);
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
        textArea.setText(String.format("00%s",Integer.toBinaryString((int) value)));
    }

}
