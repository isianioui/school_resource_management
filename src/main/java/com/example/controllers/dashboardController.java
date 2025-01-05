package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import com.example.models.*;
import com.example.dao.*;

import java.io.IOException;

public class dashboardController {
    
    // FXML UI Components
    @FXML private Label userNameLabel;
    @FXML private VBox reservationsView;
    @FXML private VBox resourcesView;
    @FXML private VBox eventsView;

    @FXML private VBox usersView;
@FXML private TableView<User> usersTable;
@FXML private TableColumn<User, String> usernameColumn;
@FXML private TableColumn<User, String> emailColumn;
@FXML private TableColumn<User, String> roleColumn;

    
    // Tables
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableView<Resource> resourcesTable;
    @FXML private TableView<Event> eventsTable;
    
    // Reservation Columns
    @FXML private TableColumn<Reservation, Integer> resIdColumn;
    @FXML private TableColumn<Reservation, String> resourceColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, String> timeColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    
    // Resource Columns
    @FXML private TableColumn<Resource, String> resourceNameColumn;
    @FXML private TableColumn<Resource, String> typeColumn;
    @FXML private TableColumn<Resource, Integer> capacityColumn;
    @FXML private TableColumn<Resource, Boolean> availabilityColumn;
    
    // Event Columns
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> eventDateColumn;
    @FXML private TableColumn<Event, String> locationColumn;
    
    // Control Buttons
    @FXML private Button addResourceButton;
    @FXML private Button editResourceButton;
    @FXML private Button deleteResourceButton;
    @FXML private Button manageUsersButton;
    @FXML private Button generateReportsButton;
    @FXML private Button viewResourcesButton;
    @FXML private Button makeReservationButton;
    
    // Class members
    private User currentUser;
    private ReservationDAO reservationDAO;
    private ResourceDAO resourceDAO;
    private EventDAO eventDAO;
    private StatisticsDAO statisticsDAO;
    private UserDAO userDAO;


    @FXML private VBox statisticsView;
@FXML private PieChart resourceUsageChart;
@FXML private BarChart<String, Number> reservationsChart;
@FXML private Label totalUsersLabel;
@FXML private Label activeReservationsLabel;
@FXML private Label availableResourcesLabel;
@FXML private PieChart userActivityChart;
@FXML private BarChart<String, Number> eventAttendanceChart;

    
    @FXML
    public void initialize() {
        initializeDAOs();
        setupTableColumns();
        setupInitialVisibility();
    }
    
    private void initializeDAOs() {
        reservationDAO = new ReservationDAO();
        resourceDAO = new ResourceDAO();
        eventDAO = new EventDAO();
        statisticsDAO = new StatisticsDAO();
    }
    
    // ... (keep existing imports and class declaration)

    @FXML
private void showUsers() {
    updateViewVisibility(false, false, false, false);
    usersView.setVisible(true);
    
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    
    ObservableList<User> users = FXCollections.observableArrayList(
        userDAO.getAllUsers()
    );
    usersTable.setItems(users);
}
private void setupTableColumns() {
    // Reservations Table
    resIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_id"));
    resourceColumn.setCellValueFactory(new PropertyValueFactory<>("resource_type"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_date"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_time"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    
    // Resources Table
    resourceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("resource_type"));
    capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
    
    // Events Table
    eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("event_date"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
}

    
    private void setupInitialVisibility() {
        reservationsView.setVisible(false);
        resourcesView.setVisible(false);
        eventsView.setVisible(false);
    }
    
    // public void setUser(User user) {
    //     this.currentUser = user;
    //     userNameLabel.setText("Welcome, " + user.getUsername());
    //     setupUserAccess();
    //     loadUserData();
    // }
    
    private void setupUserAccess() {
        boolean isAdmin = currentUser.getRole().equals("admin");
        addResourceButton.setVisible(isAdmin);
        editResourceButton.setVisible(isAdmin);
        deleteResourceButton.setVisible(isAdmin);
        manageUsersButton.setVisible(isAdmin);
        generateReportsButton.setVisible(isAdmin);
        
        // Common features for all users
        viewResourcesButton.setVisible(true);
        makeReservationButton.setVisible(true);
    }
    
    @FXML
    private void showReservations() {
        updateViewVisibility(true, false, false);
        ObservableList<Reservation> reservations = FXCollections.observableArrayList(
            reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole())
        );
        reservationsTable.setItems(reservations);
    }
    
    
    @FXML
    private void showResources() {
        updateViewVisibility(false, true, false);
        ObservableList<Resource> resources = FXCollections.observableArrayList(
            resourceDAO.getAllResources()
        );
        resourcesTable.setItems(resources);
    }
    
    @FXML
    private void showEvents() {
        updateViewVisibility(false, false, true);
        ObservableList<Event> events = FXCollections.observableArrayList(
            eventDAO.getAllEvents()
        );
        eventsTable.setItems(events);
    }
    
    private void updateViewVisibility(boolean res, boolean resource, boolean event) {
        reservationsView.setVisible(res);
        resourcesView.setVisible(resource);
        eventsView.setVisible(event);
    }
    
    @FXML
    private void showReports() {
        updateViewVisibility(false, false, false);
        System.out.println("Loading reports view...");
    }
    
    @FXML
    private void showNotifications() {
        updateViewVisibility(false, false, false);
        System.out.println("Loading notifications view...");
    }
    
    @FXML
    private void createReservation() {
        // Implement reservation creation logic
    }
    
    @FXML
    private void createEvent() {
        // Implement event creation logic
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(),
                                        Screen.getPrimary().getVisualBounds().getHeight());
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
            
            this.currentUser = null;
        } catch (IOException e) {
            System.err.println("Error loading login view: " + e.getMessage());
        }
    }
    
    private void loadUserData() {
        showReservations();
    }

    private void loadAdminDashboard() {
    // Resource usage statistics
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Terrains", statisticsDAO.getResourceCount("terrain")),
        new PieChart.Data("Salles", statisticsDAO.getResourceCount("salle"))
    );
    resourceUsageChart.setData(pieChartData);

    // Reservations by status
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.getData().add(new XYChart.Data<>("Pending", statisticsDAO.getReservationCount("pending")));
    series.getData().add(new XYChart.Data<>("Confirmed", statisticsDAO.getReservationCount("confirmed")));
    series.getData().add(new XYChart.Data<>("Rejected", statisticsDAO.getReservationCount("rejected")));
    reservationsChart.getData().add(series);

    // Update summary labels
    totalUsersLabel.setText("Total Users: " + statisticsDAO.getTotalUsers());
    activeReservationsLabel.setText("Active Reservations: " + statisticsDAO.getActiveReservations());
    availableResourcesLabel.setText("Available Resources: " + statisticsDAO.getAvailableResources());
    ObservableList<PieChart.Data> userActivityData = FXCollections.observableArrayList(
        new PieChart.Data("Active", statisticsDAO.getActiveUsers()),
        new PieChart.Data("Inactive", statisticsDAO.getInactiveUsers())
    );
    userActivityChart.setData(userActivityData);
    userActivityChart.setStartAngle(90);
}

private void updateViewVisibility(boolean res, boolean resource, boolean event, boolean stats) {
    reservationsView.setVisible(res);
    resourcesView.setVisible(resource);
    eventsView.setVisible(event);
    statisticsView.setVisible(stats);
}

public void setUser(User user) {
    this.currentUser = user;
    userNameLabel.setText("Welcome, " + user.getUsername());
    setupUserAccess();
    
    if (user.getRole().equals("admin")) {
        updateViewVisibility(false, false, false, true);
        loadAdminDashboard();
    } else {
        updateViewVisibility(true, false, false, false);
        showReservations();
    }
}

@FXML
private void showDashboard() {
    if (currentUser.getRole().equals("admin")) {
        updateViewVisibility(false, false, false, true);
        loadAdminDashboard();
    }
}




    
}
