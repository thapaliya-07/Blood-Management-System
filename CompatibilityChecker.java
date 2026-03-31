package bloodmanagement;

import java.util.ArrayList;
import java.util.List;

// Class to check blood type compatibility
public class CompatibilityChecker {
    
    // Get list of blood types that can donate to the given blood type
    public static List<BloodType> getCompatibleDonors(BloodType recipient) {
        List<BloodType> compatible = new ArrayList<>();
        
        switch (recipient) {
            case O_NEGATIVE:
                compatible.add(BloodType.O_NEGATIVE);
                break;
            case O_POSITIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.O_POSITIVE);
                break;
            case A_NEGATIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.A_NEGATIVE);
                break;
            case A_POSITIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.O_POSITIVE);
                compatible.add(BloodType.A_NEGATIVE);
                compatible.add(BloodType.A_POSITIVE);
                break;
            case B_NEGATIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.B_NEGATIVE);
                break;
            case B_POSITIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.O_POSITIVE);
                compatible.add(BloodType.B_NEGATIVE);
                compatible.add(BloodType.B_POSITIVE);
                break;
            case AB_NEGATIVE:
                compatible.add(BloodType.O_NEGATIVE);
                compatible.add(BloodType.A_NEGATIVE);
                compatible.add(BloodType.B_NEGATIVE);
                compatible.add(BloodType.AB_NEGATIVE);
                break;
            case AB_POSITIVE:
                // Universal recipient - can receive from all
                for (BloodType type : BloodType.values()) {
                    compatible.add(type);
                }
                break;
        }
        
        return compatible;
    }
    
    // Get list of blood types that can receive from the given blood type
    public static List<BloodType> getCompatibleRecipients(BloodType donor) {
        List<BloodType> compatible = new ArrayList<>();
        
        switch (donor) {
            case O_NEGATIVE:
                // Universal donor - can donate to all
                for (BloodType type : BloodType.values()) {
                    compatible.add(type);
                }
                break;
            case O_POSITIVE:
                compatible.add(BloodType.O_POSITIVE);
                compatible.add(BloodType.A_POSITIVE);
                compatible.add(BloodType.B_POSITIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case A_NEGATIVE:
                compatible.add(BloodType.A_NEGATIVE);
                compatible.add(BloodType.A_POSITIVE);
                compatible.add(BloodType.AB_NEGATIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case A_POSITIVE:
                compatible.add(BloodType.A_POSITIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case B_NEGATIVE:
                compatible.add(BloodType.B_NEGATIVE);
                compatible.add(BloodType.B_POSITIVE);
                compatible.add(BloodType.AB_NEGATIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case B_POSITIVE:
                compatible.add(BloodType.B_POSITIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case AB_NEGATIVE:
                compatible.add(BloodType.AB_NEGATIVE);
                compatible.add(BloodType.AB_POSITIVE);
                break;
            case AB_POSITIVE:
                compatible.add(BloodType.AB_POSITIVE);
                break;
        }
        
        return compatible;
    }
}
