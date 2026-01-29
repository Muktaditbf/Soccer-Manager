import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.util.List;

public class MemberListView {

    public static Scene getScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        Label title = new Label("MEMBERSHIP DATABASE");
        title.getStyleClass().add("header-label");

        // Using ListView for simplicity as table setup is verbose
        ListView<String> listView = new ListView<>();
        listView.getStyleClass().add("list-view");

        // Load Data
        List<String> userList = DatabaseHandler.getMemberNames();
        listView.getItems().addAll(userList);

        VBox container = new VBox(listView);
        container.getStyleClass().add("glass-pane");
        container.setMaxWidth(600);
        container.setMaxHeight(400);

        Button backBtn = new Button("RETURN TO DASHBOARD");
        backBtn.setOnAction(e -> SportsClubManagementSystem.changeScene(DashboardView.getScene()));

        root.getChildren().addAll(title, container, backBtn);
        return new Scene(root, 800, 600);
    }
}
