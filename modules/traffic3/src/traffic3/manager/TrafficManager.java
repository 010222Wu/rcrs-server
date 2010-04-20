package traffic3.manager;

import traffic3.objects.TrafficArea;
import traffic3.objects.TrafficAgent;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.standard.entities.StandardEntity;

import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.misc.collections.LazyMap;

/**
   The traffic manager maintains information about traffic simulator objects.
*/
public class TrafficManager {
    private Map<Area, TrafficArea> areas;
    private Map<Human, TrafficAgent> agents;
    private Map<TrafficArea, Collection<TrafficArea>> areaNeighbours;

    /**
       Construct a new TrafficManager.
    */
    public TrafficManager() {
        areas = new HashMap<Area, TrafficArea>();
        agents = new HashMap<Human, TrafficAgent>();
        areaNeighbours = new LazyMap<TrafficArea, Collection<TrafficArea>>() {
            @Override
            public Collection<TrafficArea> createValue() {
                return new HashSet<TrafficArea>();
            }
        };
    }

    /**
       Find the area that contains a point.
       @param x The X coordinate.
       @param y The Y coordinate.
       @return The TrafficArea that contains the given point, or null if no such area is found.
    */
    public TrafficArea findArea(double x, double y) {
        for (TrafficArea next : getAreas()) {
            if (next.contains(x, y)) {
                return next;
            }
        }
        return null;
    }

    /**
       Get the neighbouring areas to a TrafficArea.
       @param area The area to look up.
       @return All TrafficAreas that share an edge with the given area.
    */
    public Collection<TrafficArea> getNeighbours(TrafficArea area) {
        return areaNeighbours.get(area);
    }

    /**
       Get all agents in the same area or a neighbouring area as an agent.
       @param agent The agent to look up.
       @return All agents (except the input agent) that are in the same or a neighbouring area.
    */
    public Collection<TrafficAgent> getNearbyAgents(TrafficAgent agent) {
        Set<TrafficAgent> result = new HashSet<TrafficAgent>();
        result.addAll(agent.getArea().getAgents());
        for (TrafficArea next : getNeighbours(agent.getArea())) {
            result.addAll(next.getAgents());
        }
        result.remove(agent);
        return result;
    }

    /**
       Remove all objects from this manager.
    */
    public void clear() {
        areas.clear();
        agents.clear();
        areaNeighbours.clear();
    }

    /**
       Register a new TrafficArea.
       @param area The TrafficArea to register.
    */
    public void register(TrafficArea area) {
        areas.put(area.getArea(), area);
    }

    /**
       Register a new TrafficAgent.
       @param agent The TrafficAgent to register.
    */
    public void register(TrafficAgent agent) {
        agents.put(agent.getHuman(), agent);
    }

    /**
       Get all TrafficAgents.
       @return All TrafficAgents.
    */
    public Collection<TrafficAgent> getAgents() {
        return Collections.unmodifiableCollection(agents.values());
    }

    /**
       Get all TrafficAreas.
       @return All TrafficAreas.
    */
    public Collection<TrafficArea> getAreas() {
        return Collections.unmodifiableCollection(areas.values());
    }

    /**
       Compute pre-cached information about the world. TrafficArea and TrafficAgent objects must have already been registered with {@link #register(TrafficArea)} and {@link #register(TrafficAgent)}.
       @param world The world model.
    */
    public void cacheInformation(StandardWorldModel world) {
        areaNeighbours.clear();
        for (StandardEntity next : world) {
            if (next instanceof Area) {
                computeNeighbours((Area)next, world);
            }
        }
    }

    /**
       Get the TrafficArea that wraps a given Area.
       @param a The area to look up.
       @return The TrafficArea that wraps the given area or null if no such TrafficArea exists.
    */
    public TrafficArea getTrafficArea(Area a) {
        return areas.get(a);
    }

    /**
       Get the TrafficAgent that wraps a given human.
       @param h The human to look up.
       @return The TrafficAgent that wraps the given human or null if no such TrafficAgent exists.
    */
    public TrafficAgent getTrafficAgent(Human h) {
        return agents.get(h);
    }

    private void computeNeighbours(Area a, StandardWorldModel world) {
        Collection<TrafficArea> neighbours = areaNeighbours.get(getTrafficArea(a));
        neighbours.clear();
        for (EntityID id : a.getNeighbours()) {
            Entity e = world.getEntity(id);
            if (e instanceof Area) {
                neighbours.add(getTrafficArea((Area)e));
            }
        }
    }
}