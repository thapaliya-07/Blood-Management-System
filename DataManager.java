package bloodmanagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Class to handle saving and loading data from files
public class DataManager {
    
    private static final String USERS_FILE = "users.txt";
    private static final String HOSPITALS_FILE = "hospitals.txt";
    
    // Save all users to file
    public static void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                // Format: username,password,bloodType (save as display format like O-, A+)
                writer.write(user.getUsername() + "," + 
                           user.getPassword() + "," + 
                           user.getBloodType().getType());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    // Load all users from file
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        if (!file.exists()) {
            return users; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    // Convert blood type string to enum
                    String bloodTypeStr = parts[2].trim();
                    bloodTypeStr = bloodTypeStr.replace("+", "_POSITIVE").replace("-", "_NEGATIVE");
                    
                    try {
                        BloodType bloodType = BloodType.valueOf(bloodTypeStr);
                        users.add(new User(username, password, bloodType));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid blood type in users file: " + parts[2]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
    
    // Save all hospitals to file
    public static void saveHospitals(List<Hospital> hospitals) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOSPITALS_FILE))) {
            for (Hospital hospital : hospitals) {
                // Format: hospitalName,loginCode
                writer.write(hospital.getName() + "," + hospital.getLoginCode());
                writer.newLine();
                
                // Save blood inventory for each blood type
                for (BloodType type : BloodType.values()) {
                    // Save using the display format (O-, O+, etc.)
                    writer.write(type.getType() + ":" + hospital.getBloodQuantity(type));
                    writer.newLine();
                }
                writer.write("---"); // Separator between hospitals
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving hospitals: " + e.getMessage());
        }
    }
    
    // Load all hospitals from file
    public static List<Hospital> loadHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        File file = new File(HOSPITALS_FILE);
        
        if (!file.exists()) {
            return hospitals; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(HOSPITALS_FILE))) {
            String line;
            Hospital currentHospital = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (currentHospital != null) {
                        hospitals.add(currentHospital);
                        currentHospital = null;
                    }
                } else if (line.contains(":")) {
                    // Blood inventory line
                    String[] parts = line.split(":");
                    if (parts.length == 2 && currentHospital != null) {
                        // Convert display format (O-, O+) to enum format (O_NEGATIVE, O_POSITIVE)
                        String bloodTypeStr = parts[0].trim();
                        bloodTypeStr = bloodTypeStr.replace("+", "_POSITIVE").replace("-", "_NEGATIVE");
                        
                        try {
                            BloodType type = BloodType.valueOf(bloodTypeStr);
                            int quantity = Integer.parseInt(parts[1]);
                            currentHospital.addBlood(type, quantity);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid blood type: " + parts[0]);
                        }
                    }
                } else {
                    // Hospital name and code line
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String name = parts[0];
                        String code = parts[1];
                        currentHospital = new Hospital(name, code);
                    }
                }
            }
            
            // Add last hospital if exists
            if (currentHospital != null) {
                hospitals.add(currentHospital);
            }
            
        } catch (IOException e) {
            System.out.println("Error loading hospitals: " + e.getMessage());
        }
        
        return hospitals;
    }
}