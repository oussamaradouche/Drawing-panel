import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ShapeToolbarDragDrop extends Frame {

    private static ShapeToolbarDragDrop instance;
    private Panel toolBar;
    private Panel canvas;
    private ShapePanel draggedShapePanel;
    private CommandHistory commandHistory;
    private Button trashButton; // New button for trash

    // Private constructor to prevent instantiation
    private ShapeToolbarDragDrop() {
        initializeUI();
    }

    public static synchronized ShapeToolbarDragDrop getInstance() {
        if (instance == null) {
            instance = new ShapeToolbarDragDrop();
        }
        return instance;
    }

    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize commandHistory
        commandHistory = new CommandHistory();

        // Create toolbar and trash button
        toolBar = new Panel();
        toolBar.setLayout(new GridLayout(0, 1)); // Vertical layout
        toolBar.setBackground(new Color(240, 240, 240)); // Light gray background
        add(toolBar, BorderLayout.WEST);

        // Add Trash Button
        createTrashButton();

        // Create canvas
        canvas = new Panel();
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(null); // Absolute positioning
        add(canvas, BorderLayout.CENTER);

        // Add shape buttons to the toolbar
        addButton(new RectangleShape(50, 50, Color.black));
        addButton(new CircleShape(50, Color.BLUE));

        // Add undo and redo buttons to the bottom panel
        Panel bottomPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        addButton(bottomPanel, "Undo");
        addButton(bottomPanel, "Redo");
        add(bottomPanel, BorderLayout.SOUTH);

        Button save = new Button("Save");
        Button load = new Button("Load");
        save.addActionListener(e -> saveToFile());
        load.addActionListener(e -> loadFile());
        Panel menu = new Panel();
        menu.add(save);
        menu.add(load);
        add(menu, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true); // Make the frame visible
    }

    private void createTrashButton() {
        trashButton = new Button("Trash");
        trashButton.setBackground(Color.RED);
        trashButton.setPreferredSize(new Dimension(60, 60));
        toolBar.add(trashButton);
    }

    private void addButton(Panel panel, String label) {
        Button button = new Button(label);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(e -> {
            if ("Undo".equals(label)) {
                undo();
            } else if ("Redo".equals(label)) {
                redo();
            }
        });
        panel.add(button);
    }

    private void addButton(Shape prototypeShape) {
        Button button = new Button() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Shape shape = prototypeShape.clone();
                Dimension size = this.getSize();
                shape.draw((Graphics2D) g, size.width / 2 - shape.getDimension().width / 2,
                        size.height / 2 - shape.getDimension().height / 2);
            }
        };
        button.setPreferredSize(new Dimension(60, 60));
        button.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (draggedShapePanel == null) {
                    Shape shape = prototypeShape.clone();
                    draggedShapePanel = new ShapePanel(shape, ShapeToolbarDragDrop.this, commandHistory, trashButton);
                    draggedShapePanel.setSize(shape.getDimension());
                    draggedShapePanel.setLocation(e.getXOnScreen() - canvas.getLocationOnScreen().x,
                            e.getYOnScreen() - canvas.getLocationOnScreen().y);
                    canvas.add(draggedShapePanel);
                    canvas.validate();
                } else {
                    draggedShapePanel.setLocation(
                            e.getXOnScreen() - canvas.getLocationOnScreen().x - draggedShapePanel.getWidth() / 2,
                            e.getYOnScreen() - canvas.getLocationOnScreen().y - draggedShapePanel.getHeight() / 2);
                    canvas.repaint();
                }
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedShapePanel != null) {
                    canvas.add(draggedShapePanel);
                    canvas.validate();
                    canvas.repaint();

                    commandHistory.addCommand(new AddShapeCommand(draggedShapePanel, canvas));

                    draggedShapePanel = null;
                }
            }
        });
        toolBar.add(button);
    }

    private void undo() {
        if (commandHistory.canUndo()) {
            commandHistory.undo();
            canvas.repaint();
        }
    }

    private void redo() {
        if (commandHistory.canRedo()) {
            commandHistory.redo();
            canvas.repaint();
        }
    }

    private DrawingData extractDrawingData() {
        List<DrawingShapeData> shapesData = new ArrayList<>();
        for (Component component : canvas.getComponents()) {
            if (component instanceof ShapePanel) {
                ShapePanel shapePanel = (ShapePanel) component;
                Shape shape = shapePanel.getShape();
                Point location = shapePanel.getLocation();
                Color color = shapePanel.getColor();
                Dimension dimension = shape.getDimension();
                shapesData.add(new DrawingShapeData(shape.getClass().getName(), location, color, dimension));
            }
        }
        return new DrawingData(shapesData);
    }

    private void saveToFile() {
        FileDialog fileDialog = new FileDialog(this, "Save", FileDialog.SAVE);
        fileDialog.setVisible(true);
        String filename = fileDialog.getFile();
        String directory = fileDialog.getDirectory();
        if (filename != null && directory != null) {
            File file = new File(directory + filename);
            try (FileOutputStream fileOut = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                DrawingMemento memento = new DrawingMemento(extractDrawingData());
                out.writeObject(memento);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFile() {
        FileDialog fileDialog = new FileDialog(this, "Load", FileDialog.LOAD);
        fileDialog.setVisible(true);
        String filename = fileDialog.getFile();
        String directory = fileDialog.getDirectory();
        if (filename != null && directory != null) {
            File file = new File(directory + filename);
            try (FileInputStream fileIn = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(fileIn)) {
                DrawingMemento memento = (DrawingMemento) in.readObject();
                loadDrawingData(memento.getDrawingData());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDrawingData(DrawingData drawingData) {
        canvas.removeAll();
        for (DrawingShapeData shapeData : drawingData.getShapesData()) {
            try {
                Class<?> shapeClass = Class.forName(shapeData.getShapeClassName());
                Constructor<?> constructor = shapeClass.getConstructor(int.class, Color.class);
                Shape shape = (Shape) constructor.newInstance(shapeData.getDimension().width, shapeData.getColor());
                ShapePanel shapePanel = new ShapePanel(shape, this, commandHistory, trashButton);
                shapePanel.setColor(shapeData.getColor());
                shapePanel.setSize(shapeData.getDimension());
                shapePanel.setLocation(shapeData.getLocation());
                canvas.add(shapePanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canvas.validate();
        canvas.repaint();
    }
}