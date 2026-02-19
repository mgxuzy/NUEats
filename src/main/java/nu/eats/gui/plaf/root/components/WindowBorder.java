package nu.eats.gui.plaf.root.components;

import nu.eats.gui.plaf.Theme;

import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class WindowBorder implements Border, UIResource {
    private static final int THICKNESS = 1;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Theme.COLOR_BORDER);
        g.drawRect(x, y, width - 1, height - 1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(THICKNESS, THICKNESS, THICKNESS, THICKNESS);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}