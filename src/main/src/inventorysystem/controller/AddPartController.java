package inventorysystem.controller;

import inventorysystem.helpers.PartType;
import inventorysystem.main.Main;
import inventorysystem.model.InHouse;
import inventorysystem.model.Inventory;
import inventorysystem.model.Outsourced;
import inventorysystem.model.Part;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Add Part form, activated after someone hits the add button underneath the Part Table.
 * @author Alvin Roe
 */

public class AddPartController{
    //region Variables
    /**Utilized to create a unique, sequential id for each created Part*/
    private static int idCounter = 0;
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
    /**Reference to the min text field in the Add Part view */
    @FXML
    private TextField partTypeField;
    /**Reference to the part type text field in the Add Part view. This can be either the machineId or the Company Name */
    @FXML
    private Text partTypeText;
    /**Tracks whether the current part is an InHouse or Outsource part */
    private PartType partType = PartType.INHOUSE;
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
    /**Reference to the part Type error text in the Add Part view. This goes above the field that changes based on the radio button selections.  */
    @FXML
    private Text partTypeError;
    /**Reference to the save button in the Add Part view  */
    @FXML
    private Button saveButton;
    /**Reference to the in house radio button in the Add Part view*/
    @FXML
    private RadioButton inHouseRadio;
    /**Reference to the outsource radio button in the Add Part view*/
    @FXML
    private RadioButton outsourceRadio;
    /**The New Part that will eventually be added to "allParts" inside Inventory*/
    private InHouse newPart = new InHouse(0, "Temp", 0d, 0, 0, 0, -1);
    /**Utilized if the part is an Outsource, everything other than MachineId is used in a new instance, and this is utilized as the Outsource Company*/
    private String outsourceCompany = "Invalid";
    /**onValidateInput() doesn't start until this is true. Set true after an attempted save. Save will also run onValidateInput as well.*/
    private boolean validatingInput = false;

    //endregion
    /**Ran when the InHouse Radio Button is pressed in the Add Part view.*/
    @FXML
    private void onInHouseRadio(){
        inHouseRadio.setSelected(true);
        outsourceRadio.setSelected(false);
        if(partType == PartType.INHOUSE) return;

        partType = PartType.INHOUSE;
        partTypeText.setText("Machine ID");
        onValidatedInput();
    }

    /**Ran when the Outsource Radio Button is pressed in the Add Part view.*/
    @FXML
    private void onOutsourceRadio(){
        inHouseRadio.setSelected(false);
        outsourceRadio.setSelected(true);

        if(partType == PartType.OUTSOURCE) return;

        partType = PartType.OUTSOURCE;
        partTypeText.setText("Company Name");
        onValidatedInput();
    }

    /**
     * Ran when the Save Button is pressed in the Add Part view
     * FUTURE ENHANCEMENT check to see if the part is already in the Inventory and add the inventory amount instead of creating duplicates.
     * @throws IOException due to the change scene method.
     */
    @FXML
    private void onSavePressed() throws IOException{
        validatingInput = true;
        if(!onValidatedInput()) return;
        newPart.setName(nameField.getText());
        newPart.setId(getNextID());

        if(partType == PartType.INHOUSE) Inventory.addPart(newPart);

        else if(partType == PartType.OUTSOURCE){
            Part newPart = new Outsourced(this.newPart.getId(), this.newPart.getName(), this.newPart.getPrice(), this.newPart.getStock(), this.newPart.getMin(), this.newPart.getMax(), outsourceCompany);
            Inventory.addPart(newPart);
        }
        Main.changeScene("/inventorysystem/view/MainForm.fxml", minError, "Inventory System", 934, 347, this);
    }

    /**
     * Ran when the Cancel Button is pressed in the Add Part view
     * @throws IOException needed for change scene method.
     */
    @FXML
    private void onCancelPressed() throws IOException {
        Main.changeScene("/inventorysystem/view/MainForm.fxml", minError, "Inventory System", 934, 347, this);
    }

    /**
     * Ran whenever fields need validation. Checks for NumberFormatExceptions and checks quantities to make sure they are all appropriate.
     * This will always return false if validatingInput is false.
     * @return true if input is valid, false if input is invalid.
     */
    @FXML
    private boolean onValidatedInput(){
        if(!validatingInput) return false;
        //Stock, Min, and Max validated as numbers
        boolean parseError = false;
        ArrayList<Text> errorTexts = new ArrayList<Text>();
        int stock = 0;
        int min = 0;
        int max = 0;
        int machineID = 0;
        Double price = 0d;
        String outsource = "";

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
        if(partType == PartType.INHOUSE) {
            try {
                machineID = Integer.parseInt(partTypeField.getText());
                partTypeError.setText("");
            } catch (NumberFormatException e) {
                partTypeError.setText("Numbers Only");
                parseError = true;
            }
        }
        else if(partType == PartType.OUTSOURCE) {
            outsource = partTypeField.getText();
            partTypeError.setText("");
        }

        if(parseError){
            saveButton.setDisable(true);
            return false;
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
        if(quantityError){
            saveButton.setDisable(true);
            return false;
        }
        else
            saveButton.setDisable(false);

        newPart.setStock(stock);
        newPart.setMin(min);
        newPart.setMax(max);
        newPart.setPrice(price);
        newPart.setMachineId(machineID);
        outsourceCompany = outsource;

        return true;
    }

    /**
     * Generates a new, unused, sequential id.
     * @return the next sequential unused id
     */
    private int getNextID(){
        idCounter++;
        int generatedID = idCounter;
        ObservableList<Part> allParts = Inventory.getAllParts();
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == generatedID) generatedID = getNextID();
        }
        return generatedID;
    }
}

