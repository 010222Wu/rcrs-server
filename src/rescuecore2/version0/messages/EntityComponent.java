package rescuecore2.version0.messages;

import static rescuecore2.misc.EncodingTools.readInt32;
import static rescuecore2.misc.EncodingTools.writeInt32;
import static rescuecore2.misc.EncodingTools.readBytes;
import static rescuecore2.misc.EncodingTools.INT_32_SIZE;

import rescuecore2.messages.AbstractMessageComponent;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.worldmodel.EntityType;

import rescuecore2.version0.entities.RescueEntityFactory;
import rescuecore2.version0.entities.RescueObject;
import rescuecore2.version0.entities.RescueEntityType;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
   An Entity component to a message.
 */
public class EntityComponent extends AbstractMessageComponent {
    private RescueObject value;

    /**
       Construct an EntityComponent with no content.
       @param name The name of the component.
     */
    public EntityComponent(String name) {
        super(name);
        value = null;
    }

    /**
       Construct an EntityComponent with a specific value.
       @param name The name of the component.
       @param value The value of this component.
     */
    public EntityComponent(String name, RescueObject value) {
        super(name);
        this.value = value;
    }

    /**
       Get the value of this message component.
       @return The value of the component.
     */
    public RescueObject getValue() {
        return value;
    }

    /**
       Set the value of this message component.
       @param e The new value of the component.
     */
    public void setValue(RescueObject e) {
        value = e;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        ByteArrayOutputStream gather = new ByteArrayOutputStream();
        writeInt32(value.getID().getValue(), gather);
        value.write(gather);
        // Type
        writeInt32(value.getType().getID(), out);
        // Size
        byte[] bytes = gather.toByteArray();
        writeInt32(bytes.length, out);
        out.write(bytes);
    }

    @Override
    public void read(InputStream in) throws IOException {
        RescueEntityType type = RescueEntityFactory.INSTANCE.makeEntityType(readInt32(in));
        int size = readInt32(in);
        byte[] data = readBytes(size, in);
        EntityID id = new EntityID(readInt32(data));
        value = RescueEntityFactory.INSTANCE.makeEntity(type, id);
        value.read(new ByteArrayInputStream(data, INT_32_SIZE, data.length - INT_32_SIZE));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}