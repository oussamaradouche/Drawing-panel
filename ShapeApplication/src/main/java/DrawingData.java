import java.io.Serializable;
import java.util.List;

public class DrawingData implements Serializable {
    private List<DrawingShapeData> shapesData;

    public DrawingData(List<DrawingShapeData> shapesData) {
        this.shapesData = shapesData;
    }

    public List<DrawingShapeData> getShapesData() {
        return shapesData;
    }
}
