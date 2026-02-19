package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
import nu.eats.gui.plaf.box.BoxMeasure;

import javax.swing.*;

public enum ButtonPreset {
    XS(definer().padding(8, 12).iconGap(4).fontSize(12f).borderRadius(Theme.PILL_SHAPE_RADIUS)),
    SM(definer().padding(10, 16).iconGap(8).fontSize(14f).borderRadius(Theme.PILL_SHAPE_RADIUS)),
    MD(definer().padding(16, 24).iconGap(12).fontSize(16f).borderRadius(Theme.PILL_SHAPE_RADIUS)),
    LG(definer().padding(32, 48).iconGap(16).fontSize(20f).borderRadius(Theme.PILL_SHAPE_RADIUS)),
    XL(definer().padding(48, 64).iconGap(24).fontSize(24f).borderRadius(Theme.PILL_SHAPE_RADIUS));

    public final int verticalPadding, horizontalPadding, iconGap;
    public final float fontSize;
    public final BoxMeasure borderRadius;

    ButtonPreset(Definer definer) {
        this.verticalPadding = definer.verticalPadding;
        this.horizontalPadding = definer.horizontalPadding;
        this.iconGap = definer.iconGap;
        this.fontSize = definer.fontSize;
        this.borderRadius = definer.borderRadius;
    }

    private static Definer definer() {
        return new Definer();
    }

    public void apply(AbstractButton button) {
        button.setBorder(BorderFactory.createEmptyBorder(verticalPadding, horizontalPadding, verticalPadding,
                horizontalPadding));
        button.setFont(button.getFont().deriveFont(fontSize));
        button.setIconTextGap(iconGap);

        BoxDecoration.ensure(button).borderRadius(borderRadius);
    }

    private static class Definer {
        int verticalPadding;
        int horizontalPadding;
        int iconGap;

        float fontSize;
        BoxMeasure borderRadius;

        Definer padding(int vertical, int horizontal) {
            this.verticalPadding = vertical;
            this.horizontalPadding = horizontal;

            return this;
        }

        Definer iconGap(int gap) {
            this.iconGap = gap;

            return this;
        }

        Definer fontSize(float size) {
            this.fontSize = size;

            return this;
        }

        Definer borderRadius(BoxMeasure radius) {
            this.borderRadius = radius;

            return this;
        }
    }
}
