import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class App {
    
    private static ArrayList<ArrayList<String>> assemblerData = new ArrayList<ArrayList<String>>();
    private static Map<String, String> map;
    private static List<Integer> generalRegister = new ArrayList<Integer>(Collections.nCopies(4, 0)); // R0, R1, R2, R3. For use w LDR
    private static List<Integer> indexRegister = new ArrayList<Integer>(Collections.nCopies(3, 0)); // X1, X2, X3. For use w LDX
    private static List<Integer> memoryAddress = new ArrayList<Integer>(Collections.nCopies(2048, 0)); // 2048

    private static String locationTracker = "";

    private static Number number = new Number();
    private static InputOutput io = new InputOutput();
    public static void main(String[] args) {


        map = io.fetchOpcodes("opcodes.txt");
        ArrayList<String> memoryLocation = new ArrayList<String>();
        ArrayList<String> octalInstructions = new ArrayList<String>();
        ArrayList<String> instructions = io.fetchInstructions("input1.txt");

        assemblerData.add(0, memoryLocation);
        assemblerData.add(octalInstructions);
        assemblerData.add(instructions);

        for(int i=0;i<instructions.size();i++){
            // System.out.println("1. " + instructions.get(i));
            extractOpcodeAndNumbers(instructions.get(i));
            System.out.println(assemblerData.get(0).get(i) + "\t\t" + assemblerData.get(1).get(i) + "\t" + instructions.get(i));
        }

        io.outputFiles(assemblerData);
    }
    
    private static void extractOpcodeAndNumbers(String input) {

            // String trimmedInput = input.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
            // Regular expression pattern to extract opcode and numbers
            Pattern pattern = Pattern.compile("([A-Za-z]+)\\s*(\\d+)(?:,(\\d+))?(?:,(\\d+))?(?:,(\\d+))?");
            Matcher matcher = pattern.matcher(input);
            String location = "";
            String result = "";

            if (matcher.find()) {
                // Extracted opcode
                String opcode = matcher.group(1);
                // System.out.println("Opcode: " + opcode);
                String binary1 = "";
                String binary2 = "";
                String binary3 = "";
                String binary4 = "";
                String binary5 = "";

                if(map.get(opcode) != null){binary1 = number.decimalToBinary(map.get(opcode));}
                binary1 = number.padBinary(binary1, 6);
                // System.out.print(binary1);
                if((opcode.equals("LDR")) || (opcode.equals("LDA")) || (opcode.equals("STR") || opcode.equals("JCC")|| opcode.equals("SOB")|| opcode.equals("JGE")|| opcode.equals("AMR")|| opcode.equals("SMR"))){
                    String reg = number.decimalToBinary(matcher.group(2));
                    binary2 = number.padBinary(reg, 2);
                    String ix = number.decimalToBinary(matcher.group(3));
                    binary3 = number.padBinary(ix, 2);
                    String addr = number.decimalToBinary(matcher.group(4));
                    binary5 = number.padBinary(addr, 5);
                    if(matcher.group(5) != null){
                        binary4 = "1";
                    }else{
                        binary4 = "0";
                    }
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    if (opcode.equals("LDR")){
                        LDR(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                    } else if (opcode.equals("LDA")) {
                        LDA(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                    } else if (opcode.equals("STR")) {
                        STR(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                    }

                }else if(opcode .equals("LDX") || opcode.equals("STX") || opcode.equals("JZ") || opcode.equals("JNE")|| opcode.equals("JMA")|| opcode.equals("JSR")){
                    String reg = "";
                    binary2 = "00";
                    String ix = number.decimalToBinary(matcher.group(2));
                    binary3 = number.padBinary(ix, 2);
                    String addr = number.decimalToBinary(matcher.group(3));
                    binary5 = number.padBinary(addr, 5);
                    if(matcher.group(4) != null){
                        binary4 = "1";
                    }else{
                        binary4 = "0";
                    }
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    if (opcode.equals("LDX")) {
                        // LDX x, address [,I]
                        LDX(matcher.group(2), matcher.group(3), binary4);
                    } else if (opcode.equals("STX")) {
                        STX(matcher.group(2), matcher.group(3), binary4);
                        System.out.println("IX: " + matcher.group(2) + " Addr: " + matcher.group(3) + "Indirect Bit: " + binary4);
                    }

                }else if(opcode.equals("Data")){
                    String reg = "";
                    binary2 = "00";
                    String ix = "";
                    binary3 = "00";
                    String data = number.decimalToBinary(matcher.group(2));
                    binary5 = number.padBinary(data, 5);
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    DATA(matcher.group(2));

                }else if(opcode.equals("SETCCE")|| opcode.equals("RFS")){

                    String reg = number.decimalToBinary(matcher.group(2));
                    binary2 = number.padBinary(reg, 2);
                    String ix = "";
                    binary3 = "00";
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("MLT") || opcode.equals("DVD") || opcode.equals("TRR") || opcode.equals("AND") || opcode.equals("ORR")){
                    String reg = number.decimalToBinary(matcher.group(2));
                    binary2 = number.padBinary(reg, 2);
                    String ix = number.decimalToBinary(matcher.group(3));
                    binary3 = number.padBinary(ix, 2);
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("LOC")){
                    result = "";
                }
                if(result.length() > 0){
                    result = number.binaryToOctal(result);
                    result = number.padBinary(result, 6);
                } 

                if (opcode.equals("LOC")) {
                    // locationTracker = matcher.group(2);
                    location = getLocation(matcher.group(2), false);
                    location = "";
                } else {
                    location = getLocation(locationTracker, true);
                }


            } else {
                // System.out.println(trimmedInput);
                if(input.equals("Data End")){
                    result = "002000";
                    DATA("1024");
                    location = getLocation(locationTracker, true);

                }else if(input.equals("End: HLT")){
                    result = "000000";
                    location = getLocation("1024", true);
                }
            }

            ArrayList<String> memoryLocation = assemblerData.get(0);
            ArrayList<String> octalInstruction = assemblerData.get(1);

            memoryLocation.add(location);
            octalInstruction.add(result);
            // assemblerHashtable.put("octal_instruction", result);
            // assemblerHashtable.put("memory_location", location);
            return;
    }

    public static int computeEA(String address, String ix, String indirectBit) {
        int addressField = Integer.parseInt(address);
        int ixField = Integer.parseInt(ix);
        int iField = Integer.parseInt(indirectBit);
        // System.out.println("addressField: " + address + "\nixField: " + ix + "\niField: " + indirectBit);

        int effectiveAddress = -1;

        if (iField == 0) {
            // No indirect addressing
            if (ixField == 0) {
                // No indexing
                effectiveAddress = addressField;
            } else if (ixField >= 1 && ixField <= 3) {
                // With indexing
                effectiveAddress = indexRegister.get(ixField-1) + addressField;
            }
        } else if (iField == 1) {
            // Indirect addressing
            if (ixField == 0) {
                // No indexing
                effectiveAddress = memoryAddress.get(addressField);
            } else if (ixField >= 1 && ixField <=3) {
                effectiveAddress = memoryAddress.get(indexRegister.get(ixField-1) + addressField);
            }
        }
        // System.out.println("Effective Address: " + Integer.toString(effectiveAddress));
        return effectiveAddress;
    }
    public static void DATA(String data) {
        memoryAddress.set(Integer.parseInt(locationTracker), Integer.parseInt(data));

        // System.out.println("Data Input: ");

        // for (int i = 0; i < memoryAddress.size(); i++) {
        //     System.out.print(Integer.toString(i) + ": " + Integer.toString(memoryAddress.get(i)) + "\t");
        // }
        // System.out.println();
    }


    // R0, R1, R2, R3
    public static void LDR(String register, String ix, String address, String indirectBit) {
        int EA = computeEA(address, ix, indirectBit);
        int R = Integer.parseInt(register);

        generalRegister.set(R, memoryAddress.get(EA));

        // System.out.println("General Register: ");
        // for (int i = 0; i < generalRegister.size(); i++) {
        //     System.out.print("R" + Integer.toString(i) + ": " + Integer.toString(generalRegister.get(i)) + "\t");
        // }
        // System.out.println();
    }

    // X1, X2, X3
    // LDX x, address [,I]
    public static void LDX (String ix, String address, String indirectBit) {
        int ixField = Integer.parseInt(ix) - 1;
        int EA = computeEA(address, "0", indirectBit);

        indexRegister.set(ixField, memoryAddress.get(EA));

        // System.out.println("Index Register:");
        // for (int i = 0; i < indexRegister.size(); i++) {
        //     System.out.print("X" + Integer.toString(i + 1) + ": " + Integer.toString(indexRegister.get(i)) + "\t");
        // }
        // System.out.println();
    }

    // R0, R1, R2, R3
    public static void LDA(String register, String ix, String address, String indirectBit) {
        int EA = computeEA(address, ix, indirectBit);
        int R = Integer.parseInt(register);

        generalRegister.set(R, EA);
    }

    // R0, R1, R2, R3
    public static void STR(String register, String ix, String address, String indirectBit) {
        int EA = computeEA(address, ix, indirectBit);
        int R = Integer.parseInt(register);

        memoryAddress.set(EA, generalRegister.get(R));
    }

    // X1, X2, X3
    public static void STX(String ix, String address, String indirectBit) {
        int EA = computeEA(address, ix, indirectBit);

        System.out.println("EA: " + Integer.toString(EA) + " - Value: " + indexRegister.get(Integer.parseInt(ix)-1));
        
        memoryAddress.set(EA, indexRegister.get(Integer.parseInt(ix)-1));
    }

    public static String getLocation(String newLocation, boolean increment) {

        locationTracker = newLocation;
        String location;
        location = number.decimalToBinary(locationTracker);
        location = number.binaryToOctal(location);
        location = number.padBinary(location, 6);

        if (increment) {
            locationTracker = number.addDecimals(locationTracker, 1);
        }
        
        return location;
    }

}
