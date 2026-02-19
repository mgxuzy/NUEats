package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.box.BoxDecoration;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.*;

public class ButtonUI extends BasicButtonUI {
    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void installDefaults(AbstractButton button) {
        button.setContentAreaFilled(false); // also calls setOpaque(false);

        super.installDefaults(button);

        button.setBorder(null);
        button.setBorderPainted(false);
        button.setRolloverEnabled(true);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ButtonPreset.SM.apply(button);

        var variant = ButtonVariant.PRIMARY;

        variant.apply(button, ButtonState.DEFAULT);

        button.putClientProperty(KEY_VARIANT, variant);
    }

    @Override
    protected void installListeners(AbstractButton button) {
        super.installListeners(button);

        var buttonModel = button.getModel();

        button.addPropertyChangeListener(KEY_VARIANT, event -> {
            if (event.getNewValue() instanceof ButtonVariant variant) {
                variant.apply(button, ButtonState.of(buttonModel));
            }
        });

        buttonModel.addChangeListener(ignored -> {
            if (!(button.getClientProperty(KEY_VARIANT) instanceof ButtonVariant variant)) {
                return;
            }

            variant.apply(button, ButtonState.of(buttonModel));
        });
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        if (!(component instanceof AbstractButton button)) {
            return;
        }

        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            if (button.getClientProperty(KEY_BOX_DECORATION) instanceof BoxDecoration boxDecoration) {
                boxDecoration.paint(graphics2D, button, button.getWidth(), button.getHeight());
            }

            super.paint(graphics2D, button);
        } finally {
            graphics2D.dispose();
        }
    }

    private static final class InstanceHolder {
        private static final ButtonUI INSTANCE = new ButtonUI();
    }
}
