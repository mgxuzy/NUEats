package nu.eats.gui.plaf.field;

import javax.swing.text.JTextComponent;

public enum TextFieldState {
    DEFAULT,
    FOCUSED,
    DISABLED;

    public static TextFieldState of(JTextComponent component) {
        if (!component.isEnabled()) {
            return DISABLED;
        }

        if (component.hasFocus()) {
            return FOCUSED;
        }

        return DEFAULT;
    }
}
