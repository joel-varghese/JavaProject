package ui.components;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import config.Config;
import ui.Colors;

public class CachePanel extends JPanel {

    public CachePanel() {
        super();
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(Config.UI_MARGIN * 2,
                Config.UI_MARGIN ,
                Config.UI_MARGIN ,
                Config.UI_MARGIN ));
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Colors.GROUP_PANEL_BACKGROUND);
        g2d.fillRoundRect(Config.UI_MARGIN,
                Config.UI_MARGIN,
                getWidth() - (Config.UI_MARGIN), getHeight() - (Config.UI_MARGIN * 2),
                16,
                16);

        g2d.setColor(Colors.GROUP_PANEL_BORDER);
        g2d.drawRoundRect(Config.UI_MARGIN,
                Config.UI_MARGIN,
                getWidth() - (Config.UI_MARGIN), getHeight() - (Config.UI_MARGIN * 2),
                16,
                16);
    }

}
