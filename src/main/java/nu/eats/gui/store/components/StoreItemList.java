package nu.eats.gui.store.components;

import nu.eats.gui.components.FitPanel;
import nu.eats.gui.components.WrapLayout;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;

public class StoreItemList extends FitPanel {
    private final JPanel listContent;

    public StoreItemList() {
        super();

        setOpaque(false);

        listContent = new JPanel(new WrapLayout(WrapLayout.LEFT, Theme.SPACING_XL, Theme.SPACING_XL));

        listContent.setBackground(Color.red);

        add(listContent);
    }

    public void addRow(StoreItemRow row) {
        listContent.add(row);
        listContent.revalidate();
        listContent.repaint();
    }

    public void clear() {
        removeAll();
    }
}
