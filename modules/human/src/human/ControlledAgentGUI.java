package human;

import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;
import rescuecore2.components.AbstractViewer;
import rescuecore2.components.Agent;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.worldmodel.WorldModel;
import rescuecore2.view.ViewComponent;
import rescuecore2.view.ViewListener;
import rescuecore2.view.RenderedObject;
import rescuecore2.messages.control.Commands;
import rescuecore2.messages.control.Update;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.view.StandardWorldModelViewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.AbstractListModel;

import java.util.List;
import java.util.ArrayList;

/**
   A viewer that also connects a bunch of human-controlled fire brigades, ambulance teams and police forces.
 */
public class ControlledAgentGUI extends AbstractViewer<StandardEntity> {
    private StandardWorldModelViewer viewer;
    private StandardWorldModel world;
    private List<ControlledFireBrigade> fbs;
    private ListListModel fbListModel;
    private JList fbList;
    private List<ControlledPoliceForce> pfs;
    private ListListModel pfListModel;
    private JList pfList;
    private List<ControlledAmbulanceTeam> ats;
    private ListListModel atListModel;
    private JList atList;

    @Override
    public void initialise(Config config) {
    }

    @Override
    public String toString() {
        return "Human controlled agents";
    }

    @Override
    protected WorldModel<StandardEntity> createWorldModel() {
        world = new StandardWorldModel();
        return world;
    }

    @Override
    protected void postConnect() {
        world.index();
        JFrame frame = new JFrame("Viewer " + getViewerID() + " (" + world.getAllEntities().size() + " entities)");
        viewer = new StandardWorldModelViewer();
        viewer.view(world, null, null);
        // CHECKSTYLE:OFF:MagicNumber
        viewer.setPreferredSize(new Dimension(500, 500));
        // CHECKSTYLE:ON:MagicNumber
        fbs = new ArrayList<ControlledFireBrigade>();
        fbListModel = new ListListModel(fbs);
        fbList = new JList(fbListModel);
        pfs = new ArrayList<ControlledPoliceForce>();
        pfListModel = new ListListModel(pfs);
        pfList = new JList(pfListModel);
        ats = new ArrayList<ControlledAmbulanceTeam>();
        atListModel = new ListListModel(ats);
        atList = new JList(atListModel);
        JPanel main = new JPanel(new BorderLayout());
        // CHECKSTYLE:OFF:MagicNumber
        JPanel agents = new JPanel(new GridLayout(3, 1));
        // CHECKSTYLE:ON:MagicNumber
        JScrollPane scroll = new JScrollPane(fbList);
        scroll.setBorder(BorderFactory.createTitledBorder("Fire brigades"));
        agents.add(scroll);
        scroll = new JScrollPane(pfList);
        scroll.setBorder(BorderFactory.createTitledBorder("Police forces"));
        agents.add(scroll);
        scroll = new JScrollPane(atList);
        scroll.setBorder(BorderFactory.createTitledBorder("Ambulance teams"));
        agents.add(scroll);
        main.add(agents, BorderLayout.WEST);
        main.add(viewer, BorderLayout.CENTER);
        viewer.addViewListener(new ViewListener() {
                @Override
                public void objectsClicked(ViewComponent view, List<RenderedObject> objects) {
                    handleClick(objects);
                }
                @Override
                public void objectsRollover(ViewComponent view, List<RenderedObject> objects) {
                }
            });
        frame.add(main);
        frame.pack();
        frame.setVisible(true);
        // Connect as many fire brigades, police forces and ambulance teams as possible, but do it in a new thread.
        new AgentConnector().start();
    }

    @Override
    protected void handleCommands(Commands c) {
    }

    @Override
    protected void handleUpdate(Update u) {
        world.merge(u.getUpdatedEntities());
        viewer.repaint();
        fbListModel.refresh();
        pfListModel.refresh();
        atListModel.refresh();
    }

    private void handleClick(List<RenderedObject> clicked) {
        handleFBClick(clicked);
        handlePFClick(clicked);
        handleATClick(clicked);
    }

    private void handleFBClick(List<RenderedObject> clicked) {
        for (RenderedObject next : clicked) {
            if (next.getObject() instanceof Building) {
                Building b = (Building)next.getObject();
                for (ControlledFireBrigade agent : getSelectedFireBrigades()) {
                    agent.setTarget(b);
                }
                break;
            }
        }
        fbListModel.refresh();
    }

    private void handlePFClick(List<RenderedObject> clicked) {
        for (RenderedObject next : clicked) {
            if (next.getObject() instanceof Road) {
                Road r = (Road)next.getObject();
                for (ControlledPoliceForce agent : getSelectedPoliceForces()) {
                    agent.setTarget(r);
                }
                break;
            }
        }
        pfListModel.refresh();
    }

    private void handleATClick(List<RenderedObject> clicked) {
        for (RenderedObject next : clicked) {
            if (next.getObject() instanceof Human) {
                Human h = (Human)next.getObject();
                for (ControlledAmbulanceTeam agent : getSelectedAmbulanceTeams()) {
                    agent.setTarget(h);
                }
                break;
            }
        }
        atListModel.refresh();
    }

    private List<ControlledFireBrigade> getSelectedFireBrigades() {
        int[] selected = fbList.getSelectedIndices();
        List<ControlledFireBrigade> agents = new ArrayList<ControlledFireBrigade>(selected.length);
        for (int next : selected) {
            agents.add(fbs.get(next));
        }
        return agents;
    }

    private List<ControlledPoliceForce> getSelectedPoliceForces() {
        int[] selected = pfList.getSelectedIndices();
        List<ControlledPoliceForce> agents = new ArrayList<ControlledPoliceForce>(selected.length);
        for (int next : selected) {
            agents.add(pfs.get(next));
        }
        return agents;
    }

    private List<ControlledAmbulanceTeam> getSelectedAmbulanceTeams() {
        int[] selected = atList.getSelectedIndices();
        List<ControlledAmbulanceTeam> agents = new ArrayList<ControlledAmbulanceTeam>(selected.length);
        for (int next : selected) {
            agents.add(ats.get(next));
        }
        return agents;
    }

    private static class ListListModel extends AbstractListModel {
        private List<?> data;

        public ListListModel(List<?> data) {
            this.data = data;
        }

        @Override
        public int getSize() {
            return data.size();
        }

        @Override
        public Object getElementAt(int index) {
            return data.get(index);
        }

        public void refresh() {
            fireContentsChanged(this, 0, data.size());
        }
    }

    private class AgentConnector extends Thread {
        @Override
        public void run() {
            connectAgents(new FireBrigadeAgentType(), fbs, fbListModel);
            connectAgents(new PoliceForceAgentType(), pfs, pfListModel);
            connectAgents(new AmbulanceTeamAgentType(), ats, atListModel);
        }

        private <T extends Agent> void connectAgents(AgentType<T> type, List<? super T> list, ListListModel model) {
            ComponentLauncher launcher = new ComponentLauncher(connection);
            int count = 0;
            while (true) {
                ++count;
                T agent = type.createAgent();
                System.out.print("Connecting " + type + " " + count + "...");
                try {
                    launcher.connect(agent);
                    System.out.println("done.");
                    list.add(agent);
                }
                catch (ComponentConnectionException e) {
                    System.out.println("failed: " + e.getMessage());
                    break;
                }
                catch (InterruptedException e) {
                    System.out.println("interrupted");
                    break;
                }
                catch (ConnectionException e) {
                    System.out.println("failed: " + e);
                    break;
                }
            }
            model.refresh();
        }
    }

    private interface AgentType<T extends Agent> {
        /**
           Create an Agent of the right type.
           @return A new Agent implementation.
        */
        T createAgent();
    }

    private static class FireBrigadeAgentType implements AgentType<ControlledFireBrigade> {
        @Override
        public ControlledFireBrigade createAgent() {
            return new ControlledFireBrigade();
        }

        @Override
        public String toString() {
            return "fire brigade";
        }
    }

    private static class PoliceForceAgentType implements AgentType<ControlledPoliceForce> {
        @Override
        public ControlledPoliceForce createAgent() {
            return new ControlledPoliceForce();
        }

        @Override
        public String toString() {
            return "police force";
        }
    }

    private static class AmbulanceTeamAgentType implements AgentType<ControlledAmbulanceTeam> {
        @Override
        public ControlledAmbulanceTeam createAgent() {
            return new ControlledAmbulanceTeam();
        }

        @Override
        public String toString() {
            return "ambulance team";
        }
    }
}