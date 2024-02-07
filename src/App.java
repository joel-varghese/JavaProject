import java.util.HashMap;
import java.util.Map;
// import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    private static Map<String, String> map = new HashMap<>();
    private static String locationTracker = "";
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);
        // System.out.println("Enter a string:");
        // String input = scanner.nextLine();
        // scanner.close();
        initOpcodes();

        // Display the entered string
        // System.out.println("The binary is: " + decimalToBinary(input));

        String vals[] = fetchInstructions(); 

        for(int i=0;i<vals.length;i++){
            extractOpcodeAndNumbers(vals[i]);
        }
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

            String trimmedInput = input.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
            // Regular expression pattern to extract opcode and numbers
            Pattern pattern = Pattern.compile("([A-Za-z]+)\\s*(\\d+)(?:,(\\d+))?(?:,(\\d+))?(?:,(\\d+))?");
            Matcher matcher = pattern.matcher(trimmedInput);
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
                if((opcode.equals("LDR")) || (opcode.equals("LDA")) || (opcode.equals("STR"))){
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
                }else if(opcode .equals("LDX") || opcode.equals("STX") || opcode.equals("JZ")){
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
                }else if(opcode.equals("SETCCE")){
                    String reg = decimalToBinary(matcher.group(2));
                    binary2 = padBinary(reg, 2);
                    String ix = "";
                    binary3 = "00";
                    String addr = "";
                    binary5 = "00";
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
                    locationTracker = matcher.group(2);
                } else {
                    location = decimalToBinary(locationTracker);
                    location = binaryToOctal(location);
                    location = padBinary(location, 6);
                    locationTracker = addDecimals(locationTracker, 1);
                }


                // Extracted numbers
                // for (int i = 2; i <= matcher.groupCount(); i++) {
                //     //  System.out.print(" Number " + (i - 1) + ": " + matcher.group(i));
                // }
            } else {
                // System.out.println(trimmedInput);
                if(trimmedInput.equals("Data End")){
                     result = "002000";

                     location = decimalToBinary(locationTracker);
                     location = binaryToOctal(location);
                     location = padBinary(location, 6);
                     locationTracker = addDecimals(locationTracker, 1);

                }else if(trimmedInput.equals("End: HLT")){
                    result = "";

                    locationTracker = "1024";
                    location = decimalToBinary(locationTracker);
                    location = binaryToOctal(location);
                    location = padBinary(location, 6);
                    locationTracker = addDecimals(locationTracker, 1);
                }
            }
            System.out.println(location + "\t\t" + result + "\t" + input);
    }


    public static void initOpcodes() {
		// Map<String, String> myOpcodes8 = new HashMap<>();
		// myOpcodes8.put("HLT", "000");
		// myOpcodes8.put("DATA", "000");
		// myOpcodes8.put("LDR", "001");
		// myOpcodes8.put("STR", "002");
		// myOpcodes8.put("LDA", "003");
		// myOpcodes8.put("LDX", "004");
		// myOpcodes8.put("STX", "005");
		// myOpcodes8.put("JZ", "006");
		// myOpcodes8.put("JNE", "007");
		// myOpcodes8.put("JCC", "010");
		// myOpcodes8.put("JMA", "011");
		// myOpcodes8.put("JSR", "012");
		// myOpcodes8.put("RFS", "013");
		// myOpcodes8.put("SOB", "014");
		// myOpcodes8.put("JGE", "015");
		// myOpcodes8.put("AMR", "016");
		// myOpcodes8.put("SMR", "017");
		// myOpcodes8.put("AIR", "020");
		// myOpcodes8.put("SIR", "021");
		// myOpcodes8.put("MLT", "022");
		// myOpcodes8.put("DVD", "023");
		// myOpcodes8.put("TRR", "024");
		// myOpcodes8.put("AND", "025");
		// myOpcodes8.put("ORR", "026");
		// myOpcodes8.put("NOT", "027");
		// myOpcodes8.put("SETCCE", "044");
		// myOpcodes8.put("TRAP", "045");

        // map.put("LDX","04");
        // map.put("LDR", "01");
        // map.put("LDA", "03");
        // map.put("JZ", "06");
        // map.put("SETCCE", "44");
        // map.put("Data","0");
        // map.put("LOC","0");
        // map.put("HLT","-1");

        map.put("HLT", "-1");
		map.put("DATA", "000");
		map.put("LDR", "001");
		map.put("STR", "002");
		map.put("LDA", "003");
		map.put("LDX", "004");
		map.put("STX", "005");
		map.put("JZ", "006");
		map.put("JNE", "007");
		map.put("JCC", "010");
		map.put("JMA", "011");
		map.put("JSR", "012");
		map.put("RFS", "013");
		map.put("SOB", "014");
		map.put("JGE", "015");
		map.put("AMR", "016");
		map.put("SMR", "017");
		map.put("AIR", "020");
		map.put("SIR", "021");
		map.put("MLT", "022");
		map.put("DVD", "023");
		map.put("TRR", "024");
		map.put("AND", "025");
		map.put("ORR", "026");
		map.put("NOT", "027");
		map.put("SETCCE", "044");
		map.put("TRAP", "045");
		// return myOpcodes8;
	}
	  
    public static String[] fetchInstructions() {
		String[] myInstructions = 
            {
                "          LOC     6           ;BEGIN AT LOCATION 6",
                "          Data    10          ;PUT 10 AT LOCATION 6",
                "          Data    3           ;PUT 3 AT LOCATION 7",
                "          Data    End         ;PUT 1024 AT LOCATION 8",
                "          Data    0",
                "          Data    12",
                "          Data    9",
                "          Data    18",
                "          Data    12",
                "          LDX     2,7         ;X2 GETS 3",
                "          LDR     3,0,10      ;R3 GETS 12",
                "          LDR     2,2,10      ;R2 GETS 12",
                "          LDR     1,2,10,1    ;R1 GETS 18",
                "          LDA     0,0,0       ;R0 GETS 0 to set CONDITION CODE",
                "          LDX     1,8         ;X1 GETS 1024",
                "          SETCCE  0           ;SET CONDITION CODE FOR EQUAL",
                "          JZ      1,0         ;JUMP TO End if CC is 1",
                "          LOC     1024",
                "End:      HLT                 ;STOP"
                
	       };
		return myInstructions;
	}	


}
