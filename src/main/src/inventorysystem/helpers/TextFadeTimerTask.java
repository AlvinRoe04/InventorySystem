package inventorysystem.helpers;

import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Helper class that erases error text after a set amount of time
 * @author Alvin Roe
 */
public class TextFadeTimerTask extends TimerTask {
    /**
     * List of Error Texts to display.
     */
    private ArrayList<Text> errorTextList = new ArrayList<Text>();

    /**
     * Constructor creates the error text
     * @param errorTextDisplay a single error text to display
     * @param errorText the error message to write
     */
    public TextFadeTimerTask(Text errorTextDisplay, String errorText){
        this.errorTextList.add(errorTextDisplay);
        errorTextDisplay.setText(errorText);
    }

    /**
     * Ran after the timer is over. Erases all of the error texts.
     */
    @Override
    public void run() {
        for(int i = 0; i < errorTextList.size(); i++) errorTextList.get(i).setText("");
    }
}
