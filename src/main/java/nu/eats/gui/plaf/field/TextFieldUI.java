package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
import nu.eats.gui.plaf.box.BoxMeasure;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static nu.eats.gui.plaf.Constants.*;

public class TextFieldUI extends BasicTextFieldUI {

    private FocusListener focusListener;
    private PropertyChangeListener variantListener;

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new TextFieldUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        JTextComponent editor = getComponent();

        editor.setOpaque(false);
        editor.setFont(Theme.FONT_MEDIUM_14);

        var variant = TextFieldVariant.SECONDARY;

        editor.putClientProperty(KEY_VARIANT, variant);

        BoxDecoration.ensure(editor).borderRadius(BoxMeasure.pixels(Theme.RADIUS_MD));

        variant.apply(editor, TextFieldState.DEFAULT);
    }

    @Override
    protected void installListeners() {
        super.installListeners();

        JTextComponent component = getComponent();

        focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateState(component);

                component.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateState(component);

                component.repaint();
            }
        };

        component.addFocusListener(focusListener);

        variantListener = (PropertyChangeEvent event) -> {
            if (KEY_VARIANT.equals(event.getPropertyName())) {
                updateState(component);

                component.revalidate();
                component.repaint();
            }
        };

        component.addPropertyChangeListener(variantListener);
    }

    @Override
    protected void uninstallListeners() {
        JTextComponent component = getComponent();

        component.removeFocusListener(focusListener);
        component.removePropertyChangeListener(variantListener);

        super.uninstallListeners();
    }

    private void updateState(JTextComponent component) {
        if (component.getClientProperty(KEY_VARIANT) instanceof TextFieldVariant variant) {
            variant.apply(component, TextFieldState.of(component));
        }
    }

    @Override
    protected void paintSafely(Graphics graphics) {
        JTextComponent component = getComponent();

        if (component.getClientProperty(KEY_BOX_DECORATION) instanceof BoxDecoration boxDecoration) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();

            graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

            boxDecoration.paint(graphics2D, component, component.getWidth(), component.getHeight());

            graphics2D.dispose();
        }

        super.paintSafely(graphics);
    }
}
