package it.polito.tdp.SerieA.EsameSerieA.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


import it.polito.tdp.SerieA.EsameSerieA.db.SerieADAO;

public class Model {
	
private SerieADAO dao = new SerieADAO();


private List<Team> squadre;
private List<Season> stagioni;
private Map <Integer,Season> idMapSeason;
private Map <String, Team> idMapTeam;
//punto 2 
private List<Season> consecutive;
private List<Season> ottima;

private Team squadraSelezionata;

private Graph<Season, DefaultWeightedEdge> grafo;

private Map <Season, Integer> punteggi;


public Model() {
	
	
	
	
	this.squadre = this.dao.listTeams();
	idMapTeam = new HashMap<String, Team>();
    for(Team t : squadre) {
    	idMapTeam.put(t.getTeam(), t);
    }
    
    this.stagioni = this.dao.listAllSeasons();
    idMapSeason = new HashMap<Integer, Season>();
    for(Season s : stagioni) {
    	idMapSeason.put(s.getSeason(), s);
    }
    
}
public List<Team> getSquadre(){
	return squadre;
}
public Map<Season, Integer>  calcolaPunteggi(Team squadra) {
    
	this.squadraSelezionata = squadra;
	this.punteggi = new HashMap<Season, Integer>();
	
	
	List<Match> partite = dao.listMatchesForTeam(squadra, idMapSeason, idMapTeam);
	//per ogni partita aggiorno la mappa
	for(Match m : partite) {
		
		Season stagione = m.getSeason();
		int punti=0;
		if(m.getFtr().equals("D")) {
			punti = 1;
		}else {
			if((m.getHomeTeam().equals(squadra) && m.getFtr().equals("H")) || 
			  (m.getAwayTeam().equals(squadra) && m.getFtr().equals("A")) ){
		        punti = 3;		
		    }
		}
		
		Integer  attuale = punteggi.get(stagione);
		//Evita una nullPointer perché la prima occorenza della mappa punteggi sarà null.
		if(attuale == null) {
			attuale = 0;
		}
		punteggi.put(stagione, attuale + punti);
	}
	return punteggi;
}
public SeasonAndNumber calcolaAnnatoDoro() {
	 
	grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	
	/*for(Season s: punteggi.keySet()) {
		if(!this.grafo.containsVertex(s)) {
			this.grafo.addVertex(s);
		}
	}*/
	Graphs.addAllVertices(this.grafo, punteggi.keySet());
	
	//ARCHI 
	for(Season s1: punteggi.keySet()) {
		for(Season s2: punteggi.keySet()) {
			if(!s1.equals(s2) ) {
				if(!this.grafo.containsEdge(s1,s2) && !this.grafo.containsEdge(s2,s1) ) {
				Integer punti1= punteggi.get(s1);
				Integer punti2 = punteggi.get(s2);
				if(punti1 > punti2 ) {
					Graphs.addEdge(this.grafo, s2, s1, (Integer) punti1-punti2);
				}else {
					Graphs.addEdge(this.grafo, s1, s2, (Integer) punti2-punti1);
				}
			  }
			}
		}
	}
//ANNATA MIGLIORE
	Season migliore= null;
	Integer puntiMax=0;
    SeasonAndNumber dorata=null;
    
	for(Season s : grafo.vertexSet()) {
	 Integer valore = pesoStagione(s);
	 if(valore > puntiMax) {
		 puntiMax= valore;
		 migliore   = s;
	 }
   }
   	
   dorata = new SeasonAndNumber(migliore, puntiMax);
   
   
   return dorata;
  
}

public Integer pesoStagione(Season s) {
	int somma=0; // Puoi anche incrementare la variabile somma e decrementarla
	int pesiEntranti=0;
	int pesiUscenti = 0;
	//Uso i vertici ma potrei usare gli archi!!
    for(Season s1: Graphs.predecessorListOf(this.grafo, s)) {
    	DefaultWeightedEdge e = this.grafo.getEdge(s1, s);
    	pesiEntranti += (int) this.grafo.getEdgeWeight(e);
    }
    for(Season s1: Graphs.successorListOf(this.grafo, s)) {
    	DefaultWeightedEdge e = this.grafo.getEdge(s, s1);
    	pesiUscenti += (int) this.grafo.getEdgeWeight(e);
    }
	/*for(DefaultWeightedEdge e :this.grafo.incomingEdgesOf(s)) {
		somma += (int) grafo.getEdgeWeight(e);
	}
	for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(s)) {
		somma -= (int) grafo.getEdgeWeight(e);
	}*/
	
    somma= pesiEntranti-pesiUscenti;
	return somma;
}
public int nVertici() {
return this.grafo.vertexSet().size();
}
public int nArchi() {
return this.grafo.edgeSet().size();
}
//Punto 2 Ricorsione
//trova stagioni consecutive
public List<Season> camminoVirtuoso() {
	this.consecutive = new ArrayList<>(punteggi.keySet());
	Collections.sort(consecutive);
	
	List<Season> parziale = new ArrayList<Season>();
	this.ottima = new ArrayList<Season>();
	//per esserci un ultimo il livello minimo va inizializzato a 1
	//itera a livello 0 la ricorsione
	for(Season s: grafo.vertexSet()) {
		parziale.add(s);
		ricorsiva(1,parziale);
		parziale.remove(0);
	}
	return ottima;
	
}
/*Ricorsione
 * 
 * Soluzione parziale List di Season
 * Livello : lunghezza della lista
 * Casi terminali : non trovo altri vertici da aggiungere 
 * ---> verifica se il cammino ha lunghezza Massima tra quelli visti fin'ora
 * Generazioni delle soluzioni : vertcici connessi all'ultimo verso del percorso
 * (con arco orientato nel verso giusto)  non ancora nel percorso, relative a stagioni consecutive
 * */
  private void ricorsiva(int livello, List<Season> parziale) {
	  boolean trovato = false;
	  //genera nuove soluzioni
	  Season ultimo = parziale.get(livello-1);
	  
	  for(Season prossimo :Graphs.successorListOf(grafo, ultimo)) {
		  if(!parziale.contains(prossimo) ) {
			if(consecutive.indexOf(ultimo) +1 == consecutive.indexOf(prossimo)) {
				//candidato accettabile ricorri
				trovato = true;
				parziale.add(prossimo);
				ricorsiva(livello+1, parziale);
				parziale.remove(livello);
			}
		  }
	  }
	  //valuta caso terminale
	  if(!trovato) {
		if(parziale.size()>ottima.size()) {
		    ottima = new ArrayList<>(parziale);
		}
	  }
  }
}
