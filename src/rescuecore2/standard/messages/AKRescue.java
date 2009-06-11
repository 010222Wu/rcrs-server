package rescuecore2.standard.messages;

import rescuecore2.worldmodel.EntityID;
import rescuecore2.messages.EntityIDComponent;
import rescuecore2.messages.AbstractCommand;

/**
   An agent Rescue command.
 */
public class AKRescue extends AbstractCommand {
    private EntityIDComponent target;

    /**
       Create an empty AKRescue command.
     */
    AKRescue() {
        super("AK_RESCUE", MessageConstants.AK_RESCUE);
        init();
    }

    /**
       Construct an AKRescue command.
       @param agent The ID of the agent issuing the command.
       @param target The id of the entity to rescue.
       @param time The time the command was issued.
     */
    public AKRescue(EntityID agent, EntityID target, int time) {
        super("AK_RESCUE", MessageConstants.AK_RESCUE, agent, time);
        init();
        this.target.setValue(target);
    }

    /**
       Get the desired target.
       @return The target ID.
     */
    public EntityID getTarget() {
        return target.getValue();
    }

    private void init() {
        target = new EntityIDComponent("Target");
        addMessageComponent(target);
    }
}