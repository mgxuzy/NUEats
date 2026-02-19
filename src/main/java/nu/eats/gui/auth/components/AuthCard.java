package nu.eats.gui.auth.components;

import nu.eats.gui.components.Card;
import nu.eats.gui.components.ToolBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.CloseIcon;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.KEY_VARIANT;

public class AuthCard extends Card {
    protected final JPanel mainContent;

    private final ToolBar toolBar;

    public AuthCard() {
        super(new BorderLayout());

        this.toolBar = createToolBar();

        add(toolBar, BorderLayout.NORTH);

        this.mainContent = new JPanel();
        this.mainContent.setOpaque(false);

        add(mainContent, BorderLayout.CENTER);

        setOpaque(false);
        setBackground(Theme.COLOR_BG);
    }

    protected ToolBar createToolBar() {
        var toolBar = new ToolBar();

        toolBar.addBackAction(this::unstack);
        toolBar.add(createExitButton(), ToolBar.RIGHT);

        return toolBar;
    }

    protected JButton createExitButton() {
        var closeButton = new JButton(new CloseIcon(12));

        ButtonPreset.XS.apply(closeButton);

        setBorder(null);

        closeButton.setFont(Theme.FONT_MONO_14);
        closeButton.putClientProperty(KEY_VARIANT, ButtonVariant.SECONDARY);
        closeButton.addActionListener(_ -> System.exit(0));

        return closeButton;
    }

    public ToolBar toolBar() {
        return toolBar;
    }
}
