package components;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator; 

public class Cache<K, V> {
    private final int capacity;
    private final Map<Character, Character> cache;
    private final java.util.LinkedList<Character> accessOrder;

    public Cache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.accessOrder = new java.util.LinkedList<>();
    }

    public synchronized Character get(Character key) {
        Character value = cache.get(key);
        if (value != null) {
            accessOrder.remove(key);
            accessOrder.addFirst(key);
        }
        return value;
    }


    public synchronized boolean containsKey(Character key) {
        return cache.containsKey(key);
    }

    public synchronized void remove(Character key) {
        cache.remove(key);
        accessOrder.remove(key);
    }

    public synchronized int size() {
        return cache.size();
    }

    public synchronized void clear() {
        cache.clear();
        accessOrder.clear();
    }

    // FIFO put
    public synchronized void putLine(Character address, Memory memory) {
        if (cache.containsKey(address)) {
            return;

        } else if (cache.size() >= capacity) {
            for (int i = 0; i < 8; i++) {
                char leastRecentlyUsed = (char) accessOrder.removeLast();
                System.out.println("Least Recently Used: " + Integer.toString(leastRecentlyUsed, 8));
                System.out.println(cache.remove(leastRecentlyUsed));
                System.out.println("Removed Least Recently Used? ");
                System.out.println(cache.containsKey(leastRecentlyUsed));
                System.out.println(cache.size());

            }
        }
        char[] addressRange = generateAddressRange(address);
        // StringBuilder displayAddresses = new StringBuilder();
        // displayAddresses.append(padOctal(Integer.toString(currAddress, 8), 4));

        for (int i = addressRange.length - 1; i >= 0; i--) {
            char currAddress = addressRange[i];
            cache.put(currAddress, memory.read(currAddress));
            // displayAddresses = displayAddresses + " " + Integer.toString(currAddress);
            // displayAddresses.append(" " + padOctal(Integer.toString(currAddress, 8), 4));
            accessOrder.addFirst(currAddress);
            currAddress += 1;
        }
        return ;
    }

    // public synchronized void put(Character key, Character value) {
    //     // if (cache.containsKey(key)) {
    //     //     accessOrder.remove(key);
    //     if (cache.size() >= capacity) {
    //         for (int i = 0; i < 8; i++) {
    //             Character leastRecentlyUsed = accessOrder.removeLast();
    //             cache.remove(leastRecentlyUsed);
    //         }
    //     }
            
    //     cache.put(key, value);
    //     accessOrder.addFirst(key);
    // }

    public char[] generateAddressRange(char address) {
        char[] addressRange = new char[8];
        int modulo = address % 8;
        int startingPoint = address - modulo;
        char currAddress = (char) startingPoint;

        for (int i = 0; i < 8; i++) {
            addressRange[i] = currAddress;
            currAddress += 1;
        }

        return addressRange;
    }

    public String padOctal(String octal, int targetLength) {
        int currentLength = octal.length();
        
        if (currentLength < targetLength) {
            // Calculate the number of zeros needed for padding
            int zerosToAdd = targetLength - currentLength;

            // Create a StringBuilder to build the padded binary string
            StringBuilder paddedOctal = new StringBuilder();
            
            // Append zeros
            for (int i = 0; i < zerosToAdd; i++) {
                paddedOctal.append('0');
            }

            // Append the original binary string
            paddedOctal.append(octal);

            // Return the padded binary string
            return paddedOctal.toString();
        } else {
            // If the binary string is already equal to or longer than the target length, return it unchanged
            return octal;
        }
    }

    public void printAll() {
        for (Map.Entry<Character, Character> entry : cache.entrySet()) {
            System.out.println(Integer.toString(entry.getKey(), 8) + " " + Integer.toString(entry.getValue(), 8));

        }
    }

    public String displayCacheAddresses() {
        int count = 0;
        StringBuilder cacheDisplay = new StringBuilder();
        // for (Map.Entry<Character, Character> entry : cache.entrySet()) {
        //     cacheDisplay.append(" " +Integer.toString(entry.getKey(), 8));
        //     count++;
        //     if (count % 8 == 0) {
        //         cacheDisplay.append("\n");
        //     }
        // }
        Iterator it = accessOrder.iterator(); 

        // for (int i = 0; i < accessOrder.size(); i++) { 
        //     cacheDisplay.append("\t" + Integer.toString(accessOrder.get(i),8)); 
        //     if (i % 8 == 0) {
        //         cacheDisplay.append("\n");
        //     }
        // } 

        while (it.hasNext()) {
            cacheDisplay.append(" " + padOctal(Integer.toString((char) it.next(),8), 4)); 
            count++;
            if (count % 8 == 0) {
                cacheDisplay.append("\n");
            }
        }
        return cacheDisplay.toString();

    }

}