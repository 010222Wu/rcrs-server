package kernel;

import static rescuecore2.misc.java.JavaTools.instantiate;
import static rescuecore2.misc.java.JavaTools.instantiateFactory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import rescuecore2.connection.Connection;
import rescuecore2.connection.ConnectionException;
import rescuecore2.connection.ConnectionManager;
import rescuecore2.connection.StreamConnection;
import rescuecore2.config.Config;
import rescuecore2.config.ConfigException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.components.Component;
import rescuecore2.components.ComponentInitialisationException;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.Agent;
import rescuecore2.components.Simulator;
import rescuecore2.components.Viewer;
import rescuecore2.messages.MessageRegistry;
import rescuecore2.messages.MessageFactory;
import rescuecore2.worldmodel.WorldModel;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityRegistry;
import rescuecore2.worldmodel.EntityFactory;
import rescuecore2.misc.Pair;
import rescuecore2.misc.CommandLineOptions;
import rescuecore2.misc.java.LoadableTypeProcessor;
import rescuecore2.misc.java.LoadableType;
import rescuecore2.Constants;
import rescuecore2.log.LogException;

import kernel.ui.KernelGUI;
import kernel.ui.KernelGUIComponent;

/**
   A class for launching the kernel.
 */
public final class StartKernel {
    private static final String NO_GUI = "--nogui";
    private static final String JUST_RUN = "--just-run";

    private static final String GIS_MANIFEST_KEY = "Gis";
    private static final String PERCEPTION_MANIFEST_KEY = "Perception";
    private static final String COMMUNICATION_MANIFEST_KEY = "CommunicationModel";

    private static final String TERMINATION_KEY = "kernel.termination";

    private static final String GIS_REGEX = "(.+WorldModelCreator).class";
    private static final String PERCEPTION_REGEX = "(.+Perception).class";
    private static final String COMMUNICATION_REGEX = "(.+CommunicationModel).class";

    private static final LoadableType GIS_LOADABLE_TYPE = new LoadableType(GIS_MANIFEST_KEY, GIS_REGEX, WorldModelCreator.class);
    private static final LoadableType PERCEPTION_LOADABLE_TYPE = new LoadableType(PERCEPTION_MANIFEST_KEY, PERCEPTION_REGEX, Perception.class);
    private static final LoadableType COMMUNICATION_LOADABLE_TYPE = new LoadableType(COMMUNICATION_MANIFEST_KEY, COMMUNICATION_REGEX, CommunicationModel.class);

    private static final String KERNEL_STARTUP_TIME_KEY = "kernel.startup.connect-time";

    private static final String COMMAND_FILTERS_KEY = "kernel.commandfilters";
    private static final String AGENT_REGISTRAR_KEY = "kernel.agents.registrar";
    private static final String GUI_COMPONENTS_KEY = "kernel.ui.components";

    /** Utility class: private constructor. */
    private StartKernel() {}

    /**
       Start a kernel.
       @param args Command line arguments.
    */
    public static void main(String[] args) {
        Config config = new Config();
        boolean showGUI = true;
        boolean justRun = false;
        try {
            args = CommandLineOptions.processArgs(args, config);
            int i = 0;
            for (String arg : args) {
                if (arg.equalsIgnoreCase(NO_GUI)) {
                    showGUI = false;
                }
                else if (arg.equalsIgnoreCase(JUST_RUN)) {
                    justRun = true;
                }
                else {
                    System.out.println("Unrecognised option: " + arg);
                }
            }
            // Process jar files
            processJarFiles(config);
            // Register messages and entities
            for (String next : config.getArrayValue(Constants.MESSAGE_FACTORY_KEY, null)) {
                MessageFactory factory = instantiateFactory(next, MessageFactory.class);
                if (factory != null) {
                    MessageRegistry.register(factory);
                    System.out.println("Registered message factory: " + next);
                }
            }
            for (String next : config.getArrayValue(Constants.ENTITY_FACTORY_KEY, null)) {
                EntityFactory factory = instantiateFactory(next, EntityFactory.class);
                if (factory != null) {
                    EntityRegistry.register(factory);
                    System.out.println("Registered entity factory: " + next);
                }
            }
            final KernelInfo kernelInfo = createKernel(config);
            if (showGUI) {
                KernelGUI gui = new KernelGUI(kernelInfo.kernel, kernelInfo.componentManager, config, !justRun);
                for (KernelGUIComponent next : kernelInfo.guiComponents) {
                    gui.addKernelGUIComponent(next);
                }
                JFrame frame = new JFrame("Kernel GUI");
                frame.getContentPane().add(gui);
                frame.pack();
                frame.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            kernelInfo.kernel.shutdown();
                            System.exit(0);
                        }
                    });
                frame.setVisible(true);
            }
            initialiseKernel(kernelInfo, config);
            autostartComponents(kernelInfo, config);
            if (!showGUI || justRun) {
                waitForComponentManager(kernelInfo, config);
                while (!kernelInfo.kernel.hasTerminated()) {
                    kernelInfo.kernel.timestep();
                }
                kernelInfo.kernel.shutdown();
            }
        }
        catch (ConfigException e) {
            System.err.println("Couldn't start kernel");
            e.printStackTrace();
        }
        catch (KernelException e) {
            System.err.println("Couldn't start kernel");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("Couldn't start kernel");
            e.printStackTrace();
        }
        catch (LogException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void initialiseKernel(KernelInfo kernel, Config config) throws KernelException {
        // Start the connection manager
        ConnectionManager connectionManager = new ConnectionManager();
        try {
            connectionManager.listen(config.getIntValue(Constants.KERNEL_PORT_NUMBER), kernel.componentManager);
        }
        catch (IOException e) {
            throw new KernelException("Couldn't open kernel port", e);
        }
    }

    private static void waitForComponentManager(final KernelInfo kernel, Config config) throws KernelException {
        // Wait for all connections
        // Set up a CountDownLatch
        final CountDownLatch latch = new CountDownLatch(1);
        final long timeout = config.getIntValue(KERNEL_STARTUP_TIME_KEY, -1);
        Thread timeoutThread = null;
        if (timeout != -1) {
            timeoutThread = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(timeout);
                            latch.countDown();
                        }
                        // CHECKSTYLE:OFF:EmptyStatement is OK here
                        catch (InterruptedException e) {
                            // Ignore
                        }
                        // CHECKSTYLE:ON:EmptyStatement
                    }
                };
        }
        Thread waitThread = new Thread() {
                public void run() {
                    try {
                        kernel.componentManager.waitForAllAgents();
                        kernel.componentManager.waitForAllSimulators();
                        kernel.componentManager.waitForAllViewers();
                    }
                    // CHECKSTYLE:OFF:EmptyStatement is OK here
                    catch (InterruptedException e) {
                        // Ignore
                    }
                    // CHECKSTYLE:ON:EmptyStatement
                    latch.countDown();
                }
            };
        waitThread.start();
        if (timeoutThread != null) {
            timeoutThread.start();
        }
        // Wait at the latch until either everything is connected or the connection timeout expires
        System.out.println("Waiting for all agents, simulators and viewers to connect.");
        if (timeout > -1) {
            System.out.println("Connection timeout is " + timeout + "ms");
        }
        try {
            latch.await();
        }
        catch (InterruptedException e) {
            waitThread.interrupt();
            if (timeoutThread != null) {
                timeoutThread.interrupt();
            }
            throw new KernelException("Interrupted");
        }
    }

    private static void autostartComponents(KernelInfo info, Config config) throws InterruptedException {
        KernelChooserDialog gui = info.choices;
        Collection<Callable<Void>> all = new ArrayList<Callable<Void>>();
        // Simulators
        for (String next : gui.getSimulators()) {
            all.add(new ComponentStarter<Simulator>(Simulator.class, next, config, info.componentManager, 1));
        }
        // Viewers
        for (String next : gui.getViewers()) {
            all.add(new ComponentStarter<Viewer>(Viewer.class, next, config, info.componentManager, 1));
        }
        // Agents
        for (Pair<String, Integer> next : gui.getAgents()) {
            all.add(new ComponentStarter<Agent>(Agent.class, next.first(), config, info.componentManager, next.second()));
        }
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        service.invokeAll(all);
    }

    private static KernelInfo createKernel(Config config) throws KernelException {
        // Show the chooser GUI
        KernelChooserDialog dialog = new KernelChooserDialog(config);
        dialog.setVisible(true);
        WorldModelCreator gis = dialog.getWorldModelCreator();
        Perception perception = dialog.getPerception();
        CommunicationModel comms = dialog.getCommunicationModel();
        CommandFilter filter = makeCommandFilter(config);
        TerminationCondition termination = makeTerminationCondition(config);

        // Get the world model
        WorldModel<? extends Entity> worldModel = gis.buildWorldModel(config);
        // Initialise
        perception.initialise(config, worldModel);
        comms.initialise(config, worldModel);
        termination.initialise(config);
        // Create the kernel
        System.out.println("Kernel termination condition: " + termination);
        Kernel kernel = new Kernel(config, perception, comms, worldModel, filter, termination);
        // Create the component manager
        ComponentManager componentManager = new ComponentManager(kernel, worldModel, config);
        registerInitialAgents(config, componentManager, worldModel);
        KernelInfo result = new KernelInfo(kernel, perception, comms, componentManager, makeKernelGUIComponents(config), dialog);
        return result;
    }

    private static void registerInitialAgents(Config config, ComponentManager c, WorldModel<? extends Entity> model) throws KernelException {
        AgentRegistrar ar = instantiate(config.getValue(AGENT_REGISTRAR_KEY), AgentRegistrar.class);
        if (ar == null) {
            throw new KernelException("Couldn't instantiate agent registrar");
        }
        ar.registerAgents(model, config, c);
    }

    private static CommandFilter makeCommandFilter(Config config) {
        ChainedCommandFilter result = new ChainedCommandFilter();
        List<String> classNames = config.getArrayValue(COMMAND_FILTERS_KEY, null);
        for (String next : classNames) {
            System.out.println("Command filter found: '" + next + "'");
            CommandFilter f = instantiate(next, CommandFilter.class);
            if (f != null) {
                result.addFilter(f);
            }
        }
        return result;
    }

    private static TerminationCondition makeTerminationCondition(Config config) {
        List<TerminationCondition> result = new ArrayList<TerminationCondition>();
        for (String next : config.getArrayValue(TERMINATION_KEY, null)) {
            TerminationCondition t = instantiate(next, TerminationCondition.class);
            if (t != null) {
                result.add(t);
            }
        }
        return new OrTerminationCondition(result);
    }

    private static List<KernelGUIComponent> makeKernelGUIComponents(Config config) {
        List<KernelGUIComponent> result = new ArrayList<KernelGUIComponent>();
            List<String> classNames = config.getArrayValue(GUI_COMPONENTS_KEY, null);
            for (String next : classNames) {
                System.out.println("GUI component found: '" + next + "'");
                KernelGUIComponent c = instantiate(next, KernelGUIComponent.class);
                if (c != null) {
                    result.add(c);
                }
            }
        return result;
    }

    private static void processJarFiles(Config config) throws IOException {
        LoadableTypeProcessor processor = new LoadableTypeProcessor(config);
        processor.addConfigUpdater(LoadableType.MESSAGE_FACTORY, config, Constants.MESSAGE_FACTORY_KEY);
        processor.addConfigUpdater(LoadableType.ENTITY_FACTORY, config, Constants.ENTITY_FACTORY_KEY);
        processor.addConfigUpdater(LoadableType.AGENT, config, KernelConstants.AGENTS_KEY);
        processor.addConfigUpdater(LoadableType.SIMULATOR, config, KernelConstants.SIMULATORS_KEY);
        processor.addConfigUpdater(LoadableType.VIEWER, config, KernelConstants.VIEWERS_KEY);
        processor.addConfigUpdater(GIS_LOADABLE_TYPE, config, KernelConstants.GIS_KEY);
        processor.addConfigUpdater(PERCEPTION_LOADABLE_TYPE, config, KernelConstants.PERCEPTION_KEY);
        processor.addConfigUpdater(COMMUNICATION_LOADABLE_TYPE, config, KernelConstants.COMMUNICATION_MODEL_KEY);
        processor.process();
    }

    private static class ComponentStarter<T extends Component> implements Callable<Void> {
        private Class<T> clazz;
        private String className;
        private Config config;
        private ComponentManager componentManager;
        private int count;

        public ComponentStarter(Class<T> clazz, String className, Config config, ComponentManager componentManager, int count) {
            this.clazz = clazz;
            this.className = className;
            this.config = config;
            this.componentManager = componentManager;
            this.count = count;
        }

        public Void call() throws InterruptedException {
            Pair<Connection, Connection> connections = StreamConnection.createConnectionPair();
            componentManager.newConnection(connections.first());
            ComponentLauncher launcher = new ComponentLauncher(connections.second());
            System.out.println("Launching " + count + " instances of component '" + className + "'...");
            for (int i = 0; i < count; ++i) {
                Component c = instantiate(className, clazz);
                if (c == null) {
                    break;
                }
                System.out.println("Launching instance " + (i + 1) + "...");
                try {
                    c.initialise(config);
                    launcher.connect(c);
                    System.out.println("success");
                }
                catch (ComponentConnectionException e) {
                    System.out.println("failed: " + e.getMessage());
                    break;
                }
                catch (ComponentInitialisationException e) {
                    System.out.println("failed: " + e);
                    e.printStackTrace();
                }
                catch (ConnectionException e) {
                    System.out.println("failed: " + e);
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private static class KernelChooserDialog extends JDialog {
        private KernelLaunchGUI components;

        public KernelChooserDialog(Config config) {
            super((Frame)null, "Choose kernel options");
            components = new KernelLaunchGUI(config);
            JButton ok = new JButton("OK");
            add(components, BorderLayout.CENTER);
            add(ok, BorderLayout.SOUTH);
            ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });
            setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            pack();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }

        public WorldModelCreator getWorldModelCreator() {
            return components.getGIS();
        }

        public Perception getPerception() {
            return components.getPerception();
        }

        public CommunicationModel getCommunicationModel() {
            return components.getCommunicationModel();
        }

        public Collection<String> getSimulators() {
            return components.getSimulators();
        }

        public Collection<String> getViewers() {
            return components.getViewers();
        }

        public Collection<Pair<String, Integer>> getAgents() {
            return components.getAgents();
        }
    }

    private static class KernelInfo {
        Kernel kernel;
        ComponentManager componentManager;
        List<KernelGUIComponent> guiComponents;
        KernelChooserDialog choices;

        public KernelInfo(Kernel kernel, Perception perception, CommunicationModel comms, ComponentManager componentManager, List<KernelGUIComponent> otherComponents, KernelChooserDialog choices) {
            this.kernel = kernel;
            this.componentManager = componentManager;
            this.choices = choices;
            guiComponents = new ArrayList<KernelGUIComponent>(otherComponents);
            if (perception instanceof KernelGUIComponent) {
                guiComponents.add((KernelGUIComponent)perception);
            }
            if (comms instanceof KernelGUIComponent) {
                guiComponents.add((KernelGUIComponent)comms);
            }
        }
    }
}