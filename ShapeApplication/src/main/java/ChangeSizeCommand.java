import java.awt.*;

class ChangeSizeCommand implements Command {
    private ShapePanel shapePanel;
    private int oldWidth;
    private int oldHeight;
    private int newWidth;
    private int newHeight;

    public ChangeSizeCommand(ShapePanel shapePanel, int oldWidth, int oldHeight, int newWidth, int newHeight) {
        this.shapePanel = shapePanel;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void execute() {
        shapePanel.getShape().setWidth(newWidth);
        shapePanel.getShape().setHeight(newHeight);
        shapePanel.setSize(new Dimension(newWidth, newHeight));
        shapePanel.setPreferredSize(new Dimension(newWidth, newHeight));
        shapePanel.getParent().validate();
        shapePanel.repaint();
    }

    @Override
    public void undo() {
        shapePanel.getShape().setWidth(oldWidth);
        shapePanel.getShape().setHeight(oldHeight);
        shapePanel.setSize(new Dimension(oldWidth, oldHeight));
        shapePanel.setPreferredSize(new Dimension(oldWidth, oldHeight));
        shapePanel.getParent().validate();
        shapePanel.repaint();
    }
}
