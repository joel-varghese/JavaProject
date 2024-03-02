package core.func;

public interface LoadFunction {

    /**
     * Evaluates the supplied logic function on the given inputs, and returns
     * the resultant that should be returned by the load operation
     * 
     * @param value The value read from memory
     * @return The result
     */
    public void evaluate(char value);
}
