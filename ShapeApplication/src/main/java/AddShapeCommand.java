import java.awt.*;

class AddShapeCommand implements Command {
    private ShapePanel shapePanel;
    private Panel canvas;

    public AddShapeCommand(ShapePanel shapePanel, Panel canvas) {
        this.shapePanel = shapePanel;
        this.canvas = canvas;
    }

    @Override
    public void execute() {
        canvas.add(shapePanel);
        canvas.validate();
        canvas.repaint();
    }

    @Override
    public void undo() {
        canvas.remove(shapePanel);
        canvas.validate();
        canvas.repaint();
    }
}
