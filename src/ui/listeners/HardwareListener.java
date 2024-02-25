package ui.listeners;

import java.util.EventListener;

public interface HardwareListener extends EventListener {
    
    /**
     * Called when hardware properties have been updated
     */
    public void onUpdate();
}
