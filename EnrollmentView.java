import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EnrollmentView {

    public static Scene getScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        VBox panel = new VBox(15);
        panel.getStyleClass().add("glass-pane");
        panel.setMaxWidth(400);
        panel.setAlignment(Pos.CENTER);

        Label title = new Label("ENROLLMENT PROTOCOL");
        title.getStyleClass().add("header-label");

        ComboBox<String> memberBox = new ComboBox<>();
        memberBox.setPromptText("Select Entity");
        memberBox.getItems().addAll(DatabaseHandler.getMemberNames());
        memberBox.setPrefWidth(250);

        ComboBox<String> sportBox = new ComboBox<>();
        sportBox.setPromptText("Select Discipline");
        sportBox.getItems().addAll(DatabaseHandler.getSports());
        sportBox.setPrefWidth(250);

        Label msg = new Label("");

        Button enrollBtn = new Button("CONFIRM ENROLLMENT");
        enrollBtn.setOnAction(e -> {
            String member = memberBox.getValue();
            String sport = sportBox.getValue();

            if (member != null && sport != null) {
                DatabaseHandler.enrollMember(member, sport);
                msg.setText("MEMBERSHIP UPDATED");
                msg.setTextFill(Color.CYAN);
            } else {
                msg.setText("SELECTION REQUIRED");
                msg.setTextFill(Color.RED);
            }
        });

        Button backBtn = new Button("RETURN");
        backBtn.getStyleClass().add("button-danger");
        backBtn.setOnAction(e -> SportsClubManagementSystem.changeScene(DashboardView.getScene()));

        panel.getChildren().addAll(title, memberBox, sportBox, enrollBtn, msg);
        root.getChildren().addAll(panel, backBtn);

        return new Scene(root, 800, 600);
    }
}
