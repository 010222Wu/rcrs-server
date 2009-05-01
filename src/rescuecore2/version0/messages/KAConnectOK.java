package rescuecore2.version0.messages;

import java.util.Collection;
import java.util.List;

import rescuecore2.worldmodel.Entity;
import rescuecore2.messages.AbstractMessage;

/**
   A message for signalling a successful connection to the kernel.
 */
public class KAConnectOK extends AbstractMessage {
    private IntComponent tempID;
    private IntComponent agentID;
    private EntityComponent agent;
    private EntityListComponent world;

    public KAConnectOK() {
        super("KA_CONNECT_OK", MessageConstants.KA_CONNECT_OK);
        tempID = new IntComponent("Temp ID");
        agentID = new IntComponent("Agent ID");
        agent = new EntityComponent("Agent");
        world = new EntityListComponent("Entities");
        addMessageComponent(tempID);
        addMessageComponent(agentID);
        addMessageComponent(agent);
        addMessageComponent(world);
    }

    public KAConnectOK(int tempID, int agentID, Entity object, Collection<Entity> allEntities) {
        this();
        this.tempID.setValue(tempID);
        this.agentID.setValue(agentID);
        this.agent.setValue(object);
        this.world.setEntities(allEntities);
    }

    /**
       Get the tempID for this message.
       @return The temp ID.
     */
    public int getTempID() {
        return tempID.getValue();
    }

    /**
       Get the ID of the agent-controlled object.
       @return The agent ID.
     */
    public int getAgentID() {
        return agentID.getValue();
    }

    /**
       Get the agent-controlled entity.
       @return The agent-controlled entity.
     */
    public Entity getAgent() {
        return agent.getValue();
    }

    /**
       Get the entity list.
       @return All entities in the world.
     */
    public List<Entity> getEntities() {
        return world.getEntities();
    }
}