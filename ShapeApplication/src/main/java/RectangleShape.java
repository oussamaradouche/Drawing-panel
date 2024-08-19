import java.awt.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class RectangleShape implements Shape, Serializable {
    private int width;
    private int height;
    private Color color;

    public RectangleShape(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = (color == null ? Color.BLACK : color);
    }

    public RectangleShape() {
        // default constructor if needed
    }

    @Override
    public void draw(Graphics2D g2, int x, int y) {
        g2.setColor(color);
        g2.fillRect(x, y, width, height);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    @Override
    public Shape clone() {
        return new RectangleShape(width, height, color);
    }

    @Override
    public List<Shape> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void setDimension(Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public Editor createEditor() {
        return new RectangleEditor(this);
    }
}
