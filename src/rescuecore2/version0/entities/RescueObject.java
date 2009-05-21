package rescuecore2.version0.entities;

import rescuecore2.worldmodel.AbstractEntity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.worldmodel.WorldModel;
import rescuecore2.misc.Pair;

/**
   Abstract base class for all version0 entities.
 */
public abstract class RescueObject extends AbstractEntity<RescueEntityType> {
    /**
       Construct a RescueObject with entirely undefined property values.
       @param id The ID of this entity.
       @param type The type ID of this entity.
     */
    protected RescueObject(EntityID id, RescueEntityType type) {
        super(id, type);
    }

    /**
       Get the location of this rescue object.
       @param world The world model to look up for entity references.
       @return The coordinates of this entity, or null if the location cannot be determined.
     */
    public Pair<Integer, Integer> getLocation(WorldModel<? extends RescueObject> world) {
        return null;
    }
}