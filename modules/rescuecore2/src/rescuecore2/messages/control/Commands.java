package rescuecore2.messages.control;

import rescuecore2.messages.Control;
import rescuecore2.messages.Command;
import rescuecore2.messages.AbstractMessage;
import rescuecore2.messages.IntComponent;
import rescuecore2.messages.CommandListComponent;

import java.util.Collection;
import java.util.List;

import java.io.InputStream;
import java.io.IOException;

/**
   A message containing a list of agent commands. This is sent from the kernel to all simulators and viewers.
 */
public class Commands extends AbstractMessage implements Control {
    private IntComponent time;
    private CommandListComponent commands;

    /**
       A Commands message that populates its data from a stream.
       @param in The InputStream to read.
       @throws IOException If there is a problem reading the stream.
     */
    public Commands(InputStream in) throws IOException {
        this();
        read(in);
    }

    /**
       A populated Commands message.
       @param time The timestep of the simulation.
       @param commands All AgentCommands.
     */
    public Commands(int time, Collection<? extends Command> commands) {
        this();
        this.time.setValue(time);
        this.commands.setCommands(commands);
    }

    private Commands() {
        super("Commands", ControlMessageConstants.COMMANDS);
        time = new IntComponent("Time");
        commands = new CommandListComponent("Commands");
        addMessageComponent(time);
        addMessageComponent(commands);
    }

    /**
       Get the time of the simulation.
       @return The simulation time.
     */
    public int getTime() {
        return time.getValue();
    }

    /**
       Get the list of agent commands.
       @return The agent commands.
     */
    public List<Command> getCommands() {
        return commands.getCommands();
    }
}