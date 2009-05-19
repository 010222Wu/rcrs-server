package rescuecore2.connection;

import rescuecore2.messages.Message;
import rescuecore2.messages.MessageFactory;

import java.io.IOException;
import java.util.Collection;

/**
   Top-level interface for a communication interface between simulator components.
 */
public interface Connection {
    /**
       Send a message across this connection.
       @param msg The message to send.
       @throws IOException If the message cannot be sent.
       @throws ConnectionException If the connection has not been started up or has been shut down.
     */
    void sendMessage(Message msg) throws IOException, ConnectionException;

    /**
       Send a set of messages across this connection.
       @param messages The messages to send.
       @throws IOException If any of the messages cannot be sent.
       @throws ConnectionException If the connection has not been started up or has been shut down.
     */
    void sendMessages(Collection<? extends Message> messages) throws IOException, ConnectionException;

    /**
       Add a ConnectionListener. This listener will be notified when messages arrive on this connection.
       @param l The listener to add.
     */
    void addConnectionListener(ConnectionListener l);

    /**
       Remove a ConnectionListener.
       @param l The listener to remove.
     */
    void removeConnectionListener(ConnectionListener l);

    /**
       Start this connection up. Connections are not available for use until started.
     */
    void startup();

    /**
       Find out if this connection is still alive. The connection is alive if it has been started, not stopped, and no fatal errors have occurred.
       @return True if this connection is alive and is still able to send/receive messages, false otherwise.
     */
    boolean isAlive();

    /**
       Shut this connection down.
     */
    void shutdown();

    /**
       Set the factory for interpreting messages that come in on this connection.
       @param factory The new factory to use.
    */
    void setMessageFactory(MessageFactory factory);

    /**
       Turn byte-level logging on or off.
       @param enabled Whether to enable byte-level logging.
    */
    void setLogBytes(boolean enabled);
}