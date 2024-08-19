import java.util.Stack;

public class CommandHistory {

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public CommandHistory() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void addCommand(Command command) {
        // Clear redo stack after a new command is added
        redoStack.clear();
        undoStack.push(command);
        System.out.println("Command added to history: " + command.getClass().getSimpleName());
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if (canUndo()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            System.out.println("Undo: " + command.getClass().getSimpleName());
        }

    }

    public void redo() {
        if (canRedo()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            System.out.println("Redo: " + command.getClass().getSimpleName());
        }
    }
}
