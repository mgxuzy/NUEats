package nu.eats.gui.plaf.button;

import javax.swing.*;

public enum ButtonState {
    SELECTING,
    ACTIVE,
    HOVERING,
    DEFAULT,
    DISABLED;

    public static ButtonState of(ButtonModel model) {
        if (!model.isEnabled()) {
            return ButtonState.DISABLED;
        }

        if (model.isSelected()) {
            return ButtonState.SELECTING;
        } else if (model.isArmed() && model.isPressed()) {
            return ButtonState.ACTIVE;
        } else if (model.isRollover()) {
            return ButtonState.HOVERING;
        } else {
            return ButtonState.DEFAULT;
        }
    }
}
