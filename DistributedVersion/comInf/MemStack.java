package comInf;

/**
 *    Parametric stack derived from a parametric memory.
 *    Errors are reported.
 *    Minor additions to this class where made by Inês Justo and Miguel Lopes.
 *
 *    @author António Rui De Oliveira E Silva Borges
 *    @param <R> data type of stored objects
 */

public class MemStack<R> extends MemObject<R>
{
    /**
     *   Pointer to the first empty location.
     */

    private int stackPnt;

    /**
     *   Stack instantiation.
     *   The instantiation only takes place if the memory exists.
     *   Otherwise, an error is reported.
     *
     *     @param storage memory to be used
     *     @throws MemException when the memory does not exist
     */

    public MemStack (R [] storage) throws MemException
    {
        super (storage);
        stackPnt = 0;
    }

    /**
     *   Stack push.
     *   A parametric object is written into it.
     *   If the stack is full, an error is reported.
     *
     *    @param val parametric object to be written
     *    @throws MemException when the stack is full
     */

    @Override
    public void write (R val) throws MemException
    {
        if (stackPnt < mem.length)
        { mem[stackPnt] = val;
            stackPnt += 1;
        }
        else throw new MemException ("Stack full!");
    }

    /**
     *   Stack pop.
     *   A parametric object is read from it.
     *   If the stack is empty, an error is reported.
     *
     *    @return last parametric object that was written
     *    @throws MemException when the stack is empty
     */

    @Override
    public R read () throws MemException
    {
        if (stackPnt != 0)
        { stackPnt -= 1;
            return mem[stackPnt];
        }
        else throw new MemException ("Stack empty!");
    }

    /* ******************************************** NEW METHODS ***************************************************** */

    /**
     *   Gets the pointer to the first empty location.
     *
     *    @return Pointer to the first empty location.
     */

    public int getPointer(){
        return stackPnt;
    }
}