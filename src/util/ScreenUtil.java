package util;

import java.awt.Dimension;

public class ScreenUtil {
    public static Dimension getScreenSize() {
        return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Dimension getScaledSize(double width, double height) {
        Dimension screenSize = getScreenSize();
        return new Dimension((int) (screenSize.width * width), (int) (screenSize.height * height));
    }
}

