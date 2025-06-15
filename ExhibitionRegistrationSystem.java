import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;

public class ExhibitionRegistrationSystem extends JFrame {
    // Database connection components
    private static final String DB_URL = "jdbc:ucanaccess://VUE_Exhibition.accdb";
    private Connection connection;
    
    // GUI Components
    private JTextField txtRegistrationId, txtStudentName, txtFaculty, txtProjectTitle, txtContactNumber, txtEmail;
    private JLabel lblImagePath, lblImageDisplay;
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit, btnUploadImage;
    private String selectedImagePath = "";
    
    public ExhibitionRegistrationSystem() {
        initializeDatabase();
        initializeGUI();
    }
    
    private void initializeDatabase() {
        try {
            // Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected successfully!");
            
            // Create table if it doesn't exist
            createTableIfNotExists();
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "JDBC Driver not found. Please ensure UCanAccess JARs are in classpath: " + e.getMessage(),
                                        "Database Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            connection = null; // Ensure connection is null if driver fails
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage() + "\nEnsure 'VUE_Exhibition.accdb' is in the correct directory.",
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            connection = null; // Ensure connection is null if connection fails
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred during database initialization: " + e.getMessage(),
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            connection = null; // Ensure connection is null for any other exception
        }
    }
    
    // OPTIONAL MODIFICATION - DO NOT IMPLEMENT UNLESS PREVIOUS STEPS FAIL
private void createTableIfNotExists() {
    if (connection == null) {
        System.err.println("Cannot create table: Database connection is null.");
        return;
    }
    String createTableSQL = """
        CREATE TABLE Participants (
            RegistrationID VARCHAR(11) PRIMARY KEY,
            StudentName VARCHAR(100) NOT NULL,
            Faculty VARCHAR(50) NOT NULL,
            ProjectTitle VARCHAR(200) NOT NULL,
            ContactNumber VARCHAR(15) NOT NULL,
            EmailAddress VARCHAR(100) NOT NULL,
            ImagePath VARCHAR(500)
        )
    """; // Removed IF NOT EXISTS for specific error handling

    try (Statement stmt = connection.createStatement()) { // Use Statement for DDL
        stmt.execute(createTableSQL);
        System.out.println("Table 'Participants' created successfully.");
    } catch (SQLException e) {
        // SQL State '42S01' often indicates 'Table already exists'
        if ("someState".equals("42S01")) {
            System.out.println("Table 'Participants' already exists. Proceeding.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create table: " + e.getMessage(),
                                        "Database Table Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
    
    private void initializeGUI() {
        setTitle("Victoria University - Innovation & Technology Exhibition Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JLabel headerLabel = new JLabel("Exhibition Registration System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set window properties - Increased size
        setSize(650, 750); // Increased width and height
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Participant Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Registration ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Registration ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtRegistrationId = new JTextField(20); // Increased preferred width
        panel.add(txtRegistrationId, gbc);
        
        // Student Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtStudentName = new JTextField(20); // Increased preferred width
        panel.add(txtStudentName, gbc);
        
        // Faculty
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Faculty:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtFaculty = new JTextField(20); // Increased preferred width
        panel.add(txtFaculty, gbc);
        
        // Project Title
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Project Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtProjectTitle = new JTextField(20); // Increased preferred width
        panel.add(txtProjectTitle, gbc);
        
        // Contact Number
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtContactNumber = new JTextField(20); // Increased preferred width
        panel.add(txtContactNumber, gbc);
        
        // Email Address
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email Address:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField(20); // Increased preferred width
        panel.add(txtEmail, gbc);
        
        // Image Upload Section
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Project Image:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnUploadImage = new JButton("Upload Image");
        btnUploadImage.addActionListener(_ -> uploadImage());
        lblImagePath = new JLabel("No image selected");
        lblImagePath.setForeground(Color.GRAY);
        
        imagePanel.add(btnUploadImage);
        imagePanel.add(lblImagePath);
        panel.add(imagePanel, gbc);
        
        // Image Display
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        lblImageDisplay = new JLabel("Image will appear here", JLabel.CENTER);
        lblImageDisplay.setPreferredSize(new Dimension(250, 200)); // Increased image display size
        lblImageDisplay.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(lblImageDisplay, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Increased horizontal gap
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        btnRegister = new JButton("Register");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");
        
        // Set button colors
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(Color.WHITE);
        btnSearch.setBackground(new Color(30, 144, 255));
        btnSearch.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(255, 165, 0));
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(128, 128, 128));
        btnClear.setForeground(Color.WHITE);
        btnExit.setBackground(new Color(105, 105, 105));
        btnExit.setForeground(Color.WHITE);
        
        // Add action listeners
        btnRegister.addActionListener(_ -> registerParticipant());
        btnSearch.addActionListener(_ -> searchParticipant());
        btnUpdate.addActionListener(_ -> updateParticipant());
        btnDelete.addActionListener(_ -> deleteParticipant());
        btnClear.addActionListener(_ -> clearFields());
        btnExit.addActionListener(_ -> exitApplication());
        
        panel.add(btnRegister);
        panel.add(btnSearch);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnExit);
        
        return panel;
    }
    
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "bmp"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            lblImagePath.setText(selectedFile.getName());
            lblImagePath.setForeground(Color.BLACK);
            
            // Display image preview
            displayImage(selectedImagePath);
        }
    }
    
    private void displayImage(String imagePath) {
        try {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage().getScaledInstance(lblImageDisplay.getWidth(), lblImageDisplay.getHeight(), Image.SCALE_SMOOTH);
            lblImageDisplay.setIcon(new ImageIcon(image));
            lblImageDisplay.setText("");
        } catch (Exception e) {
            lblImageDisplay.setIcon(null);
            lblImageDisplay.setText("Image preview unavailable");
            e.printStackTrace(); // Print stack trace for debugging image loading issues
        }
    }
    
    private boolean validateInput() {
        // Check for empty fields
        if (txtRegistrationId.getText().trim().isEmpty() ||
            txtStudentName.getText().trim().isEmpty() ||
            txtFaculty.getText().trim().isEmpty() ||
            txtProjectTitle.getText().trim().isEmpty() ||
            txtContactNumber.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, txtEmail.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate contact number (basic validation)
        String contactRegex = "^[0-9+\\-\\s]+$";
        if (!Pattern.matches(contactRegex, txtContactNumber.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid contact number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void registerParticipant() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database is not connected. Please restart the application or check database configuration.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateInput()) return;
        
        String sql = "INSERT INTO Participants (RegistrationID, StudentName, Faculty, ProjectTitle, ContactNumber, EmailAddress, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, txtRegistrationId.getText().trim());
            pstmt.setString(2, txtStudentName.getText().trim());
            pstmt.setString(3, txtFaculty.getText().trim());
            pstmt.setString(4, txtProjectTitle.getText().trim());
            pstmt.setString(5, txtContactNumber.getText().trim());
            pstmt.setString(6, txtEmail.getText().trim());
            pstmt.setString(7, selectedImagePath);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Participant registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate")) { // Case-insensitive check for duplicate
                JOptionPane.showMessageDialog(this, "Registration ID already exists! Please use a unique ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace(); // For debugging
        }
    }
    
    private void searchParticipant() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database is not connected. Please restart the application or check database configuration.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String registrationId = txtRegistrationId.getText().trim();
        if (registrationId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Registration ID to search!", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, registrationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtStudentName.setText(rs.getString("StudentName"));
                txtFaculty.setText(rs.getString("Faculty"));
                txtProjectTitle.setText(rs.getString("ProjectTitle"));
                txtContactNumber.setText(rs.getString("ContactNumber"));
                txtEmail.setText(rs.getString("EmailAddress"));
                
                String imagePath = rs.getString("ImagePath");
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) { // Check if the file actually exists
                        selectedImagePath = imagePath;
                        lblImagePath.setText(imageFile.getName());
                        lblImagePath.setForeground(Color.BLACK);
                        displayImage(imagePath);
                    } else {
                        selectedImagePath = "";
                        lblImagePath.setText("Image file not found.");
                        lblImagePath.setForeground(Color.RED);
                        lblImageDisplay.setIcon(null);
                        lblImageDisplay.setText("Image file not found at " + imagePath);
                    }
                } else {
                    selectedImagePath = "";
                    lblImagePath.setText("No image selected");
                    lblImagePath.setForeground(Color.GRAY);
                    lblImageDisplay.setIcon(null);
                    lblImageDisplay.setText("Image will appear here");
                }
                
                JOptionPane.showMessageDialog(this, "Participant found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No participant found with this Registration ID!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                clearFields(); // Clear fields if no participant is found for a new search
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // For debugging
        }
    }
    
    private void updateParticipant() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database is not connected. Please restart the application or check database configuration.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateInput()) return;
        
        String sql = "UPDATE Participants SET StudentName = ?, Faculty = ?, ProjectTitle = ?, ContactNumber = ?, EmailAddress = ?, ImagePath = ? WHERE RegistrationID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, txtStudentName.getText().trim());
            pstmt.setString(2, txtFaculty.getText().trim());
            pstmt.setString(3, txtProjectTitle.getText().trim());
            pstmt.setString(4, txtContactNumber.getText().trim());
            pstmt.setString(5, txtEmail.getText().trim());
            pstmt.setString(6, selectedImagePath);
            pstmt.setString(7, txtRegistrationId.getText().trim());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Participant updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No participant found with this Registration ID to update!", "Update Failed", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // For debugging
        }
    }
    
    private void deleteParticipant() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database is not connected. Please restart the application or check database configuration.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String registrationId = txtRegistrationId.getText().trim();
        if (registrationId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Registration ID to delete!", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this participant?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Participants WHERE RegistrationID = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, registrationId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Participant deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No participant found with this Registration ID to delete!", "Delete Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); // For debugging
            }
        }
    }
    
    private void clearFields() {
        txtRegistrationId.setText("");
        txtStudentName.setText("");
        txtFaculty.setText("");
        txtProjectTitle.setText("");
        txtContactNumber.setText("");
        txtEmail.setText("");
        selectedImagePath = "";
        lblImagePath.setText("No image selected");
        lblImagePath.setForeground(Color.GRAY);
        lblImageDisplay.setIcon(null);
        lblImageDisplay.setText("Image will appear here");
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to exit?", 
            "Confirm Exit", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ExhibitionRegistrationSystem().setVisible(true);
        });
    }
}