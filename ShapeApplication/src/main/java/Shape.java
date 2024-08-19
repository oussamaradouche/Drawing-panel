import java.awt.*;
import java.util.List;

public interface Shape {
    void draw(Graphics2D g2, int x, int y);

    Shape clone();

    void setDimension(Dimension d);

    Dimension getDimension();

    Color getColor();

    void setColor(Color color);

    List<Shape> getChildren(); // Used in composites

    void setHeight(int height); // New method

    void setWidth(int width); // New method

    Editor createEditor();
}
