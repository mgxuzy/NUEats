package nu.eats.gui.store.components;

import nu.eats.gui.components.FitLayout;
import nu.eats.gui.components.FitPanel;
import nu.eats.gui.components.WrapLayout;
import nu.eats.gui.components.WrapPanel;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;

public class StoreItemList extends JPanel {
    public StoreItemList() {
        super(new WrapLayout(WrapLayout.LEFT, Theme.SPACING_XL, Theme.SPACING_XL));

        // setOpaque(false);

        setBackground(Color.red);
    }

    public void addRow(StoreItemRow row) {
        add(row);
        revalidate();
    }

    public void clear() {
        removeAll();
    }
}
