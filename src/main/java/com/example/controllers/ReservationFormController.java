package com.example.controllers;
import com.example.dao.ReservationDAO;
import com.example.dao.ResourceDAO;
import com.example.models.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ReservationFormController {
    @FXML private ComboBox<Resource> resourceComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;
    
    private ResourceDAO resourceDAO;
    private ReservationDAO reservationDAO;
    private Reservation existingReservation;
    private User currentUser;

    @FXML
    public void initialize() {
        resourceDAO = new ResourceDAO();
        reservationDAO = new ReservationDAO();
        
        loadResources();
        setupTimeComboBox();
        setupDatePicker();
    }

    private void loadResources() {
        ObservableList<Resource> resources = FXCollections.observableArrayList(resourceDAO.getAllResources());
        resourceComboBox.setItems(resources);
    }

    private void setupTimeComboBox() {
        ObservableList<String> times = FXCollections.observableArrayList(
            "08:00", "09:00", "10:00", "11:00", "12:00", 
            "13:00", "14:00", "15:00", "16:00", "17:00"
        );
        timeComboBox.setItems(times);
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
    private void handleSave() {
        if (validateInputs()) {
            Reservation reservation = new Reservation();
            reservation.setResource_id(resourceComboBox.getValue().getResource_id());
            reservation.setUser_id(currentUser.getId());
            reservation.setReservation_date(Date.valueOf(datePicker.getValue()));
            reservation.setReservation_time(Time.valueOf(timeComboBox.getValue() + ":00"));
            
            boolean success;
            if (existingReservation != null) {
                reservation.setReservation_id(existingReservation.getReservation_id());
                success = reservationDAO.updateReservation(reservation);
            } else {
                success = reservationDAO.createReservation(reservation);
            }
            
            if (success) {
                closeWindow();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (resourceComboBox.getValue() == null) {
            showError("Please select a resource");
            return false;
        }
        if (datePicker.getValue() == null) {
            showError("Please select a date");
            return false;
        }
        if (timeComboBox.getValue() == null) {
            showError("Please select a time");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) resourceComboBox.getScene().getWindow()).close();
    }

    public void setReservation(Reservation reservation) {
        this.existingReservation = reservation;
        if (reservation != null) {
            resourceComboBox.setValue(resourceDAO.getResourceById(reservation.getResource_id()));
            datePicker.setValue(reservation.getReservation_date().toLocalDate());
            timeComboBox.setValue(reservation.getReservation_time().toString().substring(0, 5));
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}
