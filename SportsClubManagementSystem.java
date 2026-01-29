import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SportsClubManagementSystem extends Application {

    private Stage primaryStage;
    private Scene loginScene, dashboardScene;

    public static void main(String[] args) {
        DatabaseHandler.init(); 
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // FIXED: Exact App Name you requested
        stage.setTitle("Sports Club Management System");

        buildLoginScene();

        stage.setScene(loginScene);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.show();
    }

    // --- 1. LOGIN ---
    private void buildLoginScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        VBox card = new VBox(25);
        card.getStyleClass().add("glass-pane");
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);

        Label title = new Label("CLUB MANAGER");
        title.getStyleClass().add("header-label");

        TextField user = new TextField(); user.setPromptText("USERNAME");
        PasswordField pass = new PasswordField(); pass.setPromptText("PASSWORD");

        Button btn = new Button("SECURE LOGIN 🔒");
        btn.getStyleClass().add("neon-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            if(user.getText().equals("admin") && pass.getText().equals("pass")) {
                buildDashboardScene();
                primaryStage.setScene(dashboardScene);
            } else {
                showAlert("Access Denied", "Try: admin / pass");
            }
        });

        card.getChildren().addAll(title, user, pass, btn);
        root.getChildren().add(card);
        loginScene = new Scene(root);
        loginScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    // --- 2. DASHBOARD ---
    private void buildDashboardScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        HBox top = new HBox(20);
        top.setPadding(new Insets(20));
        top.setAlignment(Pos.CENTER_LEFT);
        Label brand = new Label("COMMAND CENTER");
        brand.getStyleClass().add("header-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button logout = new Button("LOGOUT ➜");
        logout.getStyleClass().add("button-danger");
        logout.setOnAction(e -> primaryStage.setScene(loginScene));

        top.getChildren().addAll(brand, spacer, logout);
        root.setTop(top);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        // TILES
        grid.add(createTile("📋 TEAM ROSTER", "View & Delete Players", e -> showAllDataView(root)), 0, 0);
        grid.add(createTile("➕ ADD PLAYER", "Register New Athlete", e -> showAddMemberView(root)), 1, 0);
        grid.add(createTile("⚽ MATCH CENTER", "History & Scores", e -> showMatchView(root)), 0, 1);
        grid.add(createTile("📊 ANALYTICS", "Value & Stats", e -> showAnalyticsView(root)), 1, 1);

        root.setCenter(grid);
        dashboardScene = new Scene(root);
        dashboardScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    // --- 3. ROSTER (FIXED: Random ID + Delete) ---
    private void showAllDataView(BorderPane root) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("root");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Button back = new Button("⬅ DASHBOARD");
        back.getStyleClass().add("button-danger");
        back.setOnAction(e -> { buildDashboardScene(); primaryStage.setScene(dashboardScene); });

        Label title = new Label("ACTIVE ROSTER");
        title.getStyleClass().add("header-label");

        Button deleteBtn = new Button("🗑 DELETE PLAYER");
        deleteBtn.getStyleClass().add("button-danger");

        header.getChildren().addAll(back, title, new Region(), deleteBtn);
        HBox.setHgrow(header.getChildren().get(2), Priority.ALWAYS);

        TableView<Member> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(createCol("ID", "id"));
        table.getColumns().add(createCol("NAME", "name"));
        table.getColumns().add(createCol("PHONE", "phone"));
        table.getColumns().add(createCol("EMAIL", "email"));
        table.getColumns().add(createCol("VALUE ($)", "marketValue"));
        table.getColumns().add(createCol("TRAINING", "trainingDate"));

        table.getItems().addAll(fetchAllMembers());

        // DELETE LOGIC
        deleteBtn.setOnAction(e -> {
            Member sel = table.getSelectionModel().getSelectedItem();
            if(sel != null) {
                deleteMember(sel.getId()); // Delete from DB
                table.getItems().remove(sel); // Remove from Screen
            } else showAlert("Warning", "Select a player to delete!");
        });

        content.getChildren().addAll(header, table);
        root.setCenter(content);
    }

    // --- 4. MATCH CENTER (FIXED: Delete Button Added) ---
    private void showMatchView(BorderPane root) {
        HBox split = new HBox(20);
        split.setPadding(new Insets(20));
        split.getStyleClass().add("root");
        split.setAlignment(Pos.CENTER);

        // LEFT: FORM
        VBox form = new VBox(20);
        form.getStyleClass().add("glass-pane");
        form.setMinWidth(350);
        form.setAlignment(Pos.CENTER);

        Label formTitle = new Label("NEW MATCH");
        formTitle.getStyleClass().add("header-label");

        TextField t1 = new TextField(); t1.setPromptText("HOME TEAM");
        TextField t2 = new TextField(); t2.setPromptText("AWAY TEAM");
        TextField score = new TextField(); score.setPromptText("SCORE (e.g. 2-1)");

        Button save = new Button("SAVE RESULT 💾");
        save.getStyleClass().add("neon-btn");
        save.setMaxWidth(Double.MAX_VALUE);
        
        Button back = new Button("⬅ DASHBOARD");
        back.getStyleClass().add("button-danger");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> { buildDashboardScene(); primaryStage.setScene(dashboardScene); });

        form.getChildren().addAll(formTitle, t1, t2, score, save, back);

        // RIGHT: HISTORY TABLE
        VBox tableContainer = new VBox(10);
        HBox.setHgrow(tableContainer, Priority.ALWAYS);
        
        // Header for Right Side
        HBox rightHeader = new HBox(10);
        Label listTitle = new Label("MATCH HISTORY");
        listTitle.getStyleClass().add("header-label");
        
        // NEW: Match Delete Button
        Button deleteMatchBtn = new Button("🗑 DELETE MATCH");
        deleteMatchBtn.getStyleClass().add("button-danger");
        
        rightHeader.getChildren().addAll(listTitle, new Region(), deleteMatchBtn);
        HBox.setHgrow(rightHeader.getChildren().get(1), Priority.ALWAYS);

        TableView<Match> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(createMatchCol("ID", "id"));
        table.getColumns().add(createMatchCol("HOME", "team1"));
        table.getColumns().add(createMatchCol("AWAY", "team2"));
        table.getColumns().add(createMatchCol("SCORE", "score"));
        
        table.getItems().addAll(fetchAllMatches());

        // Save Logic
        save.setOnAction(e -> {
            if(!t1.getText().isEmpty() && !t2.getText().isEmpty()) {
                addMatch(t1.getText(), t2.getText(), score.getText());
                table.getItems().clear(); 
                table.getItems().addAll(fetchAllMatches());
            }
        });

        // Delete Logic for Matches
        deleteMatchBtn.setOnAction(e -> {
            Match sel = table.getSelectionModel().getSelectedItem();
            if(sel != null) {
                deleteMatch(sel.getId()); // Delete from DB
                table.getItems().remove(sel); // Remove from Screen
            } else showAlert("Warning", "Select a match to delete!");
        });

        tableContainer.getChildren().addAll(rightHeader, table);
        split.getChildren().addAll(form, tableContainer);
        root.setCenter(split);
    }

    // --- 5. ADD PLAYER (FIXED: Random ID) ---
    private void showAddMemberView(BorderPane root) {
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        VBox form = new VBox(15);
        form.getStyleClass().add("glass-pane");
        form.setMaxWidth(500);

        Label title = new Label("NEW SIGNING");
        title.getStyleClass().add("header-label");

        TextField name = new TextField(); name.setPromptText("FULL NAME");
        TextField phone = new TextField(); phone.setPromptText("PHONE");
        TextField email = new TextField(); email.setPromptText("EMAIL");
        TextField val = new TextField(); val.setPromptText("MARKET VALUE ($)");
        DatePicker date = new DatePicker(); date.setMaxWidth(Double.MAX_VALUE);

        Button save = new Button("CONFIRM SIGNING ✅");
        save.getStyleClass().add("neon-btn");
        save.setMaxWidth(Double.MAX_VALUE);
        
        Button back = new Button("CANCEL");
        back.getStyleClass().add("button-danger");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(e -> { buildDashboardScene(); primaryStage.setScene(dashboardScene); });

        save.setOnAction(e -> {
            try {
                double v = Double.parseDouble(val.getText());
                String d = (date.getValue()!=null) ? date.getValue().toString() : "TBD";
                addMember(name.getText(), phone.getText(), email.getText(), v, d);
                showAllDataView(root);
            } catch(Exception ex) { showAlert("Error", "Value must be a number!"); }
        });

        form.getChildren().addAll(title, name, phone, email, val, date, save, back);
        content.getChildren().add(form);
        root.setCenter(content);
    }

    // --- 6. ANALYTICS ---
    private void showAnalyticsView(BorderPane root) {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("root");

        Button back = new Button("⬅ DASHBOARD");
        back.getStyleClass().add("button-danger");
        back.setOnAction(e -> { buildDashboardScene(); primaryStage.setScene(dashboardScene); });

        Label title = new Label("CLUB FINANCIALS & STATS");
        title.getStyleClass().add("header-label");

        List<Member> members = fetchAllMembers();
        double totalValue = members.stream().mapToDouble(Member::getMarketValue).sum();
        int totalPlayers = members.size();
        int totalMatches = fetchAllMatches().size();

        HBox cards = new HBox(30);
        cards.setAlignment(Pos.CENTER);
        
        cards.getChildren().add(createStatCard("TOTAL VALUE", "$" + String.format("%,.2f", totalValue)));
        cards.getChildren().add(createStatCard("SQUAD SIZE", totalPlayers + " Players"));
        cards.getChildren().add(createStatCard("MATCHES PLAYED", totalMatches + " Games"));

        content.getChildren().addAll(title, cards, back);
        root.setCenter(content);
    }

    // --- HELPERS ---
    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");
        card.setPrefSize(250, 150);
        Label t = new Label(title); t.setStyle("-fx-text-fill: cyan; -fx-font-size: 14px;");
        Label v = new Label(value); v.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        card.getChildren().addAll(t, v);
        return card;
    }

    private Button createTile(String title, String sub, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(title + "\n" + sub);
        btn.getStyleClass().add("menu-button");
        btn.setOnAction(action);
        animateButton(btn);
        return btn;
    }

    private void animateButton(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
        btn.setOnMouseEntered(e -> { st.setToX(1.05); st.setToY(1.05); st.playFromStart(); });
        btn.setOnMouseExited(e -> { st.setToX(1.0); st.setToY(1.0); st.playFromStart(); });
    }

    private void showAlert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    private TableColumn<Member, String> createCol(String t, String p) {
        TableColumn<Member, String> c = new TableColumn<>(t); c.setCellValueFactory(new PropertyValueFactory<>(p)); return c;
    }
    private TableColumn<Match, String> createMatchCol(String t, String p) {
        TableColumn<Match, String> c = new TableColumn<>(t); c.setCellValueFactory(new PropertyValueFactory<>(p)); return c;
    }

    // --- DATA CLASSES ---
    public static class Member {
        private int id; private String name, phone, email, trainingDate, status; private double marketValue;
        public Member(int id, String name, String phone, String email, double marketValue, String trainingDate, String status) {
            this.id = id; this.name = name; this.phone = phone; this.email = email; this.marketValue = marketValue; this.trainingDate = trainingDate; this.status = status;
        }
        public int getId() { return id; } public String getName() { return name; } public String getPhone() { return phone; }
        public String getEmail() { return email; } public double getMarketValue() { return marketValue; } public String getTrainingDate() { return trainingDate; }
    }

    public static class Match {
        private int id; private String team1, team2, score;
        public Match(int id, String t1, String t2, String s) { this.id = id; this.team1 = t1; this.team2 = t2; this.score = s; }
        public int getId() { return id; } public String getTeam1() { return team1; } public String getTeam2() { return team2; } public String getScore() { return score; }
    }

    // --- DB ACTIONS ---
    private List<Member> fetchAllMembers() {
        List<Member> list = new ArrayList<>();
        try (Connection conn = DatabaseHandler.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM members")) {
            while (rs.next()) list.add(new Member(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"), rs.getDouble("market_value"), rs.getString("training_date"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private List<Match> fetchAllMatches() {
        List<Match> list = new ArrayList<>();
        try (Connection conn = DatabaseHandler.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM matches")) {
            while (rs.next()) list.add(new Match(rs.getInt("id"), rs.getString("team1"), rs.getString("team2"), rs.getString("score")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private void addMember(String n, String p, String e, double v, String d) {
        // GENERATE RANDOM ID (FIXED)
        int randomId = new Random().nextInt(9000) + 1000; // Random between 1000-9999
        try (Connection conn = DatabaseHandler.connect(); PreparedStatement ps = conn.prepareStatement("INSERT INTO members(id, name, phone, email, market_value, training_date, status) VALUES(?,?,?,?,?,?,?)")) {
            ps.setInt(1, randomId); ps.setString(2, n); ps.setString(3, p); ps.setString(4, e); ps.setDouble(5, v); ps.setString(6, d); ps.setString(7, "ACTIVE"); 
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void addMatch(String t1, String t2, String s) {
        try (Connection conn = DatabaseHandler.connect(); PreparedStatement ps = conn.prepareStatement("INSERT INTO matches(team1, team2, score) VALUES(?,?,?)")) {
            ps.setString(1, t1); ps.setString(2, t2); ps.setString(3, s); ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void deleteMember(int id) {
        try (Connection conn = DatabaseHandler.connect(); PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE id = ?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void deleteMatch(int id) {
        try (Connection conn = DatabaseHandler.connect(); PreparedStatement ps = conn.prepareStatement("DELETE FROM matches WHERE id = ?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}