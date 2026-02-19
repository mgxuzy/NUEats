package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
import nu.eats.gui.plaf.box.BoxMeasure;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static nu.eats.gui.plaf.Theme.BORDER_WIDTH_THICK;
import static nu.eats.gui.plaf.Theme.BORDER_WIDTH_THIN;

public enum TextFieldVariant {
    SECONDARY {
        @Override
        void apply(JTextComponent component, TextFieldState state) {
            var boxDecoration = BoxDecoration.ensure(component);

            switch (state) {
                case DEFAULT -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);
                    component.setCaretColor(Theme.COLOR_PRIMARY);
                    component.setFont(Theme.FONT_REGULAR_BASE);
                    component.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

                    BoxDecoration.ensure(component).borderColor(Theme.COLOR_BORDER)
                            .borderWidth(BORDER_WIDTH_THIN);
                }

                case FOCUSED -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);

                    boxDecoration.borderColor(Theme.COLOR_RING)
                            .borderWidth(BORDER_WIDTH_THICK);
                }

                case DISABLED -> {
                    component.setBackground(Theme.COLOR_BG_HOVER);
                    component.setForeground(Theme.COLOR_FG_MUTED);

                    boxDecoration.borderColor(Theme.COLOR_BORDER)
                            .borderWidth(BORDER_WIDTH_THIN);
                }
            }
        }
    },

    TERTIARY {
        void apply(JTextComponent component, TextFieldState state) {
            switch (state) {
                case DEFAULT -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);
                    component.setCaretColor(Theme.COLOR_PRIMARY);
                    component.setFont(Theme.FONT_MEDIUM_BASE);
                    component.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

                    BoxDecoration.ensure(component).borderColor(Theme.COLOR_BORDER)
                            .borderWidth(BoxMeasure.pixels(0))
                            .borderBottomWidth(BoxMeasure.pixels(1));
                }

                case FOCUSED -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);

                    BoxDecoration.ensure(component).borderColor(Theme.COLOR_BORDER_HOVER)
                            .borderBottomWidth(BoxMeasure.pixels(1));
                }
            }
        }
    };

    abstract void apply(JTextComponent component, TextFieldState state);
}
