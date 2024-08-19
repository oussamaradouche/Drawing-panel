import java.awt.*;

class RemoveShapeCommand implements Command {
    private ShapePanel shapePanel;
    private Panel canvas; // The panel from which the shape is removed

    public RemoveShapeCommand(ShapePanel shapePanel, Panel canvas) {
        this.shapePanel = shapePanel;
        this.canvas = canvas;
    }

    @Override
    public void execute() {
        canvas.remove(shapePanel);
        canvas.validate();
        canvas.repaint();
    }

    @Override
    public void undo() {
        canvas.add(shapePanel);
        canvas.validate();
        canvas.repaint();
    }
}
