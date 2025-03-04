package commonInfrastructures;

/**
 *    Memory exception.
 *    Definition of an exception for access to a stack or a FIFO in the following conditions:
 *       memory instantiation without assigned storage space;
 *       write operation on a full memory;
 *       read operation on an empty memory.
 *
 *    @author António Rui De Oliveira E Silva Borges
 */

public class MemException extends Exception

{
    /**
     *   Conventional exception instantiation.
     *
     *    @param errorMessage pertaining error message
     */

    public MemException (String errorMessage)
    {
        super (errorMessage);
    }

    /**
     *   Exception instantiation with associated raising cause.
     *
     *    @param errorMessage pertaining error message
     *    @param cause underlying exception that generated it
     */

    public MemException (String errorMessage, Throwable cause)
    {
        super (errorMessage,  cause);
    }
}