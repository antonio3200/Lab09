
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	Model model;

    public void setModel(Model model) {
		this.model = model;
	}

	@FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="boxStati"
    private ComboBox<Country> boxStati; // Value injected by FXMLLoader

    @FXML // fx:id="btnRaggiungi"
    private Button btnRaggiungi; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	int anno;
    	try {
    		anno=Integer.parseInt(txtAnno.getText());
    		if((anno<1816) || (anno>2016)) {
    			txtResult.setText("Inserire un anno tra 1816 e 2016");
        		return;
    		}
    	}
    	catch(NumberFormatException e) {
    		txtResult.setText("Inserire un anno tra 1816 e 2016");
    		return;
    	}
    	
    	try {
    		model.creaGrafo(anno);
    		List<Country> stati= model.getStati();
    		boxStati.getItems().addAll(stati);
    		txtResult.appendText(String.format("Numero componenti connesse al grafo %d\n: ",model.componentiConnesse()));
    		Map<Country,Integer> statistiche= model.getConfiniPerStato();
    		for(Country c : statistiche.keySet()) {
    			txtResult.appendText(String.format("%s %d\n", c.getNome(), statistiche.get(c)));
    		}
    		
    	}
    	catch(RuntimeException rt) {
    		txtResult.appendText("Errore "+rt.getMessage());
    		return;
    	}
    	
    }

    @FXML
    void doRaggiungi(ActionEvent event) {
    	txtResult.clear();
    	if(this.boxStati.getItems().isEmpty()) {
    		txtResult.setText("Box nazioni è vuoto,crea il grafo o seleziona un anno con nazioni confinanti");
    	}
    	Country stato= this.boxStati.getSelectionModel().getSelectedItem();
    	if(stato==null) {
    		txtResult.setText("ERRORE:selezionare uno stato");
    	}
    	try {
    		List<Country> stati= this.model.getPercorso(stato);
    		for(Country c: stati) {
    			txtResult.appendText(String.format("%s\n",c));
    		}
    	}
    	catch(RuntimeException e) {
    		txtResult.setText("ERRORE:nazione selezionata non nel grafo");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxStati != null : "fx:id=\"boxStati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRaggiungi != null : "fx:id=\"btnRaggiungi\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

