package application;


import java.util.Random;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
	
	
	private ObservableList<VisinskaRazlika> data_vr = FXCollections.observableArrayList();
	private ObservableList<Visina> data_v = FXCollections.observableArrayList();
	private Visina visina;
	private VisinskaRazlika visinskaRazlika;
	
	@FXML
	private Label myLabel;
	@FXML
	private TextField txt_od;
	@FXML
	private TextField txt_do;
	@FXML
	private TextField txt_visinskaRazlika;
	@FXML
	private TextField txt_duzinaStrane;
	@FXML
	private TextField txt_brojStanica;
	@FXML
	private TextField txt_oznaka;
	@FXML
	private TextField txt_visina;
	
	@FXML
	private TableView<VisinskaRazlika> tabela_vr;
	@FXML
    public TableColumn OD;
	@FXML
    public TableColumn DO;
	@FXML
    public TableColumn VISINSKA_RAZLIKA;
	@FXML
    public TableColumn DUZINA_NIVELMANSKE_STRANE;
	@FXML
    public TableColumn BROJ_STANICA;
	
	@FXML
	private TableView<Visina> tabela_v;
	@FXML
    public TableColumn OZNAKA;
	@FXML
    public TableColumn VISINA;
	
	
	
	public void popuniTabeluVr(ActionEvent event) {
		OD.setCellValueFactory(new PropertyValueFactory<>("Od"));
		DO.setCellValueFactory(new PropertyValueFactory<>("Do"));
		VISINSKA_RAZLIKA.setCellValueFactory(new PropertyValueFactory<>("visinskaRaz"));
		DUZINA_NIVELMANSKE_STRANE.setCellValueFactory(new PropertyValueFactory<>("duzinaStrane"));
		BROJ_STANICA.setCellValueFactory(new PropertyValueFactory<>("brojStanica"));
		visinskaRazlika = new VisinskaRazlika(txt_od.getText(), txt_do.getText(), txt_visinskaRazlika.getText(),
											  txt_duzinaStrane.getText(), txt_brojStanica.getText());
		data_vr.add(visinskaRazlika);
		tabela_vr.setItems(data_vr);
		tabela_vr.refresh();
	}
	
	public void popuniTabeluV(ActionEvent event) {
		OZNAKA.setCellValueFactory(new PropertyValueFactory<>("oznaka"));
		VISINA.setCellValueFactory(new PropertyValueFactory<>("visina"));
		visina = new Visina(txt_oznaka.getText(), txt_visina.getText());
		data_v.add(visina);
		tabela_v.setItems(data_v);
		tabela_v.refresh();
	}
	
	public void izravnaj(ActionEvent e) {
		MinimalniTrag1D mt = new MinimalniTrag1D(data_v, data_vr);
		mt.izracunajPriblizneVisine();
	}

}
