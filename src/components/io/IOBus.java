package components.io;

import java.util.Map;
import java.util.logging.Logger;

public class IOBus {

    private static final Logger LOGGER = Logger.getLogger(IOBus.class.getName());

    // A map of devices on the IO bus
    private Map<Short, Device> devices;

    public IOBus() {
        devices = new java.util.HashMap<Short, Device>();
    }

    /**
     * Adds a device to the IO bus.
     * 
     * @param device The device to add.
     */
    public void addDevice(Device device) {
        devices.put(device.getId(), device);
        LOGGER.info("Added device " + device.getClass().getName() + " with ID " + device.getId());
    }

    /**
     * Reads a word from the device with the specified ID.
     * 
     * @param id The ID of the device to read from.
     * @return The word read from the device.
     */
    public char read(short id) {
        Device device = devices.get(id);

        if (device == null) {
            LOGGER.warning("Failed to read from device with ID " + id + " as the device does not exist");
            return 0;
        }

        return device.read();
    }

    /**
     * Writes a word to the device with the specified ID.
     * 
     * @param id   The ID of the device to write to.
     * @param data The word to write to the device.
     */
    public void write(short id, char data) {
        Device device = devices.get(id);

        if (device == null) {
            LOGGER.warning("Failed to write to device with ID " + id + " as the device does not exist");
            return;
        }

        device.write(data);
    }

    public char check(short id){
        Device device = devices.get(id);

        if (device == null) {
            LOGGER.warning("Failed to check device with ID " + id + " as the device does not exist");
            return 0;
        }

        return device.check();
    }

    /**
     * Tells all devices connected to the IOBus to perform a reset
     * operation.
     */
    public void reset() {
        for (Device device : devices.values()) {
            device.reset();
        }

        LOGGER.fine("Reset all devices on the IO bus");
    }
}
