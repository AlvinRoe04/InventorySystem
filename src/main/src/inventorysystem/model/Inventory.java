package inventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.function.Predicate;

/**
 * The Inventory class that keeps all of the data utilized by the system.
 * @author Alvin Roe
 */
public class Inventory {
    /**List of parts in inventory*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**List of products in inventory*/
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to the inventory's list of parts
     * @param newPart the part that is being added to the inventory
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * Adds a product to the inventory's list of products
     * @param newProduct the product being added
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     *Returns a part with the matching part id
     * @param partId the id to lookup
     * @return returns a part with the matching id
     */
    public static Part lookupPart(int partId){
        for(Part part: allParts){
            if(part.getId() == partId) return part;
        }
        return null;
    }

    /**
     * Returns a product with the matching product id
     * @param productId the id to lookup
     * @return returns a product with the matching id
     */
    public static Product lookupProduct(int productId){
        for(Product product: allProducts){
            if(product.getId() == productId) return product;
        }
        return null;
    }

    /**
     * Returns a part based on the name given
     * @param partName name to lookup
     * @return a part with the given name
     */
    public static ObservableList<Part> lookupPart(String partName){
        Predicate<Part> containsPartName = part -> part.getName().contains(partName);
        return allParts.filtered(containsPartName);
    }
    /**
     * Returns a product based on the name given
     * @param productName name to lookup
     * @return a product with the given name
     */
    public static ObservableList<Product> lookupProduct(String productName){
        Predicate<Product> containsProductName = product -> product.getName().contains(productName);
        return allProducts.filtered(containsProductName);
    }

    /**
     * Removes a part from the index and replaces it with another.
     * @param index of the part to update
     * @param selectedPart the part that is updating the old part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }
    /**
     * Removes a product from the index and replaces it with another.
     * @param index of the product to update
     * @param newProduct the product that is updating the old part
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.remove(index);
        allProducts.add(index, newProduct);
    }

    /**
     * Deletes the selected part
     * @param selectedPart Part to delete
     * @return true if the delete was successful, false if there was not a part to delete
     */
    public static boolean deletePart(Part selectedPart){
        if(selectedPart == null) return false;

        allParts.remove(selectedPart);
        return true;
    }

    /**
     * Deletes the selected product
     * @param selectedProduct product to delete
     * @return true if the delete was successful, false if there was nothin to delete
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(selectedProduct == null) return false;

        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * Gets all parts
     * @return a list of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Gets all products
     * @return a list of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
