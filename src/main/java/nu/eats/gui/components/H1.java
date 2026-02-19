package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class H1 extends JPanel {
    public H1(String text) {
        super(new BorderLayout());

        setOpaque(false);
        setBorder(new EmptyBorder(Theme.SPACING_XL, Theme.SPACING_XL, 0, Theme.SPACING_XL));

        var label = new JLabel(text);

        label.setFont(Theme.FONT_BOLD_24);
        label.setForeground(Theme.COLOR_FG);
        label.setBorder(new EmptyBorder(0, 0, 20, 0));

        add(label, BorderLayout.CENTER);
    }
}
