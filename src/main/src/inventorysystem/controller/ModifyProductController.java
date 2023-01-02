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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * Controller for Modify Product View
 * @author Alvin Roe
 */

public class ModifyProductController implements Initializable{

    //region Variables
    /**Reference to the ID text field */
    @FXML
    private TextField idField;
    /**Reference to the Name text field in the Add Part view */
    @FXML
    private TextField nameField;
    /**Reference to the Name text field in the Add Part view */
    @FXML
    private TextField invField;
    /**Reference to the inv text field in the Add Part view */
    @FXML
    private TextField priceField;
    /**Reference to the price text field in the Add Part view */
    @FXML
    private TextField maxField;
    /**Reference to the max text field in the Add Part view */
    @FXML
    private TextField minField;
    /**Reference to the id error text field in the Add Part view */
    @FXML
    private Text idError;
    /**Reference to the id error text field in the Add Part view  */
    @FXML
    private Text nameError;
    /**Reference to the inv error text in the Add Part view  */
    @FXML
    private Text invError;
    /**Reference to the price error text in the Add Part view  */
    @FXML
    private Text priceError;
    /**Reference to the max error text in the Add Part view  */
    @FXML
    private Text maxError;
    /**Reference to the min error text in the Add Part view  */
    @FXML
    private Text minError;
    /**Reference to the save button in the Add Part view  */
    @FXML
    private Button saveButton;
    /**onValidateInput() doesn't start until this is true. Set true after an attempted save. Save will also run onValidateInput as well.*/
    private boolean validatingInput = false;
    /**Utilized to track current generated id for new products*/
    private static int productIDCounter = 0;
    /**Instance of new product being added*/
    private Product newProduct = new Product(0, "Temp Name", 0d, 0, 0, 0);
    /**Instance of the parts list used to display data in the parts table*/
    private ObservableList<Part> shownPartsList;
    /**Instance of the table where parts are displayed*/
    @FXML
    private TableView partTable;
    /**Table where associated parts are displayed*/
    @FXML
    private TableView associatedTable;
    /**Text Field where search text is entered*/
    @FXML
    private TextField searchField;
    /**Column where the part ids are listed for all the parts*/
    @FXML
    private TableColumn allPartsID;
    /**Column where the part names are listed for all the parts*/
    @FXML
    private TableColumn allPartsName;
    /**Column where the stock levels are listed for all the parts*/
    @FXML
    private TableColumn allPartsStock;
    /**Column where the prices are listed for all the parts*/
    @FXML
    private TableColumn allPartsPrice;
    /**Column where the id is listed for the associated parts*/
    @FXML
    private TableColumn productPartID;
    /**Column where the name is listed for the associated parts*/
    @FXML
    private TableColumn productPartName;
    /**Column where the inventory level is listed for the associated parts*/
    @FXML
    private TableColumn productPartStock;
    /**Column where the price is listed for the associated parts*/
    @FXML
    private TableColumn productPartPrice;
    /**Text where errors from the part table are displayed*/
    @FXML
    private Text partTableError;
    /**Text where errors from the associated table are displayed*/
    @FXML
    private Text associatedTableError;
    /**
     * Reference to the original Product
     */
    private Product originalProduct;
    /**
     * Instance of the original product that stores the modifications
     */
    private Product modifiedProduct;
    //endregion

    /**
     * Saves the modifications to the inventory
     *
     * @throws IOException Included due to scene change
     */
    @FXML
    private void onSavePressed() throws IOException{
        modifiedProduct.setName(nameField.getText());

        Inventory.updateProduct(MainController.modifyIndex, modifiedProduct);
        Main.changeScene("/inventorysystem/view/MainForm.fxml", maxField, "Inventory System", 934, 347, this);
    }

    /**
     * Adds a part to the associated parts
     */
    @FXML
    private void onAddPressed(){
        if(partTable.getSelectionModel().getSelectedItem() == null){
            Timer timer = new Timer();
            timer.schedule(new TextFadeTimerTask(partTableError, "Nothing Selected"), 800);
            return;
        }

        Part partToAdd = (Part)partTable.getSelectionModel().getSelectedItem();
        modifiedProduct.addAssociatedPart(partToAdd);
    }

    /**
     * Removes the selected part from the associated part
     */
    @FXML
    private void onRemovePressed(){
        if(associatedTable.getSelectionModel().getSelectedItem() == null){
            Timer timer = new Timer();
            timer.schedule(new TextFadeTimerTask(associatedTableError, "Nothing Selected"), 800);
            return;
        }

        Alert removeConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        removeConfirm.setTitle("Remove Confirmation");
        removeConfirm.setHeaderText("Are you sure you want to remove the associated part?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");

        removeConfirm.getButtonTypes().setAll(yesButton, cancelButton);
        Optional<ButtonType> result = removeConfirm.showAndWait();

        if(result.get() == yesButton) {
            int partToRemove = associatedTable.getSelectionModel().getSelectedIndex();
            modifiedProduct.deleteAssociatedPart(partToRemove);
        }
    }

    /**
     * Validates the input in fields
     */
    @FXML
    private void onValidatedInput(){
        //Stock, Min, and Max validated as numbers
        boolean parseError = false;
        ArrayList<Text> errorTexts = new ArrayList<Text>();
        int stock = 0;
        int min = 0;
        int max = 0;
        Double price = 0d;

        //Check for Parsing Errors
        try{
            stock = Integer.parseInt(invField.getText());
            invError.setText("");
        }
        catch(NumberFormatException e){
            invError.setText("Numbers Only");
            parseError = true;
        }
        try{
            min = Integer.parseInt(minField.getText());
            minError.setText("");
        }
        catch(NumberFormatException e){
            minError.setText("Numbers Only");
            parseError = true;
        }
        try{
            max = Integer.parseInt(maxField.getText());
            maxError.setText("");
        }
        catch(NumberFormatException e){
            maxError.setText("Numbers Only");
            parseError = true;
        }
        try{
            price = Double.parseDouble(priceField.getText());
            priceError.setText("");
        }
        catch(NumberFormatException e){
            priceError.setText("Numbers Only");
            parseError = true;
        }
        if(parseError){
            saveButton.setDisable(true);
            return;
        }

        boolean quantityError = false;
        //Check for Min, Max, and Stock validity
        if(min > max){
            minError.setText("Over Max");
            quantityError = true;
        }
        else minError.setText("");
        if (stock < min) {
            invError.setText("Below Minimum");
            quantityError = true;
        }
        else if (stock > max) {
            invError.setText("Above Maximum");
            quantityError = true;
        } else invError.setText("");
        if(quantityError)
            saveButton.setDisable(true);
        else
            saveButton.setDisable(false);

        modifiedProduct.setStock(stock);
        modifiedProduct.setMin(min);
        modifiedProduct.setMax(max);
        modifiedProduct.setPrice(price);

    }

    /**
     * Cancels changes and returns to the main view
     * @throws IOException utilized due to scene change
     */
    @FXML
    private void onCancelPressed() throws IOException {
        Main.changeScene("/inventorysystem/view/MainForm.fxml", saveButton, "Inventory System", 934, 347, this);
    }

    /**
     * Updates the table view for parts based on search text
     */
    @FXML
    private void onSearchChange(){
        shownPartsList = getPartSearchResults(searchField.getText());
        partTable.setItems(shownPartsList);

        if(shownPartsList.size() == 0){
            Timer errorTimer = new Timer();
            errorTimer.schedule(new TextFadeTimerTask(partTableError, "No Results for Search"), 500);
        }
    }

    /**
     * Initial set up of Table Views and modified product
     * @param url the url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        originalProduct = Inventory.getAllProducts().get(MainController.modifyIndex);
        modifiedProduct = new Product(originalProduct.getId(), originalProduct.getName(), originalProduct.getPrice(), originalProduct.getStock(), originalProduct.getMin(), originalProduct.getMax());
        modifiedProduct.setAssociatedParts(originalProduct.getAllAssociatedParts());

        //Populate fields with information.
        nameField.setText(modifiedProduct.getName());
        idField.setText(String.valueOf(modifiedProduct.getId()));
        priceField.setText(String.valueOf(modifiedProduct.getPrice()));
        minField.setText(String.valueOf(modifiedProduct.getMin()));
        maxField.setText(String.valueOf(modifiedProduct.getMax()));
        invField.setText(String.valueOf(modifiedProduct.getStock()));

        //Set up tables
        shownPartsList = getPartSearchResults(searchField.getText());

        partTable.setItems(shownPartsList);

        allPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedTable.setItems(modifiedProduct.getAllAssociatedParts());

        productPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartStock.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPartPrice.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }

    /**
     * Creates and returns a list of parts based on search.
     * @param searchText text to search
     * @return a list of parts based on search
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
}
