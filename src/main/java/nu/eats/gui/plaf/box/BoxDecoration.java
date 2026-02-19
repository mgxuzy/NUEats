package nu.eats.gui.plaf.box;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

import static nu.eats.gui.plaf.Constants.KEY_BOX_DECORATION;

public class BoxDecoration {
    private static final BoxMeasure ZERO_MEASURE = BoxMeasure.pixels(0);

    private final RoundRectangle2D.Double contentBox = new RoundRectangle2D.Double();
    private final RoundRectangle2D.Double borderBox = new RoundRectangle2D.Double();
    private final BorderSide top = new BorderSide();
    private final BorderSide bottom = new BorderSide();
    private final BorderSide left = new BorderSide();
    private final BorderSide right = new BorderSide();

    private final JComponent component;

    private BoxMeasure borderWidth = ZERO_MEASURE;
    private boolean hasBorder = false;
    private boolean hasEdgeBorders = false;
    private Color borderColor;
    private BasicStroke borderStroke;
    private double width = -1;
    private double height = -1;
    private boolean isDirty = true;
    private float resolvedBorderWidth = 0;
    private BoxMeasure arcWidth = ZERO_MEASURE;
    private BoxMeasure arcHeight = ZERO_MEASURE;
    private float resolvedArcWidth = 0;
    private float resolvedArcHeight = 0;
    private Shape shape = Shape.RECTANGLE;

    private BoxDecoration(JComponent component) {
        this.component = component;
    }

    public static BoxDecoration ensure(JComponent component) {
        if (component.getClientProperty(KEY_BOX_DECORATION) instanceof BoxDecoration boxDecoration) {
            return boxDecoration;
        }

        var boxDecoration = new BoxDecoration(component);

        component.putClientProperty(KEY_BOX_DECORATION, boxDecoration);

        return boxDecoration;
    }

    public void setDirty() {
        if (!isDirty) {
            isDirty = true;

            SwingUtilities.invokeLater(component::repaint);
        }
    }

    public BoxDecoration shape(Shape shape) {
        if (this.shape == shape) {
            return this;
        }

        this.shape = shape;

        setDirty();

        return this;
    }

    public BoxDecoration borderColor(Color borderColor) {
        if (Objects.equals(this.borderColor, borderColor)) {
            return this;
        }

        this.borderColor = borderColor;

        setDirty();

        return this;
    }

    public BoxDecoration borderWidth(BoxMeasure borderWidth) {
        if (this.borderWidth.equals(borderWidth)) {
            return this;
        }

        this.borderWidth = borderWidth;
        this.hasBorder = borderWidth.value() > 0;
        this.hasEdgeBorders = false;

        setDirty();

        return this;
    }

    public BoxDecoration borderTopWidth(BoxMeasure width) {
        return setIndividualBorderWidth(top, width);
    }

    public BoxDecoration borderBottomWidth(BoxMeasure width) {
        return setIndividualBorderWidth(bottom, width);
    }

    public BoxDecoration borderLeftWidth(BoxMeasure width) {
        return setIndividualBorderWidth(left, width);
    }

    public BoxDecoration borderRightWidth(BoxMeasure width) {
        return setIndividualBorderWidth(right, width);
    }

    public BoxDecoration borderTopColor(Color color) {
        return setIndividualBorderColor(top, color);
    }

    public BoxDecoration borderBottomColor(Color color) {
        return setIndividualBorderColor(bottom, color);
    }

    public BoxDecoration borderLeftColor(Color color) {
        return setIndividualBorderColor(left, color);
    }

    public BoxDecoration borderRightColor(Color color) {
        return setIndividualBorderColor(right, color);
    }

    public BoxDecoration borderRadius(double radius) {
        return borderRadius(BoxMeasure.pixels(radius));
    }

    public BoxDecoration borderRadius(double arcWidth, double arcHeight) {
        return borderRadius(BoxMeasure.pixels(arcWidth), BoxMeasure.pixels(arcHeight));
    }

    public BoxDecoration borderRadius(BoxMeasure radius) {
        return borderRadius(radius, radius);
    }

    public BoxDecoration borderRadius(BoxMeasure arcWidth, BoxMeasure arcHeight) {
        if (this.arcWidth.equals(arcWidth) && this.arcHeight.equals(arcHeight)) {
            return this;
        }

        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;

        setDirty();

        return this;
    }

    public BoxDecoration size(double width, double height) {
        if (this.width == width && this.height == height) {
            return this;
        }

        this.width = width;
        this.height = height;

        setDirty();

        return this;
    }

    private BoxDecoration setIndividualBorderWidth(BorderSide side, BoxMeasure width) {
        if (side.width.equals(width)) {
            return this;
        }

        side.width = width;

        this.hasEdgeBorders = true;
        this.hasBorder = false;

        setDirty();

        return this;
    }

    private BoxDecoration setIndividualBorderColor(BorderSide side, Color color) {
        if (Objects.equals(side.color, color)) {
            return this;
        }

        side.color = color;

        setDirty();

        return this;
    }

    private void validate() {
        if (!isDirty) {
            return;
        }

        double minDimension = Math.min(width, height);

        resolvedBorderWidth = (float) borderWidth.resolve(minDimension);
        top.resolvedWidth = (float) top.width.resolve(height);
        bottom.resolvedWidth = (float) bottom.width.resolve(height);
        left.resolvedWidth = (float) left.width.resolve(width);
        right.resolvedWidth = (float) right.width.resolve(width);

        resolvedArcWidth = (float) arcWidth.resolve(minDimension);
        resolvedArcHeight = (float) arcHeight.resolve(minDimension);

        double effectiveArcWidth = (shape == Shape.CIRCLE) ? width : Math.min(resolvedArcWidth, minDimension);
        double effectiveArcHeight = (shape == Shape.CIRCLE) ? height : Math.min(resolvedArcHeight, minDimension);

        if (hasBorder && resolvedBorderWidth > 0) {
            validateUniformBorder(effectiveArcWidth, effectiveArcHeight);
        } else if (hasEdgeBorders) {
            validateEdgeBorders(effectiveArcWidth, effectiveArcHeight);
        } else {
            contentBox.setRoundRect(0, 0, width, height, effectiveArcWidth, effectiveArcHeight);
        }

        isDirty = false;
    }

    private void validateUniformBorder(double effectiveArcWidth, double effectiveArcHeight) {
        if (borderStroke == null || borderStroke.getLineWidth() != resolvedBorderWidth) {
            borderStroke = new BasicStroke(resolvedBorderWidth);
        }

        double edgeOffset = resolvedBorderWidth * 0.5;
        double contentInset = resolvedBorderWidth;

        borderBox.setRoundRect(
                edgeOffset, edgeOffset,
                width - resolvedBorderWidth, height - resolvedBorderWidth,
                effectiveArcWidth, effectiveArcHeight);

        contentBox.setRoundRect(
                contentInset, contentInset,
                Math.max(0, width - contentInset * 2), Math.max(0, height - contentInset * 2),
                Math.max(0, effectiveArcWidth - resolvedBorderWidth),
                Math.max(0, effectiveArcHeight - resolvedBorderWidth));
    }

    private void validateEdgeBorders(double effectiveArcWidth, double effectiveArcHeight) {
        top.updateAsHorizontal(0, width, top.resolvedWidth * 0.5);
        bottom.updateAsHorizontal(0, width, height - bottom.resolvedWidth * 0.5);
        left.updateAsVertical(0, height, left.resolvedWidth * 0.5);
        right.updateAsVertical(0, height, width - right.resolvedWidth * 0.5);

        double maxBorder = Math.max(
                Math.max(left.resolvedWidth, right.resolvedWidth),
                Math.max(top.resolvedWidth, bottom.resolvedWidth));

        contentBox.setRoundRect(
                left.resolvedWidth, top.resolvedWidth,
                Math.max(0, width - left.resolvedWidth - right.resolvedWidth),
                Math.max(0, height - top.resolvedWidth - bottom.resolvedWidth),
                Math.max(0, effectiveArcWidth - maxBorder),
                Math.max(0, effectiveArcHeight - maxBorder));
    }

    public void paint(Graphics2D graphics2D, JComponent c, double w, double h) {
        if (this.width != w || this.height != h) {
            this.width = w;
            this.height = h;
            this.isDirty = true;
        }

        if (isDirty) {
            validate();
        }

        Color bg = c.getBackground();

        if (bg != null) {
            graphics2D.setColor(bg);
            graphics2D.fill(contentBox);
        }

        if (hasBorder && resolvedBorderWidth > 0) {
            graphics2D.setStroke(borderStroke);
            graphics2D.setColor(borderColor);
            graphics2D.draw(borderBox);
        } else if (hasEdgeBorders) {
            top.paint(graphics2D);
            bottom.paint(graphics2D);
            left.paint(graphics2D);
            right.paint(graphics2D);
        }
    }

    public enum Shape {
        RECTANGLE, CIRCLE
    }

    /**
     * Encapsulates the width, color, stroke, and geometry for a single border side.
     */
    private static class BorderSide {
        final Line2D.Double line = new Line2D.Double();
        BoxMeasure width = ZERO_MEASURE;
        Color color;
        BasicStroke stroke;
        float resolvedWidth = 0;

        void ensureStroke() {
            if (stroke == null || stroke.getLineWidth() != resolvedWidth) {
                stroke = new BasicStroke(resolvedWidth);
            }
        }

        void updateAsHorizontal(double x1, double x2, double y) {
            if (resolvedWidth > 0) {
                ensureStroke();
                line.setLine(x1, y, x2, y);
            }
        }

        void updateAsVertical(double y1, double y2, double x) {
            if (resolvedWidth > 0) {
                ensureStroke();
                line.setLine(x, y1, x, y2);
            }
        }

        void paint(Graphics2D g) {
            if (resolvedWidth > 0) {
                g.setStroke(stroke);
                g.setColor(color);
                g.draw(line);
            }
        }
    }
}