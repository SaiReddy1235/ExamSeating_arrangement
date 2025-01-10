import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExamSeatings{

    private static String[][] seatingArrangement;
    private static int rows;
    private static int columns;
    private static JFrame frame;

    public static void main(String[] args) {
        // Input for rows and columns
        rows = Integer.parseInt(JOptionPane.showInputDialog("Enter number of rows in the exam hall:"));
        columns = Integer.parseInt(JOptionPane.showInputDialog("Enter number of columns in the exam hall:"));

        seatingArrangement = new String[rows][columns];

        // Initialize seating arrangement
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatingArrangement[i][j] = "Empty";
            }
        }

        // Create main frame
        frame = new JFrame("Exam Seating Arrangement System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Buttons
        JButton allocateButton = new JButton("Allocate Seat");
        JButton viewButton = new JButton("View Seating");
        JButton resetButton = new JButton("Reset Seat");
        JButton exitButton = new JButton("Exit");

        // Add Action Listeners
        allocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allocateSeat();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSeatingArrangement();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSeat();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Add buttons to panel
        panel.add(allocateButton);
        panel.add(viewButton);
        panel.add(resetButton);
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void allocateSeat() {
        try {
            int row = Integer.parseInt(JOptionPane.showInputDialog("Enter row number (0-indexed):"));
            int column = Integer.parseInt(JOptionPane.showInputDialog("Enter column number (0-indexed):"));

            if (isValidSeat(row, column)) {
                if (seatingArrangement[row][column].equals("Empty")) {
                    String rollNumber = JOptionPane.showInputDialog("Enter student roll number:");
                    seatingArrangement[row][column] = rollNumber;
                    JOptionPane.showMessageDialog(frame, "Seat allocated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Seat is already occupied.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid seat position.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please try again.");
        }
    }

    private static void viewSeatingArrangement() {
        StringBuilder seatingDisplay = new StringBuilder();
        seatingDisplay.append("Current Seating Arrangement:\n\n");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatingDisplay.append(seatingArrangement[i][j]).append("\t");
            }
            seatingDisplay.append("\n");
        }

        JTextArea textArea = new JTextArea(seatingDisplay.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Seating Arrangement", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void resetSeat() {
        try {
            int row = Integer.parseInt(JOptionPane.showInputDialog("Enter row number (0-indexed):"));
            int column = Integer.parseInt(JOptionPane.showInputDialog("Enter column number (0-indexed):"));

            if (isValidSeat(row, column)) {
                seatingArrangement[row][column] = "Empty";
                JOptionPane.showMessageDialog(frame, "Seat reset successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid seat position.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please try again.");
        }
    }

    private static boolean isValidSeat(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
}
