package kernel.standard;

import kernel.Kernel;
import kernel.AgentProxy;
import kernel.CommandFilter;

import rescuecore2.config.Config;
import rescuecore2.messages.Command;

import java.util.Collection;

/**
   A CommandFilter that ignores agent commands for some number of timesteps.
 */
public class FrozenAgentsCommandFilter implements CommandFilter {
    private static final String IGNORE_AGENT_COMMANDS_KEY = "kernel.agents.ignoreuntil";

    private int freezeTime;
    private Kernel kernel;

    @Override
    public void initialise(Config config, Kernel k) {
        freezeTime = config.getIntValue(IGNORE_AGENT_COMMANDS_KEY, 0);
        this.kernel = k;
    }

    @Override
    public void filter(Collection<Command> commands, AgentProxy agent) {
        int time = kernel.getTime();
        if (time < freezeTime) {
            commands.clear();
        }
    }
}