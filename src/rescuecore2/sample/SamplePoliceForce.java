package rescuecore2.sample;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import rescuecore2.worldmodel.EntityID;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityType;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.messages.AKMove;
import rescuecore2.standard.messages.AKClear;

/**
   A sample police force agent.
 */
public class SamplePoliceForce extends AbstractSampleAgent {
    /**
       Default constructor.
    */
    public SamplePoliceForce() {
        ignoreBlockedRoads = false;
    }

    @Override
    protected void postConnect() {
        super.postConnect();
        world.indexClass(StandardEntityType.ROAD);
    }

    @Override
    protected void think(int time, List<EntityID> changed) {
        // Am I on a blocked road?
        StandardEntity location = location();
        if (location instanceof Road && ((Road)location).getBlock() > 0) {
            AKClear clear = new AKClear(entityID, location.getID(), time);
            System.out.println(me() + " clearing road: " + clear);
            send(clear);
            return;
        }
        // Plan a path to a blocked road
        List<EntityID> path = breadthFirstSearch(location(), getBlockedRoads());
        if (path != null) {
            AKMove move = new AKMove(entityID, path, time);
            System.out.println(me() + " moving to road: " + move);
            send(move);
            return;
        }
        System.out.println(me() + " couldn't plan a path to a blocked road.");
        send(new AKMove(entityID, randomWalk(), time));
    }

    @Override
    protected int[] getRequestedEntityIDs() {
        return new int[] {StandardEntityType.POLICE_FORCE.getID()
        };
    }

    private List<Road> getBlockedRoads() {
        Collection<StandardEntity> e = world.getEntitiesOfType(StandardEntityType.ROAD);
        List<Road> result = new ArrayList<Road>();
        for (StandardEntity next : e) {
            if (next instanceof Road) {
                Road r = (Road)next;
                if (r.getBlock() > 0) {
                    result.add(r);
                }
            }
        }
        return result;
    }
}