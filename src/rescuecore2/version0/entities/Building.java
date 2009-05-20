package rescuecore2.version0.entities;

import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.worldmodel.WorldModel;
import rescuecore2.worldmodel.properties.IntProperty;
import rescuecore2.worldmodel.properties.IntArrayProperty;
import rescuecore2.worldmodel.properties.BooleanProperty;
import rescuecore2.worldmodel.properties.EntityRefListProperty;
import rescuecore2.misc.Pair;

import java.util.List;
import java.util.ArrayList;

/**
   The Building object.
 */
public class Building extends RescueObject {
    private IntProperty x;
    private IntProperty y;
    private IntProperty floors;
    private BooleanProperty ignition;
    private IntProperty fieryness;
    private IntProperty brokenness;
    private IntProperty code;
    private IntProperty attributes;
    private IntProperty groundArea;
    private IntProperty totalArea;
    private IntProperty temperature;
    private IntProperty importance;
    private EntityRefListProperty entrances;
    private IntArrayProperty apexes;

    /**
       Construct a Building object with entirely undefined property values.
       @param id The ID of this entity.
    */
    public Building(EntityID id) {
        this(id, RescueEntityType.BUILDING);
    }

    /**
       Construct a subclass of a Building object with entirely undefined property values.
       @param id The ID of this entity.
       @param type The real type of this building.
    */
    protected Building(EntityID id, RescueEntityType type) {
        super(id, type);
        x = new IntProperty(RescuePropertyType.X);
        y = new IntProperty(RescuePropertyType.Y);
        floors = new IntProperty(RescuePropertyType.FLOORS);
        ignition = new BooleanProperty(RescuePropertyType.IGNITION);
        fieryness = new IntProperty(RescuePropertyType.FIERYNESS);
        brokenness = new IntProperty(RescuePropertyType.BROKENNESS);
        code = new IntProperty(RescuePropertyType.BUILDING_CODE);
        attributes = new IntProperty(RescuePropertyType.BUILDING_ATTRIBUTES);
        groundArea = new IntProperty(RescuePropertyType.BUILDING_AREA_GROUND);
        totalArea = new IntProperty(RescuePropertyType.BUILDING_AREA_TOTAL);
        temperature = new IntProperty(RescuePropertyType.TEMPERATURE);
        importance = new IntProperty(RescuePropertyType.IMPORTANCE);
        apexes = new IntArrayProperty(RescuePropertyType.BUILDING_APEXES);
        entrances = new EntityRefListProperty(RescuePropertyType.ENTRANCES);
        addProperties(x, y, floors, ignition, fieryness, brokenness, code, attributes, groundArea, totalArea, temperature, importance, apexes, entrances);
    }

    @Override
    protected Entity copyImpl() {
        return new Building(getID());
    }

    @Override
    public Pair<Integer, Integer> getLocation(WorldModel<? extends RescueObject> world) {
        return new Pair<Integer, Integer>(x.getValue(), y.getValue());
    }

    /**
       Get the x coordinate property.
       @return The x property.
     */
    public IntProperty getXProperty() {
        return x;
    }

    /**
       Get the x coordinate of the centre of this building.
       @return The x coordinate.
     */
    public int getX() {
        return x.getValue();
    }

    /**
       Set the x coordinate of the center of this building.
       @param x The new x coordinate.
    */
    public void setX(int x) {
        this.x.setValue(x);
    }

    /**
       Find out if the x coordinate has been defined.
       @return True if the x coordinate has been defined, false otherwise.
     */
    public boolean isXDefined() {
        return x.isDefined();
    }

    /**
       Undefine the x coordinate.
    */
    public void undefineX() {
        x.undefine();
    }

    /**
       Get the y coordinate property.
       @return The y property.
     */
    public IntProperty getYProperty() {
        return y;
    }

    /**
       Get the y coordinate of the centre of this building.
       @return The y coordinate.
     */
    public int getY() {
        return y.getValue();
    }

    /**
       Set the y coordinate of the center of this building.
       @param y The new y coordinate.
    */
    public void setY(int y) {
        this.y.setValue(y);
    }

    /**
       Find out if the y coordinate has been defined.
       @return True if the y coordinate has been defined, false otherwise.
     */
    public boolean isYDefined() {
        return y.isDefined();
    }

    /**
       Undefine the y coordinate.
    */
    public void undefineY() {
        y.undefine();
    }

    /**
       Get the floors property.
       @return The floors property.
     */
    public IntProperty getFloorsProperty() {
        return floors;
    }

    /**
       Get the number of floors in this building.
       @return The number of floors.
     */
    public int getFloors() {
        return floors.getValue();
    }

    /**
       Set the number of floors in this building.
       @param floors The new number of floors.
    */
    public void setFloors(int floors) {
        this.floors.setValue(floors);
    }

    /**
       Find out if the floors property has been defined.
       @return True if the floors property has been defined, false otherwise.
     */
    public boolean isFloorsDefined() {
        return floors.isDefined();
    }

    /**
       Undefine the floors property.
    */
    public void undefineFloors() {
        floors.undefine();
    }

    /**
       Get the ignition property.
       @return The ignition property.
     */
    public BooleanProperty getIgnitionProperty() {
        return ignition;
    }

    /**
       Get the value of the ignition property.
       @return The ignition property value.
     */
    public boolean getIgnition() {
        return ignition.getValue();
    }

    /**
       Set the ignition property.
       @param ignition The new ignition value.
    */
    public void setIgnition(boolean ignition) {
        this.ignition.setValue(ignition);
    }

    /**
       Find out if the ignition property has been defined.
       @return True if the ignition property has been defined, false otherwise.
     */
    public boolean isIgnitionDefined() {
        return ignition.isDefined();
    }

    /**
       Undefine the ingition property.
    */
    public void undefineIgnition() {
        ignition.undefine();
    }

    /**
       Get the fieryness property.
       @return The fieryness property.
     */
    public IntProperty getFierynessProperty() {
        return fieryness;
    }

    /**
       Get the fieryness of this building.
       @return The fieryness property value.
     */
    public int getFieryness() {
        return fieryness.getValue();
    }

    /**
       Set the fieryness of this building.
       @param fieryness The new fieryness value.
    */
    public void setFieryness(int fieryness) {
        this.fieryness.setValue(fieryness);
    }

    /**
       Find out if the fieryness property has been defined.
       @return True if the fieryness property has been defined, false otherwise.
     */
    public boolean isFierynessDefined() {
        return fieryness.isDefined();
    }

    /**
       Undefine the fieryness property.
    */
    public void undefineFieryness() {
        fieryness.undefine();
    }

    /**
       Get the brokenness property.
       @return The brokenness property.
     */
    public IntProperty getBrokennessProperty() {
        return brokenness;
    }

    /**
       Get the brokenness of this building.
       @return The brokenness value.
     */
    public int getBrokenness() {
        return brokenness.getValue();
    }

    /**
       Set the brokenness of this building.
       @param brokenness The new brokenness.
    */
    public void setBrokenness(int brokenness) {
        this.brokenness.setValue(brokenness);
    }

    /**
       Find out if the brokenness property has been defined.
       @return True if the brokenness property has been defined, false otherwise.
     */
    public boolean isBrokennessDefined() {
        return brokenness.isDefined();
    }

    /**
       Undefine the brokenness property.
    */
    public void undefineBrokenness() {
        brokenness.undefine();
    }

    /**
       Get the building code property.
       @return The building code property.
     */
    public IntProperty getBuildingCodeProperty() {
        return code;
    }

    /**
       Get the building code of this building.
       @return The building code.
     */
    public int getBuildingCode() {
        return code.getValue();
    }

    /**
       Set the building code of this building.
       @param newCode The new building code.
    */
    public void setBuildingCode(int newCode) {
        this.code.setValue(newCode);
    }

    /**
       Find out if the building code has been defined.
       @return True if the building code has been defined, false otherwise.
     */
    public boolean isBuildingCodeDefined() {
        return code.isDefined();
    }

    /**
       Undefine the building code.
    */
    public void undefineBuildingCode() {
        code.undefine();
    }

    /**
       Get the building attributes property.
       @return The building attributes property.
     */
    public IntProperty getBuildingAttributesProperty() {
        return attributes;
    }

    /**
       Get the building attributes of this building.
       @return The building attributes.
     */
    public int getBuildingAttributes() {
        return attributes.getValue();
    }

    /**
       Set the building attributes of this building.
       @param newAttributes The new building attributes.
    */
    public void setBuildingAttributes(int newAttributes) {
        this.attributes.setValue(newAttributes);
    }

    /**
       Find out if the building attributes property has been defined.
       @return True if the building attributes property has been defined, false otherwise.
     */
    public boolean isBuildingAttributesDefined() {
        return attributes.isDefined();
    }

    /**
       Undefine the building attributes.
    */
    public void undefineBuildingAttributes() {
        attributes.undefine();
    }

    /**
       Get the ground area property.
       @return The ground area property.
     */
    public IntProperty getGroundAreaProperty() {
        return groundArea;
    }

    /**
       Get the ground area of this building.
       @return The ground area.
     */
    public int getGroundArea() {
        return groundArea.getValue();
    }

    /**
       Set the ground area of this building.
       @param ground The new ground area.
    */
    public void setGroundArea(int ground) {
        this.groundArea.setValue(ground);
    }

    /**
       Find out if the ground area property has been defined.
       @return True if the ground area property has been defined, false otherwise.
     */
    public boolean isGroundAreaDefined() {
        return groundArea.isDefined();
    }

    /**
       Undefine the ground area.
    */
    public void undefineGroundArea() {
        groundArea.undefine();
    }

    /**
       Get the total area property.
       @return The total area property.
     */
    public IntProperty getTotalAreaProperty() {
        return totalArea;
    }

    /**
       Get the total area of this building.
       @return The total area.
     */
    public int getTotalArea() {
        return totalArea.getValue();
    }

    /**
       Set the total area of this building.
       @param total The new total area.
    */
    public void setTotalArea(int total) {
        this.totalArea.setValue(total);
    }

    /**
       Find out if the total area property has been defined.
       @return True if the total area property has been defined, false otherwise.
     */
    public boolean isTotalAreaDefined() {
        return totalArea.isDefined();
    }

    /**
       Undefine the total area property.
    */
    public void undefineTotalArea() {
        totalArea.undefine();
    }

    /**
       Get the temperature property.
       @return The temperature property.
     */
    public IntProperty getTemperatureProperty() {
        return temperature;
    }

    /**
       Get the temperature of this building.
       @return The temperature.
     */
    public int getTemperature() {
        return temperature.getValue();
    }

    /**
       Set the temperature of this building.
       @param temperature The new temperature.
    */
    public void setTemperature(int temperature) {
        this.temperature.setValue(temperature);
    }

    /**
       Find out if the temperature property has been defined.
       @return True if the temperature property has been defined, false otherwise.
     */
    public boolean isTemperatureDefined() {
        return temperature.isDefined();
    }

    /**
       Undefine the temperature property.
    */
    public void undefineTemperature() {
        temperature.undefine();
    }

    /**
       Get the building importance property.
       @return The importance property.
     */
    public IntProperty getImportanceProperty() {
        return importance;
    }

    /**
       Get the importance of this building.
       @return The importance.
     */
    public int getImportance() {
        return importance.getValue();
    }

    /**
       Set the importance of this building.
       @param importance The new importance.
    */
    public void setImportance(int importance) {
        this.importance.setValue(importance);
    }

    /**
       Find out if the importance property has been defined.
       @return True if the importance property has been defined, false otherwise.
     */
    public boolean isImportanceDefined() {
        return importance.isDefined();
    }

    /**
       Undefine the importance property.
    */
    public void undefineImportance() {
        importance.undefine();
    }

    /**
       Get the entrances property.
       @return The entrances property.
     */
    public EntityRefListProperty getEntrancesProperty() {
        return entrances;
    }

    /**
       Get the entrances of this building.
       @return The entrances.
     */
    public List<EntityID> getEntrances() {
        return entrances.getValue();
    }

    /**
       Set the entrances of this building.
       @param entrances The new entrances.
    */
    public void setEntrances(List<EntityID> entrances) {
        this.entrances.setValue(entrances);
    }

    /**
       Find out if the entrances property has been defined.
       @return True if the entrances property has been defined, false otherwise.
     */
    public boolean isEntrancesDefined() {
        return entrances.isDefined();
    }

    /**
       Undefine the entrances property.
    */
    public void undefineEntrances() {
        entrances.undefine();
    }

    /**
       Get the apexes property.
       @return The apexes property.
     */
    public IntArrayProperty getApexesProperty() {
        return apexes;
    }

    /**
       Get the apexes of this building.
       @return The building apexes.
     */
    public int[] getApexes() {
        return apexes.getValue();
    }

    /**
       Set the apexes of this building.
       @param apexes The new apexes.
    */
    public void setApexes(int[] apexes) {
        this.apexes.setValue(apexes);
    }

    /**
       Find out if the apexes property has been defined.
       @return True if the apexes property has been defined, false otherwise.
     */
    public boolean isApexesDefined() {
        return apexes.isDefined();
    }

    /**
       Undefine the apexes property.
    */
    public void undefineApexes() {
        apexes.undefine();
    }

    /**
       Get the apexes as a list of coordinates.
       @return A list of coordinates.
     */
    public List<Pair<Integer, Integer>> getApexesAsList() {
        List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
        if (apexes.isDefined()) {
            int[] values = apexes.getValue();
            for (int i = 0; i < values.length; i += 2) {
                result.add(new Pair<Integer, Integer>(values[i], values[i + 1]));
            }
        }
        return result;
    }
}