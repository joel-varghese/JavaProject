package core;

public enum Condition {
    OVERFLOW(
            (byte) 0b0000,
            (byte) 0b1000,
            "Overflow"),
    UNDERFLOW(
            (byte) 0b0001,
            (byte) 0b0100,
            "Underflow"),
    DIVZERO(
            (byte) 0b0010,
            (byte) 0b0010,
            "Divide by zero"),
    EQUALORNOT(
            (byte) 0b0011,
            (byte) 0b0001,
            "Equal or not equal");

    // The id of the condition
    public final byte id;

    // The code of the condition
    public final byte code;

    // The message of the condition
    public final String message;

    private Condition(byte id, byte code, String message) {
        this.id = id;
        this.code = code;
        this.message = message;
    }

    /**
     * Returns the condition with the given id
     * 
     * @param id The id of the condition
     * @return The condition
     */
    public static Condition fromId(byte id) {
        for (Condition condition : Condition.values()) {
            if (condition.id == id) {
                return condition;
            }
        }

        return null;
    }

    /**
     * Returns whether or not the condition is set in the condition code
     * 
     * @param condition The condition to check
     * @param cc        The condition code to check
     * @return Whether or not the condition is set
     */
    public static boolean isSet(Condition condition, byte cc) {
        return (cc & condition.code) == condition.code;
    }

    /**
     * Sets the condition in the condition code
     * 
     * @param condition The condition to set
     * @param cc        The condition code to set
     * @return The new condition code
     */
    public static byte set(Condition condition, byte cc) {
        return (byte) (cc | condition.code);
    }

    /**
     * Clears the condition in the condition code
     * 
     * @param condition The condition to clear
     * @param cc        The condition code to clear
     * @return The new condition code
     */
    public static byte clear(Condition condition, byte cc) {
        return (byte) (cc & ~condition.code);
    }
}
