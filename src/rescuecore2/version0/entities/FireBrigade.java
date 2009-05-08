package rescuecore2.version0.entities;

import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.version0.entities.properties.IntProperty;
import rescuecore2.version0.entities.properties.PropertyType;

/**
   The FireBrigade object.
 */
public class FireBrigade extends Human {
    private IntProperty water;

    /**
       Construct a FireBrigade object with entirely undefined values.
       @param id The ID of this entity.
    */
    public FireBrigade(EntityID id) {
        super(id, EntityConstants.FIRE_BRIGADE);
        water = new IntProperty(PropertyType.WATER_QUANTITY);
        addProperties(water);
    }

    @Override
    protected Entity copyImpl() {
        return new FireBrigade(getID());
    }
}