package components;

import java.util.List;
import java.util.logging.Logger;
import java.io.File;


import components.cpu.Processor;
import components.io.IOBus;
import components.Cache;

public class Computer {

    private static final Logger LOGGER = Logger.getLogger(Computer.class.getName());

    // The processor object of the minicomputer
    public Processor processor;

    // The memory object of the minicomputer
    public Memory memory;

    // The IO bus of the minicomputer
    public IOBus ioBus;

    // The initial program loader ROM
    public ROM ipl;

    // ROM files that have been loaded into memory
    public List<ROM> roms;

    // Paragraph files that have been loaded into memory
    public List<Paragraph> paragraphs;

    // Cache
    public Cache<Character, Character> cache;

    /**
     * Creates a new minicomputer with a memory and processor.
     */
    public Computer() {
        memory = new Memory();
        ioBus = new IOBus();
        processor = new Processor(memory, ioBus);
        roms = new java.util.ArrayList<ROM>();
        paragraphs = new java.util.ArrayList<Paragraph>();
        cache = new Cache<>(128); // 16 x 8
    }

    public Computer(ROM ipl, Paragraph paragraph) {
        this();
        this.ipl = ipl;
        loadROM(ipl);
        loadParagraph(paragraph);
    }

    /**
     * Creates a new minicomputer with a memory and processor and loads the initial
     * program loader ROM.
     * 
     * @param ipl The initial program loader ROM.
     */
    public Computer(ROM ipl) {
        this();
        this.ipl = ipl;
        loadROM(ipl);
    }

    /**
     * Loads the provided ROM file into memory, and adds to the list of loaded ROMs.
     * 
     * @param rom The ROM file to load.
     */
    public void loadROM(ROM rom) {
        LOGGER.info("Loading ROM file " + rom.getPath());
        roms.add(rom);

        for (char address : rom.read().keySet()) {
            memory.privilegedWrite(address, rom.read().get(address));
        }
    }

    public void loadParagraph(Paragraph paragraph) {
        LOGGER.info("Loading paragraph file " + paragraph.getPath());
        paragraphs.add(paragraph);

        for (char address : paragraph.read().keySet()) {
            memory.privilegedWrite(address, paragraph.read().get(address));
        }
    }

    /**
     * Resets the minicomputer by clearing the memory and processor,
     * clears the ROMs and loads the initial program loader ROM.
     */
    public void reset() {
        ioBus.reset();
        roms.clear();

        memory = new Memory();
        processor = new Processor(memory, ioBus);

        LOGGER.info("Reset the minicomputer");
    }

    /**
     * Tells the processor to run with the current instruction address.
     */
    public void run() {
        LOGGER.info("Beginning RUN execution mode");
        processor.run();
    }

    /**
     * Tells the processor to step through the next instruction.
     */
    public void step() {
        LOGGER.info("Beginning STEP execution mode");
        processor.step();
    }

    public String fetchCacheContent(char address) {
        StringBuilder content = new StringBuilder();
        
        content.append(Cache.padOctal(Integer.toString(address, 8), 6));

        // If address is not in cache - "Miss"
        if (!cache.containsKey(address)) {
            // Search for address in memory, load address to cache
            loadCache(address);
        } 

        // Else if address is already in cache - "Hit"
        content.append(" " + Cache.padOctal(Integer.toString(cache.get(address), 8), 6));
        return content.toString();
    }

    public String loadCache(char address) {
        cache.putLine(address, memory);
        return cache.displayCacheAddresses();
    }

    public String fetchParagraphContent() {
        StringBuilder content = new StringBuilder();
        char address = Paragraph.startAddress;
        String addressCounter = Integer.toOctalString(address);

        while (address <= Paragraph.endAddress) {
            content.append(memory.read(address));
            //address++;
            addressCounter = Integer.toOctalString(Integer.parseInt(addressCounter, 8) + 1);
            address =  (char) Integer.parseInt(addressCounter, 8);
        }

        return content.toString();
    }

    public String searchUserWord(String userWord) {
        String result;
        StringBuilder memoryWord = new StringBuilder();
        char address = Paragraph.startAddress;
        String addressCounter = Integer.toOctalString(address);
        int sentenceNumber = 1; 
        int wordNumber = 1;     // word number in the sentence

        boolean found = false;
        boolean isAWord = false;

        while (address <= Paragraph.endAddress && !found) {

            if(memory.read(address) == (char) '.') {
                sentenceNumber++;
                wordNumber = 0;
            }

            if(memory.read(address) == (char) ' ') {
                wordNumber++;
            }
            
            if (memory.read(address) == userWord.charAt(0)) {
                String tempAddressCounter = addressCounter;
                char tempAddress = address;

                String tempAddressCounterMinus1 = Integer.toOctalString(Integer.parseInt(tempAddressCounter, 8) - 1);
                char tempAddressMinus1 =  (char) Integer.parseInt(tempAddressCounterMinus1, 8);

                isAWord = memory.read(tempAddressMinus1) == (char) ' ' || tempAddressMinus1 < Paragraph.startAddress;

                for (int i = 0; i < userWord.length(); i++) {
                    memoryWord.append(memory.read(tempAddress));
                    // memoryCounter++;
                    tempAddressCounter = Integer.toOctalString(Integer.parseInt(tempAddressCounter, 8) + 1);
                    tempAddress =  (char) Integer.parseInt(tempAddressCounter, 8);
                }

                isAWord = isAWord && (memory.read(tempAddress) == (char) ' ' || memory.read(tempAddress) == (char) '.');
                if (userWord.equals(memoryWord.toString()) && isAWord) {
                    found = true;
                    break;
                } else {                
                    memoryWord = new StringBuilder();
                }
            }

            isAWord = false;

            addressCounter = Integer.toOctalString(Integer.parseInt(addressCounter, 8) + 1);
            address =  (char) Integer.parseInt(addressCounter, 8);
            
        }

        if (found) {
           result = "User Word: " + userWord + "\nSentence #: " + sentenceNumber + "\nWord #: " + wordNumber;
        } else {
            result = "Word NOT found!";
        }
        return result;
    }
}
