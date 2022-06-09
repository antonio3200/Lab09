package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private BordersDAO dao;
	private SimpleGraph<Country,DefaultEdge> grafo;
	private CountryMap idMap;
	private List<Country> stati;

	public Model() {
		dao= new BordersDAO();
	}
	
	public void creaGrafo(int anno) {
		idMap= new CountryMap();
		stati=dao.loadAllCountries(idMap);
		List<Border> confini= dao.getCountryPairs(idMap, anno);
		grafo= new SimpleGraph<>(DefaultEdge.class);
		//aggiungere i vertici e gli archi
		for(Border b : confini) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}
		System.out.format("Inseriti: %d vertici, %d archi\n", grafo.vertexSet().size(), grafo.edgeSet().size());
		stati= new ArrayList<>(grafo.vertexSet());
		Collections.sort(stati);
		
	}

	public List<Country> getStati() {
		if(stati==null) {
			return new ArrayList<Country>();
		}
		return stati;
	}
	
	public Map<Country,Integer> getConfiniPerStato(){
		if(grafo==null) {
			throw new RuntimeException("Grafo non esistente");
		}
		Map<Country,Integer> risultato= new HashMap<Country,Integer>();
		for(Country c : this.grafo.vertexSet()) {
			risultato.put(c, this.grafo.degreeOf(c));
		}
		return risultato;
	}
	public int componentiConnesse() {
		if(grafo==null) {
			throw new RuntimeException("Grafo non esistente");
		}
		ConnectivityInspector<Country,DefaultEdge> ci = new ConnectivityInspector<Country,DefaultEdge>(grafo);
		return ci.connectedSets().size();
	}
	
	public List<Country> getPercorso(Country nazione){
		if(!this.grafo.vertexSet().contains(nazione)) {
			throw new RuntimeException("Paese selezionato non presente nel grafo");
	}
		List<Country> nazioniRaggiungibili= this.calcolaPercorso(nazione);
		System.out.println("Stati Raggiungibili: "+nazioniRaggiungibili.size());
		return nazioniRaggiungibili;
		}
	public List<Country> calcolaPercorso(Country paeseSelezionato){
		List<Country> percorso= new LinkedList<Country>();
		
		//Visita in profondità DepthFirstIterator migliore per ricerca di un cammino 
		//Visita in ampiezza migliore per short paths
		
		GraphIterator<Country,DefaultEdge> dfi= new DepthFirstIterator<Country,DefaultEdge>(grafo,paeseSelezionato);
		while(dfi.hasNext()) {
			percorso.add(dfi.next());
		}
		return percorso;
	}
	
	
	
	

}
