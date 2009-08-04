package kernel;

import rescuecore2.connection.Connection;
import rescuecore2.messages.Command;
import rescuecore2.messages.control.Update;
import rescuecore2.messages.control.Commands;
import rescuecore2.worldmodel.Entity;

import java.util.Collection;
import java.util.Collections;

/**
   This class is the kernel interface to a viewer.
 */
public class Viewer extends AbstractComponent {
    /**
       Construct a viewer.
       @param c The connection to the viewer.
     */
    public Viewer(Connection c) {
        super(c);
    }

    /**
       Send an update message to this viewer.
       @param time The simulation time.
       @param updates The updated entities.
    */
    public void sendUpdate(int time, Collection<? extends Entity> updates) {
        send(Collections.singleton(new Update(time, updates)));
    }

    /**
       Send a set of agent commands to this viewer.
       @param time The current time.
       @param commands The agent commands to send.
     */
    public void sendAgentCommands(int time, Collection<? extends Command> commands) {
        send(Collections.singleton(new Commands(time, commands)));
    }

    @Override
    public String toString() {
        return "Viewer: " + getConnection().toString();
    }
}