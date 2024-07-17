import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DisplayCusData extends JFrame {

    private JLabel idLabel, lastNameLabel, firstNameLabel, phoneNumLabel;
    private JButton prevBtn, nextBtn;
    private int currentIndex = 0;
    private ResultSet resultSet;
    private Connection connection;
    private Statement statement;

    public DisplayCusData() {
        setTitle("Customer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        idLabel = new JLabel("Id:");
        lastNameLabel = new JLabel("Last Name:");
        firstNameLabel = new JLabel("First Name:");
        phoneNumLabel = new JLabel("Phone Number:");

        prevBtn = new JButton("Previous");
        nextBtn = new JButton("Next");

        prevBtn.addActionListener(e -> showPreviousCustomer());
        nextBtn.addActionListener(e -> showNextCustomer());

        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(idLabel);

        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(lastNameLabel);

        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.add(firstNameLabel);

        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(phoneNumLabel);

        JPanel panel5 = new JPanel(new FlowLayout());
        panel5.add(prevBtn);
        panel5.add(nextBtn);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        add(panel5);

        connectToDatabase();
        loadCustomerData();

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/MySQL";
            String user = "root";
            String password = "chetra123";

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCustomerData() {
        try {
            String sql = "SELECT * FROM customers";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                displayCustomer(resultSet);
            } else {
                JOptionPane.showMessageDialog(this, "No customers found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch customer data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCustomer(ResultSet rs) throws SQLException {
        idLabel.setText("Id: " + rs.getInt("customer_id"));
        lastNameLabel.setText("Last Name: " + rs.getString("customer_last_name"));
        firstNameLabel.setText("First Name: " + rs.getString("customer_first_name"));
        phoneNumLabel.setText("Phone Number: " + rs.getString("customer_phone"));
    }

    private void showPreviousCustomer() {
        try {
            if (resultSet.previous()) {
                displayCustomer(resultSet);
            } else {
                resultSet.first();
                JOptionPane.showMessageDialog(this, "Reached the first customer", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to navigate to previous customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNextCustomer() {
        try {
            if (resultSet.next()) {
                displayCustomer(resultSet);
            } else {
                resultSet.last();
                JOptionPane.showMessageDialog(this, "Reached the last customer", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to navigate to next customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

