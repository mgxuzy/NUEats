package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
import nu.eats.gui.plaf.box.BoxMeasure;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;
import static nu.eats.gui.plaf.Constants.KEY_BOX_DECORATION;

public class Section extends JPanel {
    public Section() {
        setOpaque(false);
        setBackground(Theme.COLOR_BG);

        BoxDecoration.ensure(this).borderRadius(BoxMeasure.pixels(Theme.RADIUS_LG));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            if (getClientProperty(KEY_BOX_DECORATION) instanceof BoxDecoration decoration) {
                decoration.paint(graphics2D, this, getWidth(), getHeight());
            }

            super.paintComponent(graphics2D);
        } finally {
            graphics2D.dispose();
        }
    }
}
