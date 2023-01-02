package inventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class for Products
 * @author Alvin Roe
 */
public class Product {
    /**List of parts associated with the instance of the product*/
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**id of the product*/
    private int id;
    /**Name of the product*/
    private String name;
    /**Price of the product*/
    private double price;
    /**Inventory level of the product*/
    private int stock;
    /**Minimum inventory level of the product*/
    private int min;
    /**Maximum inventory level of the product*/
    private int max;

    /**
     * Standard constructor for Product
     * @param id id of the product
     * @param name name of the product
     * @param price price of the product
     * @param stock inventory level of the product
     * @param min minimum inventory level of the product
     * @param max maximum inventory level of the product
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets id
     * @param id new id to set id to
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets price
     * @param price new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets inventory level
     * @param stock new inventory level
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets minimum inventory level
     * @param min new minimum inventory level
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets maximum inventory level
     * @param max new maximum inventory level
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets stock
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Gets min
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets max
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * Add an associated part to the associated part list
     * @param part the part to add
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Deletes selected part
     * @param selectedAssociatedPart part to delete
     * @return true if the delete is successful
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    /**
     * Deletes associated part based on index
     * @param index to remove
     * @return true if successful
     */
    public boolean deleteAssociatedPart(int index){
        associatedParts.remove(index);
        return true;
    }

    /**
     * Gets list of all associated parts
     * @return all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    /**
     * replaces the list of associated parts with a new list of associated parts
     * @param newAssociatedParts new list of associated parts
     */
    public void setAssociatedParts(ObservableList<Part> newAssociatedParts){
        associatedParts = newAssociatedParts;
    }
}
