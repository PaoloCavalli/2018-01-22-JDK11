package it.polito.tdp.SerieA.EsameSerieA;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;



import it.polito.tdp.SerieA.EsameSerieA.model.Model;
import it.polito.tdp.SerieA.EsameSerieA.model.Season;
import it.polito.tdp.SerieA.EsameSerieA.model.SeasonAndNumber;
import it.polito.tdp.SerieA.EsameSerieA.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	
    	
      Team  t = this.boxSquadra.getValue();
    	     if(t== null ) {
    	    	 txtResult.appendText("Seleziona squadra");
    	    	 return;
    	     }
    Map<Season, Integer> punteggi = model.calcolaPunteggi(t);
    txtResult.clear();
    
    for(Season s: punteggi.keySet()) {
    	txtResult.appendText(String.format("%s: %d \n", s.getDescription(), punteggi.get(s)));
      }
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    txtResult.clear();
    SeasonAndNumber annata =   this.model.calcolaAnnatoDoro();
   
    txtResult.appendText(String.format("Annata d'oro: %s:%d \n", annata.getS().getDescription(), annata.getPunti()));
    txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi \n ", this.model.nVertici(), this.model.nArchi()));
    
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    List<Season> percorso = model.camminoVirtuoso();
    txtResult.appendText(percorso.toString());
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }
    public void setModel ( Model model) {
    	this.model= model;
    	this.boxSquadra.getItems().clear();
    	this.boxSquadra.getItems().addAll(this.model.getSquadre());
    }
}