package core;

public enum Opcode {
    HLT((byte) 0b000000), // HALT
    LDR((byte) 0b000001), // Load Register
    STR((byte) 0b000010), // Store Register
    LDA((byte) 0b000011), // Load Register with Address
    LDX((byte) 0b100001), // Load Index Register from Memory
    STX((byte) 0b100010), // Store Index Register to Memory

    // Transfer of Control Operations
    JZ((byte) 0b001010), // Jump if Zero
    JNE((byte) 0b001011), // Jump if Not Equal
    JCC((byte) 0b001100), // Jump if Condition Code
    JMA((byte) 0b001101), // Jump Unconditionally
    JSR((byte) 0b001110), // Jump and Save Return Address
    RFS((byte) 0b001111), // Return from Subroutine with return code in GPR 0
    SOB((byte) 0b010000), // Subtract One and Branch
    JGE((byte) 0b010001), // Jump greater than or equal to

    // Arithmetic and Logical Operations
    AMR((byte) 0b000100), // Add Memory to Register
    SMR((byte) 0b000101), // Subtract Memory from Register
    AIR((byte) 0b000110), // Add Immediate to Register
    SIR((byte) 0b000111), // Subtract Immediate from Register
    MLT((byte) 0b010010), // Multiply Register by Register
    DVD((byte) 0b010011), // Divide Register by Register
    TRR((byte) 0b010100), // Test the Equality of Register and Register
    AND((byte) 0b010101), // Logical And of Register and Register
    ORR((byte) 0b010110), // Logical Or of Register and Register
    NOT((byte) 0b010111), // Logical Not of Register

    // Arithmetic Shift Operations
    SRC((byte) 0b011000), // Shift Register by Count
    RRC((byte) 0b011001), // Rotate Register by Count

    // I/O Operations
    IN((byte) 0b111101), // Input Character to Register from Device
    OUT((byte) 0b111110), // Output Character to Device from Register
    CHK((byte) 0b111111), // Check Device Status to Register
    TRP((byte) 0b011000); // TRAP

    // The opcode of the instruction
    public final byte opcode;

    private Opcode(byte opcode) {
        this.opcode = opcode;
    }

    /**
     * Given a word, read the first 6 bits and return the corresponding instruction.
     * 
     * @param word The word to read from.
     * @return The instruction stored in the first 6 bits of the word.
     */
    public static Opcode fromWord(char word) {
        byte opcode = (byte) (word >> 10);
        for (Opcode instruction : Opcode.values()) {
            if (instruction.opcode == opcode) {
                return instruction;
            }
        }

        // If attempting to read an invalid instruction, halt the program
        return HLT;
    }

    /**
     * Given a 16-bit word, return whether or not the indirect addressing bit is set
     * in bit 10, where bit 0 is the most significant bit.
     * 
     * @param word The word to read from.
     * @return Whether or not the indirect addressing bit is set.
     */
    public static boolean isIndirectAddressing(char word) {
        // Left shift by 10 bits to get rid of the opcode, index register, and GPR, then
        // right shift by 15 bits to get rid of the last 15 bits
        return ((word << 10) >> 15) == 1;
    }

    /**
     * Retrieve the address bits in an instruction, 11 through 15
     * 
     * @param word The word to read from.
     * @return The data stored in bits 11-15.
     */
    public byte getAddress(char word) {
        return (byte) (word & 0b0000000000011111);
    }
}
