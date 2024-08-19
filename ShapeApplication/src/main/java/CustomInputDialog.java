import java.awt.*;

class CustomInputDialog extends Dialog {
    private TextField[] textFields;
    private Button okButton;
    private Button cancelButton;
    private String[] inputValues;

    public CustomInputDialog(Frame parent, String title, String... prompts) {
        super(parent, title, true);
        setLayout(new BorderLayout());

        Panel panel = new Panel();
        textFields = new TextField[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            textFields[i] = new TextField(10);
            panel.add(new Label(prompts[i]));
            panel.add(textFields[i]);
        }
        add(panel, BorderLayout.CENTER);

        Panel buttonPanel = new Panel();
        okButton = new Button("OK");
        okButton.addActionListener(e -> {
            inputValues = new String[prompts.length];
            for (int i = 0; i < prompts.length; i++) {
                inputValues[i] = textFields[i].getText();
            }
            setVisible(false);
        });
        buttonPanel.add(okButton);

        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(e -> {
            inputValues = null;
            setVisible(false);
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public String[] showDialog() {
        setVisible(true);
        return inputValues;
    }
}
