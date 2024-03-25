package ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import config.Config;
import ui.Colors;
import ui.components.IndicatorGroup;
import ui.panels.registers.CacheRegisterPanel;

public class IndicatorPanel extends JPanel {

    // General Purpose Registers (r0, r1, r2, r3) indicator group
    // 16-bit
    public IndicatorGroup r0Group, r1Group, r2Group, r3Group;

    // Index Registers (x1, x2, x3) indicator group
    // 16-bit
    public IndicatorGroup x1Group, x2Group, x3Group;

    // Memory Buffer Regietser (mbr) indicator group
    // 16-bit
    public IndicatorGroup mbrGroup;

    // Memory Address Register (mar) indicator group
    // 12-bit
    public IndicatorGroup marGroup;

    // Program Counter (pc) indiciator group
    // 12-bit
    public IndicatorGroup pcGroup;

    // Instruction Register (ir) indicator group
    // 16-bit
    public IndicatorGroup irGroup;

    // Memory Fault Register (mfr) indicator group
    // 4-bit
    public IndicatorGroup mfrGroup;

    // Condition Code (cc) indicator group
    // 4-bit
    public IndicatorGroup ccGroup;

    // IPL Button to Load Listing file
    public JButton IPLButton;

    public CacheRegisterPanel cacheGroup;

    public IndicatorPanel() {
        super();
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(Config.UI_MARGIN * 2,
                Config.UI_MARGIN * 2,
                Config.UI_MARGIN * 2,
                Config.UI_MARGIN * 2));

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Setup indicators for index registers
        x1Group = new IndicatorGroup(16, true, "X1");
        x2Group = new IndicatorGroup(16, true, "X2");
        x3Group = new IndicatorGroup(16, true, "X3");

        // Setup indicators for general purpose registers
        r0Group = new IndicatorGroup(16, true, "R0");
        r1Group = new IndicatorGroup(16, true, "R1");
        r2Group = new IndicatorGroup(16, true, "R2");
        r3Group = new IndicatorGroup(16, true, "R3");

        // Setup indiacator for memory buffer register
        mbrGroup = new IndicatorGroup(16, true, "Memory Buffer Register");

        // Setup indicator for memory address register
        marGroup = new IndicatorGroup(12, false, "Memory Address Register");

        // Setup indicator for memory fault register
        mfrGroup = new IndicatorGroup(4, false, "MFR");

        // Setup indicator for program counter register
        pcGroup = new IndicatorGroup(12, false, "Program Counter");

        // Setup indicaator for instruction register
        irGroup = new IndicatorGroup(16, true, "Instruction Register");

        // Setup indicator for condition code register
        ccGroup = new IndicatorGroup(4, false, "CC");

        cacheGroup = new CacheRegisterPanel();

        IPLButton = new JButton("IPL");

        // Add instruction, program counter, and condition code indicator groups to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0.5;
        add(ccGroup, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(pcGroup, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(irGroup, gbc);

        // Add memory buffer, address, and fault indicator groups to the panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(mfrGroup, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(marGroup, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        add(mbrGroup, gbc);

        // Add index register indicator groups to the panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(x1Group, gbc);

        gbc.gridy = 3;
        add(x2Group, gbc);

        gbc.gridy = 4;
        add(x3Group, gbc);

        // Add general purpose register indicator groups to the panel
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(r0Group, gbc);

        gbc.gridy = 3;
        add(r1Group, gbc);

        gbc.gridy = 4;
        add(r2Group, gbc);

        gbc.gridy = 5;
        add(r3Group, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 2;
        add(IPLButton, gbc);

        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.weightx = 0.5;
        add(cacheGroup, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Colors.FRONT_PANEL_BACKGROUND);
        g2d.fillRoundRect(Config.UI_MARGIN,
                Config.UI_MARGIN,
                getWidth() - (Config.UI_MARGIN * 2), getHeight() - (Config.UI_MARGIN * 2),
                32,
                32);
    }

}
