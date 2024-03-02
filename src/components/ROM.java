package components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.HashMap;

public class ROM {

    private static final Logger LOGGER = Logger.getLogger(Computer.class.getName());

    // The ROM file
    private File file;

    public ROM(File file) {
        this.file = file;
    }

    public Map<Character, Character> read() {
        Map<Character, Character> region = new HashMap<Character, Character>();

        if (!file.exists()) {
            return region;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                String input = line.replaceAll("\\s+", " ");
                String[] parts = input.split(" ");

                if (parts.length != 2) {
                    continue;
                }

                char address = (char) Integer.parseInt(parts[0], 8);
                char data = (char) Integer.parseInt(parts[1], 8);

                region.put(address, data);
            }

            br.close();
            LOGGER.info("Finished reading ROM file " + file.getName());

        } catch (IOException e) {
            LOGGER.warning("Failed to read ROM file " + file.getName() + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.warning("Failed to read ROM file " + file.getName() + ": " + e.getMessage());
        }

        return region;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
}
