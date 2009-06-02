package rescuecore2.messages.control;

import java.util.Collection;
import java.util.List;

import rescuecore2.messages.Control;
import rescuecore2.messages.AbstractMessage;
import rescuecore2.messages.IntComponent;
import rescuecore2.messages.EntityListComponent;

import rescuecore2.version0.entities.RescueEntity;
import rescuecore2.version0.entities.RescueEntityType;
import rescuecore2.version0.entities.RescueEntityFactory;

/**
   A message for signalling a successful connection to the kernel.
 */
public class KSConnectOK extends AbstractMessage implements Control {
    private IntComponent simulatorID;
    private EntityListComponent<RescueEntityType, RescueEntity> world;

    /**
       An empty KSConnectOK message.
     */
    public KSConnectOK() {
        super("KS_CONNECT_OK", ControlMessageConstants.KS_CONNECT_OK);
        simulatorID = new IntComponent("Simulator ID");
        world = new EntityListComponent<RescueEntityType, RescueEntity>("Entities", RescueEntityFactory.INSTANCE);
        addMessageComponent(simulatorID);
        addMessageComponent(world);
    }

    /**
       A populated KSConnectOK message.
       @param simulatorID The ID of the simulator that has successfully connected.
       @param allEntities All Entities in the world.
     */
    public KSConnectOK(int simulatorID, Collection<RescueEntity> allEntities) {
        this();
        this.simulatorID.setValue(simulatorID);
        this.world.setEntities(allEntities);
    }

    /**
       Get the simulator ID for this message.
       @return The simulator ID.
     */
    public int getSimulatorID() {
        return simulatorID.getValue();
    }

    /**
       Get the entity list.
       @return All entities in the world.
     */
    public List<RescueEntity> getEntities() {
        return world.getEntities();
    }
}