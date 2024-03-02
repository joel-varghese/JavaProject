package util;

public class FormatUtils {

    /**
     * Returns a value as a binary string of specified length
     * 
     * @param value  The value to convert to a binary string
     * @param length The length of the binary string
     */
    public static String toBinaryString(int value, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static String toHexString(char value){
        return String.format("0x%04X", (int) value);
    }

}
