package bloodmanagement;

//User class for regular users who can donate or receive blood
public class User {
 private String username;
 private String password;
 private BloodType bloodType;
 
 public User(String username, String password, BloodType bloodType) {
     this.username = username;
     this.password = password;
     this.bloodType = bloodType;
 }
 
 // Getters
 public String getUsername() {
     return username;
 }
 
 public String getPassword() {
     return password;
 }
 
 public BloodType getBloodType() {
     return bloodType;
 }
 
 // Check if login credentials match
 public boolean authenticate(String username, String password) {
     return this.username.equals(username) && this.password.equals(password);
 }
}
