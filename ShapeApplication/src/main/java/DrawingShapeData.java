import java.io.Serializable;
import java.awt.*;;

public class DrawingShapeData implements Serializable {
    private String shapeClassName;
    private Point location;
    private Color color;
    private Dimension dimension;

    public DrawingShapeData(String shapeClassName, Point location, Color color, Dimension dimension) {
        this.shapeClassName = shapeClassName;
        this.location = location;
        this.color = color;
        this.dimension = dimension;
    }

    public String getShapeClassName() {
        return shapeClassName;
    }

    public Point getLocation() {
        return location;
    }

    public Color getColor() {
        return color;
    }

    public Dimension getDimension() {
        return dimension;
    }
}