import java.awt.*;

public class ChangeColorCommand implements Command {
    private Shape shape;
    private Color oldColor;
    private Color newColor;
    private ShapePanel shapePanel;

    public ChangeColorCommand(ShapePanel shapePanel, Shape shape, Color newColor) {
        this.shape = shape;
        this.oldColor = shape.getColor();

        this.newColor = newColor;
        this.shapePanel = shapePanel;
    }

    @Override
    public void execute() {
        System.out.println("Executing ChangeColorCommand");
        shape.setColor(newColor);
        shapePanel.repaint();

    }

    @Override
    public void undo() {
        System.out.println("Undoing color change: Current color=" + shape.getColor() + ", reverting to " + oldColor);
        shape.setColor(oldColor);
        shapePanel.repaint();
    }

}
