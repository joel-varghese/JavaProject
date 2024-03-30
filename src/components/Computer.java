package components;

import java.util.List;
import java.util.logging.Logger;

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
        cache = new Cache<>(128); // 16 x 8
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
        if (!cache.containsKey(address)) {
            loadCache(address);
        } 
        content.append(" " + Cache.padOctal(Integer.toString(cache.get(address), 8), 6));

        return content.toString();
    }


    public String loadCache(char address) {
        cache.putLine(address, memory);

        return cache.displayCacheAddresses();
    }



}
