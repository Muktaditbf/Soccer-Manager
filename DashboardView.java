import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DashboardView {

    public static Scene getScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        Label title = new Label("COMMAND CENTER");
        title.getStyleClass().add("header-label");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Button btnMembers = createMenuButton("SHOW MEMBER DATA");
        btnMembers.setOnAction(e -> SportsClubManagementSystem.changeScene(MemberListView.getScene()));

        Button btnAdd = createMenuButton("ADD NEW MEMBER");
        btnAdd.setOnAction(e -> SportsClubManagementSystem.changeScene(AddMemberView.getScene()));

        Button btnEnroll = createMenuButton("ENROLL MEMBER");
        btnEnroll.setOnAction(e -> SportsClubManagementSystem.changeScene(EnrollmentView.getScene()));

        Button btnSession = createMenuButton("NEW TRAINING SESSION");
        btnSession.setOnAction(e -> SportsClubManagementSystem.changeScene(TrainingSessionView.getScene()));

        grid.add(btnMembers, 0, 0);
        grid.add(btnAdd, 1, 0);
        grid.add(btnEnroll, 0, 1);
        grid.add(btnSession, 1, 1);

        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.getStyleClass().add("button-danger");
        logoutBtn.setOnAction(e -> {
            Parent loginRoot = LoginView.getView();
            Scene scene = new Scene(loginRoot, 800, 600);
            SportsClubManagementSystem.changeScene(scene);
        });

        root.getChildren().addAll(title, grid, logoutBtn);
        return new Scene(root, 800, 600);
    }

    private static Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}
