import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


public class SimpleCache {
    private static final int CACHE_SIZE = 16; // Number of cache lines
    private static final int WORD_SIZE = 4;   // Size of each word in bytes
    private static final int BLOCK_SIZE = 4;  // Number of words in each cache block
    private static final int MEMORY_SIZE = 2048; // Total size of memory in words

    private Map<Integer, CacheLine> cache;
    private Queue<Integer> fifoQueue;

    public SimpleCache() {
        cache = new HashMap<>();
        fifoQueue = new ArrayDeque<>();
    }

    // Read operation
    public int[] read(int address) {
        int blockNumber = address / BLOCK_SIZE;
        int blockOffset = address % BLOCK_SIZE;
        int tag = blockNumber;

        if (cache.containsKey(tag)) {
            System.out.println("Cache hit for address " + address);
            return cache.get(tag).getData();
        } else {
            System.out.println("Cache miss for address " + address);
            int[] data = fetchDataFromMemory(address);
            evictIfNecessary();
            cache.put(tag, new CacheLine(tag, data));
            fifoQueue.add(tag);
            return data;
        }
    }

    // Fetch data from memory
    private int[] fetchDataFromMemory(int address) {
        // Simulate fetching data from memory
        System.out.println("Fetching data from memory for address " + address);
        int[] data = new int[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            // Simulate data retrieval from memory
            data[i] = address + i * WORD_SIZE;
        }
        return data;
    }

    // Evict cache line if necessary
    private void evictIfNecessary() {
        if (cache.size() >= CACHE_SIZE) {
            int tagToEvict = fifoQueue.poll();
            System.out.println("Evicting cache line with tag " + tagToEvict);
            cache.remove(tagToEvict);
        }
    }

    public static void main(String[] args) {
        SimpleCache cache = new SimpleCache();
        // Simulate read operations
        for (int i = 0; i < MEMORY_SIZE; i++) {
            cache.read(i);
        }
    }
}



class CacheLine {
    private int tag;
    private int[] data;

    public CacheLine(int tag, int[] data) {
        this.tag = tag;
        this.data = data;
    }

    public int getTag() {
        return tag;
    }

    public int[] getData() {
        return data;
    }
}
