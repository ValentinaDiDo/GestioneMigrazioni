package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue; //CODA DEGLI EVENTI
	
	// PARAMETRI DI SIMULAZIONE
	private int nInizialeMigranti;
	private Country nazioneIniziale;
	
	// OUTPUT DELLA SIMULAZIONE
	private int nPassi;
	private Map<Country, Integer> persone; //QUANTI MIGRANTI CI SONO IN OGNI NAZIONE ALLA FINE DELLA SIMULAZIONE 
	//OPPURE List<CountryAndNumber> personeStanziali;
	
	// DTSTO DEL MONDO SIMULATO
	private Graph<Country, DefaultEdge> grafo;
	
	//COSTRUTTORE
	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void inizializza(Country partenza, int migranti) {
		this.nazioneIniziale = partenza;
		this.nInizialeMigranti = migranti;
		
		this.persone = new HashMap<Country, Integer>();
		for(Country c : this.grafo.vertexSet()) {
			this.persone.put(c, 0);
		}
		
		this.queue.add(new Evento(1, this.nazioneIniziale, this.nInizialeMigranti));
	}
	
	//METODO ESECUZIONE SIMULAZIONE
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll(); //ESTRAGGO LA TESTA DELLA CODA
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		int stanziali  = e.getNumPersone() / 2;
		int migranti = e.getNumPersone() - stanziali;
		int confinanti = this.grafo.degreeOf(e.getCountry());
		int gruppiMigranti = migranti / confinanti;
		
		this.persone.put(e.getCountry(), this.persone.get(e.getCountry())+stanziali);
		
		if (gruppiMigranti != 0) {
			for (Country vicino : Graphs.neighborListOf(this.grafo, e.getCountry())) {
				this.queue.add(new Evento(e.getTime() + 1, vicino, gruppiMigranti));
			}
		}
	}
	
	public int getnPassi() {
		return nPassi;
	}

	public Map<Country, Integer> getPersone() {
		return persone;
	}
	
	
	
	
	
}