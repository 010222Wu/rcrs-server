package collapse;

import rescuecore2.config.Config;
import rescuecore2.messages.control.KSCommands;
import rescuecore2.worldmodel.ChangeSet;

import rescuecore2.standard.components.StandardSimulator;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.Building;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
   A simple collapse simulator.
 */
public class CollapseSimulator extends StandardSimulator {
    private static final Log LOG = LogFactory.getLog(CollapseSimulator.class);

    private static final String[] CODES = {"wood", "steel", "concrete"};
    private static final String CONFIG_PREFIX = "collapse.";
    private static final String DESTROYED_SUFFIX = ".p-destroyed";
    private static final String SEVERE_SUFFIX = ".p-severe";
    private static final String MODERATE_SUFFIX = ".p-moderate";
    private static final String SLIGHT_SUFFIX = ".p-slight";
    private static final String NONE_SUFFIX = ".p-none";
    private static final int DESTROYED = 100;
    private static final int SEVERE = 75;
    private static final int MODERATE = 50;
    private static final int SLIGHT = 25;
    private static final int NONE = 0;

    private CollapseStats[] stats;

    @Override
    public String getName() {
        return "Basic collapse simulator";
    }

    @Override
    protected void postConnect() {
        super.postConnect();
        stats = new CollapseStats[CODES.length];
        for (int i = 0; i < CODES.length; ++i) {
            stats[i] = new CollapseStats(i, config);
        }
    }

    @Override
    protected void processCommands(KSCommands c, ChangeSet changes) {
        int time = c.getTime();
        // CHECKSTYLE:OFF:MagicNumber
        int[][] count = new int[CODES.length][6];
        // CHECKSTYLE:ON:MagicNumber
        if (time == 1) {
            // Work out what has collapsed
            for (StandardEntity next : model) {
                if (next instanceof Building) {
                    Building b = (Building)next;
                    int damage = b.isBuildingCodeDefined() ? stats[b.getBuildingCode()].damage() : 0;
                    b.setBrokenness(damage);
                    changes.addChange(b, b.getBrokennessProperty());
                    // CHECKSTYLE:OFF:MagicNumber
                    ++count[b.getBuildingCode()][damage/25];
                    ++count[b.getBuildingCode()][5];
                    // CHECKSTYLE:ON:MagicNumber
                }
            }
            // CHECKSTYLE:OFF:MagicNumber
            LOG.info("Finished collapsing buildings: ");
            for (int i = 0; i < CODES.length; ++i) {
                LOG.info("Building code " + i + ": " + count[i][5] + " buildings");
                LOG.info("  " + count[i][0] + " undamaged");
                LOG.info("  " + count[i][1] + " slightly damaged");
                LOG.info("  " + count[i][2] + " moderately damaged");
                LOG.info("  " + count[i][3] + " severely damaged");
                LOG.info("  " + count[i][4] + " destroyed");
            }
            // CHECKSTYLE:ON:MagicNumber
        }
        // Check for fire
        for (StandardEntity next : model) {
            if (next instanceof Building) {
                Building b = (Building)next;
                if (!b.isFierynessDefined()) {
                    continue;
                }
                int minDamage = NONE;
                switch (b.getFierynessEnum()) {
                case HEATING:
                    minDamage = SLIGHT;
                    break;
                case BURNING:
                    minDamage = MODERATE;
                    break;
                case INFERNO:
                    minDamage = SEVERE;
                    break;
                case BURNT_OUT:
                    minDamage = DESTROYED;
                    break;
                default:
                    break;
                }
                int damage = b.isBrokennessDefined() ? b.getBrokenness() : 0;
                if (damage < minDamage) {
                    LOG.info(b + " damaged by fire. New brokenness: " + minDamage);
                    b.setBrokenness(minDamage);
                    changes.addChange(b, b.getBrokennessProperty());
                }
            }
        }
    }

    private class CollapseStats {
        private double destroyed;
        private double severe;
        private double moderate;
        private double slight;

        CollapseStats(int code, Config config) {
            destroyed = config.getFloatValue(CONFIG_PREFIX + CODES[code] + DESTROYED_SUFFIX);
            severe = destroyed + config.getFloatValue(CONFIG_PREFIX + CODES[code] + SEVERE_SUFFIX);
            moderate = severe + config.getFloatValue(CONFIG_PREFIX + CODES[code] + MODERATE_SUFFIX);
            slight = moderate + config.getFloatValue(CONFIG_PREFIX + CODES[code] + SLIGHT_SUFFIX);
        }

        int damage() {
            double d = random.nextDouble();
            if (d < destroyed) {
                return DESTROYED;
            }
            if (d < severe) {
                return SEVERE;
            }
            if (d < moderate) {
                return MODERATE;
            }
            if (d < slight) {
                return SLIGHT;
            }
            return NONE;
        }
    }
}