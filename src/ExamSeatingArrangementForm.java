import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ExamSeatingArrangementForm extends Frame {

    private Label rollNoLabel, nameLabel, seatNoLabel;
    private TextField rollNoTF, nameTF, seatNoTF;
    private Button generateSeatNoButton, deleteButton, searchButton;

    private Connection conn;

    public ExamSeatingArrangementForm() {
        super("Exam Hall Seating Arrangement");

        setLayout(new FlowLayout());

        rollNoLabel = new Label("Roll No.");
        rollNoTF= new TextField(20);
        nameTF = new TextField(20);
        nameLabel = new Label("Name");
        seatNoLabel = new Label("Seat No.");
        seatNoTF = new TextField(10);
        generateSeatNoButton = new Button("Generate Seat No.");
        deleteButton = new Button("Delete Student");
        searchButton = new Button("Search Student");

        add(rollNoLabel);
        add(rollNoTF);
        add(nameLabel);
        add(nameTF);
        add(seatNoLabel);
        add(seatNoTF);
        add(generateSeatNoButton);
        add(deleteButton);
        add(searchButton);

        generateSeatNoButton.addActionListener(e -> generateSeatNo());
        deleteButton.addActionListener(e -> deleteStudent());
        searchButton.addActionListener(e -> searchStudent());

        // Connect to the database.
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating_arrangement", "root", "adi@123root.com");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(400, 200);
        setVisible(true);
    }

    private void generateSeatNo() {
        int rollNo = Integer.parseInt(rollNoTF.getText());
        String name = nameTF.getText();

        // Generate a seat number for the student.
        int seatNo = rollNo % 100;

        // Save the student details to the database.
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO students (roll_no, name, seat_no) VALUES (" + rollNo + ", '" + name + "', " + seatNo + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        seatNoTF.setText(String.valueOf(seatNo));
    }

    private void deleteStudent() {
        int rollNo = Integer.parseInt(rollNoTF.getText());

        // Delete the student record from the database.
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM students WHERE roll_no = " + rollNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resetForm();
    }

    private void searchStudent() {
        int rollNo = Integer.parseInt(rollNoTF.getText());

        // Search for the student record in the database.
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE roll_no = " + rollNo);

            if (rs.next()) {
                nameTF.setText(rs.getString("name"));
                seatNoTF.setText(rs.getString("seat_no"));
            } else {
                resetForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetForm() {
        rollNoTF.setText("");
        nameTF.setText("");
        seatNoTF.setText("");
    }

    public static void main(String[] args) {
        new ExamSeatingArrangementForm();
    }
}
