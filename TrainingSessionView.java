import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TrainingSessionView {

    public static Scene getScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        VBox panel = new VBox(15);
        panel.getStyleClass().add("glass-pane");
        panel.setMaxWidth(400);
        panel.setAlignment(Pos.CENTER);

        Label title = new Label("SCHEDULE SESSION");
        title.getStyleClass().add("header-label");

        ComboBox<String> sportBox = new ComboBox<>();
        sportBox.setPromptText("Select Discipline");
        sportBox.getItems().addAll(DatabaseHandler.getSports());
        sportBox.setPrefWidth(250);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Target Date");
        datePicker.setPrefWidth(250);

        Label msg = new Label("");

        Button scheduleBtn = new Button("INITIATE SCHEDULE");
        scheduleBtn.setOnAction(e -> {
            String sport = sportBox.getValue();
            var date = datePicker.getValue();

            if (sport != null && date != null) {
                DatabaseHandler.addSession(sport, date.toString());
                msg.setText("SESSION CONFIRMED");
                msg.setTextFill(Color.CYAN);
            } else {
                msg.setText("INVALID PARAMETERS");
                msg.setTextFill(Color.RED);
            }
        });

        Button backBtn = new Button("RETURN");
        backBtn.getStyleClass().add("button-danger");
        backBtn.setOnAction(e -> SportsClubManagementSystem.changeScene(DashboardView.getScene()));

        panel.getChildren().addAll(title, sportBox, datePicker, scheduleBtn, msg);
        root.getChildren().addAll(panel, backBtn);

        return new Scene(root, 800, 600);
    }
}
