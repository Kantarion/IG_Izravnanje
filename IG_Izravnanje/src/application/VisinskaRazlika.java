package application;

import javafx.beans.property.SimpleStringProperty;

public class VisinskaRazlika {

	private SimpleStringProperty Od;
	private SimpleStringProperty Do;
	private SimpleStringProperty visinskaRaz;
	private SimpleStringProperty duzinaStrane;
	private SimpleStringProperty brojStanica;

	public VisinskaRazlika(String Od, String Do, String visinskaRaz, String duzinaStrane, String brojStanica) {
		this.Od = new SimpleStringProperty(Od);
		this.Do = new SimpleStringProperty(Do);
		this.visinskaRaz = new SimpleStringProperty(visinskaRaz);
		this.duzinaStrane = new SimpleStringProperty(duzinaStrane);
		this.brojStanica = new SimpleStringProperty(brojStanica);
	}

	public String getOd() {
		return Od.get();
	}

	public String getDo() {
		return Do.get();
	}

	public String getVisinskaRaz() {
		return visinskaRaz.get();
	}

	public String getDuzinaStrane() {
		return duzinaStrane.get();
	}

	public String getBrojStanica() {
		return brojStanica.get();
	}

	@Override
	public String toString() {
		return Od + " - " + Do;
	}
}
