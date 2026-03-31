package bloodmanagement;

import java.util.HashMap;

//Hospital class to manage blood inventory
public class Hospital {
 private String name;
 private String loginCode;
 private HashMap<BloodType, Integer> bloodInventory;
 
 public Hospital(String name, String loginCode) {
     this.name = name;
     this.loginCode = loginCode;
     this.bloodInventory = new HashMap<>();
     
     // Initialize all blood types with 0 units
     for (BloodType type : BloodType.values()) {
         bloodInventory.put(type, 0);
     }
 }
 
 public String getName() {
     return name;
 }
 
 public String getLoginCode() {
     return loginCode;
 }
 
 // Check if hospital login code is correct
 public boolean authenticate(String code) {
     return this.loginCode.equals(code);
 }
 
 // Add blood units
 public void addBlood(BloodType type, int units) {
     int current = bloodInventory.get(type);
     bloodInventory.put(type, current + units);
 }
 
 // Remove blood units
 public boolean removeBlood(BloodType type, int units) {
     int current = bloodInventory.get(type);
     if (current >= units) {
         bloodInventory.put(type, current - units);
         return true;
     }
     return false; // Not enough blood available
 }
 
 // Get blood quantity for a specific type
 public int getBloodQuantity(BloodType type) {
     return bloodInventory.get(type);
 }
 
 // Check if hospital has a specific blood type available
 public boolean hasBloodType(BloodType type) {
     return bloodInventory.get(type) > 0;
 }
 
 // Get status level (CRITICAL, AVERAGE, SUFFICIENT)
 public String getStatusLevel(BloodType type) {
     int quantity = bloodInventory.get(type);
     if (quantity < 10) {
         return "CRITICAL";
     } else if (quantity < 30) {
         return "AVERAGE";
     } else {
         return "SUFFICIENT";
     }
 }
 
 // Check if any blood type is critical
 public boolean hasCriticalBlood() {
     for (BloodType type : BloodType.values()) {
         if (bloodInventory.get(type) < 10) {
             return true;
         }
     }
     return false;
 }
 
 public HashMap<BloodType, Integer> getBloodInventory() {
     return bloodInventory;
 }
}