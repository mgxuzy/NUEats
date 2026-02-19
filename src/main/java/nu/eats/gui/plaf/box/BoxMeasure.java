package nu.eats.gui.plaf.box;

public class BoxMeasure {
    private final double value;
    private final BoxUnit unit;

    private BoxMeasure(double value, BoxUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    private BoxMeasure(double value) {
        this(value, BoxUnit.PIXEL);
    }

    public static BoxMeasure pixels(double value) {
        return new BoxMeasure(value, BoxUnit.PIXEL);
    }

    public static BoxMeasure percent(double value) {
        return new BoxMeasure(value, BoxUnit.PERCENT);
    }

    public static BoxMeasure normalizedPercent(double value) {
        return new BoxMeasure(value, BoxUnit.NORMALIZED_PERCENT);
    }

    public double resolve(double total) {
        return switch (unit) {
            case PIXEL -> value;
            case PERCENT -> total * (value * 0.01);
            case NORMALIZED_PERCENT -> total * value;
        };
    }

    public double value() {
        return value;
    }

    public BoxUnit unit() {
        return unit;
    }
}
