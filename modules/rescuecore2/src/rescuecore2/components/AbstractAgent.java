package rescuecore2.components;

import rescuecore2.connection.Connection;
import rescuecore2.connection.ConnectionListener;
import rescuecore2.connection.ConnectionException;
import rescuecore2.messages.Message;
import rescuecore2.messages.control.KASense;
import rescuecore2.messages.control.AKConnect;
import rescuecore2.messages.control.AKAcknowledge;
import rescuecore2.messages.control.KAConnectOK;
import rescuecore2.messages.control.KAConnectError;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.config.Config;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**
   Abstract base class for agent implementations.
   @param <T> The subclass of Entity that this agent understands.
 */
public abstract class AbstractAgent<T extends Entity> extends AbstractComponent<T> implements Agent {
    /**
       The ID of the entity controlled by this agent.
     */
    private EntityID entityID;

    /**
       Create a new AbstractAgent.
     */
    protected AbstractAgent() {
        config = new Config();
    }

    @Override
    public final void postConnect(Connection c, EntityID agentID, Collection<Entity> entities, Config kernelConfig) {
        super.postConnect(c, entities, kernelConfig);
        this.entityID = agentID;
        c.addConnectionListener(new AgentListener());
        postConnect();
    }

    @Override
    public EntityID getID() {
        return entityID;
    }

    @Override
    public void connect(Connection connection, RequestIDGenerator generator) throws ConnectionException, ComponentConnectionException, InterruptedException {
        int requestID = generator.generateRequestID();
        AKConnect connect = new AKConnect(requestID, 1, getName(), getRequestedEntityURNs());
        CountDownLatch latch = new CountDownLatch(1);
        AgentConnectionListener l = new AgentConnectionListener(requestID, latch);
        connection.addConnectionListener(l);
        connection.sendMessage(connect);
        // Wait for a reply
        latch.await();
        l.testSuccess();
    }

    /**
       Notification that a timestep has started.
       @param time The timestep.
       @param changed A collection of entities that changed this timestep.
     */
    protected abstract void think(int time, Collection<EntityID> changed);

    /**
       Perform any post-connection work required before acknowledgement of the connection is made. The default implementation does nothing.
     */
    protected void postConnect() {
    }

    /**
       Process an incoming sense message. The default implementation updates the world model and calls {@link #think}. Subclasses should generally not override this method but instead implement the {@link #think} method.
       @param sense The sense message.
     */
    protected void processSense(KASense sense) {
        model.merge(sense.getChangeSet());
        Collection<EntityID> changed = sense.getChangeSet().getChangedEntities();
        think(sense.getTime(), changed);
    }

    /**
       Get the entity controlled by this agent.
       @return The entity controlled by this agent.
     */
    protected T me() {
        if (entityID == null) {
            return null;
        }
        return model.getEntity(entityID);
    }

    private class AgentConnectionListener implements ConnectionListener {
        private int requestID;
        private CountDownLatch latch;
        private ComponentConnectionException failureReason;

        public AgentConnectionListener(int requestID, CountDownLatch latch) {
            this.requestID = requestID;
            this.latch = latch;
            failureReason = null;
        }

        @Override
        public void messageReceived(Connection c, Message msg) {
            if (msg instanceof KAConnectOK) {
                handleConnectOK(c, (KAConnectOK)msg);
            }
            if (msg instanceof KAConnectError) {
                handleConnectError(c, (KAConnectError)msg);
            }
        }

        private void handleConnectOK(Connection c, KAConnectOK ok) {
            if (ok.getRequestID() == requestID) {
                c.removeConnectionListener(this);
                postConnect(c, ok.getAgentID(), ok.getEntities(), ok.getConfig());
                try {
                    c.sendMessage(new AKAcknowledge(requestID, ok.getAgentID()));
                }
                catch (ConnectionException e) {
                    failureReason = new ComponentConnectionException(e);
                }
                latch.countDown();
            }
        }

        private void handleConnectError(Connection c, KAConnectError error) {
            if (error.getRequestID() == requestID) {
                c.removeConnectionListener(this);
                failureReason = new ComponentConnectionException(error.getReason());
                latch.countDown();
            }
        }

        /**
           Check if the connection succeeded and throw an exception if is has not.
        */
        void testSuccess() throws ComponentConnectionException {
            if (failureReason != null) {
                throw failureReason;
            }
        }
    }

    private class AgentListener implements ConnectionListener {
        @Override
        public void messageReceived(Connection c, Message msg) {
            if (msg instanceof KASense) {
                KASense sense = (KASense)msg;
                if (!entityID.equals(sense.getAgentID())) {
                    return;
                }
                processSense(sense);
            }
        }
    }
}