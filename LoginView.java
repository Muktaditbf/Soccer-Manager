import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginView {

    public static Parent getView() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root"); // Ensure root background is applied if used as scene root

        // Glass Panel
        VBox panel = new VBox(15);
        panel.getStyleClass().add("glass-pane");
        panel.setMaxWidth(350);
        panel.setAlignment(Pos.CENTER);

        Label title = new Label("SYSTEM ACCESS");
        title.getStyleClass().add("header-label");

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Label msgLabel = new Label("");
        msgLabel.setTextFill(Color.RED);

        Button loginBtn = new Button("INITIATE LOGIN");
        loginBtn.setOnAction(e -> {
            String user = userField.getText();
            String pass = passField.getText();

            if (DatabaseHandler.validateLogin(user, pass)) {
                // Navigate to Dashboard
                SportsClubManagementSystem.changeScene(DashboardView.getScene());
            } else {
                msgLabel.setText("ACCESS DENIED");
            }
        });

        panel.getChildren().addAll(title, userField, passField, loginBtn, msgLabel);
        root.getChildren().add(panel);

        return root;
    }
}
