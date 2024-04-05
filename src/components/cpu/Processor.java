package components.cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import components.Memory;
import components.io.IOBus;
import core.Condition;
import core.Fault;
import core.GeneralRegister;
import core.IndexRegister;
import core.Instruction;
import core.func.ArithmeticFunction;
import core.func.IOFunction;
import core.func.LoadFunction;
import core.func.LogicFunction;
import core.func.StoreFunction;
import core.func.TransferFunction;
import ui.listeners.HardwareListener;
import util.FormatUtils;

public class Processor {

    private static final Logger LOGGER = Logger.getLogger(Processor.class.getName());

    // General Purpose Registers
    public char R0 = 0, R1 = 0, R2 = 0, R3 = 0;

    // Index Registers
    public char X1 = 0, X2 = 0, X3 = 0;

    // Memory Address, Memory Buffer Registers
    public char MAR = 0, MBR = 0;

    // Program Counter
    public char PC = 0;

    // Instruction Register
    private char IR = 0;

    // Machine Fault Register
    private byte MFR = 0;

    // Condition Code
    private byte CC = 0;

    // Memory Object
    private Memory memory;

    // IO Bus
    private IOBus ioBus;

    // Whether or not the minicomputer is halted
    private boolean halted = false;

    // Whether or not to increment the PC after executing an instruction
    private boolean incrementPC = true;

    // Store the address
    public char disAddress;

    // Load Number
    public int loadNum;

    // List of listeners
    private List<HardwareListener> listeners = new ArrayList<>();

    public Processor(Memory memory, IOBus ioBus) {
                this.memory = memory;
        this.ioBus = ioBus;
        this.loadNum = 0;
    }

    /**
     * Attaches a HardwareListener to the processor.
     * 
     * @param listener The listener to add.
     */
    public void addListener(HardwareListener listener) {
        listeners.add(listener);
    }

    /**
     * Starts the processor, executing instructions until halted.
     */
    public void run() {
        halted = false;

        while (!halted) {
            step();
        }

        LOGGER.info("Run has completed");
    }

    /**
     * Executes a single instruction.
     */
    public void step() {
        incrementPC = true;

        IR = memory.read(PC);

        execute(new Instruction(IR));

        if (incrementPC)
            PC++;

        notifyListeners();
    }

    /**
     * Executes the given instruction.
     * 
     * @param i The instruction to execute.
     */
    public void execute(Instruction i) {
        LOGGER.info("Executing instruction " + i.toString() + " at PC " + FormatUtils.toHexString(PC));
        // System.out.println("WE AT "+i.getOpcode());
        if(i.getOpcode().toString() == "LDR"){

        }
        switch (i.getOpcode()) {
            case AIR:
                arithmetic(i, (char value) -> {
                    if (i.getImmed() == 0)
                        return value;

                    if (value == 0)
                        return i.getImmed();

                    return ALU.add(value, i.getImmed());
                });
                break;
            case AMR:
                arithmetic(i, (char value) -> {
                    return ALU.add(value, memory.read(effectiveAddress(i)));
                });
                break;
            case AND:
                logic(i, (char rx, char ry) -> {
                    return (char) (rx & ry);
                });
                break;
            case CHK:
                io(i, (char value) -> {
                    return ioBus.check((short) i.getImmed());
                });
                break;
            case DVD:
                arithmetic(i, (char rx, char ry) -> {
                    if (ry == 0) {
                        CC = Condition.set(Condition.DIVZERO, CC);
                        return 0;
                    }

                    return rx;
                });
                break;
            case HLT:
                halt();
                break;
            case IN:
                io(i, (char value) -> {
                    return ioBus.read((short) i.getImmed());
                });
                break;
            case JCC:
                jump(i, (char value) -> {
                    Fault check = Fault.fromId(i.getGPR().id);

                    if (Fault.isSet(check, MFR)) {
                        return effectiveAddress(i);
                    } else {
                        return PC++;
                    }
                });
                break;
            case JGE:
                jump(i, (char value) -> {
                    if (value > 0) {
                        return effectiveAddress(i);
                    } else {
                        return PC++;
                    }
                });
                break;
            case JMA:
                jump(i, (char value) -> effectiveAddress(i));
                break;
            case JNE:
                jump(i, (char value) -> {
                    if (value != 0) {
                        return effectiveAddress(i);
                    } else {
                        return PC++;
                    }
                });
                break;
            case JSR:
                jump(i, (char value) -> {
                    this.R3 = value++;
                    return effectiveAddress(i);
                });
                break;
            case JZ:
                jump(i, (char value) -> {
                    if (value == 0) {
                        return effectiveAddress(i);
                    } else {
                        return PC++;
                    }
                });
                break;
            case LDA:
                load(i.getGPR(), effectiveAddress(i));
                break;
            case LDR:
                load(effectiveAddress(i), (char value) -> load(i.getGPR(), value));
                break;
            case LDX:
                load(effectiveAddress(i), (char value) -> load(i.getIXR(), value));
                break;
            case MLT:
                arithmetic(i, (char rx, char ry) -> {
                    return rx;
                });
                break;
            case NOT:
                logic(i, (char rx, char ry) -> {
                    return (char) ~rx;
                });
                break;
            case ORR:
                logic(i, (char rx, char ry) -> {
                    return (char) (rx | ry);
                });
                break;
            case OUT:
                io(i, (char value) -> {
                    ioBus.write((short) i.getImmed(), value);
                    return value;
                });
                break;
            case RFS:
                jump(i, (char value) -> {
                    R0 = i.getImmed();
                    return R3;
                });
                break;
            case RRC:
                break;
            case SIR:
                arithmetic(i, (char value) -> {
                    if (i.getImmed() == 0)
                        return value;

                    if (value == 0)
                        return i.getImmed();

                    return ALU.subtract(value, i.getImmed());
                });
                break;
            case SMR:
                arithmetic(i, (char value) -> {
                    return ALU.subtract(value, memory.read(effectiveAddress(i)));
                });
                break;
            case SOB:
                jump(i, (char value) -> {
                    load(i.getGPR(), value--);
                    if (value > 0) {
                        return effectiveAddress(i);
                    } else {
                        return PC++;
                    }
                });
                break;
            case SRC:
                break;
            case STR:
                store(effectiveAddress(i), () -> getValue(i.getGPR()));
                break;
            case STX:
                store(effectiveAddress(i), () -> getValue(i.getIXR()));
                break;
            case TRP:
                halt();
                break;
            case TRR:
                logic(i, (char rx, char ry) -> {
                    if (rx == ry) {
                        CC = 1;
                    } else {
                        CC = 0;
                    }

                    return rx;
                });
                break;
            default:
                LOGGER.severe("An invalid opcode was encountered: " + i.getOpcode());
                halt();
                break;

        }
    }

    /**
     * Given an instruction, return the effective address stored within the word.
     * 
     * @param i The instruction to to evaluate
     * @return The effective address from the instruction
     */
    public char effectiveAddress(Instruction i) {
        // TODO: No indexing will generate a severe log message, even though in this
        // context it is not an error.
        if (i.isIndirectAddressing()) {
            LOGGER.fine("Evaluating effective address with indirect addressing");
            return memory.read(ALU.add(getValue(i.getIXR()), i.getAddress()));
        } else {
            LOGGER.fine("Evaluating effective address without indirect addressing");
            return ALU.add(getValue(i.getIXR()), i.getAddress());
        }
    }

    public char getIR() {
        return IR;
    }

    public char getMFR() {
        return (char) MFR;
    }

    public char getCC() {
        return (char) CC;
    }

    public boolean isHalted() {
        return halted;
    }

    protected void halt() {
        LOGGER.info("Halting minicomputer");
        halted = true;
    }

    /**
     * Loads the given value into the specified general purpose register.
     * 
     * @param r     The register to set the value of.
     * @param value The value to set the register to.
     */
    protected void load(GeneralRegister r, char value) {
        switch (r) {
            case GPR0:
                R0 = value;
                break;
            case GPR1:
                R1 = value;
                break;
            case GPR2:
                R2 = value;
                break;
            case GPR3:
                R3 = value;
                break;
            default:
                LOGGER.severe("Invalid general purpose register " + r);
                break;
        }
    }

    /**
     * Loads the given value into the specified index register.
     * 
     * @param r     The register to set the value of.
     * @param value The value to set the register to.
     */
    protected void load(IndexRegister r, char value) {
        switch (r) {
            case IX1:
                X1 = value;
                break;
            case IX2:
                X2 = value;
                break;
            case IX3:
                X3 = value;
                break;
            default:
                LOGGER.severe("Invalid index register " + r);
                break;
        }
    }

    /**
     * Retrieves the value stored at the given address and uses the result to run
     * the given load function.
     * 
     * @param r The register to store the result in.
     * @param f The load function to evaluate.
     */
    protected void load(char address, LoadFunction f) {
        MBR = memory.read(address);
        MAR = address;
        disAddress = address;
        loadNum++;
        f.evaluate(MBR);
    }

    /**
     * Evaluates the given store function, which returns the value to be written to
     * memory, and stores the result at the given address.
     * 
     * @param address The address to store the result at.
     * @param f       The store function to evaluate.
     */
    protected void store(char address, StoreFunction f) {
        MBR = f.evaluate();
        MAR = address;
        memory.write(MAR, MBR);
    }

    /**
     * Evaluates the given transfer function using the value of the supplied
     * general purpose register. The return value of the transfer function is
     * used as the address to jump to. A jump will not automatically increment the
     * program counter, and must be done by the transfer function.
     * 
     * @param i The instruction to read from
     * @param f The transfer function to use to compute the address
     */
    protected void jump(Instruction i, TransferFunction f) {
        PC = f.evaluate(getValue(i.getGPR()));
        skipIncrement();
    }

    /**
     * Evaluates the given arithmetic function using the value stored in the RX and
     * RY registers as specified by the instruction. The result of the arithmetic
     * function is stored in the RX register.
     * 
     * @param i The instruction to read from
     * @param f The arithmetic function to use to compute the result
     */
    protected void arithmetic(Instruction i, ArithmeticFunction f) {
        load(i.getGPR(), f.evaluate(getValue(i.getGPR())));
    }

    /**
     * Evaluates the given arithmetic function using the value stored in the RX and
     * RY registers as specified by the instruction. The result of the arithmetic
     * function is stored in the RX register.
     * 
     * @param i The instruction to read from
     * @param f The arithmetic function to use to compute the result
     */
    protected void arithmetic(Instruction i, LogicFunction f) {
        load(i.getGPR(), f.evaluate(getValue(i.getGPR()), getValue(i.getGPR())));
    }

    /**
     * Evaluates the given logic function using the value stored in RX and RY as
     * specified by the instruction. The result of the logic function is stored in
     * the RX register.
     * 
     * @param i The instruction to read from
     * @param f The logic function to use to compute the result
     */
    protected void logic(Instruction i, LogicFunction f) {
        load(i.getRX(), f.evaluate(getValue(i.getRX()), getValue(i.getRY())));
    }

    /**
     * Evaluates the given IO function using the value stored in the specified
     * general purpose register.
     * 
     * @param i The instruction to read from
     * @param f The IO function to use to compute the result
     */
    protected void io(Instruction i, IOFunction f) {
        load(i.getGPR(), f.evaluate(getValue(i.getGPR())));
    }

    /**
     * Returns the current value of the specified general purpose register.
     * 
     * @param r The register to read from.
     * @return The value of the register.
     */
    private char getValue(GeneralRegister r) {
        switch (r) {
            case GPR0:
                return R0;
            case GPR1:
                return R1;
            case GPR2:
                return R2;
            case GPR3:
                return R3;
            default:
                LOGGER.severe("Invalid register " + r);
                return 0;
        }
    }

    /**
     * Returns the current value of the specified index register.
     * 
     * @param r The register to read from.
     * @return The value of the register.
     */
    private char getValue(IndexRegister r) {
        switch (r) {
            case IX1:
                return X1;
            case IX2:
                return X2;
            case IX3:
                return X3;
            default:
                LOGGER.severe("Invalid register " + r);
                return 0;
        }
    }

    /**
     * Causes the processor to skip incrementing the program counter after the
     * execution of the current instruction.
     */
    private void skipIncrement() {
        LOGGER.fine("Skipping increment of program counter this cycle");
        incrementPC = false;
    }

    /**
     * Notifies all listeners that the state of the processor has changed.
     */
    private void notifyListeners() {
        for (HardwareListener listener : listeners) {
            listener.onUpdate();
        }
    }

}
