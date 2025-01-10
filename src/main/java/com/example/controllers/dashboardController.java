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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.Optional;
import java.awt.Desktop;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp; // Add this import at the top
import java.time.LocalDate;

import com.example.models.*;
import com.example.services.EmailService;
import com.example.dao.*;
import com.example.database.databaseConnection;

import java.io.File;
import java.io.IOException;

public class dashboardController {

    // FXML UI Components
    @FXML
    private Label userNameLabel;
    @FXML
    private VBox reservationsView;
    @FXML
    private VBox resourcesView;
    @FXML
    private VBox eventsView;

    @FXML
    private VBox usersView;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    // Tables
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableView<Resource> resourcesTable;
    @FXML
    private TableView<Event> eventsTable;

    // Reservation Columns
    @FXML
    private TableColumn<Reservation, Integer> resIdColumn;
    @FXML
    private TableColumn<Reservation, String> resourceColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> timeColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;

    // Resource Columns
    @FXML
    private TableColumn<Resource, String> resourceNameColumn;
    @FXML
    private TableColumn<Resource, String> typeColumn;
    @FXML
    private TableColumn<Resource, Integer> capacityColumn;
    @FXML
    private TableColumn<Resource, Boolean> availabilityColumn;

    // Event Columns
    @FXML
    private TableColumn<Event, String> eventNameColumn;
    @FXML
    private TableColumn<Event, String> eventDateColumn;
    @FXML
    private TableColumn<Event, String> locationColumn;

    // Control Buttons
    // @FXML
    // private Button addResourceButton;
    // @FXML
    // private Button editResourceButton;
    // @FXML
    // private Button deleteResourceButton;
    // @FXML
    // private Button manageUsersButton;
    // @FXML
    // private Button generateReportsButton;
    // @FXML
    // private Button viewResourcesButton;
    // @FXML
    // private Button makeReservationButton;

    // Class members
    private User currentUser;
    private ReservationDAO reservationDAO;
    private ResourceDAO resourceDAO;
    private EventDAO eventDAO;
    private StatisticsDAO statisticsDAO;
    private UserDAO userDAO;

    @FXML
    private VBox statisticsView;
    @FXML
    private PieChart resourceUsageChart;
    @FXML
    private BarChart<String, Number> reservationsChart;
    @FXML
    private Label totalUsersLabel;
    @FXML
    private Label activeReservationsLabel;
    @FXML
    private Label availableResourcesLabel;
    @FXML
    private PieChart userActivityChart;
    @FXML
    private BarChart<String, Number> eventAttendanceChart;
    @FXML
    private TableColumn<User, Timestamp> lastLoginColumn;

    @FXML
    private VBox reservationFormView;
    @FXML
    private ComboBox<Resource> resourceComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> timeComboBox;

    private TableColumn<Resource, String> descriptionColumn;
    @FXML
    private ComboBox<String> resourceTypeField;

    private String userRole;

    @FXML
    private Button newReservationButton;
    @FXML
    private Button newResourceButton;
    @FXML
    private Button newEventButton;

    @FXML
private TableView<Report> reportsTable;
@FXML
private TableColumn<Report, String> reportTitleColumn;
@FXML
private TableColumn<Report, String> reportDescriptionColumn;
@FXML
private TableColumn<Report, Timestamp> reportDateColumn;
@FXML
private TableColumn<Report, Void> reportActionsColumn;

    @FXML
    public void initialize() {

        initializeDAOs();
        setupTableColumns();
        setupInitialVisibility();
        setupReservationTable();
        Connection conn = databaseConnection.getConnection();
        resourceDAO = new ResourceDAO(conn);
        loadResources();
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminder_sent"));
        resourceTypeField.setItems(FXCollections.observableArrayList("terrain", "salle"));
        setupEventTable();
        refreshEventTable();
          reportTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    reportDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description")); 
    reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
    setupReportActionsColumn();
    reportDAO = new ReportDAO(conn);
    reportTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    reportDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description")); 
    reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
    reportsView.setVisible(false);


    }

    private void hideButtonsForNonAdmin() {
        Platform.runLater(() -> {
            if (!"admin".equals(userRole)) {
                // Hide all buttons immediately
                usersButton.setVisible(false);
                usersButton.setManaged(false);

                // if (newReservationButton != null) {
                //     newReservationButton.setVisible(false);
                //     newReservationButton.setManaged(false);
                // }

                if (newResourceButton != null) {
                    newResourceButton.setVisible(false);
                    newResourceButton.setManaged(false);
                }

                if (newEventButton != null) {
                    newEventButton.setVisible(false);
                    newEventButton.setManaged(false);
                }
            }
        });
    }

    public void setUserRole(String role) {
        this.userRole = role;
        Platform.runLater(() -> {
            if (!"admin".equals(userRole)) {
                usersButton.setVisible(false);
                usersButton.setManaged(false);
                // newReservationButton.setVisible(false);
                // newReservationButton.setManaged(false);
                newResourceButton.setVisible(false);
                newResourceButton.setManaged(false);
                newEventButton.setVisible(false);
                newEventButton.setManaged(false);
            }
        });
    }

    private void initializeDAOs() {
        Connection conn = databaseConnection.getConnection();
        reservationDAO = new ReservationDAO();
        resourceDAO = new ResourceDAO(conn);
        eventDAO = new EventDAO();
        statisticsDAO = new StatisticsDAO();
        userDAO = new UserDAO();
    }

    // private void updateViewVisibility(boolean res, boolean resource, boolean
    // event, boolean stats, boolean users) {
    // reservationsView.setVisible(res);
    // resourcesView.setVisible(resource);
    // eventsView.setVisible(event);
    // statisticsView.setVisible(stats);
    // usersView.setVisible(users);
    // }

    private void updateViewVisibility(boolean res, boolean resource, boolean event, boolean stats, boolean users) {
        // Hide all views first
        reservationsView.setVisible(false);
        resourcesView.setVisible(false);
        eventsView.setVisible(false);
        statisticsView.setVisible(false);
        usersView.setVisible(false);
        reportsView.setVisible(false);  
        reportFormView.setVisible(false);  
        reportFormView.setManaged(false);

        // Show only the requested view
        reservationsView.setVisible(res);
        resourcesView.setVisible(resource);
        eventsView.setVisible(event);
        statisticsView.setVisible(stats);
        usersView.setVisible(users);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Hide users button immediately if not admin
        if (!"admin".equals(currentUser.getRole())) {
            usersButton.setVisible(false);
        }
    }

    // ... (keep existing imports and class declaration)
    @FXML
    private Button usersButton;

    @FXML
    private void showUsers() {
        if (!"admin".equals(currentUser.getRole())) {
            usersTable.setVisible(false); // Hide the Actions column
            usersButton.setVisible(false);

        }
        updateViewVisibility(false, false, false, false, true);

        // Initialize all columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));

        // Setup status display
        accountStatusColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Active" : "Inactive");
                }
            }
        });

        // Setup action buttons
        setupUserTable();

        // Load data
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
        usersTable.setItems(users);
    }

    @FXML
    private TableColumn<Reservation, Boolean> reminderColumn;

    private void setupTableColumns() {
        // Reservations Table
        resIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_id"));
        resourceColumn.setCellValueFactory(new PropertyValueFactory<>("resource_type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("reservation_time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminder_sent"));
        reminderColumn.setCellFactory(column -> new TableCell<Reservation, Boolean>() {
            @Override
            protected void updateItem(Boolean reminderSent, boolean empty) {
                super.updateItem(reminderSent, empty);
                if (empty || reminderSent == null) {
                    setText(null);
                } else {
                    setText(reminderSent ? "Sent" : "Not Sent");
                }
            }
        });

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

    private void setupUserAccess() {
        boolean isAdmin = currentUser.getRole().equals("admin");

        // Keep only the reminder column setup
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminder_sent"));
        reminderColumn.setCellFactory(column -> new TableCell<Reservation, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item ? "Sent" : "Not Sent"));
            }
        });
    }

    @FXML
    private void showReservations() {
        updateViewVisibility(true, false, false, false, false);
        resourceFormView.setVisible(false);
        reservationFormView.setVisible(false);
        eventsView.setVisible(false);
        eventFormView.setVisible(false);

        ObservableList<Reservation> reservations = FXCollections.observableArrayList(
                reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole()));
        reservationsTable.setItems(reservations);
    }

    @FXML
    private void showResources() {
        reservationFormView.setVisible(false);
        resourceFormView.setVisible(false);
        eventsView.setVisible(false);
        eventFormView.setVisible(false);

        updateViewVisibility(false, true, false, false, false);
        ObservableList<Resource> resources = FXCollections.observableArrayList(
                resourceDAO.getAllResources());
        resourcesTable.setItems(resources);
    }

    @FXML
    private void showEvents() {
        reservationFormView.setVisible(false);
        resourceFormView.setVisible(false);
        eventFormView.setVisible(false);

        updateViewVisibility(false, false, true, false, false);
        ObservableList<Event> events = FXCollections.observableArrayList(
                eventDAO.getAllEvents());
        eventsTable.setItems(events);
    }

    private void updateViewVisibility(boolean res, boolean resource, boolean event) {
        reservationsView.setVisible(res);
        resourcesView.setVisible(resource);
        eventsView.setVisible(event);
    }

    @FXML
private VBox reportsView;


    @FXML
    private void showReports() {
        // Hide other views
        reservationsView.setVisible(false);
    resourcesView.setVisible(false);
    eventsView.setVisible(false);
    statisticsView.setVisible(false);
    statisticsView.setManaged(false);  

    usersView.setVisible(false);
    reservationFormView.setVisible(false);
    eventFormView.setVisible(false);
    reportsView.setVisible(true);
    reportsView.setManaged(true);

        
        updateViewVisibility(false, false, false, true, false);
        reportsView.setVisible(true);
        
        refreshReportsTable();
    }
    

    @FXML
    private void showNotifications() {
        reservationFormView.setVisible(false);
        resourceFormView.setVisible(false);
        eventsView.setVisible(false);
        eventFormView.setVisible(false);

        updateViewVisibility(false, false, false);
        System.out.println("Loading notifications view...");
    }

    @FXML
    private TableColumn<Reservation, Void> reservationActionsColumn;

    private void setupReservationTable() {
        // Setup reminder column
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminder_sent"));
        reminderColumn.setCellFactory(column -> new TableCell<Reservation, Boolean>() {
            @Override
            protected void updateItem(Boolean reminderSent, boolean empty) {
                super.updateItem(reminderSent, empty);
                if (empty || reminderSent == null) {
                    setText(null);
                } else {
                    setText(reminderSent ? "Sent" : "Not Sent");
                }
            }
        });
    
        reservationActionsColumn.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final Button viewButton = new Button("View");
            private final HBox buttons = new HBox(5);
    
            {
                viewButton.getStyleClass().add("info-button");
                buttons.getChildren().add(viewButton);
    
                // Only add admin buttons if user is admin
                if ("admin".equals(currentUser.getRole())) {
                    Button editButton = new Button("Edit");
                    Button deleteButton = new Button("Delete");
                    Button reminderButton = new Button("Send Reminder");
                    Button confirmButton = new Button("Confirm");
                    Button cancelButton = new Button("Cancel");
    
                    editButton.getStyleClass().add("success-button");
                    deleteButton.getStyleClass().add("danger-button");
                    reminderButton.getStyleClass().add("info-button");
                    confirmButton.getStyleClass().add("success-button");
                    cancelButton.getStyleClass().add("danger-button");
    
                    buttons.getChildren().addAll(editButton, deleteButton, reminderButton, confirmButton, cancelButton);
    
                    editButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        editReservation(reservation);
                    });
    
                    confirmButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        updateReservationStatus(reservation, "confirmed");
                    });
    
                    cancelButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        updateReservationStatus(reservation, "rejected");
                    });
    
                    deleteButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        deleteReservation(reservation);
                    });
    
                    reminderButton.setOnAction(event -> {
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        sendReservationReminder(reservation);
                    });
                }
    
                viewButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    viewReservationDetails(reservation);
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }
    

    private void updateReservationStatus(Reservation reservation, String newStatus) {
        if (reservationDAO.updateReservationStatus(reservation.getReservation_id(), newStatus)) {
            reservation.setStatus(newStatus);
            refreshReservationsTable();
        }
    }

    private void sendReservationReminder(Reservation reservation) {
        System.out.println("Attempting to find user with ID: " + reservation.getUser_id());
        User user = userDAO.getUserById(reservation.getUser_id());
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("User not found in the database.");
            alert.showAndWait();
            return;
        }

        String status = reservation.getStatus();
        String emailSubject = "Reservation Reminder";
        String emailBody = String.format(
                "Dear %s,\n\nYour reservation for %s on %s at %s is %s.\n\nBest regards,\nResource Management System EST ESSAOUIRA",
                user.getUsername(),
                resourceDAO.getResourceById(reservation.getResource_id()).getName(),
                reservation.getReservation_date(),
                reservation.getReservation_time(),
                status);

        EmailService.sendEmail(user.getEmail(), emailSubject, emailBody);
        reservationDAO.updateReminderStatus(reservation.getReservation_id(), true);
        refreshReservationsTable();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder Sent");
        alert.setContentText("Reminder email has been sent successfully!");
        alert.showAndWait();
    }

    @FXML
    private VBox reservationDetailsView;
    @FXML
    private Label reservationIdLabel;
    @FXML
    private Label resourceNameLabel;
    @FXML
    private Label resourceTypeLabel;
    @FXML
    private Label reservedByLabel;
    @FXML
    private Label reservationDateLabel;
    @FXML
    private Label reservationTimeLabel;
    @FXML
    private Label reservationStatusLabel;
    @FXML
    private Label reminderStatusLabel;
    @FXML
    private Button backToReservationsButton;

    // private void viewReservationDetails(Reservation reservation) {
    // Alert alert = new Alert(Alert.AlertType.INFORMATION);
    // alert.setTitle("Reservation Details");
    // alert.setHeaderText("Reservation #" + reservation.getReservation_id());

    // // Get resource name from resourceDAO
    // Resource resource =
    // resourceDAO.getResourceById(reservation.getResource_id());
    // // Get user details from userDAO
    // User user = userDAO.getUserById(reservation.getUser_id());

    // String content = String.format("""
    // Resource: %s
    // Type: %s
    // Reserved by: %s
    // Date: %s
    // Time: %s
    // Status: %s
    // Reminder: %s
    // """,
    // resource.getName(),
    // reservation.getResource_type(),
    // user.getUsername(),
    // reservation.getReservation_date(),
    // reservation.getReservation_time(),
    // reservation.getStatus(),
    // reservation.getReminder_sent() ? "Sent" : "Not Sent"
    // );

    // alert.setContentText(content);
    // alert.showAndWait();
    // }

    private void viewReservationDetails(Reservation reservation) {
        // Hide reservations table view
        reservationsView.setVisible(false);

        // Show details view
        reservationDetailsView.setVisible(true);

        // Get additional data
        Resource resource = resourceDAO.getResourceById(reservation.getResource_id());
        User user = userDAO.getUserById(reservation.getUser_id());

        // Set labels
        reservationIdLabel.setText("Reservation #" + reservation.getReservation_id());
        resourceNameLabel.setText("Resource: " + resource.getName());
        resourceTypeLabel.setText("Type: " + reservation.getResource_type());
        reservedByLabel.setText("Reserved by: " + user.getUsername());
        reservationDateLabel.setText("Date: " + reservation.getReservation_date());
        reservationTimeLabel.setText("Time: " + reservation.getReservation_time());
        reservationStatusLabel.setText("Status: " + reservation.getStatus());
        reminderStatusLabel.setText("Reminder: " + (reservation.getReminder_sent() ? "Sent" : "Not Sent"));

        backToReservationsButton.setOnAction(e -> {
            reservationDetailsView.setVisible(false);
            reservationsView.setVisible(true);
        });
    }

    // @FXML
    // private void openNewReservationForm() {
    // try {
    // FXMLLoader loader = new
    // FXMLLoader(getClass().getResource("/views/reservationForm.fxml"));
    // Parent root = loader.load();

    // Stage stage = new Stage();
    // stage.setTitle("New Reservation");
    // stage.setScene(new Scene(root));
    // stage.showAndWait();

    // // Refresh table after form closes
    // refreshReservationsTable();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    @FXML
    private void openNewReservationForm() {
        reservationsView.setVisible(false);
        reservationFormView.setVisible(true);
        loadResources();
        setupTimeComboBox();
        setupDatePicker();
    }

    private void loadResources() {
        ObservableList<Resource> resources = FXCollections.observableArrayList(resourceDAO.getAllResources());
        resourceComboBox.setItems(resources);
        resourceComboBox.setPromptText("Select Resource");
    }

    private void setupTimeComboBox() {
        ObservableList<String> times = FXCollections.observableArrayList(
                "08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00");
        timeComboBox.setItems(times);
        timeComboBox.setPromptText("Select Time");
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
    }

    @FXML
    private void handleBack() {
        reservationFormView.setVisible(false);
        reservationsView.setVisible(true);
        refreshReservationsTable();
    }

    // private void editReservation(Reservation reservation) {
    // try {
    // FXMLLoader loader = new
    // FXMLLoader(getClass().getResource("/views/reservationForm.fxml"));
    // Parent root = loader.load();

    // ReservationFormController controller = loader.getController();
    // controller.setReservation(reservation);

    // Stage stage = new Stage();
    // stage.setTitle("Edit Reservation");
    // stage.setScene(new Scene(root));
    // stage.showAndWait();

    // refreshReservationsTable();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    private void deleteReservation(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reservation");
        alert.setContentText("Are you sure you want to delete this reservation?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (reservationDAO.deleteReservation(reservation.getReservation_id())) {
                refreshReservationsTable();
            }
        }
    }

    private void refreshReservationsTable() {
        // ObservableList<Reservation> reservations = FXCollections.observableArrayList(
        // reservationDAO.getUserReservations(currentUser.getId(),
        // currentUser.getRole())
        // );
        // reservationsTable.setItems(reservations);

        ObservableList<Reservation> reservations = FXCollections.observableArrayList(
                reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole()));
        // Add debug print
        for (Reservation res : reservations) {
            System.out.println("Reservation ID: " + res.getReservation_id() +
                    " Reminder Status: " + res.getReminder_sent());
        }
        reservationsTable.setItems(reservations);
    }

    @FXML
    private void createEvent() {
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
                new PieChart.Data("Salles", statisticsDAO.getResourceCount("salle")));
        resourceUsageChart.setData(pieChartData);
        // Clear existing data from reservations chart
        System.out.println("Clearing old data...");
        reservationsChart.getData().clear();

        System.out.println("Adding data to the chart...");
        System.out.println("Pending: " + statisticsDAO.getReservationCount("pending"));
        System.out.println("Confirmed: " + statisticsDAO.getReservationCount("confirmed"));
        System.out.println("Rejected: " + statisticsDAO.getReservationCount("rejected"));

        // Create separate series for each status
        XYChart.Series<String, Number> pendingSeries = new XYChart.Series<>();
        pendingSeries.setName("Pending ");
        pendingSeries.getData().add(new XYChart.Data<>("Status", statisticsDAO.getReservationCount("pending")));

        XYChart.Series<String, Number> confirmedSeries = new XYChart.Series<>();
        confirmedSeries.setName("Confirmed ");
        confirmedSeries.getData().add(new XYChart.Data<>("Status", statisticsDAO.getReservationCount("confirmed")));

        XYChart.Series<String, Number> rejectedSeries = new XYChart.Series<>();
        rejectedSeries.setName("Rejected ");
        rejectedSeries.getData().add(new XYChart.Data<>("Status", statisticsDAO.getReservationCount("rejected")));

        // Add all series to the chart
        reservationsChart.getData().addAll(pendingSeries, confirmedSeries, rejectedSeries);

        // Update summary labels
        totalUsersLabel.setText("Total Users: " + statisticsDAO.getTotalUsers());
        activeReservationsLabel.setText("Active Reservations: " + statisticsDAO.getActiveReservations());
        availableResourcesLabel.setText("Available Resources: " + statisticsDAO.getAvailableResources());
        ObservableList<PieChart.Data> userActivityData = FXCollections.observableArrayList(
                new PieChart.Data("Active", statisticsDAO.getActiveUsers()),
                new PieChart.Data("Inactive", statisticsDAO.getInactiveUsers()));
        userActivityChart.setData(userActivityData);
        userActivityChart.setStartAngle(90);
    }

    private void updateViewVisibility(boolean res, boolean resource, boolean event, boolean stats) {
        reservationsView.setVisible(res);
        resourcesView.setVisible(resource);
        eventsView.setVisible(event);
        statisticsView.setVisible(stats);
    }

    // public void setUser(User user) {
    // this.currentUser = user;
    // userNameLabel.setText("Welcome, " + user.getUsername());
    // setupUserAccess();

    // if (user.getRole().equals("admin")) {
    // updateViewVisibility(false, false, false, true);
    // loadAdminDashboard();
    // } else {
    // updateViewVisibility(true, false, false, false);
    // showReservations();
    // }
    // }

    public void setUser(User user) {
        this.currentUser = user;
        userNameLabel.setText("Welcome, " + user.getUsername());

        boolean isAdmin = user.getRole().equals("admin");
    

        loadUserData(); 
        setupUserAccess(); 
        showDashboard();

    }

    // @FXML
    // private void showDashboard() {
    // if (currentUser.getRole().equals("admin")) {
    // // Hide all other views first
    // usersView.setVisible(false);
    // reservationsView.setVisible(false);
    // resourcesView.setVisible(false);
    // eventsView.setVisible(false);

    // // Show statistics view and load dashboard data
    // statisticsView.setVisible(true);
    // loadAdminDashboard();
    // }
    // }

    // @FXML
    // private void showDashboard() {
    // if (currentUser.getRole().equals("admin")) {
    // updateViewVisibility(false, false, false, true, false);
    // loadAdminDashboard();
    // }
    // }
    @FXML
    private void showDashboard() {
        updateViewVisibility(false, false, false, true, false);

        usersView.setVisible(false);
        reservationsView.setVisible(false);
        resourcesView.setVisible(false);
        eventsView.setVisible(false);
        resourceFormView.setVisible(false);
        reservationFormView.setVisible(false);
        eventsView.setVisible(false);
        eventFormView.setVisible(false);
        reportsView.setVisible(false);

        reportsView.setManaged(false); 
        reportFormView.setVisible(false);
        reportFormView.setManaged(false);
        statisticsView.setVisible(true);

        loadAdminDashboard();
    }

    @FXML
    private TableColumn<User, Boolean> accountStatusColumn;
    @FXML
    private TableColumn<User, Void> actionsColumn;

    private void setupUserTable() {
        accountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

        lastLoginColumn.setCellFactory(column -> new TableCell<User, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button actionButton = new Button();

            {
                actionButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    boolean newStatus = !user.getAccountStatus();
                    if (userDAO.updateAccountStatus(user.getId(), newStatus)) {
                        user.setAccountStatus(newStatus);
                        updateButtonStyle();
                        refreshTable();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    actionButton.setText(user.getAccountStatus() ? "Deactivate" : "Activate");
                    updateButtonStyle();
                    setGraphic(actionButton);
                }
            }

            private void updateButtonStyle() {
                User user = getTableView().getItems().get(getIndex());
                if (user.getAccountStatus()) {
                    actionButton.getStyleClass().setAll("button", "danger-button");
                } else {
                    actionButton.getStyleClass().setAll("button", "success-button");
                }
            }
        });
    }

    private void refreshTable() {
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
        usersTable.setItems(users);
    }

    @FXML
    private HBox topNavBar;
    @FXML
    private Button homeButton;
    @FXML
    private Button contactButton;
    @FXML
    private Button notificationButton;

    @FXML
    private void handleHomeClick() {
        showDashboard();
    }

    @FXML
    private void handleContactClick() {
        System.out.println("Contact clicked");
    }

    @FXML
    private void handleNotificationClick() {
        System.out.println("Notifications clicked");
    }

    @FXML
    private void handleSave() {
        if (validateInputs()) {
            Reservation reservation = new Reservation();
            Resource selectedResource = resourceComboBox.getValue();
            System.out.println("Selected Resource ID: " + selectedResource.getResource_id());
            System.out.println("Selected Resource Name: " + selectedResource.getName());

            reservation.setResource_id(selectedResource.getResource_id());
            reservation.setResource_type(selectedResource.getResource_type()); 

            reservation.setUser_id(currentUser.getId());
            reservation.setReservation_date(Date.valueOf(datePicker.getValue()));
            reservation.setReservation_time(Time.valueOf(timeComboBox.getValue() + ":00"));
            reservation.setStatus("pending");

            boolean success = reservationDAO.createReservation(reservation);
            System.out.println("Reservation creation success: " + success);

            if (success) {
                reservationFormView.setVisible(false);
                reservationsView.setVisible(true);
                refreshReservationsTable();
            }
        }
    }

    private boolean validateInputs() {
        if (resourceComboBox.getValue() == null) {
            showAlert("Please select a resource");
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Please select a date");
            return false;
        }
        if (timeComboBox.getValue() == null) {
            showAlert("Please select a time");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private VBox reservationEditView;
    @FXML
    private ComboBox<Resource> editResourceComboBox;
    @FXML
    private DatePicker editDatePicker;
    @FXML
    private TextField editTimeField; // Changed to TextField
    @FXML
    private ComboBox<String> editStatusComboBox;
    @FXML
    private Button saveEditButton;
    @FXML
    private Button cancelEditButton;

    private void editReservation(Reservation reservation) {
        reservationsView.setVisible(false);
        reservationEditView.setVisible(true);

        ObservableList<Resource> resources = FXCollections.observableArrayList(resourceDAO.getAllResources());
        editResourceComboBox.setItems(resources);
        editResourceComboBox.setPromptText("Select Resource");
        editResourceComboBox.setValue(resourceDAO.getResourceById(reservation.getResource_id()));

        editDatePicker.setValue(reservation.getReservation_date().toLocalDate());

        editTimeField.setText(reservation.getReservation_time().toString().substring(0, 5));
        editTimeField.setPromptText("Enter time (HH:mm)");

        editStatusComboBox.setItems(FXCollections.observableArrayList("pending", "confirmed", "rejected"));
        editStatusComboBox.setValue(reservation.getStatus());

        saveEditButton.setOnAction(e -> {
            updateReservation(reservation);
            reservationEditView.setVisible(false);
            reservationsView.setVisible(true);
            refreshReservationsTable();
        });

        cancelEditButton.setOnAction(e -> {
            reservationEditView.setVisible(false);
            reservationsView.setVisible(true);
        });
    }

    private void updateReservation(Reservation reservation) {
        reservation.setResource_id(editResourceComboBox.getValue().getResource_id());
        reservation.setReservation_date(Date.valueOf(editDatePicker.getValue()));
        reservation.setReservation_time(Time.valueOf(editTimeField.getText() + ":00"));
        reservation.setStatus(editStatusComboBox.getValue());

        reservationDAO.updateReservation(reservation);
    }

    // resources upadates

    @FXML
    private VBox resourceFormView;

    @FXML
    private TextField resourceNameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField capacityField;

    @FXML
    private void openNewResourceForm() {
        resourceFormView.setVisible(true);
        resourcesView.setVisible(false);
    }

    @FXML
    private void handleSaveResource() {
        Resource newResource = new Resource();
        newResource.setName(resourceNameField.getText());
        newResource.setResource_type(resourceTypeField.getValue());
        newResource.setDescription(descriptionField.getText());

        int capacity = Integer.parseInt(capacityField.getText());
        if (newResource.isValidCapacity(capacity)) {
            newResource.setCapacity(capacity);
            newResource.setAvailability(true);

            if (resourceDAO.createResource(newResource)) {
                refreshResourceTable();
                resourceFormView.setVisible(false);
                resourcesView.setVisible(true);
                clearResourceForm();
            }
        }
    }

    private void refreshResourceTable() {
        ObservableList<Resource> resources = FXCollections.observableArrayList(resourceDAO.getAllResources());
        resourcesTable.setItems(resources);
    }

    private void clearResourceForm() {
        resourceNameField.clear();
        resourceTypeField.setValue(null);

        descriptionField.clear();
        capacityField.clear();
    }

    @FXML
    private void handleBackToResources() {
        resourceFormView.setVisible(false);
        resourcesView.setVisible(true);
    }

    // events methods
    @FXML
    private TableColumn<Event, String> eventDescriptionColumn;
    @FXML
    private TableColumn<Event, Time> eventTimeColumn;

    @FXML
    private VBox eventFormView;
    @FXML
    private TextField eventNameField;
    @FXML
    private TextArea eventDescriptionField;
    @FXML
    private DatePicker eventDatePicker;
    @FXML
    private TextField eventTimeField;
    @FXML
    private TextField eventLocationField;

    @FXML
    private void openNewEventForm() {
        eventFormView.setVisible(true);
        eventsView.setVisible(false);
    }

    @FXML
    private void handleSaveEvent() {
        try {
            Event newEvent = new Event();
            newEvent.setName(eventNameField.getText());
            newEvent.setDescription(eventDescriptionField.getText());
            newEvent.setEvent_date(Date.valueOf(eventDatePicker.getValue()));
            newEvent.setEvent_time(Time.valueOf(eventTimeField.getText() + ":00"));
            newEvent.setLocation(eventLocationField.getText());
            newEvent.setCreated_by(currentUser.getId()); // Keep as ID for database storage

            if (eventDAO.createEvent(newEvent)) {
                refreshEventTable();
                eventFormView.setVisible(false);
                eventsView.setVisible(true);
                clearEventForm();
            }
        } catch (Exception e) {
            showAlert("Please enter time in HH:mm format (e.g., 14:30)");
        }
    }

    private void refreshEventTable() {
        ObservableList<Event> events = FXCollections.observableArrayList(eventDAO.getAllEvents());
        eventsTable.setItems(events);
    }

    private void clearEventForm() {
        eventNameField.clear();
        eventDescriptionField.clear();
        eventDatePicker.setValue(null);
        eventTimeField.clear();
        eventLocationField.clear();
    }

    @FXML
    private void handleBackToEvents() {
        eventFormView.setVisible(false);
        eventsView.setVisible(true);
    }

    @FXML
    private TableColumn<Event, Integer> createdByColumn;

    private void setupEventTable() {
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("event_date"));
        eventTimeColumn.setCellValueFactory(new PropertyValueFactory<>("event_time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("creator_name"));

        eventsTable.refresh();
    }

    // report part
    private ReportDAO reportDAO;
    private File selectedFile;
    @FXML
    private VBox reportFormView;
    @FXML
    private TextField reportTitleField;
    @FXML
    private TextArea reportDescriptionField;
    @FXML
    private CheckBox reportIsPublicCheck;

    @FXML
    private void showUploadReportForm() {
        statisticsView.setVisible(false);
        statisticsView.setManaged(false);
        usersView.setVisible(false);
        usersView.setManaged(false);
        reservationsView.setVisible(false);
        reservationsView.setManaged(false);
        resourcesView.setVisible(false);
        resourcesView.setManaged(false);
        eventsView.setVisible(false);
        eventsView.setManaged(false);
        reportsView.setVisible(false);
        reportsView.setManaged(false);
        resourceFormView.setVisible(false);
        resourceFormView.setManaged(false);
        reservationFormView.setVisible(false);
        reservationFormView.setManaged(false);
        eventFormView.setVisible(false);
        eventFormView.setManaged(false);
        reportFormView.setVisible(true);
        reportFormView.setManaged(true);
        List<Event> events = eventDAO.getAllEvents();
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
        relatedEventComboBox.setItems(observableEvents);
        
        relatedEventComboBox.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                setText(empty ? "" : event.getName());
            }
        });
    }
    

    @FXML
private void chooseReportFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    );
    selectedFile = fileChooser.showOpenDialog(null);
}
@FXML
private void handleReportUpload() {
    if (selectedFile != null) {
        Report report = new Report();
        report.setTitle(reportTitleField.getText());
        report.setDescription(reportDescriptionField.getText());
        report.setFileType(selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1));
        report.setUploadedBy(currentUser.getId());
        report.setPublic(reportIsPublicCheck.isSelected());
        
        Event selectedEvent = relatedEventComboBox.getValue();
        if (selectedEvent != null) {
            report.setRelatedEventId(selectedEvent.getEvent_id());
        }

        if (reportDAO.uploadReport(report, selectedFile)) {
            clearReportForm();
            reportFormView.setVisible(false);
            reportsView.setVisible(true);
            refreshReportsTable();
        }
    }
}
private void clearReportForm() {
    reportTitleField.clear();
    reportDescriptionField.clear();
    reportIsPublicCheck.setSelected(false);
    selectedFile = null;
}


@FXML
private void cancelReportUpload() {
    clearReportForm();
    reportFormView.setVisible(false);
    reportsView.setVisible(true);
}

private void refreshReportsTable() {
    ObservableList<Report> reports = FXCollections.observableArrayList(reportDAO.getAllReports());
    reportsTable.setItems(reports);
    reportsTable.refresh();

}

@FXML
private void viewReport(Report report) {
    try {
        File file = new File("reports/" + report.getFilePath());
        Desktop.getDesktop().open(file);
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error opening file");
    }
}

private void setupReportActionsColumn() {
    reportActionsColumn.setCellFactory(column -> new TableCell<Report, Void>() {
        private final Button viewButton = new Button("View");
        private final Button deleteButton = new Button("Delete");
        private final HBox buttons = new HBox(5, viewButton, deleteButton);

        {
            viewButton.getStyleClass().add("info-button");
            deleteButton.getStyleClass().add("danger-button");

            // Only show delete button for admin or report owner
            deleteButton.setVisible(false);

            viewButton.setOnAction(event -> {
                Report report = getTableView().getItems().get(getIndex());
                viewReport(report);
            });

            deleteButton.setOnAction(event -> {
                Report report = getTableView().getItems().get(getIndex());
                if (currentUser.getRole().equals("admin") || 
                    report.getUploadedBy() == currentUser.getId()) {
                    deleteReport(report);
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Report report = getTableView().getItems().get(getIndex());
                deleteButton.setVisible(currentUser.getRole().equals("admin") || 
                                     report.getUploadedBy() == currentUser.getId());
                setGraphic(buttons);
            }
        }
    });
}
private void deleteReport(Report report) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Report");
    alert.setContentText("Are you sure you want to delete this report?");

    if (alert.showAndWait().get() == ButtonType.OK) {
        if (reportDAO.deleteReport(report.getReportId())) {
            refreshReportsTable();
        }
    }
}


@FXML
private ComboBox<Event> relatedEventComboBox;

private void loadEventComboBox() {
    List<Event> events = eventDAO.getAllEvents();
    relatedEventComboBox.setItems(FXCollections.observableArrayList(events));
    relatedEventComboBox.setCellFactory(param -> new ListCell<Event>() {
        @Override
        protected void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);
            if (empty || event == null) {
                setText(null);
            } else {
                setText(event.getName() );
            }
        }
    });
}

}
