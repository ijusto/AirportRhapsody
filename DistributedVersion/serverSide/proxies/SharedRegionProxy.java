package serverSide.proxies;

import comInf.Message;
import comInf.MessageException;

public interface SharedRegionProxy {
    /**
     * Process message and decide which shared region
     * function to call. Replies to the message accordingly.
     * @param msg received messages
     * @return reply
     */
    public Message processAndReply(Message msg) throws MessageException;

}
