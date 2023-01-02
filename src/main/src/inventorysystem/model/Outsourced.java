package inventorysystem.model;

/**
 * Outsourced part class. Has variable for the Outsource company who creates part.
 */
public class Outsourced extends Part{
    /**
     * Name of the company who made part
     */
    private String companyName;

    /**
     * Standard constructor for an outsourced part. Outsourced parts track the company name that created that part.
     * @param id The unique ID for the Part
     * @param name The name of the Part
     * @param price The price of the Part
     * @param stock The amount on the Part on hand
     * @param min The minimum amount of the Part allowed in the system
     * @param max The maximum amount of the Part allowed in the system.
     * @param companyName The company that makes the Outsourced part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Changes the name of the company that created the Outsourced Part.
     * @param companyName The new Company Name that we are replacing the old Company Name with.
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * Returns the name of the company who created the Outsourced part.
     * @return the company name that created the Outsourced part.
     */
    public String getCompanyName(){
        return companyName;
    }

}
