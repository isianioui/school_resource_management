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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp; // Add this import at the top
import java.time.LocalDate;


import com.example.models.*;
import com.example.services.EmailService;
import com.example.dao.*;

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


    @FXML private VBox reservationFormView;
    @FXML private ComboBox<Resource> resourceComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;



    @FXML
    public void initialize() {
        initializeDAOs();
        setupTableColumns();
        setupInitialVisibility();
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminder_sent"));

        setupReservationTable();
        

    }

    private void initializeDAOs() {
        reservationDAO = new ReservationDAO();
        resourceDAO = new ResourceDAO();
        eventDAO = new EventDAO();
        statisticsDAO = new StatisticsDAO();
        userDAO = new UserDAO(); // Add this line

    }

    // private void updateViewVisibility(boolean res, boolean resource, boolean event, boolean stats, boolean users) {
    //     reservationsView.setVisible(res);
    //     resourcesView.setVisible(resource);
    //     eventsView.setVisible(event);
    //     statisticsView.setVisible(stats);
    //     usersView.setVisible(users);
    // }

    private void updateViewVisibility(boolean res, boolean resource, boolean event, boolean stats, boolean users) {
        // Hide all views first
        reservationsView.setVisible(false);
        resourcesView.setVisible(false);
        eventsView.setVisible(false);
        statisticsView.setVisible(false);
        usersView.setVisible(false);
    
        // Show only the requested view
        reservationsView.setVisible(res);
        resourcesView.setVisible(resource);
        eventsView.setVisible(event);
        statisticsView.setVisible(stats);
        usersView.setVisible(users);
    }

    // ... (keep existing imports and class declaration)

    @FXML
    private void showUsers() {
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
    @FXML private TableColumn<Reservation, Boolean> reminderColumn;

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
        ObservableList<Reservation> reservations = FXCollections.observableArrayList(
                reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole()));
        reservationsTable.setItems(reservations);
    }

    @FXML
    private void showResources() {
        updateViewVisibility(false, true, false, false, false);
        ObservableList<Resource> resources = FXCollections.observableArrayList(
                resourceDAO.getAllResources());
        resourcesTable.setItems(resources);
    }

    @FXML
    private void showEvents() {
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
    
        // Setup action buttons
        reservationActionsColumn.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final Button viewButton = new Button("View");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button reminderButton = new Button("Send Reminder");
            private final Button confirmButton = new Button("Confirm");
            private final Button cancelButton = new Button("Cancel");
            private final HBox buttons = new HBox(5, viewButton, editButton, deleteButton, reminderButton, confirmButton, cancelButton);    
    
            {
                viewButton.getStyleClass().add("info-button");
                editButton.getStyleClass().add("success-button");
                deleteButton.getStyleClass().add("danger-button");
                reminderButton.getStyleClass().add("info-button");
                confirmButton.getStyleClass().add("success-button");
                cancelButton.getStyleClass().add("danger-button");
    
                viewButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    viewReservationDetails(reservation);
                });
                
    
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
            status
        );
    
        EmailService.sendEmail(user.getEmail(), emailSubject, emailBody);
        reservationDAO.updateReminderStatus(reservation.getReservation_id(), true);
        refreshReservationsTable();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder Sent");
        alert.setContentText("Reminder email has been sent successfully!");
        alert.showAndWait();
    }

@FXML private VBox reservationDetailsView;
@FXML private Label reservationIdLabel;
@FXML private Label resourceNameLabel;
@FXML private Label resourceTypeLabel;
@FXML private Label reservedByLabel;
@FXML private Label reservationDateLabel;
@FXML private Label reservationTimeLabel;
@FXML private Label reservationStatusLabel;
@FXML private Label reminderStatusLabel;
@FXML private Button backToReservationsButton;
    
    
    // private void viewReservationDetails(Reservation reservation) {
    //     Alert alert = new Alert(Alert.AlertType.INFORMATION);
    //     alert.setTitle("Reservation Details");
    //     alert.setHeaderText("Reservation #" + reservation.getReservation_id());
        
    //     // Get resource name from resourceDAO
    //     Resource resource = resourceDAO.getResourceById(reservation.getResource_id());
    //     // Get user details from userDAO
    //     User user = userDAO.getUserById(reservation.getUser_id());
        
    //     String content = String.format("""
    //         Resource: %s
    //         Type: %s
    //         Reserved by: %s
    //         Date: %s
    //         Time: %s
    //         Status: %s
    //         Reminder: %s
    //         """,
    //         resource.getName(),
    //         reservation.getResource_type(),
    //         user.getUsername(),
    //         reservation.getReservation_date(),
    //         reservation.getReservation_time(),
    //         reservation.getStatus(),
    //         reservation.getReminder_sent() ? "Sent" : "Not Sent"
    //     );
        
    //     alert.setContentText(content);
    //     alert.showAndWait();
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
    //     try {
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/reservationForm.fxml"));
    //         Parent root = loader.load();
            
    //         Stage stage = new Stage();
    //         stage.setTitle("New Reservation");
    //         stage.setScene(new Scene(root));
    //         stage.showAndWait();
            
    //         // Refresh table after form closes
    //         refreshReservationsTable();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
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
        "13:00", "14:00", "15:00", "16:00", "17:00"
    );
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
    //     try {
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/reservationForm.fxml"));
    //         Parent root = loader.load();
            
    //         ReservationFormController controller = loader.getController();
    //         controller.setReservation(reservation);
            
    //         Stage stage = new Stage();
    //         stage.setTitle("Edit Reservation");
    //         stage.setScene(new Scene(root));
    //         stage.showAndWait();
            
    //         refreshReservationsTable();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
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
        //     reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole())
        // );
        // reservationsTable.setItems(reservations);

        ObservableList<Reservation> reservations = FXCollections.observableArrayList(
        reservationDAO.getUserReservations(currentUser.getId(), currentUser.getRole())
    );
    // Add debug print
    for (Reservation res : reservations) {
        System.out.println("Reservation ID: " + res.getReservation_id() + 
                         " Reminder Status: " + res.getReminder_sent());
    }
    reservationsTable.setItems(reservations);
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
    //     this.currentUser = user;
    //     userNameLabel.setText("Welcome, " + user.getUsername());
    //     setupUserAccess();

    //     if (user.getRole().equals("admin")) {
    //         updateViewVisibility(false, false, false, true);
    //         loadAdminDashboard();
    //     } else {
    //         updateViewVisibility(true, false, false, false);
    //         showReservations();
    //     }
    // }

    public void setUser(User user) {
        this.currentUser = user;
        userNameLabel.setText("Welcome, " + user.getUsername());
        
        // Update UI based on user role without button visibility checks
        boolean isAdmin = user.getRole().equals("admin");
        // Add any other role-specific UI updates here that don't involve the removed buttons
        
        loadUserData(); // If you have this method
        setupUserAccess(); // Now this will only handle the reminder column
        showDashboard();

    }
    


    // @FXML
    // private void showDashboard() {
    //     if (currentUser.getRole().equals("admin")) {
    //         // Hide all other views first
    //         usersView.setVisible(false);
    //         reservationsView.setVisible(false);
    //         resourcesView.setVisible(false);
    //         eventsView.setVisible(false);

    //         // Show statistics view and load dashboard data
    //         statisticsView.setVisible(true);
    //         loadAdminDashboard();
    //     }
    // }

    
// @FXML
// private void showDashboard() {
//     if (currentUser.getRole().equals("admin")) {
//         updateViewVisibility(false, false, false, true, false);
//         loadAdminDashboard();
//     }
// }
@FXML
private void showDashboard() {
    // Hide all other views first
    usersView.setVisible(false);
    reservationsView.setVisible(false);
    resourcesView.setVisible(false);
    eventsView.setVisible(false);
    
    // Show statistics view for all users
    statisticsView.setVisible(true);
    
 // Load dashboard data using the existing loadAdminDashboard method
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
        // Implement contact functionality
        System.out.println("Contact clicked");
    }

    @FXML
    private void handleNotificationClick() {
        // Implement notification functionality
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
            reservation.setResource_type(selectedResource.getResource_type()); // Add this line

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

@FXML private VBox reservationEditView;
@FXML private ComboBox<Resource> editResourceComboBox;
@FXML private DatePicker editDatePicker;
@FXML private TextField editTimeField;  // Changed to TextField
@FXML private ComboBox<String> editStatusComboBox;
@FXML private Button saveEditButton;
@FXML private Button cancelEditButton;

private void editReservation(Reservation reservation) {
    reservationsView.setVisible(false);
    reservationEditView.setVisible(true);
    
    // Load resources
    ObservableList<Resource> resources = FXCollections.observableArrayList(resourceDAO.getAllResources());
    editResourceComboBox.setItems(resources);
    editResourceComboBox.setPromptText("Select Resource");
    editResourceComboBox.setValue(resourceDAO.getResourceById(reservation.getResource_id()));
    
    // Set date
    editDatePicker.setValue(reservation.getReservation_date().toLocalDate());
    
    // Set time in text field
    editTimeField.setText(reservation.getReservation_time().toString().substring(0, 5));
    editTimeField.setPromptText("Enter time (HH:mm)");
    
    // Set status options
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




}
