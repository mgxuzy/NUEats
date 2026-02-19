package nu.eats.gui.plaf.scroll;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneLayout extends javax.swing.ScrollPaneLayout {
    @Override
    public void layoutContainer(Container parent) {
        var scrollPane = (JScrollPane) parent;
        var bounds = scrollPane.getBounds();
        var insets = parent.getInsets();

        var left = insets.left;
        var top = insets.top;
        var availWidth = bounds.width - left - insets.right;
        var availHeight = bounds.height - top - insets.bottom;

        // Viewport takes the full available area
        if (viewport != null) {
            viewport.setBounds(left, top, availWidth, availHeight);
        }

        // Overlay scrollbars on top of the viewport
        if (vsb != null && vsb.isVisible()) {
            var vsbWidth = vsb.getPreferredSize().width;

            vsb.setBounds(left + availWidth - vsbWidth, top, vsbWidth, availHeight);

            parent.setComponentZOrder(vsb, 0);
        }

        if (hsb != null && hsb.isVisible()) {
            var hsbHeight = hsb.getPreferredSize().height;

            hsb.setBounds(left, top + availHeight - hsbHeight, availWidth, hsbHeight);

            parent.setComponentZOrder(hsb, 0);
        }
    }
}
