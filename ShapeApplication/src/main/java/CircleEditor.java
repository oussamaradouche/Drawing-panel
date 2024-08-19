import java.awt.*;

public class CircleEditor implements Editor {
    private CircleShape circle;

    public CircleEditor(CircleShape circle) {
        this.circle = circle;
    }

    @Override
    public void changeColor(Color newColor) {
        circle.setColor(newColor);
    }

    @Override
    public void changeSize(int newSizeWidth, int newSizeHeight) {
        circle.setWidth(newSizeWidth);
        circle.setHeight(newSizeHeight);
    }
}
