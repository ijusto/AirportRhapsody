package comInf;

/**
 *    Parametric FIFO derived from a parametric memory.
 *    Errors are reported.
 *    Minor additions to this class where made by Inês Justo and Miguel Lopes.
 *
 *    @author António Rui De Oliveira E Silva Borges
 *    @param <R> data type of stored objects
 */

public class MemFIFO<R> extends MemObject<R>
{
    /**
     *   Pointer to the first empty location.
     */

    private int inPnt;

    /**
     *   Pointer to the first occupied location.
     */

    private int outPnt;

    /**
     *   Signaling FIFO empty state.
     */

    private boolean empty;

    /**
     *   Number of parametric objects written into the FIFO.
     */

    private int nObjects;

    /**
     *   FIFO instantiation.
     *   The instantiation only takes place if the memory exists.
     *   Otherwise, an error is reported.
     *
     *     @param storage memory to be used
     *     @throws MemException when the memory does not exist
     */

    public MemFIFO (R [] storage) throws MemException
    {
        super (storage);
        inPnt = outPnt = 0;
        empty = true;
        nObjects = 0;
    }

    /**
     *   FIFO insertion.
     *   A parametric object is written into it.
     *   If the FIFO is full, an error is reported.
     *
     *    @param val parametric object to be written
     *    @throws MemException when the FIFO is full
     */

    @Override
    public void write (R val) throws MemException
    {
        if ((inPnt != outPnt) || empty)
        { mem[inPnt] = val;
            nObjects += 1;
            inPnt = (inPnt + 1) % mem.length;
            empty = false;
        }
        else throw new MemException ("Fifo full!");
    }

    /**
     *   FIFO retrieval.
     *   A parametric object is read from it.
     *   If the FIFO is empty, an error is reported.
     *
     *    @return first parametric object that was written
     *    @throws MemException when the FIFO is empty
     */

    @Override
    public R read () throws MemException
    {
        R val;

        if (!empty)
        { val = mem[outPnt];
            nObjects -= 1;
            outPnt = (outPnt + 1) % mem.length;
            empty = (inPnt == outPnt);
        }
        else throw new MemException ("Fifo empty!");
        return val;
    }

    /* ******************************************** NEW METHODS ***************************************************** */
    /**
     *   Get the number of parametric objects that were written into the FIFO.
     *
     *    @return number of parametric objects that was written.
     */

    public int getNObjects(){
        return nObjects;
    }

    /**
     *   Check if FIFO is empty.
     *
     *    @return <li> true, if the FIFO is empty </li>
     *            <li> false, otherwise </li>
     */

    public boolean isEmpty(){
        return empty;
    }
}