package org.example.project_1;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class ExamSeating2 extends Application {

    private static Map<String, String> studentDatabase = new HashMap<>();
    private static Map<String, String> seatingArrangement = new HashMap<>();
    private static int rows = 5;
    private static int columns = 5;
    private Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        primaryStage.setTitle("Exam Seating Arrangement System");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        Button adminButton = new Button("Admin Login");
        Button studentButton = new Button("Student Login");
        Button invigilatorButton = new Button("Invigilator Login");

        adminButton.setOnAction(e -> adminPanel());
        studentButton.setOnAction(e -> studentPanel());
        invigilatorButton.setOnAction(e -> invigilatorPanel());

        mainLayout.getChildren().addAll(adminButton, studentButton, invigilatorButton);

        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void adminPanel() {
        VBox adminLayout = new VBox(10);
        adminLayout.setPadding(new Insets(10));

        Button addStudentButton = new Button("Add Student");
        Button setSeatingSizeButton = new Button("Set Rows & Columns");
        Button generateSeatingButton = new Button("Generate Seating Plan");
        Button viewSeatingButton = new Button("View Seating Plan");
        Button backButton = new Button("Back");

        addStudentButton.setOnAction(e -> addStudent());
        setSeatingSizeButton.setOnAction(e -> setSeatingSize());
        generateSeatingButton.setOnAction(e -> generateSeatingPlan());
        viewSeatingButton.setOnAction(e -> viewSeatingPlan());
        backButton.setOnAction(e -> start(mainStage));

        adminLayout.getChildren().addAll(addStudentButton, setSeatingSizeButton, generateSeatingButton, viewSeatingButton, backButton);

        Scene adminScene = new Scene(adminLayout, 400, 300);
        mainStage.setScene(adminScene);
    }

    private void studentPanel() {
        VBox studentLayout = new VBox(10);
        studentLayout.setPadding(new Insets(10));

        Label rollLabel = new Label("Enter Roll Number:");
        TextField rollField = new TextField();
        Button checkSeatButton = new Button("Check Seat");
        Button backButton = new Button("Back");

        checkSeatButton.setOnAction(e -> {
            String rollNumber = rollField.getText();
            String seat = seatingArrangement.get(rollNumber);
            showAlert("Seat Information", seat != null ? "Your seat is: " + seat : "Seat not found for roll number: " + rollNumber);
        });

        backButton.setOnAction(e -> start(mainStage));

        studentLayout.getChildren().addAll(rollLabel, rollField, checkSeatButton, backButton);

        Scene studentScene = new Scene(studentLayout, 400, 300);
        mainStage.setScene(studentScene);
    }

    private void invigilatorPanel() {
        VBox invigilatorLayout = new VBox(10);
        invigilatorLayout.setPadding(new Insets(10));

        Label rollLabel = new Label("Enter Roll Number:");
        TextField rollField = new TextField();
        Button verifySeatButton = new Button("Verify Seat");
        Button backButton = new Button("Back");

        verifySeatButton.setOnAction(e -> {
            String rollNumber = rollField.getText();
            String seat = seatingArrangement.get(rollNumber);
            showAlert("Verification", seat != null ? "Seat verified: " + seat : "Invalid roll number.");
        });

        backButton.setOnAction(e -> start(mainStage));

        invigilatorLayout.getChildren().addAll(rollLabel, rollField, verifySeatButton, backButton);

        Scene invigilatorScene = new Scene(invigilatorLayout, 400, 300);
        mainStage.setScene(invigilatorScene);
    }

    private void addStudent() {
        TextInputDialog rollInput = new TextInputDialog();
        rollInput.setTitle("Add Student");
        rollInput.setHeaderText("Enter Roll Number:");
        Optional<String> rollNumber = rollInput.showAndWait();

        if (rollNumber.isPresent() && !rollNumber.get().trim().isEmpty()) {
            TextInputDialog nameInput = new TextInputDialog();
            nameInput.setTitle("Add Student");
            nameInput.setHeaderText("Enter Name:");
            Optional<String> name = nameInput.showAndWait();

            if (name.isPresent() && !name.get().trim().isEmpty()) {
                studentDatabase.put(rollNumber.get().trim(), name.get().trim());
                showAlert("Success", "Student added: " + name.get().trim() + " (" + rollNumber.get().trim() + ")");
            }
        }
    }

    private void setSeatingSize() {
        TextInputDialog rowInput = new TextInputDialog(String.valueOf(rows));
        rowInput.setTitle("Set Seating Size");
        rowInput.setHeaderText("Enter Number of Rows:");
        Optional<String> rowValue = rowInput.showAndWait();

        TextInputDialog columnInput = new TextInputDialog(String.valueOf(columns));
        columnInput.setTitle("Set Seating Size");
        columnInput.setHeaderText("Enter Number of Columns:");
        Optional<String> columnValue = columnInput.showAndWait();

        if (rowValue.isPresent() && columnValue.isPresent()) {
            rows = Integer.parseInt(rowValue.get().trim());
            columns = Integer.parseInt(columnValue.get().trim());
            showAlert("Success", "Seating size set to " + rows + " rows and " + columns + " columns.");
        }
    }

    private void generateSeatingPlan() {
        seatingArrangement.clear();
        List<String> rollNumbers = new ArrayList<>(studentDatabase.keySet());
        Collections.shuffle(rollNumbers);

        int seatNumber = 0;
        for (String rollNumber : rollNumbers) {
            int row = seatNumber / columns + 1;
            int column = seatNumber % columns + 1;
            String seat = "Row " + row + ", Column " + column;
            seatingArrangement.put(rollNumber, seat);
            seatNumber++;
        }

        showAlert("Success", "Seating plan generated.");
    }

    private void viewSeatingPlan() {
        Stage seatingStage = new Stage();
        TableView<StudentSeat> table = new TableView<>();
        ObservableList<StudentSeat> data = FXCollections.observableArrayList();

        TableColumn<StudentSeat, String> rollColumn = new TableColumn<>("Roll Number");
        rollColumn.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));

        TableColumn<StudentSeat, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudentSeat, String> seatColumn = new TableColumn<>("Seat");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));

        table.getColumns().addAll(rollColumn, nameColumn, seatColumn);

        for (Map.Entry<String, String> entry : seatingArrangement.entrySet()) {
            data.add(new StudentSeat(entry.getKey(), studentDatabase.get(entry.getKey()), entry.getValue()));
        }

        table.setItems(data);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> seatingStage.close());

        VBox layout = new VBox(10, table, backButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 500, 400);
        seatingStage.setScene(scene);
        seatingStage.setTitle("Seating Plan");
        seatingStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class StudentSeat {
        private final String rollNumber, name, seat;

        public StudentSeat(String rollNumber, String name, String seat) {
            this.rollNumber = rollNumber;
            this.name = name;
            this.seat = seat;
        }

        public String getRollNumber() { return rollNumber; }
        public String getName() { return name; }
        public String getSeat() { return seat; }
    }
}
