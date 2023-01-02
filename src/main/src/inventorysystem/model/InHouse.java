package inventorysystem.model;

/**
 * An extension of the Part class specifically for parts created in house.
 * @author Alvin Roe
 */
public class InHouse extends Part{
    /**
     * Machine ID for machine that created part
     */
    private int machineId;

    /**
     * Standard constructor for a part created In House. Has the Machine ID variable added to the part class.
     * @param id The unique ID for the Part
     * @param name The name of the Part
     * @param price The price of the Part
     * @param stock The amount on the Part on hand
     * @param min The minimum amount of the Part allowed in the system
     * @param max The maximum amount of the Part allowed in the system.
     * @param machineId The machine ID utilized to create the part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the Machine ID variable.
     * @param machineId The new Machine ID that we are setting the variable to.
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * Returns the Machine ID for the In House part
     * @return the Machine ID
     */
    public int getMachineId(){
        return machineId;
    }



}
