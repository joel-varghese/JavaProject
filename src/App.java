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


        // Display the entered string
        // System.out.println("The binary is: " + decimalToBinary(input));


        initOpcodes();
        String vals[] = fetchInstructions();

        String[] answers = new String[2];
      
        String trimmedInput = vals[0].trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
        // Regular expression pattern to extract opcode and numbers
        Pattern pattern = Pattern.compile("([A-Za-z]+)\\s*(\\d+)(?:,(\\d+))?(?:,(\\d+))?(?:,(\\d+))?");
        Matcher matcher = pattern.matcher(trimmedInput);
        String index = "0";
        if(matcher.find()){
            index = matcher.group(2);
        }
        int decimalInd = Integer.parseInt(index);
        String serial = "000000";
      
        for(int i=0;i<vals.length;i++){
          
//             String opcode = extractOpcodeAndNumbers(vals[i]);
//             String ans = "";
//             if(i==0){ans = serial+"   "+opcode;}else{ans = integerToOctal(decimalInd, 6) +"   "+opcode;decimalInd++;}
//             System.out.println(ans);
               answers = extractOpcodeAndNumbers(vals[i]);
               System.out.println(answers[0] + "\t\t" + answers[1] + "\t" + vals[i]);
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
    
    private static String[] extractOpcodeAndNumbers(String input) {

            String trimmedInput = input.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
            // Regular expression pattern to extract opcode and numbers
            Pattern pattern = Pattern.compile("([A-Za-z]+)\\s*(\\d+)(?:,(\\d+))?(?:,(\\d+))?(?:,(\\d+))?");
            Matcher matcher = pattern.matcher(trimmedInput);
            String location = "";
            String result = "";
            String[] answers = new String[2];

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
                    binary5 = "00";
                    binary4 = "0";
                    result = binary1 + binary2 + binary3 + binary4 + binary5;
                }else if(opcode.equals("MLT") || opcode.equals("DVD") || opcode.equals("TRR") || opcode.equals("AND") || opcode.equals("ORR")){
                    String reg = decimalToBinary(matcher.group(2));
                    binary2 = padBinary(reg, 2);
                    String ix = decimalToBinary(matcher.group(3));
                    binary3 = padBinary(ix, 2);
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
                    // locationTracker = matcher.group(2);
                    location = getLocation(matcher.group(2), false);
                    location = "";
                } else {
                    location = getLocation(locationTracker, true);
                }


                // Extracted numbers
                // for (int i = 2; i <= matcher.groupCount(); i++) {
                //     //  System.out.print(" Number " + (i - 1) + ": " + matcher.group(i));
                // }
            } else {
                // System.out.println(trimmedInput);
                if(trimmedInput.equals("Data End")){
                     result = "002000";
                    location = getLocation(locationTracker, true);

                }else if(trimmedInput.equals("End: HLT")){
                    result = "";

                    location = getLocation("1024", true);
                }
            }

            // return answer;
            answers[0] = location;
            answers[1] = result;

            return answers;
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


    public static void initOpcodes() {

      map.put("LDX","04");
      map.put("LDR", "01");
      map.put("STR", "02");
      map.put("LDA", "03");
      map.put("STX", "05");
      map.put("JZ", "06");
      map.put("JNE", "07");
      map.put("JCC", "10");
      map.put("JMA", "11");
      map.put("JSR", "12");
      map.put("RFS", "13");
      map.put("SOB", "14");
      map.put("JGE", "15");
      map.put("AMR", "16");
      map.put("SMR", "17");
      map.put("AIR", "20");
      map.put("SIR", "21");
      map.put("MLT", "22");
      map.put("DVD", "23");
      map.put("TRR", "24");
      map.put("AND", "25");
      map.put("ORR", "26");
      map.put("NOT", "27");
      map.put("SETCCE", "44");
      map.put("TRAP", "045");
      map.put("Data","0");
      map.put("LOC","0");
      map.put("HLT","-1");

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

           String[] myInstructions2 = 
           {"          LOC     6           ;BEGIN AT LOCATION 6",
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
           "          DVD     2,1         ;",
           "          TRR     3,0         ;",
           "          JSR     2,9,1       ;",
           "          SETCCE  0           ;SET CONDITION CODE FOR EQUAL",
           "          JZ      1,0         ;JUMP TO End if CC is 1",
           "          LOC     1024",
           "End:      HLT                 ;STOP"
           };

		return myInstructions;
	}	

}
