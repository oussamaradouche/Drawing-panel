import java.awt.*;

public class RectangleEditor implements Editor {
    private RectangleShape rectangle;

    public RectangleEditor(RectangleShape rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void changeColor(Color newColor) {
        rectangle.setColor(newColor);
    }

    @Override
    public void changeSize(int newSizeWidth, int newSizeHeight) {
        rectangle.setWidth(newSizeWidth);
        rectangle.setHeight(newSizeHeight);
    }
}
