package components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.HashMap;

public class Paragraph {

    private static final Logger LOGGER = Logger.getLogger(Computer.class.getName());

    public static char startAddress;

    public static char endAddress;

    public static String addressCounter;

    private File file;

    public Paragraph(File file) {
        this.file = file;
    }

    public Map<Character, Character> read() {
        Map<Character, Character> region = new HashMap<Character, Character>();

        if (!file.exists()) {
            return region;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            addressCounter = "1000";
            startAddress = (char) Integer.parseInt(addressCounter, 8);
            int character;
            while ((character = br.read()) != -1) {
                char address = (char) Integer.parseInt(addressCounter, 8);
                char data = (char) character;
                region.put(address, data);

                endAddress = address;
                addressCounter = Integer.toOctalString(Integer.parseInt(addressCounter, 8) + 1);
            }
                
            br.close();
            // System.out.println("Hello Debug Paragraph #1");
            // for (Map.Entry<Character,Character> entry : region.entrySet())  {
            //     System.out.println("Key = " + entry.getKey() + 
            //                     ", Value = " + entry.getValue()); 
            // }
            LOGGER.info("Finished reading paragraph file " + file.getName());


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
