// Enum to represent all blood types
package bloodmanagement;

public enum BloodType {
    O_NEGATIVE("O-"),
    O_POSITIVE("O+"),
    A_NEGATIVE("A-"),
    A_POSITIVE("A+"),
    B_NEGATIVE("B-"),
    B_POSITIVE("B+"),
    AB_NEGATIVE("AB-"),
    AB_POSITIVE("AB+");
    
    private String type;
    
    BloodType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type;
    }
}