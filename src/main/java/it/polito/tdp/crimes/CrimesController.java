/**
 /**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	  		
    	Integer anno = this.boxAnno.getValue();
    	String catID = this.boxCategoria.getValue();
    	
    	if(anno!=null && catID!=null) {
    		
    	model.creaGrafo(catID,anno);
    		
    	this.txtResult.setText("GRAFO CREATO\nVertici: " + model.numeroVertici() + "\nArchi: " + model.numeroArchi() + "\nArchi con peso massimo:\n");
    		
    	List<Adiacenza> archi = this.model.getArchi();
    	
    	for (Adiacenza a:archi) {
    		txtResult.appendText(a.toString() + "\n");
    	}
    		
    	this.boxArco.getItems().addAll(archi);
    	
    	this.boxArco.setDisable(false);
    	this.btnPercorso.setDisable(false);
    	
    	} else {
    		txtResult.setText("Selezionare i valori dalle apposite tendine!");
    		return;
    	}
    		  	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	txtResult.clear();

    	Adiacenza a = this.boxArco.getValue();
    		
    	if (a!= null) {
    		List <String> res = model.camminoMassimo(a);
    		
    		if (res.size()>0) {
    		
    		txtResult.appendText("Cammino trovato (peso = " + model.pesoCammino() + "):\n");
    		for (String s:res) 
    			txtResult.appendText(s + "\n");
    		} else {
    			txtResult.setText("Cammino non trovato.");
    		}
    		
    		
    		
    	} else {
    		txtResult.setText("Selezionare arco per il percorso");
    		return;
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for (int i=2014;i<=2017;i++)
    		this.boxAnno.getItems().add(i);
    	
    	
    	this.boxCategoria.getItems().addAll(model.listAllCategories());
    	
    	this.boxArco.setDisable(true);
    	this.btnPercorso.setDisable(true);
    }
}
