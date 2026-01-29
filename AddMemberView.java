import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddMemberView {

    public static Scene getScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        VBox panel = new VBox(15);
        panel.getStyleClass().add("glass-pane");
        panel.setMaxWidth(400);
        panel.setAlignment(Pos.CENTER);

        Label title = new Label("NEW MEMBER REGISTRATION");
        title.getStyleClass().add("header-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Comms Frequency (Phone)");

        TextField emailField = new TextField();
        emailField.setPromptText("Neural Link (Email)");

        Label glbMsg = new Label("");

        Button saveBtn = new Button("REGISTER ENTITY");
        saveBtn.setOnAction(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (name.isEmpty() || phone.isEmpty()) {
                glbMsg.setText("DATA INCOMPLETE");
                glbMsg.setTextFill(Color.RED);
            } else {
                if (DatabaseHandler.addMember(name, phone, email)) {
                    glbMsg.setText("REGISTRATION SUCCESSFUL");
                    glbMsg.setTextFill(Color.CYAN);
                    nameField.clear();
                    phoneField.clear();
                    emailField.clear();
                } else {
                    glbMsg.setText("DATABASE ERROR");
                    glbMsg.setTextFill(Color.RED);
                }
            }
        });

        Button backBtn = new Button("ABORT / RETURN");
        backBtn.getStyleClass().add("button-danger");
        backBtn.setOnAction(e -> SportsClubManagementSystem.changeScene(DashboardView.getScene()));

        panel.getChildren().addAll(title, nameField, phoneField, emailField, saveBtn, glbMsg);
        root.getChildren().addAll(panel, backBtn);

        return new Scene(root, 800, 600);
    }
}
