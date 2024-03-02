package core.func;

public interface TransferFunction {

    /**
     * Evaluates the supplied function on the given word, and returns
     * the address that the minicomputer should jump to
     * 
     * @param word The word to evaluate
     * @return The address to jump to
     */
    public char evaluate(char word);

}
