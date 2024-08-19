import java.awt.*;
import java.awt.event.*;

public class ShapePanel extends Panel {
    private Shape shape;
    private Point mousePt;
    private Image offscreen;
    private Graphics offG;
    private boolean isDragging = false; // Indicates if the shape is being dragged
    private Color color;
    private CommandHistory commandHistory;
    private boolean isSelected = false;
    private Frame parentFrame;
    private PopupMenu popup;
    private Button trashButton;

    public ShapePanel(Shape shape, Frame parentFrame, CommandHistory commandHistory, Button trashButton) {
        this.shape = shape;
        this.parentFrame = parentFrame;
        this.commandHistory = commandHistory;
        this.trashButton = trashButton;
        this.color = shape.getColor();
        initialize();
    }

    public ShapePanel(Shape shape, CommandHistory commandHistory) {
        this.shape = shape;
        this.commandHistory = commandHistory;
        initialize();
    }

    private void initialize() {
        Dimension size = shape.getDimension();
        setSize(size);
        setPreferredSize(size);
        preparePopupMenu();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isSelected) {
                    int dx = e.getX() - mousePt.x;
                    int dy = e.getY() - mousePt.y;
                    Rectangle bounds = getBounds();
                    bounds.translate(dx, dy);
                    setBounds(bounds);
                    isDragging = true;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                if (e.getButton() == MouseEvent.BUTTON3 && popup != null) {
                    popup.show(ShapePanel.this, e.getX(), e.getY());
                } else if (e.isControlDown()) {
                    toggleSelection();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging && trashButton != null) {
                    Point mouseLoc = e.getLocationOnScreen();
                    Rectangle bounds = new Rectangle(trashButton.getLocationOnScreen(), trashButton.getSize());
                    if (bounds.contains(mouseLoc)) {
                        Panel parentPanel = (Panel) getParent();
                        RemoveShapeCommand deleteCommand = new RemoveShapeCommand(ShapePanel.this, parentPanel);
                        commandHistory.addCommand(deleteCommand);
                        deleteCommand.execute();
                    }
                    isDragging = false;
                }
            }
        });
    }

    private void preparePopupMenu() {
        popup = new PopupMenu();
        MenuItem editColor = new MenuItem("Edit Color");
        editColor.addActionListener(e -> {
            Color newColor = showColorChooser();
            ChangeColorCommand colorCommand = new ChangeColorCommand(this, shape, newColor);
            commandHistory.addCommand(colorCommand);
            shape.setColor(newColor);
            repaint();
        });
        popup.add(editColor);

        MenuItem editSize = new MenuItem("Edit Size");
        editSize.addActionListener(e -> {
            CustomInputDialog inputDialog;
            if (this.getShape() instanceof CircleShape) {
                inputDialog = new CustomInputDialog(parentFrame, "Input", "Enter new radius:");
            } else {
                inputDialog = new CustomInputDialog(parentFrame, "Input", "Enter new height:", "Enter new width:");
            }
            String[] inputValues = inputDialog.showDialog();
            if (inputValues != null) {
                if (inputValues.length == 1) {
                    int newRadius = Integer.parseInt(inputValues[0]);
                    int oldRadius = ((CircleShape) this.getShape()).getRadius();
                    ChangeSizeCommand sizeCommand = new ChangeSizeCommand(this, oldRadius, oldRadius, newRadius,
                            newRadius);
                    commandHistory.addCommand(sizeCommand);
                    sizeCommand.execute();
                } else if (inputValues.length == 2) {
                    int newHeight = Integer.parseInt(inputValues[0]);
                    int newWidth = Integer.parseInt(inputValues[1]);
                    int oldHeight = this.getShape().getDimension().height;
                    int oldWidth = this.getShape().getDimension().width;
                    ChangeSizeCommand sizeCommand = new ChangeSizeCommand(this, oldHeight, oldWidth, newHeight,
                            newWidth);
                    commandHistory.addCommand(sizeCommand);
                    sizeCommand.execute();
                }
            }
        });
        popup.add(editSize);

        add(popup);
    }

    private Color showColorChooser() {
        CustomColorChooser colorChooser = new CustomColorChooser(parentFrame);
        return colorChooser.showDialog();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }

    public void toggleSelection() {
        isSelected = !isSelected;
        repaint();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (offscreen == null || offscreen.getWidth(this) != getWidth() || offscreen.getHeight(this) != getHeight()) {
            // Reinitialize the offscreen image if it's null or the size has changed
            prepareOffscreenImage();
        }
        offG.setColor(getBackground());
        offG.fillRect(0, 0, getWidth(), getHeight());
        offG.setColor(color); // Ensure the correct color is set before drawing
        shape.draw((Graphics2D) offG, 0, 0);
        g.drawImage(offscreen, 0, 0, this);
    }

    private void prepareOffscreenImage() {
        offscreen = createImage(getWidth(), getHeight());
        offG = offscreen.getGraphics();
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color; // Set the ShapePanel's color field
        if (shape != null) {
            shape.setColor(color); // Make sure to also set the Shape's color
        }
        repaint(); // Repaint the panel with the new color
    }
}
