package kernel;

import java.util.Collection;

import rescuecore2.worldmodel.WorldModel;
import rescuecore2.worldmodel.Entity;
import rescuecore2.messages.Message;

/**
   A model of communication. Implementers are responsible for determining what communications are received by each agent in the world.
   @param <S> The subclass of WorldModel that this model understands.
   @param <T> The subclass of Entity that this model understands.
 */
public interface CommunicationModel<T extends Entity, S extends WorldModel<T>> extends WorldModelAware<T, S> {
    /**
       Process a set of agent commands and work out what communications a particular agent can hear.
       @param agent The agent-controlled entity.
       @param agentCommands The set of all agent commands last timestep.
       @return A collection of communication update messages to be sent to the agent.
     */
    Collection<Message> process(T agent, Collection<Message> agentCommands);
}