package components.io;

public interface Device {

    /**
     * Writes a word to the device.
     * 
     * @param data The word to write to the device.
     */
    public void write(char data);

    /**
     * Returns the data currently stored by the device, note this may return a
     * single element from a buffer.
     * 
     * @return The data currently stored by the device.
     */
    public char read();

    /**
     * Checks if the device is ready to be read from.
     * 
     * @return True if the device is ready to be read from, false otherwise.
     */
    public char check();

    /**
     * Gets the ID of the device.
     * 
     * @return The ID of the device.
     */
    public short getId();

    public void reset();

}
