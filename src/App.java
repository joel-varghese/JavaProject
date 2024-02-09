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
    private static Map<String, String> map = new HashMap<>();
    private static String locationTracker = "";
    public static void main(String[] args) {

        fetchOpcodes();
        ArrayList<String> memoryLocation = new ArrayList<String>();
        ArrayList<String> octalInstructions = new ArrayList<String>();
        ArrayList<String> instructions = fetchInstructions("src/input/input1.txt");

        assemblerData.add(0, memoryLocation);
        assemblerData.add(octalInstructions);
        assemblerData.add(instructions);

        for(int i=0;i<instructions.size();i++){
            // System.out.println("1. " + instructions.get(i));
            extractOpcodeAndNumbers(instructions.get(i));
            System.out.println(assemblerData.get(0).get(i) + "\t\t" + assemblerData.get(1).get(i) + "\t" + instructions.get(i));
        }

        outputFiles();
    }

    public static String decimalToBinary(String decimalStr) {
        int number = Integer.parseInt(decimalStr);
        String binaryStr = Integer.toBinaryString(number);
        return new StringBuilder(binaryStr).toString();
    }

    public static String binaryToOctal(String binaryStr) {
        // Convert binary string to integer
        int num = Integer.parseInt(binaryStr, 2);

        // Convert integer to octal string
        return Integer.toOctalString(num);
    }

    public static String integerToOctal(int val,int digits) {
        // Convert binary string to integer

        // Convert integer to octal string
        String octal = Integer.toOctalString(val);
        return padBinary(octal, digits);
    }

    private static String padBinary(String binary, int targetLength) {
        int currentLength = binary.length();
        
        if (currentLength < targetLength) {
            // Calculate the number of zeros needed for padding
            int zerosToAdd = targetLength - currentLength;

            // Create a StringBuilder to build the padded binary string
            StringBuilder paddedBinary = new StringBuilder();
            
            // Append zeros
            for (int i = 0; i < zerosToAdd; i++) {
                paddedBinary.append('0');
            }

            // Append the original binary string
            paddedBinary.append(binary);

            // Return the padded binary string
            return paddedBinary.toString();
        } else {
            // If the binary string is already equal to or longer than the target length, return it unchanged
            return binary;
        }
    }

    public static String addDecimals(String decimalStr, int decimalInt) {
        int number = Integer.parseInt(decimalStr);
        number += decimalInt;
        return Integer.toString(number);
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
                if(map.get(opcode) != null){binary1 = decimalToBinary(map.get(opcode));}
                binary1 = padBinary(binary1, 6);
                // System.out.print(binary1);
                if((opcode.equals("LDR")) || (opcode.equals("LDA")) || (opcode.equals("STR") || opcode.equals("JCC")|| opcode.equals("SOB")|| opcode.equals("JGE")|| opcode.equals("AMR")|| opcode.equals("SMR"))){
                    String reg = decimalToBinary(matcher.group(2));
                    binary2 = padBinary(reg, 2);
                    String ix = decimalToBinary(matcher.group(3));
                    binary3 = padBinary(ix, 2);
                    String addr = decimalToBinary(matcher.group(4));
                    binary5 = padBinary(addr, 5);
                    if(matcher.group(5) != null){
                        binary4 = "1";
                    }else{
                        binary4 = "0";
                    }
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode .equals("LDX") || opcode.equals("STX") || opcode.equals("JZ") || opcode.equals("JNE")|| opcode.equals("JMA")|| opcode.equals("JSR")){
                    String reg = "";
                    binary2 = "00";
                    String ix = decimalToBinary(matcher.group(2));
                    binary3 = padBinary(ix, 2);
                    String addr = decimalToBinary(matcher.group(3));
                    binary5 = padBinary(addr, 5);
                    if(matcher.group(4) != null){
                        binary4 = "1";
                    }else{
                        binary4 = "0";
                    }
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("Data")){
                    String reg = "";
                    binary2 = "00";
                    String ix = "";
                    binary3 = "00";
                    String data = decimalToBinary(matcher.group(2));
                    binary5 = padBinary(data, 5);
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("SETCCE")|| opcode.equals("RFS")){

                    String reg = decimalToBinary(matcher.group(2));
                    binary2 = padBinary(reg, 2);
                    String ix = "";
                    binary3 = "00";
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("MLT") || opcode.equals("DVD") || opcode.equals("TRR") || opcode.equals("AND") || opcode.equals("ORR")){
                    String reg = decimalToBinary(matcher.group(2));
                    binary2 = padBinary(reg, 2);
                    String ix = decimalToBinary(matcher.group(3));
                    binary3 = padBinary(ix, 2);
                    String addr = "";
                    binary5 = "00000";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("LOC")){
                    result = "";
                }
                if(result.length() > 0){
                    result = binaryToOctal(result);
                    result = padBinary(result, 6);
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

    public static String getLocation(String newLocation, boolean increment) {

        locationTracker = newLocation;
        String location;
        location = decimalToBinary(locationTracker);
        location = binaryToOctal(location);
        location = padBinary(location, 6);

        if (increment) {
            locationTracker = addDecimals(locationTracker, 1);
        }
        
        return location;
    }


    public static void fetchOpcodes() {
        // ArrayList<String> opcodes = new ArrayList<String>();
        String filename = "src/input/opcodes.txt";
        BufferedReader reader = null;

        try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
            String trimmedLine;

			while (line != null) {
				// System.out.println(line);
                trimmedLine = line.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
                String[] codes = trimmedLine.split(",");
                map.put(codes[0], codes[1]);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        

	}
	  
    public static ArrayList<String> fetchInstructions(String filename) {

        ArrayList<String> myInstructions = new ArrayList<String>();
        BufferedReader reader;
        String trimmedInput;

        try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();

			while (line != null) {
				// System.out.println(line);
				// read next line
                trimmedInput = line.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
                myInstructions.add(trimmedInput);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return myInstructions;
	}	

    public static void outputFiles() {
        String path = "src/output/";
        String filePath1 = path + "load_file.txt";
        String filePath2 = path + "listing_file.txt";

        BufferedWriter writer1 = null;
        BufferedWriter writer2 = null;

        try {

            File file1 = new File(filePath1);
            File file2 = new File(filePath2);
       
            /* This logic will make sure that the file 
             * gets created if it is not present at the
             * specified location*/
            
            FileWriter fw1 = new FileWriter(file1);
            FileWriter fw2 = new FileWriter(file2);
            writer1 = new BufferedWriter(fw1);
            writer2 = new BufferedWriter(fw2);
            
            ArrayList<String> memoryLocation = assemblerData.get(0);
            ArrayList<String> octalInstructions = assemblerData.get(1);
            ArrayList<String> instructions = assemblerData.get(2);

            for (int i = 0; i < instructions.size(); i++) {
                if (memoryLocation.get(i) != "" && octalInstructions.get(i) != "" ) {
                    writer1.write(memoryLocation.get(i) + "\t\t\t" + octalInstructions.get(i) + "\n");
                } 
                writer2.write(memoryLocation.get(i) + "\t\t" + octalInstructions.get(i) + "\t\t" + instructions.get(i) + "\n");
                
            }

            System.out.println("File written Successfully");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(writer1 != null) {
                    writer1.close();
                }

                if(writer2 != null) {
                    writer2.close();
                }
 
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

}
