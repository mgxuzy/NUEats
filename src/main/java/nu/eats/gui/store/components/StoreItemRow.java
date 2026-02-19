package nu.eats.gui.store.components;

import nu.eats.domain.store.Product;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.PlaceholderIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import static nu.eats.gui.plaf.Constants.KEY_VARIANT;

public class StoreItemRow extends JButton {
    private static final int CARD_WIDTH = 160;
    private static final int CARD_HEIGHT = 210;
    private static final int CHECK_SIZE = 18;

    public StoreItemRow(Product product) {
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        var priceText = String.format("₱%.2f", product.price());
        var imageUri = product.imageUri();

        Icon image;

        try {
            image = scaleIcon(new ImageIcon(URI.create(imageUri).toURL()), 110, 110);
        } catch (MalformedURLException | NullPointerException | IllegalArgumentException _) {
            image = new PlaceholderIcon(110, 110);
        }

        var itemImageDisplay = new JLabel();

        itemImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        itemImageDisplay.setIcon(image);

        add(itemImageDisplay, BorderLayout.CENTER);

        // --- Item Summary ---
        var itemSummary = new JPanel();

        itemSummary.setLayout(new BoxLayout(itemSummary, BoxLayout.Y_AXIS));
        itemSummary.setOpaque(false);

        var itemNameLabel = new JLabel(product.name());

        itemNameLabel.setForeground(Theme.COLOR_FG);
        itemNameLabel.setFont(Theme.FONT_MEDIUM_14);
        itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        var itemPriceLabel = new JLabel(priceText);

        itemPriceLabel.setForeground(Theme.COLOR_FG_MUTED);
        itemPriceLabel.setFont(Theme.FONT_REGULAR_12);
        itemPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemSummary.add(Box.createVerticalStrut(12));
        itemSummary.add(itemNameLabel);
        itemSummary.add(Box.createVerticalStrut(4));
        itemSummary.add(itemPriceLabel);

        add(itemSummary, BorderLayout.SOUTH);

        BoxDecoration.ensure(this).borderRadius(Theme.FONT_SIZE_XS);

        putClientProperty(KEY_VARIANT, ButtonVariant.SECONDARY);

        model.addChangeListener(_ -> {
            switch (ButtonState.of(model)) {
                case DISABLED -> {
                    itemNameLabel.setForeground(Theme.COLOR_PLACEHOLDER_FG);

                    var strikeFont = getFont()
                            .deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON));

                    itemNameLabel.setFont(strikeFont);
                }

                case DEFAULT -> {
                    itemNameLabel.setFont(getFont());
                    itemNameLabel.setForeground(Theme.COLOR_FG);
                }
            }
        });
    }

    private static Path2D getCheck(int x, int y) {
        var checkPath = new Path2D.Float();

        // Start (Left wing)
        var x1 = x + (CHECK_SIZE * 0.28f);
        var y1 = y + (CHECK_SIZE * 0.52f);

        // Pivot (Bottom point)
        var x2 = x + (CHECK_SIZE * 0.46f);
        var y2 = y + (CHECK_SIZE * 0.72f);

        // End (Right wing)
        var x3 = x + (CHECK_SIZE * 0.74f);
        var y3 = y + (CHECK_SIZE * 0.32f);

        checkPath.moveTo(x1, y1);
        checkPath.lineTo(x2, y2);
        checkPath.lineTo(x3, y3);

        return checkPath;
    }

    private ImageIcon scaleIcon(ImageIcon src, int width, int height) {
        var sourceImage = src.getImage();
        var resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g2 = resizedBufferedImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(sourceImage, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(resizedBufferedImage);
    }
}
