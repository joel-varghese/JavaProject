package components.cpu;

public class ALU {

    /**
     * Adds two 16-bit numbers and returns the result
     * 
     * @param a The first number to add
     * @param b The second number to add
     * @return The sum of the two numbers
     */
    public static char add(char a, char b) {
        return (char) (a + b);
    }

    /**
     * Subtracts two 16-bit numbers and returns the result
     * 
     * @param a The first number to subtract
     * @param b The second number to subtract
     * @return The difference of the two numbers
     */
    public static char subtract(char a, char b) {
        return (char) (a - b);
    }
}
