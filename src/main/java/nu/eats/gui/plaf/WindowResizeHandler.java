package nu.eats.gui.plaf;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class WindowResizeHandler implements MouseInputListener {

    private static final int BORDER_DRAG_THICKNESS = 7;
    private static final int CORNER_DRAG_WIDTH = 16;

    private static final int[] CURSOR_MAPPING = {
            Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
            Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
            Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
            Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };

    private final JRootPane rootPane;
    private final Component titlePane;

    private boolean isDraggingWindow;
    private int dragCursorType;
    private Cursor lastCursor;

    // Stable resize fields
    private Point startDrag;
    private Rectangle startBounds;

    public WindowResizeHandler(JRootPane rootPane, Component titlePane) {
        this.rootPane = rootPane;
        this.titlePane = titlePane;
    }

    private Window getWindow(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof Window window) {
            return window;
        } else if (source instanceof Component component) {
            return SwingUtilities.getWindowAncestor(component);
        }
        return null;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        Window window = getWindow(event);
        if (window == null)
            return;

        Frame frame = (window instanceof Frame f) ? f : null;
        Dialog dialog = (window instanceof Dialog d) ? d : null;

        int frameState = (frame != null) ? frame.getExtendedState() : 0;

        Point point = event.getPoint();
        Component source = (Component) event.getSource();
        if (source != window) {
            point = SwingUtilities.convertPoint(source, point, window);
        }

        // Setup initial state
        startDrag = event.getLocationOnScreen();
        startBounds = window.getBounds();

        // Determine action
        if (rootPane != null && rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
            return;
        }

        // Check if on TitlePane
        boolean isOverTitle = false;
        if (titlePane != null && titlePane.isVisible()) {
            Point pTitle = SwingUtilities.convertPoint(window, point, titlePane);
            if (titlePane.contains(pTitle)) {
                isOverTitle = true;
            }
        }

        // Calculate corner for resize
        dragCursorType = 0;
        int corner = calculateCorner(window, point.x, point.y);
        int cursorType = getCursorType(corner);

        boolean resizable = (frame != null && frame.isResizable() && (frameState & Frame.MAXIMIZED_BOTH) == 0)
                || (dialog != null && dialog.isResizable());

        if (cursorType != 0 && resizable) {
            dragCursorType = cursorType; // Resize mode
            isDraggingWindow = false;
        } else if (isOverTitle) {
            dragCursorType = 0;
            if (frame != null && (frameState & Frame.MAXIMIZED_BOTH) == 0) {
                isDraggingWindow = true; // Move mode
            } else if (dialog != null) {
                isDraggingWindow = true;
            }
            if ((frame != null && (frameState & Frame.MAXIMIZED_BOTH) != 0)) {
                isDraggingWindow = false;
            }
        } else {
            isDraggingWindow = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        Window window = getWindow(event);
        if (window == null || startDrag == null || startBounds == null)
            return;

        Point currentScreen = event.getLocationOnScreen();
        int dx = currentScreen.x - startDrag.x;
        int dy = currentScreen.y - startDrag.y;

        if (isDraggingWindow) {
            window.setLocation(startBounds.x + dx, startBounds.y + dy);
        } else if (dragCursorType != 0) {
            Rectangle bounds = new Rectangle(startBounds);
            Dimension min = window.getMinimumSize();

            switch (dragCursorType) {
                case Cursor.N_RESIZE_CURSOR -> {
                    bounds.y += dy;
                    bounds.height -= dy;
                }
                case Cursor.S_RESIZE_CURSOR -> {
                    bounds.height += dy;
                }
                case Cursor.W_RESIZE_CURSOR -> {
                    bounds.x += dx;
                    bounds.width -= dx;
                }
                case Cursor.E_RESIZE_CURSOR -> {
                    bounds.width += dx;
                }
                case Cursor.NW_RESIZE_CURSOR -> {
                    bounds.x += dx;
                    bounds.width -= dx;
                    bounds.y += dy;
                    bounds.height -= dy;
                }
                case Cursor.NE_RESIZE_CURSOR -> {
                    bounds.width += dx;
                    bounds.y += dy;
                    bounds.height -= dy;
                }
                case Cursor.SW_RESIZE_CURSOR -> {
                    bounds.x += dx;
                    bounds.width -= dx;
                    bounds.height += dy;
                }
                case Cursor.SE_RESIZE_CURSOR -> {
                    bounds.width += dx;
                    bounds.height += dy;
                }
            }

            // Constrain minimum size
            if (bounds.width < min.width) {
                bounds.width = min.width;
                if (dragCursorType == Cursor.W_RESIZE_CURSOR || dragCursorType == Cursor.NW_RESIZE_CURSOR
                        || dragCursorType == Cursor.SW_RESIZE_CURSOR) {
                    bounds.x = startBounds.x + startBounds.width - min.width;
                }
            }
            if (bounds.height < min.height) {
                bounds.height = min.height;
                if (dragCursorType == Cursor.N_RESIZE_CURSOR || dragCursorType == Cursor.NW_RESIZE_CURSOR
                        || dragCursorType == Cursor.NE_RESIZE_CURSOR) {
                    bounds.y = startBounds.y + startBounds.height - min.height;
                }
            }

            window.setBounds(bounds);
            rootPane.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        isDraggingWindow = false;
        dragCursorType = 0;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
            return;
        }

        Window window = getWindow(event);
        if (window == null)
            return;

        Frame frame = (window instanceof Frame f) ? f : null;
        Dialog dialog = (window instanceof Dialog d) ? d : null;

        Point point = event.getPoint();
        if (event.getSource() != window) {
            point = SwingUtilities.convertPoint((Component) event.getSource(), point, window);
        }

        int cursorType = getCursorType(calculateCorner(window, point.x, point.y));

        boolean isFrameResizable = (frame != null && frame.isResizable()
                && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0);
        boolean isDialogResizable = (dialog != null && dialog.isResizable());

        if (cursorType != 0 && (isFrameResizable || isDialogResizable)) {
            window.setCursor(Cursor.getPredefinedCursor(cursorType));
        } else {
            window.setCursor(lastCursor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        Window window = getWindow(event);
        if (window == null)
            return;
        lastCursor = window.getCursor();
        mouseMoved(event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        Window window = getWindow(event);
        if (window == null)
            return;
        window.setCursor(lastCursor);
    }

    // mouseClicked kept same logic
    @Override
    public void mouseClicked(MouseEvent event) {
        Window window = getWindow(event);
        if (!(window instanceof Frame frame)) {
            return;
        }

        if (titlePane != null) {
            Point point = event.getPoint();
            if (event.getSource() != window) {
                point = SwingUtilities.convertPoint((Component) event.getSource(), point, window);
            }
            Point convertedPoint = SwingUtilities.convertPoint(window, point, titlePane);

            int state = frame.getExtendedState();

            if (titlePane.contains(convertedPoint)) {
                boolean isLeftClick = SwingUtilities.isLeftMouseButton(event);
                boolean isDoubleClick = (event.getClickCount() % 2) == 0;

                if (isDoubleClick && isLeftClick && frame.isResizable()) {
                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                        frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                    } else {
                        frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                    }
                }
            }
        }
    }

    private int calculateCorner(Window w, int x, int y) {
        Insets insets = w.getInsets();
        int xPosition = calculatePosition(x - insets.left, w.getWidth() - insets.left - insets.right);
        int yPosition = calculatePosition(y - insets.top, w.getHeight() - insets.top - insets.bottom);

        if (xPosition == -1 || yPosition == -1) {
            return -1;
        }
        return yPosition * 5 + xPosition;
    }

    private int calculatePosition(int spot, int width) {
        if (spot < BORDER_DRAG_THICKNESS) {
            return 0;
        }
        if (spot < CORNER_DRAG_WIDTH) {
            return 1;
        }
        if (spot >= (width - BORDER_DRAG_THICKNESS)) {
            return 4;
        }
        if (spot >= (width - CORNER_DRAG_WIDTH)) {
            return 3;
        }
        return 2;
    }

    private int getCursorType(int corner) {
        if (corner == -1) {
            return 0;
        }
        return CURSOR_MAPPING[corner];
    }
}