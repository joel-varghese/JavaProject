public class Number {
    public String decimalToBinary(String decimalStr) {
        int number = Integer.parseInt(decimalStr);
        String binaryStr = Integer.toBinaryString(number);
        return new StringBuilder(binaryStr).toString();
    }

    public String binaryToOctal(String binaryStr) {
        // Convert binary string to integer
        int num = Integer.parseInt(binaryStr, 2);

        // Convert integer to octal string
        return Integer.toOctalString(num);
    }

    public String integerToOctal(int val,int digits) {
        // Convert integer to octal string
        String octal = Integer.toOctalString(val);
        return padBinary(octal, digits);
    }

    public String padBinary(String binary, int targetLength) {
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

    public String addDecimals(String decimalStr, int decimalInt) {
        int number = Integer.parseInt(decimalStr);
        number += decimalInt;
        return Integer.toString(number);
    }


    public int charToInt(char c) {
        int numericC = (int) c;

        if (numericC >= 48 && numericC <= 57) {
            numericC = numericC - 48;
        }

        return numericC;
    }

}
