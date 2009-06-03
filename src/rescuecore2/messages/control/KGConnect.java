package rescuecore2.messages.control;

import rescuecore2.messages.Control;
import rescuecore2.messages.AbstractMessage;
import rescuecore2.messages.IntComponent;

/**
   A message for connecting to the GIS.
 */
public class KGConnect extends AbstractMessage implements Control {
    private IntComponent version;

    /**
       A KGConnect message. The version number will be zero.
     */
    public KGConnect() {
        super("KG_CONNECT", ControlMessageConstants.KG_CONNECT);
        version = new IntComponent("Version", 0);
        addMessageComponent(version);
    }

    /**
       A KGConnect message with a specified version number.
       @param The version number field.
     */
    public KGConnect(int version) {
        this();
        this.version.setValue(version);
    }

    /**
       Get the version number of the message.
       @return The version number field.
    */
    public int getVersion() {
        return version.getValue();
    }
}