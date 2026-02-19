package nu.eats.gui.components.chip;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonVariant;

import javax.swing.*;

import static nu.eats.gui.plaf.Constants.KEY_VARIANT;

public class Chip extends JToggleButton {
    public Chip(String text) {
        super(text);

        setFont(Theme.FONT_MEDIUM_14);
        putClientProperty(KEY_VARIANT, ButtonVariant.SECONDARY);
    }
}
