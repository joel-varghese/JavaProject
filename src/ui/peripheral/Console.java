package ui.peripheral;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import components.io.Device;


public class Console extends JPanel implements Device{

    public JTextArea textArea = new JTextArea();

    public Console() {
        super();
        setLayout(new BorderLayout());

        textArea.setEditable(false);

        add(textArea, BorderLayout.CENTER);
    }

    public void write(char data) {
        textArea.append(String.valueOf(data));
    }

    public char read() {
        return 0;
    }

    public char check() {
        return 0;
    }

    public short getId() {
        return 1;
    }

    public void reset() {
        textArea.setText("");
    }

}
