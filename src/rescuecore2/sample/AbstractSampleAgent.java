package rescuecore2.sample;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import rescuecore2.worldmodel.WorldModel;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.components.AbstractAgent;

import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Human;

/**
   Abstract base class for sample agents.
 */
public abstract class AbstractSampleAgent extends AbstractAgent<StandardEntity> {
    private static final int MESH_SIZE = 10000;
    private static final int RANDOM_WALK_LENGTH = 50;

    /**
       The world model referenced as a StandardWorldModel. Note that this will reference the same object as {@link AbstractAgent#model}.
     */
    protected StandardWorldModel world;

    /**
       The search algorithm.
     */
    protected SampleSearch search;

    /**
       Construct an AbstractSampleAgent.
     */
    protected AbstractSampleAgent() {
    }

    @Override
    protected WorldModel<StandardEntity> createWorldModel() {
        world = new StandardWorldModel();
        return world;
    }

    @Override
    protected void postConnect() {
        world.index(MESH_SIZE);
        search = new SampleSearch(world, true);
    }

    /**
       Get the location of the entity controlled by this agent.
       @return The location of the entity controlled by this agent.
     */
    protected StandardEntity location() {
        Human me = (Human)me();
        return me.getPosition(world);
    }

    /**
       Construct a random walk starting from this agent's current location. Buildings will only be entered at the end of the walk.
       @return A random walk.
     */
    protected List<EntityID> randomWalk() {
        List<EntityID> result = new ArrayList<EntityID>(RANDOM_WALK_LENGTH);
        Set<StandardEntity> seen = new HashSet<StandardEntity>();
        StandardEntity current = location();
        for (int i = 0; i < RANDOM_WALK_LENGTH; ++i) {
            result.add(current.getID());
            seen.add(current);
            List<StandardEntity> neighbours = new ArrayList<StandardEntity>(search.findNeighbours(current));
            Collections.shuffle(neighbours);
            boolean found = false;
            for (StandardEntity next : neighbours) {
                if (seen.contains(next)) {
                    continue;
                }
                if (next instanceof Building && i < RANDOM_WALK_LENGTH - 1) {
                    continue;
                }
                current = next;
                found = true;
                break;
            }
            if (!found) {
                // We reached a dead-end.
                break;
            }
        }
        return result;
    }
}