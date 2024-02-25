package ui.components;

import config.Config;
import ui.Colors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class Indicator extends JComponent {

    public static final int SIZE = 20;

    public static final int SPACING = SIZE / 2;

    public static final int WIDTH = SIZE + SPACING;

    public static final int HEIGHT = SIZE * 2;

    private boolean state = false;

    private String label;

    /**
     * Default constructor
     */
    public Indicator() {
        super();
    }

    /**
     * Creates a new indicator with the specified label positioned above the light
     * 
     * @param label The label to display
     */
    public Indicator(String label) {
        super();
        this.label = label;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (Config.UI_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(0, 0, WIDTH, HEIGHT);
        }

        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            if (label != null) {
                g2d.setFont(Config.UI_FONT_MONOSPACE);
                g2d.setColor(Color.BLACK);
                g2d.drawString(label,
                        (WIDTH - g2d.getFontMetrics().stringWidth(label.toString())) / 2,
                        10);
            }

        }

        if (state) {
            g.setColor(Colors.INDICATOR_ON);
        } else {
            g.setColor(Colors.INDICATOR_OFF);
        }

        g.fillOval(SPACING / 2, (HEIGHT - SPACING) / 2, SIZE, SIZE);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void set(boolean state) {
        this.state = state;
        repaint();
    }
}
