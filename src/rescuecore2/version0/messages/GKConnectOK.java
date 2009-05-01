package rescuecore2.version0.messages;

import java.util.List;

import rescuecore2.worldmodel.Entity;
import rescuecore2.messages.AbstractMessage;

/**
   A message for signalling a successful connection to the GIS.
 */
public class GKConnectOK extends AbstractMessage {
    private EntityListComponent world;

    /**
       A GKConnectOK with no entities.
     */
    public GKConnectOK() {
        super("GK_CONNECT_OK", MessageConstants.GK_CONNECT_OK);
        world = new EntityListComponent("Entities");
        addMessageComponent(world);
    }

    /**
       A GKConnectOK with a specified entity list.
       @param entities The entities to send.
     */
    public GKConnectOK(List<Entity> entities) {
        this();
        world.setEntities(entities);
    }

    /**
       Get the entity list.
       @return All entities.
     */
    public List<Entity> getEntities() {
        return world.getEntities();
    }
}