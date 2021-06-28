package it.polito.tdp.crimes.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo; 
	private List<Adiacenza> archi;
	
	private List<String> camminoMassimo;
	private int pesoMinimo;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<Date> listAllDate(){
			return dao.listAllDate();
	}
	
	public List<String> listAllCategories(){
		return dao.listAllCategories();
	}
	
	public void creaGrafo(String catID, Integer anno) { 
		
		this.archi = new ArrayList<Adiacenza>();
		int pesoMassimo = 0;
		int pesoMinimo = 10000;
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
			
		Graphs.addAllVertices(grafo, dao.listVertices(catID, anno)); 
		
		for(Adiacenza a:dao.listAdiacenze(catID, anno)) { 
			if (!grafo.containsEdge(grafo.getEdge(a.getId1(), a.getId2()))) {
			Graphs.addEdge(this.grafo, a.getId1(), a.getId2(), a.getPeso());
			if (a.getPeso()>pesoMassimo)
				pesoMassimo = (int)a.getPeso();
			}
		}		
		
		for(Adiacenza a:dao.listAdiacenze(catID, anno)) { 
			if ((int)a.getPeso()==pesoMassimo)
				this.archi.add(a);
		}
			
	}
		
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}

	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public List<Adiacenza> getArchi(){						
		return this.archi;
	}
	
	public List<String> camminoMassimo(Adiacenza a){
		
		this.camminoMassimo = new ArrayList<String>();
		this.pesoMinimo = 100000;
		String partenza = a.getId1();
		String arrivo = a.getId2();
		
		List<String> parziale = new ArrayList<String>();
		parziale.add(partenza);
		
		recursive(parziale,arrivo,0);
		
		return this.camminoMassimo;
	}
	
	public int pesoCammino() {
		return this.pesoMinimo;
	}
	
	private void recursive(List<String> parziale, String arrivo, int pesoParziale) {
		
		if (parziale.get(parziale.size()-1).equals(arrivo) && pesoParziale < this.pesoMinimo) {
			
			if (parziale.size()>camminoMassimo.size()) {
				this.pesoMinimo = pesoParziale;
				this.camminoMassimo = new ArrayList<String>(parziale);
			}
		}
		
		//System.out.println(parziale);
		
		for (String s:Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			int pesoAggiuntivo = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), s));
			
			if (!parziale.contains(s)) {
		
				pesoParziale += pesoAggiuntivo;
				parziale.add(s);
				recursive(parziale,arrivo,pesoParziale);
				
				pesoParziale -= pesoAggiuntivo;
				parziale.remove(s);								
			}									
		}	
	}
		
}
