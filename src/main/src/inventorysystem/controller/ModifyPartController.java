package inventorysystem.controller;

import inventorysystem.helpers.PartType;
import inventorysystem.helpers.TextFadeTimerTask;
import inventorysystem.main.Main;
import inventorysystem.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * Controller for the Modify Part View.
 * @author Alvin Roe
 */
public class ModifyPartController implements Initializable{
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
    /**Field for Displaying unique part type information, MachineID or Company Name depending on part type*/
    @FXML
    private TextField partTypeField;
    /**Label next to Part Type Field that changes depending on Part Type.*/
    @FXML
    private Text partTypeText;
    /**Part Type*/
    private PartType partType = PartType.INHOUSE;
    /**Error Text above the Part Type Field*/
    @FXML
    private Text partTypeError;
    /**In House Radio button. Changes Part Type to In House.*/
    @FXML
    private RadioButton inHouseRadio;
    /**Outsource button. Changes Part Type to Outsource.*/
    @FXML
    private RadioButton outsourceRadio;
    /**The instance storing all the new part information.*/
    private InHouse modifiedPart = new InHouse(0, "Temp", 0d, 0, 0, 0, -1);
    /**Variable for the Outsource Company information*/
    private String outsourceCompany = "Invalid";
    /**In House Radio button. Changes Part Type to In House.*/

    //endregion

    /**
     * Triggers when the In House Radio Button is pressed. Changes the Part Type to In House.
     */
    @FXML
    private void onInHouseRadio(){
        inHouseRadio.setSelected(true);
        outsourceRadio.setSelected(false);

        if(partType == PartType.INHOUSE) return;

        partType = PartType.INHOUSE;
        partTypeText.setText("Machine ID");
        onValidatedInput();
    }
    /**
     * Triggers when the Outsource Radio Button is pressed. Changes the Part Type to Outsource.
     */
    @FXML
    private void onOutsourceRadio(){
        outsourceRadio.setSelected(true);
        inHouseRadio.setSelected(false);

        if(partType == PartType.OUTSOURCE) return;

        partType = PartType.OUTSOURCE;
        partTypeText.setText("Company Name");
        onValidatedInput();
    }

    /**
     * Saves the modified part data.
     * @throws IOException utilized for change scene
     */
    @FXML
    private void onSavePressed() throws IOException{
        modifiedPart.setName(nameField.getText());

        if(partType == PartType.INHOUSE) Inventory.updatePart(MainController.modifyIndex, modifiedPart);

        else if(partType == PartType.OUTSOURCE){
            Part newPart = new Outsourced(modifiedPart.getId(), modifiedPart.getName(), modifiedPart.getPrice(), modifiedPart.getStock(), modifiedPart.getMin(), modifiedPart.getMax(), outsourceCompany);
            Inventory.updatePart(MainController.modifyIndex, newPart);
        }
        Main.changeScene("/inventorysystem/view/MainForm.fxml", minError, "Inventory System", 934, 347, this);
    }

    /**
     * Throws away all changes and goes back to the main view
     * @throws IOException
     */
    @FXML
    private void onCancelPressed() throws IOException {
        Main.changeScene("/inventorysystem/view/MainForm.fxml", minError, "Inventory System", 934, 347, this);
    }

    /**
     * Sets up the index, the part data from the selected part in the main view, and displays the text in the view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        copyPartData(modifiedPart, Inventory.getAllParts().get(MainController.modifyIndex));
        setPartFields(modifiedPart);

        if(Inventory.getAllParts().get(MainController.modifyIndex) instanceof InHouse){
            onInHouseRadio();
            modifiedPart.setMachineId(((InHouse) Inventory.getAllParts().get(MainController.modifyIndex)).getMachineId());
            partTypeField.setText(String.valueOf(modifiedPart.getMachineId()));
        }
        else if(Inventory.getAllParts().get(MainController.modifyIndex) instanceof Outsourced){
            onOutsourceRadio();
            outsourceCompany = ((Outsourced) Inventory.getAllParts().get(MainController.modifyIndex)).getCompanyName();
            partTypeField.setText(outsourceCompany);
        }
    }

    /**
     * Takes one Part and copies it's parameters to another part.
     * @param copyCatPart the new instance
     * @param original the original whose data is being copied to the new instance
     */
    private void copyPartData(Part copyCatPart, Part original){
        copyCatPart.setName(original.getName());
        copyCatPart.setId(original.getId());
        copyCatPart.setMax(original.getMax());
        copyCatPart.setMin(original.getMin());
        copyCatPart.setStock(original.getStock());
        copyCatPart.setPrice(original.getPrice());

    }

    /**
     * Takes a part instance and fills the view's fields with its data.
     * @param part the part whose data is going to fill the fields
     */
    private void setPartFields(Part part) {
        idField.setText(String.valueOf(part.getId()));
        nameField.setText(part.getName());
        invField.setText(String.valueOf(part.getStock()));
        priceField.setText(String.valueOf(part.getPrice()));
        minField.setText(String.valueOf(part.getMin()));
        maxField.setText(String.valueOf(part.getMax()));

    }

    /**
     * Validates the fields and stores them in the modified part if possible.
     */
    @FXML
    private void onValidatedInput(){
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

        modifiedPart.setStock(stock);
        modifiedPart.setMin(min);
        modifiedPart.setMax(max);
        modifiedPart.setPrice(price);
        modifiedPart.setMachineId(machineID);
        outsourceCompany = outsource;
    }
}
