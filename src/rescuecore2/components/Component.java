package rescuecore2.components;

import rescuecore2.connection.Connection;
import rescuecore2.connection.ConnectionException;

/**
   Top-level interface for components of the Robocup Rescue simulation. Agents, simulators and viewers are all components.
 */
public interface Component {
    /**
       Connect this component to the kernel.
       @param c The Connection to use to talk to the kernel.
       @param uniqueID A unique ID to use for making the connection.
       @return True iff the connection was successful.
       @throws InterruptedException If this thread is interrupted while connecting.
       @throws ConnectionException If there is a communication error.
     */
    boolean connect(Connection c, int uniqueID) throws InterruptedException, ConnectionException;
}