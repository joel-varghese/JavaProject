package core;

public enum IndexRegister {
    NOX((byte) 0b00), // No Index Register
    IX1((byte) 0b01), // Index Register 1
    IX2((byte) 0b10), // Index Register 2
    IX3((byte) 0b11); // Index Register 3

    public final byte id;

    private IndexRegister(byte id) {
        this.id = id;
    }

    public static IndexRegister fromWord(char word) {
        // Left shift by 8 bits to get rid of the opcode and GPR, then right shift by 14
        // bits to
        // get rid of the last 14 bits
        return fromByte((byte) ((word << 8) >> 14));
    }

    public static IndexRegister fromByte(byte id) {
        for (IndexRegister indexRegister : IndexRegister.values()) {
            if (indexRegister.id == id) {
                return indexRegister;
            }
        }

        return NOX;
    }
}
