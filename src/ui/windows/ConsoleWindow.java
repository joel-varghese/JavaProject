package ui.windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import config.Config;
import ui.peripheral.Console;
import ui.peripheral.Keyboard;
import util.ScreenUtil;

public class ConsoleWindow extends JFrame {

    public Console console = new Console();

    public Keyboard keyboard = new Keyboard();

    public ConsoleWindow() {
        super("Console");
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(console, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        add(keyboard, gbc);

        setSize(ScreenUtil.getScaledSize(Config.UI_CONSOLE_SCALE_WIDTH, Config.UI_CONSOLE_SCALE_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

}
