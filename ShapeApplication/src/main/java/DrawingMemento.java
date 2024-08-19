import java.io.Serializable;

public class DrawingMemento implements Serializable {
    private static final long serialVersionUID = 1L; // This helps ensure version compatibility
    private final DrawingData drawingData;

    public DrawingMemento(DrawingData drawingData) {
        this.drawingData = drawingData;
    }

    public DrawingData getDrawingData() {
        return drawingData;
    }
}
