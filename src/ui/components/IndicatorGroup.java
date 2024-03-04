package ui.components;

import config.Config;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class IndicatorGroup extends JComponent {

    public static int HEIGHT = Indicator.HEIGHT * 2;

    public static int INDICATOR_Y = (HEIGHT - Indicator.HEIGHT) / 2;

    public static int OVERLINE_HEIGHT = 4;

    public static int OVERLINE_Y = INDICATOR_Y - (OVERLINE_HEIGHT * 2);

    private Indicator[] indicators;

    private int bits;

    private String title;

    /**
     * Creates a new group of Indicators with the specified number of bits
     * 
     * @param bits The number of bits to create
     */
    public IndicatorGroup(int bits) {
        this(bits, false, null);
    }

    /**
     * Creates a new group of Indicators with the specified number of bits and
     * numeric labels for each of the indicators if numbered is true.
     * 
     * @param bits     The number of bits to create.
     * @param numbered Whether or not to number the indicators.
     */
    public IndicatorGroup(int bits, boolean numbered) {
        this(bits, numbered, null);
    }

    /**
     * Creates a new group of Indicators with the specified number of bits and
     * a title. The labels will have numeric labels for each of the indicators
     * if numbered is true.
     * 
     * @param bits     The number of bits to create.
     * @param numbered Whether or not to number the indicators.
     * @param title    The title of the group.
     */
    public IndicatorGroup(int bits, boolean numbered, String title) {
        super();
        this.title = title;
        this.bits = bits;

        // Setup the stored indicators
        indicators = new Indicator[bits];

        for (int bit = 0; bit < bits; bit++) {
            int index = bits - bit -1;
            indicators[bit] = (numbered) ? new Indicator(Integer.toString(index)) : new Indicator();
            indicators[bit].setBounds((bit * Indicator.WIDTH) + (Indicator.SPACING / 2),
                    INDICATOR_Y,
                    Indicator.WIDTH,
                    Indicator.HEIGHT);
            add(indicators[bit]);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        if (Config.UI_DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawRect(0, 0, getWidth(), getHeight());
        }

        if (this.title != null) {
            g2d.setFont(Config.UI_FONT_MONOSPACE);
            g2d.setColor(Color.BLACK);
            g2d.drawString(this.title,
                    (getWidth() - g2d.getFontMetrics().stringWidth(this.title)) / 2,
                    10);
        }

        g2d.setColor(Color.BLACK);
        g2d.fillRect(Indicator.SPACING / 2, OVERLINE_Y, getWidth() - Indicator.SPACING, OVERLINE_HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), HEIGHT);
    }

    @Override
    public int getWidth() {
        // TODO: Remove SPACING so that grids can line up properly, instead reduce
        // overline width
        return (Indicator.WIDTH * bits) + Indicator.SPACING;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    public void set(char value) {
        for (int i = 0; i < indicators.length; i++) {
            int mask = 1 << i;
            boolean state = (value & mask) >> i == 1;
            indicators[(indicators.length - 1) - i].set(state);;
        }

        repaint();
    }

}
