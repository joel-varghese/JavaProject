package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import components.Computer;
import components.ROM;
import config.Config;
import ui.components.FrontPanelMenu;
import ui.panels.ControlPanel;
import ui.panels.IndicatorPanel;
import ui.panels.InputPanel;
import ui.panels.registers.*;
import ui.windows.ConsoleWindow;
import util.ScreenUtil;

public class FrontPanel extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(Computer.class.getName());

    // Panel containing all of the indicator lights for registers
    public IndicatorPanel indicatorPanel = new IndicatorPanel();

    // Panel containing control buttons for the minicomputer
    public ControlPanel controlPanel = new ControlPanel();

    // Panel containing all text inputs for settable registers
    public InputPanel inputPanel = new InputPanel();

    // Panel containing all cache registers
    public CacheRegisterPanel cacheRegisterPanel = new CacheRegisterPanel();

    // Menu bar for the front panel
    public FrontPanelMenu menu = new FrontPanelMenu();

    public ConsoleWindow consoleWindow = new ConsoleWindow();

    public String displayAddresses;

    public int values[] = new int[20] ;
    public boolean val = false;

    public int found =0;
    public boolean fnd = false;

    private final JFileChooser fileChooser = new JFileChooser();

    private Computer computer;

    public FrontPanel(Computer computer) {
        super("Minicomputer");

        // Add the console and keyboard to the IO bus
        computer.ioBus.addDevice(consoleWindow.console);
        computer.ioBus.addDevice(consoleWindow.keyboard);

        this.computer = computer;

        // Setup the file chooser for loading ROMs
        fileChooser.setDialogTitle("Load ROM");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("ROM (.txt)", Config.ROM_FILE_EXTENSION));

        registerListeners();
        registerActionListeners();
        registerKeyListeners();

        JPanel stackPanel = new JPanel();
        stackPanel.setOpaque(false);
        stackPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(controlPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        stackPanel.add(inputPanel, gbc);

        setJMenuBar(menu);
        add(indicatorPanel, BorderLayout.CENTER);
        add(stackPanel, BorderLayout.SOUTH);

        setSize(ScreenUtil.getScaledSize(Config.UI_SCALE_WIDTH, Config.UI_SCALE_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Registers all of the listeners for the front panel. Will be called after
     * startup and any reset operation.
     */
    public void registerListeners() {
        computer.processor.addListener(() -> {
            updateIndicators();
            updateTextInputs();
        });
    }

    /**
     * Registers all action listeners for the front panel. Will be called once at
     * startup.
     */
    public void registerActionListeners() {

        // Configure load ROM from menu bar button
        menu.fileLoadROM.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                ROM rom = new ROM(fileChooser.getSelectedFile());
                computer.loadROM(rom);
                JOptionPane.showMessageDialog(this, "ROM loaded successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menu.actionReset.addActionListener(e -> {
            computer.reset();
            registerListeners();
            JOptionPane.showMessageDialog(this, "Minicomputer reset successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Launch the console window
        menu.viewConsole.addActionListener(e -> {
            consoleWindow.setVisible(true);
        });

        controlPanel.stepForwardButton.addActionListener(e -> {
            computer.step();
            String input = computer.displaySet;
            input = input.replaceAll("\\s", "");
            int num = Integer.parseInt(input);
            if(num > 0){
                displayAddresses = displayAddresses + computer.displaySet + "\n"; 
            }
            indicatorPanel.cacheGroup.cache.textArea.setText(displayAddresses);

            if(computer.processor.loadNum > 4 && computer.processor.loadNum < 7 && !val){
                indicatorPanel.cacheGroup.printer.textArea.setText("Ener 20 Nums");
            }
            if(computer.processor.loadNum > 7 && found == 0 && !fnd){
                indicatorPanel.cacheGroup.printer.textArea.setText("Ener a num");
            }
            if(computer.processor.loadNum > 7 && found != 0){
                indicatorPanel.cacheGroup.printer.textArea.setText(String.valueOf(found));
            }
        });

        controlPanel.runButton.addActionListener(e -> {
            computer.run();
            if(computer.processor.loadNum > 4 && computer.processor.loadNum < 7 ){
                indicatorPanel.cacheGroup.printer.textArea.setText("Ener 20 Nums");
            }
            if(computer.processor.loadNum > 7){
                indicatorPanel.cacheGroup.printer.textArea.setText("Ener a num");
            }
        });

        controlPanel.loadButton.addActionListener(e -> {
            
            computer.processor.MAR = inputPanel.miscRegisterPanel.mar.get();

            // Load from MAR to MBR
            if (computer.processor.MAR != 0) {
                computer.processor.MBR = computer.memory.read(computer.processor.MAR);
                inputPanel.miscRegisterPanel.mbr.set(computer.processor.MBR);
                updateIndicators();
            } else {
                computer.processor.MBR = 0;
                updateIndicators();
            }

        });

        controlPanel.storeButton.addActionListener(e -> {
            computer.processor.MAR = inputPanel.miscRegisterPanel.mar.get();
            computer.processor.MBR = inputPanel.miscRegisterPanel.mbr.get();

            // Load MBR to memory at MAR address
            computer.memory.write(computer.processor.MAR, computer.processor.MBR);
            updateIndicators();

        });

        indicatorPanel.IPLButton.addActionListener(e -> {
            System.out.println("LEmon");
            computer.reset();
            indicatorPanel.cacheGroup.cache.textArea.setText("000");
            indicatorPanel.cacheGroup.printer.textArea.setText("000");
            indicatorPanel.cacheGroup.console.textArea.setText("000");
            for (int i = 0; i < values.length; i++) {
                values[i] = 0; // Assigning zero to each element
            }
            fnd = false;
            val = false;
            found = 0;
            registerListeners();
            JOptionPane.showMessageDialog(this, "Minicomputer reset successfully!", "Success",
            JOptionPane.INFORMATION_MESSAGE);


            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                ROM rom = new ROM(fileChooser.getSelectedFile());
                computer.loadROM(rom);
                JOptionPane.showMessageDialog(this, "ROM loaded successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            }

        });

        indicatorPanel.cacheGroup.submitCacheButton.addActionListener(e -> {
            String address = indicatorPanel.cacheGroup.console.get();
            String displayAddresses = computer.cache.displayCacheAddresses();
            // indicatorPanel.cacheGroup.cache.textArea.setText(displayAddresses);
            // indicatorPanel.cacheGroup.printer.textArea.setText(addressContent);
            System.out.println(address);
            if(computer.processor.loadNum > 4 && !val){
                String[] parts = address.split("\\s+"); // Split by one or more spaces
                for (int i = 0; i < parts.length; i++) {    
                    try {
                        values[i] = Integer.parseInt(parts[i]);
                    } catch (NumberFormatException en) {
                    System.err.println(parts[i]);
                    }
                }
                val = true;
                indicatorPanel.cacheGroup.printer.textArea.setText("");
                indicatorPanel.cacheGroup.console.textArea.setText("");
            }
            else if(computer.processor.loadNum > 7){
                int find=0;
                int min=90000;
                try {
                    find = Integer.parseInt(address);
                } catch (NumberFormatException en) {
                System.err.println(address);
                }
                for(int i=0;i<values.length;i++){
                    if(values[i] > 0 && values[i]-find < min){
                        found = values[i];
                        min = values[i]-find;
                    }
                    System.out.println(found);
                }
                fnd = true;
                indicatorPanel.cacheGroup.printer.textArea.setText("");
                indicatorPanel.cacheGroup.console.textArea.setText("");
            }
        });
    }

    /**
     * Registers all key listeners for the front panel. Will be called once at
     * startup.
     */
    public void registerKeyListeners() {
        inputPanel.miscRegisterPanel.pc.textField.addActionListener(e -> {
            computer.processor.PC = inputPanel.miscRegisterPanel.pc.get();
            updateIndicators();
        });

        inputPanel.miscRegisterPanel.mar.textField.addActionListener(e -> {
            computer.processor.MAR = inputPanel.miscRegisterPanel.mar.get();

            updateIndicators();
        });

        inputPanel.miscRegisterPanel.mbr.textField.addActionListener(e -> {
            computer.processor.MBR = inputPanel.miscRegisterPanel.mbr.get();
            updateIndicators();
        });

        inputPanel.generalRegisterPanel.r0.textField.addActionListener(e -> {
            computer.processor.R0 = inputPanel.generalRegisterPanel.r0.get();
            updateIndicators();
        });

        inputPanel.generalRegisterPanel.r1.textField.addActionListener(e -> {
            computer.processor.R1 = inputPanel.generalRegisterPanel.r1.get();
            updateIndicators();
        });

        inputPanel.generalRegisterPanel.r2.textField.addActionListener(e -> {
            computer.processor.R2 = inputPanel.generalRegisterPanel.r2.get();
            updateIndicators();
        });

        inputPanel.generalRegisterPanel.r3.textField.addActionListener(e -> {
            computer.processor.R3 = inputPanel.generalRegisterPanel.r3.get();
            updateIndicators();
        });

        inputPanel.indexRegisterPanel.x1.textField.addActionListener(e -> {
            computer.processor.X1 = inputPanel.indexRegisterPanel.x1.get();
            updateIndicators();
        });

        inputPanel.indexRegisterPanel.x2.textField.addActionListener(e -> {
            computer.processor.X2 = inputPanel.indexRegisterPanel.x2.get();
            updateIndicators();
        });

        inputPanel.indexRegisterPanel.x3.textField.addActionListener(e -> {
            computer.processor.X3 = inputPanel.indexRegisterPanel.x3.get();
            updateIndicators();
        });

    }

    public void updateIndicators() {
        indicatorPanel.r0Group.set(computer.processor.R0);
        indicatorPanel.r1Group.set(computer.processor.R1);
        indicatorPanel.r2Group.set(computer.processor.R2);
        indicatorPanel.r3Group.set(computer.processor.R3);
        indicatorPanel.x1Group.set(computer.processor.X1);
        indicatorPanel.x2Group.set(computer.processor.X2);
        indicatorPanel.x3Group.set(computer.processor.X3);
        indicatorPanel.mbrGroup.set(computer.processor.MBR);
        indicatorPanel.marGroup.set(computer.processor.MAR);
        indicatorPanel.pcGroup.set(computer.processor.PC);
        indicatorPanel.irGroup.set(computer.processor.getIR());
        indicatorPanel.mfrGroup.set(computer.processor.getMFR());
        indicatorPanel.ccGroup.set(computer.processor.getCC());
    }

    public void updateTextInputs() {
        // Update misc regsiter group text inputs
        inputPanel.miscRegisterPanel.pc.set(computer.processor.PC);
        inputPanel.miscRegisterPanel.mar.set(computer.processor.MAR);
        inputPanel.miscRegisterPanel.mbr.set(computer.processor.MBR);

        // Update general purpose register text inputs
        inputPanel.generalRegisterPanel.r0.set(computer.processor.R0);
        inputPanel.generalRegisterPanel.r1.set(computer.processor.R1);
        inputPanel.generalRegisterPanel.r2.set(computer.processor.R2);
        inputPanel.generalRegisterPanel.r3.set(computer.processor.R3);

        // Update index register text inputs
        inputPanel.indexRegisterPanel.x1.set(computer.processor.X1);
        inputPanel.indexRegisterPanel.x2.set(computer.processor.X2);
        inputPanel.indexRegisterPanel.x3.set(computer.processor.X3);
    }

        // Custom DocumentListener implementation for demonstration
    // private static class CustomDocumentListener implements javax.swing.event.DocumentListener {
    //     @Override
    //     public void insertUpdate(DocumentEvent e) {
    //         System.out.println("Inserted text: " + e.getDocument().getText(0, e.getDocument().getLength()));
    //     }
    
    //     @Override
    //     public void removeUpdate(DocumentEvent e) {
    //         System.out.println("Removed text: " + e.getDocument().getText(0, e.getDocument().getLength()));
    //     }
    
    //     @Override
    //     public void changedUpdate(DocumentEvent e) {
    //         // Not used for plain text components
    //         System.out.println("Updated text: "  + e.getDocument().getText(0, e.getDocument().getLength()));
    //     }
    // }
}
