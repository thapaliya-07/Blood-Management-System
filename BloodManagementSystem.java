package bloodmanagement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// Main application class with GUI
public class BloodManagementSystem extends JFrame {
    
    // Data storage
    private List<User> users;
    private List<Hospital> hospitals;
    private User currentUser;
    private Hospital currentHospital;
    
    // Admin credentials for adding hospitals
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    // GUI components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public BloodManagementSystem() {
        // Load existing data from files
        users = DataManager.loadUsers();
        hospitals = DataManager.loadHospitals();
        
        // Add sample hospitals ONLY if no hospitals exist (first time running)
        if (hospitals.isEmpty()) {
            Hospital h1 = new Hospital("City Hospital", "CITY123");
            h1.addBlood(BloodType.O_NEGATIVE, 25);
            h1.addBlood(BloodType.A_POSITIVE, 35);
            h1.addBlood(BloodType.B_POSITIVE, 5); // Critical
            hospitals.add(h1);
            
            Hospital h2 = new Hospital("General Hospital", "GEN456");
            h2.addBlood(BloodType.O_NEGATIVE, 8); // Critical
            h2.addBlood(BloodType.AB_POSITIVE, 40);
            h2.addBlood(BloodType.A_NEGATIVE, 15);
            hospitals.add(h2);
            
            Hospital h3 = new Hospital("Central Hospital", "CENT789");
            h3.addBlood(BloodType.B_NEGATIVE, 20);
            h3.addBlood(BloodType.O_POSITIVE, 32);
            hospitals.add(h3);
            
            // Save initial hospitals
            DataManager.saveHospitals(hospitals);
        }
        
        // Setup frame
        setTitle("Blood Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Use CardLayout for switching between screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Add all screens
        mainPanel.add(createWelcomeScreen(), "welcome");
        mainPanel.add(createLoginScreen(), "login");
        mainPanel.add(createSignupScreen(), "signup");
        mainPanel.add(createHospitalLoginScreen(), "hospitalLogin");
        mainPanel.add(createAdminLoginScreen(), "adminLogin");
        mainPanel.add(createAddHospitalScreen(), "addHospital");
        // User and hospital dashboards will be created after login
        
        add(mainPanel);
        
        // Show welcome screen first
        cardLayout.show(mainPanel, "welcome");
    }
    
    // Welcome screen with blood theme
    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 20, 60)); // Crimson red
        
        JLabel title = new JLabel("💉 Blood Management System 🩸", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 10, 20));
        centerPanel.setBackground(new Color(220, 20, 60));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 100, 150));
        
        JButton userLoginBtn = createStyledButton("User Login");
        JButton userSignupBtn = createStyledButton("User Sign Up");
        JButton hospitalLoginBtn = createStyledButton("Hospital Login");
        JButton addHospitalBtn = createStyledButton("Add Hospital");
        JButton exitBtn = createStyledButton("Exit");
        
        userLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        userSignupBtn.addActionListener(e -> cardLayout.show(mainPanel, "signup"));
        hospitalLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "hospitalLogin"));
        addHospitalBtn.addActionListener(e -> cardLayout.show(mainPanel, "adminLogin"));
        exitBtn.addActionListener(e -> System.exit(0));
        
        centerPanel.add(userLoginBtn);
        centerPanel.add(userSignupBtn);
        centerPanel.add(hospitalLoginBtn);
        centerPanel.add(addHospitalBtn);
        centerPanel.add(exitBtn);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // User login screen
    private JPanel createLoginScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel title = new JLabel("User Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(240, 240, 240));
        
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        
        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            for (User user : users) {
                if (user.authenticate(username, password)) {
                    currentUser = user;
                    mainPanel.add(createUserDashboard(), "userDashboard");
                    cardLayout.show(mainPanel, "userDashboard");
                    userField.setText("");
                    passField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        });
        
        backBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            cardLayout.show(mainPanel, "welcome");
        });
        
        formPanel.add(loginBtn);
        formPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // User signup screen
    private JPanel createSignupScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel title = new JLabel("User Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(240, 240, 240));
        
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel bloodLabel = new JLabel("Blood Type:");
        JComboBox<BloodType> bloodCombo = new JComboBox<>(BloodType.values());
        
        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        formPanel.add(bloodLabel);
        formPanel.add(bloodCombo);
        
        JButton signupBtn = new JButton("Sign Up");
        JButton backBtn = new JButton("Back");
        
        signupBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            BloodType bloodType = (BloodType) bloodCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            users.add(new User(username, password, bloodType));
            DataManager.saveUsers(users); // Save to file
            JOptionPane.showMessageDialog(this, "Sign up successful! Please login.");
            userField.setText("");
            passField.setText("");
            cardLayout.show(mainPanel, "login");
        });
        
        backBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            cardLayout.show(mainPanel, "welcome");
        });
        
        formPanel.add(signupBtn);
        formPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Hospital login screen
    private JPanel createHospitalLoginScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel title = new JLabel("Hospital Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(240, 240, 240));
        
        JLabel hospLabel = new JLabel("Hospital Name:");
        JComboBox<String> hospCombo = new JComboBox<>();
        for (Hospital h : hospitals) {
            hospCombo.addItem(h.getName());
        }
        
        JLabel codeLabel = new JLabel("Login Code:");
        JPasswordField codeField = new JPasswordField();
        
        formPanel.add(hospLabel);
        formPanel.add(hospCombo);
        formPanel.add(codeLabel);
        formPanel.add(codeField);
        
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        
        loginBtn.addActionListener(e -> {
            String hospName = (String) hospCombo.getSelectedItem();
            String code = new String(codeField.getPassword());
            
            for (Hospital h : hospitals) {
                if (h.getName().equals(hospName) && h.authenticate(code)) {
                    currentHospital = h;
                    mainPanel.add(createHospitalDashboard(), "hospitalDashboard");
                    cardLayout.show(mainPanel, "hospitalDashboard");
                    codeField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid login code!");
        });
        
        backBtn.addActionListener(e -> {
            codeField.setText("");
            cardLayout.show(mainPanel, "welcome");
        });
        
        formPanel.add(loginBtn);
        formPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Admin login screen for adding hospitals
    private JPanel createAdminLoginScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(240, 240, 240));
        
        JLabel userLabel = new JLabel("Admin Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Admin Password:");
        JPasswordField passField = new JPasswordField();
        
        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                userField.setText("");
                passField.setText("");
                cardLayout.show(mainPanel, "addHospital");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials!");
            }
        });
        
        backBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            cardLayout.show(mainPanel, "welcome");
        });
        
        formPanel.add(loginBtn);
        formPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Add note at bottom
        JLabel noteLabel = new JLabel("Note: Only admins can add hospitals", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        noteLabel.setForeground(Color.GRAY);
        noteLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(noteLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Add Hospital screen
    private JPanel createAddHospitalScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel title = new JLabel("Add New Hospital", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(240, 240, 240));
        
        JLabel nameLabel = new JLabel("Hospital Name:");
        JTextField nameField = new JTextField();
        JLabel codeLabel = new JLabel("Login Code:");
        JPasswordField codeField = new JPasswordField();
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(codeLabel);
        formPanel.add(codeField);
        
        JButton addBtn = new JButton("Add Hospital");
        JButton backBtn = new JButton("Back");
        
        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String code = new String(codeField.getPassword());
            
            if (name.isEmpty() || code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            // Check if hospital name already exists
            for (Hospital h : hospitals) {
                if (h.getName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "Hospital with this name already exists!");
                    return;
                }
            }
            
            // Create new hospital
            Hospital newHospital = new Hospital(name, code);
            
            // Ask for initial blood quantities for each blood type
            boolean addedSuccessfully = true;
            for (BloodType type : BloodType.values()) {
                String input = JOptionPane.showInputDialog(this, 
                    "Enter initial quantity for " + type + ":", 
                    "Blood Inventory Setup", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (input == null) {
                    // User cancelled
                    JOptionPane.showMessageDialog(this, "Hospital addition cancelled!");
                    addedSuccessfully = false;
                    break;
                }
                
                try {
                    int quantity = Integer.parseInt(input);
                    if (quantity < 0) {
                        JOptionPane.showMessageDialog(this, "Quantity cannot be negative!");
                        addedSuccessfully = false;
                        break;
                    }
                    newHospital.addBlood(type, quantity);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number for " + type + "!");
                    addedSuccessfully = false;
                    break;
                }
            }
            
            if (addedSuccessfully) {
                hospitals.add(newHospital);
                DataManager.saveHospitals(hospitals); // Save to file
                JOptionPane.showMessageDialog(this, "Hospital added successfully with blood inventory!");
                nameField.setText("");
                codeField.setText("");
                
                // Refresh hospital login screen to show new hospital
                mainPanel.remove(mainPanel.getComponent(3)); // Remove old hospital login
                mainPanel.add(createHospitalLoginScreen(), "hospitalLogin", 3); // Add updated one
                
                cardLayout.show(mainPanel, "welcome");
            }
        });
        
        backBtn.addActionListener(e -> {
            nameField.setText("");
            codeField.setText("");
            cardLayout.show(mainPanel, "welcome");
        });
        
        formPanel.add(addBtn);
        formPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // User dashboard
    private JPanel createUserDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Check if currentUser is null (safety check)
        if (currentUser == null) {
            return panel;
        }
        
        JLabel title = new JLabel("Welcome, " + currentUser.getUsername() + " (" + currentUser.getBloodType() + ")", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 100, 150));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton donateBtn = createStyledButton("Donate Blood");
        JButton receiveBtn = createStyledButton("Receive Blood");
        JButton compatBtn = createStyledButton("Compatibility Chart");
        JButton logoutBtn = createStyledButton("Logout");
        
        donateBtn.addActionListener(e -> showDonateBloodDialog());
        receiveBtn.addActionListener(e -> showReceiveBloodDialog());
        compatBtn.addActionListener(e -> showCompatibilityChart());
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "welcome");
        });
        
        buttonPanel.add(donateBtn);
        buttonPanel.add(receiveBtn);
        buttonPanel.add(compatBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        // Check for critical blood alerts
        checkCriticalAlerts();
        
        return panel;
    }
    
    // Hospital dashboard
    private JPanel createHospitalDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Check if currentHospital is null (safety check)
        if (currentHospital == null) {
            return panel;
        }
        
        JLabel title = new JLabel(currentHospital.getName() + " Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 100, 150));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton addBloodBtn = createStyledButton("Add Blood Units");
        JButton removeBloodBtn = createStyledButton("Remove Blood Units");
        JButton viewStatusBtn = createStyledButton("View Blood Status");
        JButton logoutBtn = createStyledButton("Logout");
        
        addBloodBtn.addActionListener(e -> showAddBloodDialog());
        removeBloodBtn.addActionListener(e -> showRemoveBloodDialog());
        viewStatusBtn.addActionListener(e -> showBloodStatus());
        logoutBtn.addActionListener(e -> {
            currentHospital = null;
            cardLayout.show(mainPanel, "welcome");
        });
        
        buttonPanel.add(addBloodBtn);
        buttonPanel.add(removeBloodBtn);
        buttonPanel.add(viewStatusBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Show donate blood dialog
    private void showDonateBloodDialog() {
        // Find hospitals that need this blood type critically
        List<Hospital> urgentHospitals = new ArrayList<>();
        BloodType donorType = currentUser.getBloodType();
        
        for (Hospital h : hospitals) {
            List<BloodType> canDonateTo = CompatibilityChecker.getCompatibleRecipients(donorType);
            for (BloodType type : canDonateTo) {
                if (h.getBloodQuantity(type) < 10) {
                    urgentHospitals.add(h);
                    break;
                }
            }
        }
        
        // Show urgent hospitals first
        StringBuilder message = new StringBuilder();
        if (!urgentHospitals.isEmpty()) {
            message.append("🚨 URGENT REQUESTS:\n\n");
            for (Hospital h : urgentHospitals) {
                message.append("• " + h.getName() + " needs " + donorType + " blood!\n");
            }
            message.append("\nSelect a hospital to donate:\n");
        } else {
            message.append("Select a hospital to donate blood:\n");
        }
        
        String[] hospitalNames = new String[hospitals.size()];
        for (int i = 0; i < hospitals.size(); i++) {
            hospitalNames[i] = hospitals.get(i).getName();
        }
        
        String selected = (String) JOptionPane.showInputDialog(this, message.toString(),
                "Donate Blood", JOptionPane.QUESTION_MESSAGE, null, hospitalNames, hospitalNames[0]);
        
        if (selected != null) {
            String unitsStr = JOptionPane.showInputDialog(this, "How many units to donate?");
            try {
                int units = Integer.parseInt(unitsStr);
                if (units > 0) {
                    for (Hospital h : hospitals) {
                        if (h.getName().equals(selected)) {
                            h.addBlood(currentUser.getBloodType(), units);
                            DataManager.saveHospitals(hospitals); // Save after donation
                            JOptionPane.showMessageDialog(this, "Thank you for donating " + units + " units!");
                            return;
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }
    
    // Show receive blood dialog
    private void showReceiveBloodDialog() {
        BloodType recipientType = currentUser.getBloodType();
        List<BloodType> compatible = CompatibilityChecker.getCompatibleDonors(recipientType);
        
        // Find hospitals with compatible blood
        List<Hospital> availableHospitals = new ArrayList<>();
        for (Hospital h : hospitals) {
            for (BloodType type : compatible) {
                if (h.hasBloodType(type)) {
                    availableHospitals.add(h);
                    break;
                }
            }
        }
        
        if (availableHospitals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hospitals have compatible blood available!");
            return;
        }
        
        StringBuilder message = new StringBuilder();
        message.append("Hospitals with blood compatible for " + recipientType + ":\n\n");
        for (Hospital h : availableHospitals) {
            message.append("• " + h.getName() + "\n");
            for (BloodType type : compatible) {
                int qty = h.getBloodQuantity(type);
                if (qty > 0) {
                    message.append("  - " + type + ": " + qty + " units\n");
                }
            }
        }
        
        String[] hospitalNames = new String[availableHospitals.size()];
        for (int i = 0; i < availableHospitals.size(); i++) {
            hospitalNames[i] = availableHospitals.get(i).getName();
        }
        
        String selected = (String) JOptionPane.showInputDialog(this, message.toString(),
                "Receive Blood", JOptionPane.INFORMATION_MESSAGE, null, hospitalNames, hospitalNames[0]);
        
        if (selected != null) {
            for (Hospital h : availableHospitals) {
                if (h.getName().equals(selected)) {
                    // Show available blood types
                    List<String> availableTypes = new ArrayList<>();
                    for (BloodType type : compatible) {
                        if (h.hasBloodType(type)) {
                            availableTypes.add(type.toString());
                        }
                    }
                    
                    String bloodType = (String) JOptionPane.showInputDialog(this, "Select blood type:",
                            "Receive Blood", JOptionPane.QUESTION_MESSAGE, null,
                            availableTypes.toArray(), availableTypes.get(0));
                    
                    if (bloodType != null) {
                        String unitsStr = JOptionPane.showInputDialog(this, "How many units needed?");
                        try {
                            int units = Integer.parseInt(unitsStr);
                            BloodType selectedType = BloodType.valueOf(bloodType.replace("+", "_POSITIVE").replace("-", "_NEGATIVE"));
                            
                            if (h.removeBlood(selectedType, units)) {
                                DataManager.saveHospitals(hospitals); // Save after receiving blood
                                JOptionPane.showMessageDialog(this, "Blood transfusion successful!");
                            } else {
                                JOptionPane.showMessageDialog(this, "Not enough blood available!");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input!");
                        }
                    }
                }
            }
        }
    }
    
    // Show compatibility chart
    private void showCompatibilityChart() {
        StringBuilder chart = new StringBuilder();
        chart.append("BLOOD TYPE COMPATIBILITY CHART\n");
        chart.append("=====================================\n\n");
        
        for (BloodType type : BloodType.values()) {
            chart.append(type + " can receive from: ");
            List<BloodType> donors = CompatibilityChecker.getCompatibleDonors(type);
            for (int i = 0; i < donors.size(); i++) {
                chart.append(donors.get(i));
                if (i < donors.size() - 1) chart.append(", ");
            }
            chart.append("\n");
            
            chart.append(type + " can donate to: ");
            List<BloodType> recipients = CompatibilityChecker.getCompatibleRecipients(type);
            for (int i = 0; i < recipients.size(); i++) {
                chart.append(recipients.get(i));
                if (i < recipients.size() - 1) chart.append(", ");
            }
            chart.append("\n\n");
        }
        
        JTextArea textArea = new JTextArea(chart.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Compatibility Chart", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Show add blood dialog for hospitals
    private void showAddBloodDialog() {
        JComboBox<BloodType> typeCombo = new JComboBox<>(BloodType.values());
        JTextField unitsField = new JTextField();
        
        Object[] message = {
            "Blood Type:", typeCombo,
            "Units to Add:", unitsField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Add Blood Units", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BloodType type = (BloodType) typeCombo.getSelectedItem();
                int units = Integer.parseInt(unitsField.getText());
                
                if (units > 0) {
                    currentHospital.addBlood(type, units);
                    DataManager.saveHospitals(hospitals); // Save after adding blood
                    JOptionPane.showMessageDialog(this, units + " units of " + type + " added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Units must be positive!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number!");
            }
        }
    }
    
    // Show remove blood dialog for hospitals
    private void showRemoveBloodDialog() {
        JComboBox<BloodType> typeCombo = new JComboBox<>(BloodType.values());
        JTextField unitsField = new JTextField();
        
        Object[] message = {
            "Blood Type:", typeCombo,
            "Units to Remove:", unitsField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Remove Blood Units", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BloodType type = (BloodType) typeCombo.getSelectedItem();
                int units = Integer.parseInt(unitsField.getText());
                
                if (units > 0) {
                    if (currentHospital.removeBlood(type, units)) {
                        DataManager.saveHospitals(hospitals); // Save after removing blood
                        JOptionPane.showMessageDialog(this, units + " units of " + type + " removed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough blood available!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Units must be positive!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number!");
            }
        }
    }
    
    // Show blood status for hospitals
    private void showBloodStatus() {
        StringBuilder status = new StringBuilder();
        status.append("BLOOD INVENTORY STATUS - " + currentHospital.getName() + "\n");
        status.append("=================================================\n\n");
        
        for (BloodType type : BloodType.values()) {
            int quantity = currentHospital.getBloodQuantity(type);
            String level = currentHospital.getStatusLevel(type);
            String indicator = "";
            
            if (level.equals("CRITICAL")) {
                indicator = "🔴";
            } else if (level.equals("AVERAGE")) {
                indicator = "🟡";
            } else {
                indicator = "🟢";
            }
            
            status.append(indicator + " " + type + ": " + quantity + " units - " + level + "\n");
        }
        
        JTextArea textArea = new JTextArea(status.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 350));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Blood Status", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Check and display critical blood alerts
    private void checkCriticalAlerts() {
        StringBuilder alerts = new StringBuilder();
        boolean hasCritical = false;
        
        for (Hospital h : hospitals) {
            if (h.hasCriticalBlood()) {
                for (BloodType type : BloodType.values()) {
                    int qty = h.getBloodQuantity(type);
                    if (qty < 10) {
                        alerts.append("⚠️ " + h.getName() + "'s " + type + " is CRITICAL (" + qty + " units)\n");
                        hasCritical = true;
                    }
                }
            }
        }
        
        if (hasCritical) {
            JOptionPane.showMessageDialog(this, alerts.toString(), "CRITICAL ALERTS", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
    
    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BloodManagementSystem app = new BloodManagementSystem();
            app.setVisible(true);
        });
    }
}