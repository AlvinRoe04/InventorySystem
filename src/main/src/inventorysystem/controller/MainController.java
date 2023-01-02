package inventorysystem.controller;


import inventorysystem.helpers.TextFadeTimerTask;
import inventorysystem.main.Main;
import inventorysystem.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * Controller for the Main View
 * @author Alvin Roe
 */
public class MainController implements Initializable {
    //region Variables
    /**Column where Part IDs are displayed from the Shown Parts List*/
    @FXML
    private TableColumn partIdColumn;
    /**Column where Part Names are displayed from the Shown Parts List*/
    @FXML
    private TableColumn partNameColumn;
    /**Column where Part Stock Levels are displayed from the Shown Parts List*/
    @FXML
    private TableColumn partInvLevelColumn;
    /**Column where Part Prices are displayed from the Shown Parts List*/
    @FXML
    private TableColumn partPriceCostColumn;
    /**Column where the Product IDs are displayed from the Shown Products List*/
    @FXML
    private TableColumn productIdColumn;
    /**Column where the Product Names are displayed from the Shown Products List*/
    @FXML
    private TableColumn productNameColumn;
    /**Column where the Product Inventory Level are displayed from the Shown Products List*/
    @FXML
    private TableColumn productInventoryLevelColumn;
    /**Column where the Product Prices are displayed from the Shown Products List*/
    @FXML
    private TableColumn productPriceCostColumn;
    /**The Part Table where all the Part Columns are located. Used to display data from the Shown Parts List*/
    @FXML
    private TableView partTable;
    /**The Product Table where all the Product Columns are located. Used to display data from the Shown Product List*/
    @FXML
    private TableView productTable;
    /**Text for displaying errors*/
    @FXML
    private Text errorText;
    /**Used to search for specific parts*/
    @FXML
    private TextField partsSearchTextField;
    /**Used to search for specific products*/
    @FXML
    private TextField productSearchTextField;
    /**The index of the object modified. Used by both the Modify Part Controller and Modify Product Controller to correctly modify the correct part/product*/
    public static int modifyIndex = 0;
    /**List of parts to display in the Part Table. If no searches are done, it represents all of the parts in the inventory.*/
    private ObservableList<Part> shownPartsList;
    /**List of products to display in the Part Table. If no searches are done, it represents all of the products in the inventory.*/
    private ObservableList<Product> shownProductList;

    //endregion

    /**
     * Sets up the table views
     *
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shownPartsList = getPartSearchResults(partsSearchTextField.getText());
        shownProductList = getProductSearchResults(productSearchTextField.getText());

        partTable.setItems(shownPartsList);

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        productTable.setItems(shownProductList);

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    /**
     * Switches to the Add Part View
     * @throws IOException included due to scene change
     */

    @FXML
    private void onAddPartPressed() throws IOException{
        Main.changeScene("/inventorysystem/view/AddPartForm.fxml", partTable, "Add Part", 600, 400, this);
    }

    /**
     * Switches to the Modify Part view, and changes the modify index so that the Modify Part Controller knows what part to change.
     * @throws IOException included due to scene change
     */
    @FXML
    private void onModifyPartPressed() throws IOException{
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "Error: Nothing Selected"), 500);
            return;
        }

        modifyIndex = getPartModifyIndex(partTable, Inventory.getAllParts());
        Main.changeScene("/inventorysystem/view/ModifyPartForm.fxml", partTable, "Modify Part", 600, 400, this);

    }

    /**
     * Deletes the selected part, if a part is selected.
     */
    @FXML
    private void onDeletePartPressed() {
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "Error: Nothing Selected"), 500);
            return;
        }
        Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirm.setTitle("Delete Confirmation");
        deleteConfirm.setHeaderText("Are you sure you want to delete?");
        deleteConfirm.setContentText("This cannot be reversed.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");

        deleteConfirm.getButtonTypes().setAll(yesButton, cancelButton);
        Optional<ButtonType> result = deleteConfirm.showAndWait();

        if(result.get() == yesButton) {
            Inventory.deletePart(selectedPart);
            shownPartsList = getPartSearchResults(partsSearchTextField.getText());
            partTable.setItems(shownPartsList);
        }
    }

    /**
         * Changes the view to the Add Product View when the Add button under the Products table is pressed.
         * @throws IOException included due to scene change
     */
    @FXML
    private void onAddProductPressed() throws IOException{
        Main.changeScene("/inventorysystem/view/AddProductForm.fxml", partTable, "Add Product", 900, 551, this);
    }

    /**
     * Changes view to modify product view. Changes modify index to the correct index for teh product.
     * RUNTIME ERROR, an error occurred where the selected item wasn't being selected correctly. This was due to the table sorting. Created teh getProductModifyIndex method to fix this.
     * @throws IOException included due to scene change
     */
    @FXML
    private void onModifyProductPressed() throws IOException{
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "Error: Nothing Selected"), 500);
            return;
        }

        modifyIndex = getProductModifyIndex(productTable, Inventory.getAllProducts());
        Main.changeScene("/inventorysystem/view/ModifyProductForm.fxml", partTable, "Modify Product", 900, 551, this);
    }

    /**
     * Finds the original index of the unsorted version of the table. This works as long as there are no duplicate ids.
     *
     * @param table the table with the selected object
     * @param originalList the original ObservableList from before the sorting
     * @return the index of the object in the original list that is being modified
     */
    private int getProductModifyIndex(TableView table, ObservableList<Product> originalList){
        int modifyProductId = ((Product) table.getSelectionModel().getSelectedItem()).getId();
        int i = 0;
        for(i = 0; i < originalList.size(); i++){
            if(originalList.get(i).getId() == modifyProductId) return i;
        }
        return i;
    }
    /**
     * Finds the original index of the unsorted version of the table. This works as long as there are no duplicate ids.
     *
     * @param table the table with the selected object
     * @param originalList the original ObservableList from before the sorting
     * @return the index of the object in the original list that is being modified
     */
    private int getPartModifyIndex(TableView table, ObservableList<Part> originalList){
        int modifyPartId = ((Part) table.getSelectionModel().getSelectedItem()).getId();
        int i = 0;
        for(i = 0; i < originalList.size(); i++){
            if(originalList.get(i).getId() == modifyPartId) return i;
        }
        return i;
    }

    /**
     * Deletes the selected product, if there is a product selected.
     */
    @FXML
    private void onDeleteProductPressed() {
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "Error: Nothing Selected"), 500);
            return;
        }
        else if(selectedProduct.getAllAssociatedParts().size() > 0){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "Has Associated Parts"), 500);
            return;
        }
        Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirm.setTitle("Delete Confirmation");
        deleteConfirm.setHeaderText("Are you sure you want to delete?");
        deleteConfirm.setContentText("This cannot be reversed.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");

        deleteConfirm.getButtonTypes().setAll(yesButton, cancelButton);
        Optional<ButtonType> result = deleteConfirm.showAndWait();

        if(result.get() == yesButton){
        Inventory.deleteProduct(selectedProduct);
        shownProductList = getProductSearchResults(productSearchTextField.getText());
        productTable.setItems(shownProductList);
        }
    }

    /**
     * Exits the application
     */
    @FXML
    private void onExitButtonPressed(){
        System.exit(0);
    }

    /**
     * Updates the Part Table based on text in search
     */
    @FXML
    private void onPartsSearchChange(){
        shownPartsList = getPartSearchResults(partsSearchTextField.getText());
        partTable.setItems(shownPartsList);

        if(shownPartsList.size() == 0){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "No Results for Search"), 500);
        }
    }
    /**
     * Updates the Product Table based on text in search
     */
    @FXML
    private void onProductSearchChange(){
        shownProductList = getProductSearchResults(productSearchTextField.getText());
        productTable.setItems(shownProductList);

        if(shownProductList.size() == 0){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(errorText, "No Results for Search"), 500);
        }
    }

    /**
     * Returns an Observable List of Parts based on search text. Checks both names for partial matches, and ids for full matches.
     * @param searchText the text that is being searched
     * @return a list of parts that match the search
     */
    private ObservableList<Part> getPartSearchResults(String searchText) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();


        for(int i = 0; i < allParts.size(); i++){
            //Check for Name partial/full matches
            if(allParts.get(i).getName().contains(searchText)){
                searchResults.add(allParts.get(i));
            }
            //Check for IDs
            try {
                int searchId = Integer.parseInt(searchText);
                if (searchId == allParts.get(i).getId()) {
                    searchResults.add(allParts.get(i));
                }
            }
            catch(NumberFormatException e){/*Ignore this error */}
        }

        return searchResults;
    }
    /**
     * Returns an Observable List of Products based on search text. Checks both names for partial matches, and ids for full matches.
     * @param searchText the text that is being searched
     * @return a list of products that match the search
     */
    private ObservableList<Product> getProductSearchResults(String searchText){
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();


        for(int i = 0; i < allProducts.size(); i++){
            //Check for Name partial/full matches
            if(allProducts.get(i).getName().contains(searchText)){
                searchResults.add(allProducts.get(i));
            }
            //Check for IDs
            try {
                int searchId = Integer.parseInt(searchText);
                if (searchId == allProducts.get(i).getId()) {
                    searchResults.add(allProducts.get(i));
                }
            }
            catch(NumberFormatException e){/*Ignore this error */}
        }

        return searchResults;
    }



}
