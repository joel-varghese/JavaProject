import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import config.Config;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import components.Computer;
import components.ROM;
import components.Paragraph;
import ui.FrontPanel;

public class App {
    
    private static ArrayList<ArrayList<String>> assemblerData = new ArrayList<ArrayList<String>>();
    private static Map<String, String> map;
    private static List<Integer> generalRegister = new ArrayList<Integer>(Collections.nCopies(4, 0)); // R0, R1, R2, R3. For use w LDR
    private static List<Integer> indexRegister = new ArrayList<Integer>(Collections.nCopies(3, 0)); // X1, X2, X3. For use w LDX
    private static List<Integer> memoryAddress = new ArrayList<Integer>(Collections.nCopies(2048, 0)); // 2048
    private static List<Integer> conditionCode = new ArrayList<Integer>(Collections.nCopies(4, 0));  // cc(0) = OVERFLOW, cc(1) = UNDERFLOW, cc(2) = DIVZERO, cc(3) = EQUALORNOT
    public static Logger LOGGER = Logger.getLogger("");
    private static String PC = "";

    private static Number number = new Number();
    private static InputOutput io = new InputOutput();
    public static void main(String[] args) {
        
        String LOG_FILE = "minicomputer-" + System.currentTimeMillis() + "." + Config.LOG_FILE_EXTENSION;

        Computer computer;
        map = io.fetchOpcodes("opcodes.txt");

                try {
            LogManager.getLogManager().reset();
            LOGGER.setUseParentHandlers(false);

            FileHandler fh = new FileHandler(LOG_FILE, true);
            LOGGER.addHandler(fh);

            ConsoleHandler ch = new ConsoleHandler();
            LOGGER.addHandler(ch);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to configure logging for the application",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }


        File iplFile = new File(Config.ROM_IPL_FILENAME);
        File paragraphFile = new File(Config.PARAGRAPH_FILENAME);
        if (iplFile.exists() && paragraphFile.exists()) {
            computer = new Computer(new ROM(iplFile), new Paragraph(paragraphFile));
        } else if (iplFile.exists()) {
            computer = new Computer(new ROM(iplFile));
        } else {
            computer = new Computer();
        }

                FrontPanel window = new FrontPanel(computer);
        window.menu.fileOpenLog.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(LOG_FILE));
            } catch (IOException e1) {
                // LOGGER.severe("Failed to open log file: " + e1.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Failed to open log file",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        window.setVisible(true);

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
        io.outputAssemblerData(memoryAddress, generalRegister, indexRegister);
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
                if((opcode.equals("LDR")) || (opcode.equals("LDA")) || (opcode.equals("STR") || opcode.equals("JCC")|| opcode.equals("SOB")|| opcode.equals("JGE")|| opcode.equals("AMR")|| opcode.equals("SMR") || opcode.equals("JGE") || opcode.equals("SRC") || opcode.equals("RRC"))){
                    // Use register, index register, address, and optionally indirect bit
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

                    switch(opcode) {
                        case "LDR":
                            // r, x, address[,I]
                            LDR(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "LDA":
                            // r, x, address[,I]
                            LDA(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "STR":
                            // r, x, address[,I]
                            STR(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "JCC":
                            JCC(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "SOB":
                            // r, x, address[,I]
                            SOB(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "JGE":
                            // r,x, address[,I]
                            JGE(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;  
                        case "AMR":
                            // r, x, address[,I]
                            AMR(matcher.group(2), matcher.group(3), matcher.group(4), binary4);
                            break;
                        case "SRC":
                            // r = register, L/R = index register, A/L = indirect bit, count = address
                            SRC(matcher.group(2), matcher.group(3), binary4, matcher.group(4));
                            break;
                        case "RRC":
                            RRC(matcher.group(2), matcher.group(3), binary4, matcher.group(4));
                            break;
                    }

                }else if(opcode .equals("LDX") || opcode.equals("STX") || opcode.equals("JZ") || opcode.equals("JNE")|| opcode.equals("JMA")|| opcode.equals("JSR")){
                    // Use index register, address, and optinally indirect bit
                    String reg = "";
                    binary2 = "00";
                    String ix = number.decimalToBinary(matcher.group(2));   // x
                    binary3 = number.padBinary(ix, 2);
                    String addr = number.decimalToBinary(matcher.group(3)); // add
                    binary5 = number.padBinary(addr, 5);
                    if(matcher.group(4) != null){  // [,I]
                        binary4 = "1";
                    }else{
                        binary4 = "0";
                    }
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    switch (opcode) {
                        case "LDX":
                            // LDX x, address [,I]
                            LDX(matcher.group(2), matcher.group(3), binary4);
                            break;
                        case "STX":
                            // STX x, address[,I]
                            STX(matcher.group(2), matcher.group(3), binary4);
                            break;
                        case "JZ":
                            // JZ x, address[,I]
                            JZ(matcher.group(2), matcher.group(3), binary4);
                            break;
                        case "JNE":
                            // JNE x, address[,I]
                            JNE(matcher.group(2), matcher.group(3), binary4);
                            break;
                        case "JMA":
                            JMA(matcher.group(2), matcher.group(3), binary4);
                            break;
                        case "JSR":
                            JSR(matcher.group(2), matcher.group(3), binary4);
                            break;
                    }
                } else if(opcode.equals("MLT") || opcode.equals("DVD") || opcode.equals("TRR") || opcode.equals("AND") || opcode.equals("ORR") || opcode.equals("IN") || opcode.equals("OUT")){
                    // Use 2 registers: register x and register y
                    String reg = number.decimalToBinary(matcher.group(2)); // rx
                    binary2 = number.padBinary(reg, 2);
                    String ix = number.decimalToBinary(matcher.group(3)); // ry
                    binary3 = number.padBinary(ix, 2);
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    switch (opcode) {
                        case "MLT":
                            // rx,ry
                            MLT(matcher.group(2), matcher.group(3));
                            break;
                        case "DVD":
                            DVD(matcher.group(2), matcher.group(3));
                            break;
                        case "TRR":
                            TRR(matcher.group(2), matcher.group(3));
                            break;
                        case "AND":
                            AND(matcher.group(2), matcher.group(3));
                            break;
                        case "ORR":
                            ORR(matcher.group(2), matcher.group(3));
                            break;
                        case "IN":
                            IN(matcher.group(2), matcher.group(3));
                            break;
                        case "OUT":
                            OUT(matcher.group(2), matcher.group(3));
                            break;
                    }

                } else if (opcode.equals("AIR") || opcode.equals("SIR")){
                    // Use register and address
                    String reg = number.decimalToBinary(matcher.group(2));
                    binary2 = number.padBinary(reg, 2);
                    String ix = "";
                    binary3 = "00";
                    String addr = number.decimalToBinary(matcher.group(4));
                    binary5 = number.padBinary(addr, 5);
                    binary4 = "0";
                    
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                    switch (opcode) {
                        case "AIR":
                            // AIR r, immed is stored in address field
                            AIR(matcher.group(2), matcher.group(4));
                            break;
                        case "SIR":
                            // SIR r, immed
                            SIR(matcher.group(2), matcher.group(4));
                            break;
                    }

                } else if (opcode.equals("RFS")) {
                    // Use address for immed
                    String reg = "";
                    binary2 = "00";
                    String ix = "";
                    binary3 = "00";
                    String addr = number.decimalToBinary(matcher.group(4));
                    binary5 = number.padBinary(addr, 5);
                    binary4 = "0";
                    
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    // RFS Immed
                    RFS(matcher.group(2));

                }else if(opcode.equals("SETCCE")|| opcode.equals("NOT")){
                    // Use register
                    String reg = number.decimalToBinary(matcher.group(2)); // r
                    binary2 = number.padBinary(reg, 2);
                    String ix = "";
                    binary3 = "00";
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    switch (opcode) {
                        case "SETCCE":
                            // SETCCE(reg);
                            SETCCE(matcher.group(2));
                            break;
                        case "NOT":
                            NOT(matcher.group(2));
                            break;
                    }  

                } else if(opcode.equals("Data")){
                    String reg = "";
                    binary2 = "00";
                    String ix = "";
                    binary3 = "00";
                    String data = number.decimalToBinary(matcher.group(2));
                    binary5 = number.padBinary(data, 5);
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;

                    DATA(matcher.group(2));

                } else if (opcode.equals("LOC")){
                    result = "";
                }

                if(result.length() > 0){
                    result = number.binaryToOctal(result);
                    result = number.padBinary(result, 6);
                } 

                if (opcode.equals("LOC")) {
                    // PC = matcher.group(2);
                    location = setLocation(matcher.group(2), false);
                    location = "";
                } else {
                    location = setLocation(PC, true);
                }
                
            } else {
                // System.out.println(trimmedInput);
                if(input.equals("Data End")){
                    result = "002000";
                    DATA("1024");
                    location = setLocation(PC, true);

                }else if(input.equals("End: HLT")){
                    result = "000000";
                    location = setLocation("1024", true);
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
        memoryAddress.set(Integer.parseInt(PC), Integer.parseInt(data));

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
        int EA = computeEA(address, "00", indirectBit);
        // System.out.println("EA: " + Integer.toString(EA) + " - Value: " + indexRegister.get(Integer.parseInt(ix)-1));
        memoryAddress.set(EA, indexRegister.get(Integer.parseInt(ix)-1));
    }

    public static void SETCCE(String reg) {
        int r = Integer.parseInt(reg);
        if (r < 0 || r >= conditionCode.size()) {
            System.out.println("Invalid register index");
            return;
        }

        // Assuming c(r) returns the value in the register r
        // Check if the value in register r is 0
        if (generalRegister.get(r) == 0) {
            // Set the E bit of the condition code to 1
            conditionCode.set(3, 1);
        } else {
            // Set the E bit of the condition code to 0
            conditionCode.set(3, 0);
        }
    }

    // Jump If Zero
    public static void JZ(String ix, String address, String indirectBit) {
        // int x, int address, boolean indirect
        int EA = computeEA(address, ix, indirectBit);
        if (conditionCode.get(3) == 1) { // Check if E bit of condition code is 1 (Zero flag)
            setLocation(Integer.toString(EA), false);
        } else {
            setLocation(PC, true);
        }
    }

    // Jump If Not Equal
    public static void JNE(String ix, String address, String indirectBit){
        // int x, int address, boolean indirect
        int EA = computeEA(address, ix, indirectBit);
        if (conditionCode.get(3) == 0) { // Check if E bit of condition code is 0
            setLocation(Integer.toString(EA), false);
        } else {
            setLocation(PC, true);
        }        
    }

    // Jump If Condition Code
    public static void JCC(String cc, String ix, String address, String indirectBit){
        // int condition code, int x, int address, boolean indirect
        int EA = computeEA(address, ix, indirectBit);
        if (conditionCode.get(Integer.parseInt(cc)) == 1) { // Check if E bit of condition code is 0
            setLocation(Integer.toString(EA), false);
        } else {
            setLocation(PC, true);
        }        
    }

    // Unconditional Jump To Address
    public static void JMA(String ix, String address, String indirectBit) {
        // int x, int address, boolean indirect
        int EA = computeEA(address, ix, indirectBit); 
        setLocation(Integer.toString(EA), false);
    }

    // Jump and Save Return Address
    public static void JSR(String ix, String address, String indirectBit) {
        int EA;
        int argumentPointer;
        setLocation(PC, true);
        generalRegister.set(3, Integer.parseInt(PC));

        EA = computeEA(address, ix, indirectBit); 
        setLocation(Integer.toString(EA), false);

        /*
         * R0 should contain pointer to arguments
         *  Argument list should end with –1 (all 1s) value
         */
        // Set R0 to point to the arguments
        generalRegister.set(0, EA);

        // Example: Process arguments until -1 is encountered
        argumentPointer = EA;
        while (memoryAddress.get(argumentPointer) != -1) {
            // Run subroutine and Process arguments here
            argumentPointer++;
        }
    }

    // Return From Subroutine 
    public static void RFS(String immediate) {
        int immed = Integer.parseInt(immediate);
        Integer returnLocation = generalRegister.get(3);

        generalRegister.set(0, immed);
        setLocation(Integer.toString(returnLocation), false);
    }

    // Subtract One and Branch
    public static void SOB(String register, String ix, String address, String indirectBit) {
        int R = Integer.parseInt(register);
        int contentR = generalRegister.get(R);
        int EA = computeEA(address, ix, indirectBit);

        generalRegister.set(R, contentR - 1);

        if (generalRegister.get(R) > 0) {
            // Set PC to the effective address
            setLocation(Integer.toString(EA), false);
        } else {
            // PC <- PC + 1
            setLocation(PC, true);
        }
    }

    // Jump Greater Than or Equal To
    public static void JGE(String register, String ix, String address, String indirectBit) {
        int R = Integer.parseInt(register);
        int contentR = generalRegister.get(R);
        int EA = computeEA(address, ix, indirectBit);

        if (contentR >= 0) {
            // Set PC to the effective address
            setLocation(Integer.toString(EA), false);
        } else {
            // PC <- PC + 1
            setLocation(PC, true);
        }
    }

    // Add Memory To Register
    public static void AMR(String register, String ix, String address, String indirectBit) {
        // r, x, address[,I]
        int EA = computeEA(address, ix, indirectBit);
        int R = Integer.parseInt(register);
        int contentR = generalRegister.get(R);

        generalRegister.set(R, contentR + EA);
    }

    public static void SMR(String register, String ix, String address, String indirectBit) {
        // r, x, address[,I]
        int EA = computeEA(address, ix, indirectBit);
        int R = Integer.parseInt(register);
        int contentR = generalRegister.get(R);

        generalRegister.set(R, contentR - EA);        
    }

    // Add  Immediate to Register
    public static void AIR(String register, String immediate) {
        int r = Integer.parseInt(register);
        int immed = Integer.parseInt(immediate);
        int contentR = generalRegister.get(r);

        if (immed == 0) {
            return;
        } else if (contentR == 0){
            generalRegister.set(r, immed);
        } else if (contentR != 0) {
            generalRegister.set(r, contentR + immed);
        }
    }

    // Subtract  Immediate from Register
    public static void SIR(String register, String immediate){
        int r = Integer.parseInt(register);
        int immed = Integer.parseInt(immediate);
        int contentR = generalRegister.get(r);

        if (immed == 0) {
            return;
        } else if (contentR == 0){
            generalRegister.set(r, -immed);
        } else if (contentR != 0) {
            generalRegister.set(r, contentR - immed);
        }        
    }

    // Multiply Register by Register
    public static void MLT(String registerX, String registerY) {
        int rx = Integer.parseInt(registerX);
        int ry = Integer.parseInt(registerY);

        // Check if rx and ry are valid registers
        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2)) {
            System.out.println("Invalid register combination");
            return;
        }

        // Perform the multiplication
        long result = (long)generalRegister.get(rx) * (long)generalRegister.get(ry);

        // Check for overflow
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            // Set the OVERFLOW bit of the condition code to True (1) if overflow
            conditionCode.set(0, 1);
            return;
        }
        // Store the result in rx and rx+1
        generalRegister.set(rx, (int) (result >> 32)); // High-order bits
        generalRegister.set(rx + 1, (int) result);     // Low-order bits

    }

    // Divide Register by Register
    public static void DVD(String registerX, String registerY) {
        int rx = Integer.parseInt(registerX);
        int ry = Integer.parseInt(registerY);

        // Check if rx and ry are valid registers
        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2)) {
            System.out.println("Invalid register combination");
            return;
        }

        // Check if divisor is zero
        if (generalRegister.get(ry) == 0) {
            // set DIVZERO flag
            conditionCode.set(2, 1);
            return;
        }

        // Perform the division
        generalRegister.set(rx, generalRegister.get(rx) / generalRegister.get(ry)); // Quotient
        generalRegister.set(rx + 1, generalRegister.get(rx) % generalRegister.get(ry)); // Remainder
    }   

    // Test the Equality of Register and Register
    public static void TRR(String registerX, String registerY) {
        int rx = Integer.parseInt(registerX);
        int ry = Integer.parseInt(registerY);

        if (generalRegister.get(rx) == generalRegister.get(ry)) {
            conditionCode.set(3, 1);
        } else {
            conditionCode.set(3, 0);
        }
    }

    // Logical And of Register and Register
    public static void AND(String registerX, String registerY) {
        int rx = Integer.parseInt(registerX);
        int ry = Integer.parseInt(registerY);
        int contentRX = generalRegister.get(rx);
        int contentRY = generalRegister.get(ry);

        generalRegister.set(rx, contentRX & contentRY);
    }

    // Logical Or of Register and Register
    public static void ORR(String registerX, String registerY) {
        int rx = Integer.parseInt(registerX);
        int ry = Integer.parseInt(registerY);
        int contentRX = generalRegister.get(rx);
        int contentRY = generalRegister.get(ry);
        
        generalRegister.set(rx, contentRX | contentRY);
    }

    // Logical Not of Register To Register
    public static void NOT(String registerX) {
        int rx = Integer.parseInt(registerX);
        int contentRX = generalRegister.get(rx);
        
        generalRegister.set(rx, ~ contentRX);
    }

    // Shift Register by Count
    public static void SRC(String register, String LorR, String AorL, String shiftCount) {
        int r = Integer.parseInt(register);
        int LR = Integer.parseInt(LorR);
        int AL = Integer.parseInt(AorL);
        int count = Integer.parseInt(shiftCount);
        int contentR = generalRegister.get(r);

        if (count == 0) {
            return; // No shift occurs if count is 0
        }

        // Extracting the L/R and A/L from IX and I fields
        int leftRight = (LR != 0) ? 1 : 0; // L/R
        int arithmeticLogical = (AL != 0) ? 1 : 0; // A/L

        // Applying the shift operation based on L/R and A/L
        if (leftRight == 1) { // Left shift
            if (arithmeticLogical == 1) { // Logical shift
                contentR <<= count;
                // generalRegister.set(r, contentR <<= count);
            } else { // Arithmetic shift
                int signBit = (contentR & 0x8000) >> 15;
                contentR <<= count;
                for (int i = 0; i < count; i++) {
                    contentR = contentR |= signBit; // Propagate sign bit
                }
            }
        } else { // Right shift
            if (arithmeticLogical == 1) { // Logical shift
                // int contentR = generalRegister.get(r);
                contentR >>>= count;
            } else { // Arithmetic shift
                // int contentR = generalRegister.get(r);
                int signBit = (contentR & 0x8000) >> 15;
                contentR = (contentR & 0xFFFF) >>> count;
                for (int i = 0; i < count; i++) {
                    contentR = contentR |= signBit << 15; // Propagate sign bit
                }
            }
        }
        generalRegister.set(r, contentR);
    }

    // Rotate Register by Count
    public static void RRC(String register, String LorR, String AorL, String shiftCount) {
        int r = Integer.parseInt(register);
        int LR = Integer.parseInt(LorR);
        int AL = Integer.parseInt(AorL);
        int count = Integer.parseInt(shiftCount);

        if (count == 0) {
            return; // No rotation occurs if count is 0
        }

        // Extracting the L/R and A/L fields
        boolean leftRotate = (LR == 1);
        boolean logicalRotate = (AL == 1);

        int contentR = generalRegister.get(r);

        // Perform rotation operation based on L/R and A/L
        for (int i = 0; i < count; i++) {
            if (leftRotate) {
                int msb = (contentR & 0x8000) >> 15; // Extract the most significant bit
                contentR <<= 1; // Left shift by 1
                if (logicalRotate) {
                    contentR |= msb; // Logical rotation: set LSB to previous MSB
                } else {
                    contentR |= msb ^ ((contentR & 0x4000) >> 14); // Arithmetic rotation: XOR MSB with 2nd MSB
                }
            } else {
                int lsb = contentR & 0x1; // Extract the least significant bit
                contentR >>>= 1; // Right shift by 1
                if (logicalRotate) {
                    contentR |= lsb << 15; // Logical rotation: set MSB to previous LSB
                } else {
                    contentR |= lsb << 15 ^ ((contentR & 0x4000) >> 14); // Arithmetic rotation: XOR LSB with 2nd LSB
                }
            }
        }
        // Update the register with the rotated value
        generalRegister.set(r, contentR);
    }

    // Input Character To Register from Device
    public static void IN(String register, String deviceID) {
        // Choose the register where you want to store the character (e.g., register 0)
        int r = Integer.parseInt(register);
        int devid = Integer.parseInt(deviceID);

        // Read a character from the device and store it into the selected register
        char c = io.inputCharacterToRegister(generalRegister, r, devid);
        generalRegister.set(r, number.charToInt(c));

        // Display the result
        System.out.println("Character stored in register " + r + ": " + generalRegister.get(r));
    }

    // Output Character to Device from Register
    public static void OUT(String register, String deviceID) {
        int r = Integer.parseInt(register);
        int devid = Integer.parseInt(deviceID);

        // Simulating output to device by printing the character
        System.out.println("Output character from register " + register + " to device " + Integer.toString(devid) + ": " + Integer.toString(generalRegister.get(r)));
    }

    // Method to input character to register from device
    public static void inputCharacterToRegister(int r, int devid) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter a character from device " + devid + ": ");
        try {
            char inputChar = (char)reader.read();
            generalRegister.set(r, number.charToInt(inputChar));
        } catch (IOException e) {
            System.out.println("Error reading character from device " + devid + ": " + e.getMessage());
        }
    }
    

    public static String setLocation(String newLocation, boolean increment) {
        String location;

        PC = newLocation;
        location = number.decimalToBinary(PC);
        location = number.binaryToOctal(location);
        location = number.padBinary(location, 6);

        if (increment) {
            PC = number.addDecimals(PC, 1);
        }
        
        return location;
    }

}
