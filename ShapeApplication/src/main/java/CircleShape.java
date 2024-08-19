import java.awt.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class CircleShape implements Shape, Serializable {
    private int diameter;
    private Color color;

    public CircleShape(int diameter, Color color) {
        this.diameter = diameter;
        this.color = color;
    }

    public CircleShape() {

    }

    public int getRadius() {
        return diameter;
    }

    @Override
    public void draw(Graphics2D g2, int x, int y) {
        g2.setColor(color);
        g2.fillOval(x, y, diameter, diameter);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(diameter, diameter);
    }

    @Override
    public Shape clone() {
        return new CircleShape(diameter, color);
    }

    @Override
    public List<Shape> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void setDimension(Dimension d) {
        this.diameter = d.width;
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
        setDimension(new Dimension(height, height));
    }

    @Override
    public void setWidth(int width) {
        setDimension(new Dimension(width, width));
    }

    @Override
    public Editor createEditor() {
        return new CircleEditor(this);
    }
}
