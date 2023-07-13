package application;

import javafx.beans.property.SimpleStringProperty;

public class Visina {
	
	private SimpleStringProperty oznaka;
	private SimpleStringProperty visina;
	private boolean definise_datum;
	
	public Visina(String oznaka, String visina, boolean definise_datum) {
		this.oznaka = new SimpleStringProperty(oznaka);
		this.visina = new SimpleStringProperty(visina);
		this.definise_datum = definise_datum;
	}

	public String getOznaka() {
		return oznaka.get();
	}

	public String getVisina() {
		return visina.get();
	}
	

	
	public boolean definiseDatum() {
		return definise_datum;
	}

	public void setOznaka(String oznaka) {
		this.oznaka.set(oznaka);
	}

	public void setVisina(String visina) {
		this.visina.set(visina);
	}

	public void setDefinise_datum(boolean definise_datum) {
		this.definise_datum = definise_datum;
	}

	public boolean isDefinise_datum() {
		return definise_datum;
	}

	
	
}
