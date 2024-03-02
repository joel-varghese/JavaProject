package core;

public enum GeneralRegister {
    GPR0((byte) 0b00), // General Purpose Register 1
    GPR1((byte) 0b01), // General Purpose Register 2
    GPR2((byte) 0b10), // General Purpose Register 3
    GPR3((byte) 0b11); // General Purpose Register 4

    public final byte id;

    private GeneralRegister(byte id) {
        this.id = id;
    }

    public static GeneralRegister fromId(byte id) {
        for (GeneralRegister register : GeneralRegister.values()) {
            if (register.id == id) {
                return register;
            }
        }

        return GPR0;
    }

    /**
     * Given a 16-bit word, return the general purpose register selected by bits
     * 8-9, where bit 0 is the most significant bit.
     * 
     * @param word The word to read from.
     * @return The register selected by bits 8-9.
     */
    public static GeneralRegister fromWord(char word) {
        byte id = (byte) ((word >> 8) & 0b11);
        for (GeneralRegister register : GeneralRegister.values()) {
            if (register.id == id) {
                return register;
            }
        }

        return GPR0;
    }

}
