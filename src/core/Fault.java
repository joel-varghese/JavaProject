package core;

public enum Fault {
    ILLEGAL_MEMORY_RESERVED(
            (byte) 0b0000,
            (byte) 0b0001,
            "Illegal memory address to reserved locations"),
    ILLEGAL_TRAP(
            (byte) 0b0001,
            (byte) 0b0010,
            "Illegal TRAP code"),
    ILLEGAL_OPCODE(
            (byte) 0b0010,
            (byte) 0b100,
            "Illegal Operation code"),
    ILLEGAL_MEMORY_ADDRESS(
            (byte) 0b0011,
            (byte) 0b1000,
            "Illegal memory address beyond 2048");

    // The id of the fault
    public final byte id;

    // The code of the fault
    public final byte code;

    // The message of the fault
    public final String message;

    private Fault(byte id, byte code, String message) {
        this.id = id;
        this.code = code;
        this.message = message;
    }

    /**
     * Returns the fault with the given id
     * 
     * @param id The id of the fault
     * @return The fault
     */
    public static Fault fromId(byte id) {
        for (Fault fault : Fault.values()) {
            if (fault.id == id) {
                return fault;
            }
        }

        return null;
    }

    /**
     * Returns whether or not the fault is set in the condition code
     * 
     * @param fault The fault to check
     * @param cc    The condition code to check
     * @return Whether or not the fault is set
     */
    public static boolean isSet(Fault fault, byte cc) {
        return (cc & fault.code) == fault.code;
    }

    /**
     * Returns whether or not the fault is set in the condition code
     * 
     * @param id The id of the fault
     * @param cc The condition code to check
     * @return Whether or not the fault is set
     */
    public static boolean isSet(byte id, byte cc) {
        return isSet(fromId(id), cc);
    }

    /**
     * Sets the fault in the condition code and returns the new condition code
     * 
     * @param fault The fault to set
     * @param cc    The condition code to set the fault in
     * @return The new condition code
     */
    public static byte set(Fault fault, byte cc) {
        return (byte) (cc | fault.code);
    }

    /**
     * Clears the fault from the and returns the new condition code
     * 
     * @param fault The fault to clear
     * @param cc    The condition code to clear the fault from
     * @return The new condition code
     */
    public static byte clear(Fault fault, byte cc) {
        return (byte) (cc & ~fault.code);
    }
}
