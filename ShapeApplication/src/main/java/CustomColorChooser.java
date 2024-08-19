import java.awt.*;

public class CustomColorChooser extends Dialog {
    private Color selectedColor = null; // Initialize selectedColor as null
    private static final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };

    public CustomColorChooser(Frame parent) {
        super(parent, "Choose Color", true);
        setLayout(new GridLayout(colors.length + 1, 1)); // +1 for the Cancel button
        for (Color color : colors) {
            addButton(color);
        }
        addCancelButton(); // Add the Cancel button
        setSize(200, 250); // Increased height to accommodate the Cancel button
        setLocationRelativeTo(parent);
    }

    private void addButton(Color color) {
        Button button = new Button();
        button.setBackground(color);
        button.addActionListener(e -> {
            selectedColor = color;
            setVisible(false);
        });
        add(button);
    }

    private void addCancelButton() {
        Button cancelButton = new Button("Cancel"); // Create the Cancel button
        cancelButton.addActionListener(e -> {
            selectedColor = null; // Set selectedColor to null to indicate cancellation
            setVisible(false);
        });
        add(cancelButton);
    }

    public Color showDialog() {
        setVisible(true);
        return selectedColor;
    }
}
