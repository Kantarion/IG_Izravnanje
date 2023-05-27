package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
	private RadioButton radio_klasicno;
	@FXML
	private RadioButton radio_minimalanTrag;
	@FXML
	private RadioButton datum1d;

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
	private TextField txt_s0;

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

	public void ucitaj(ActionEvent event) {
		OD.setCellValueFactory(new PropertyValueFactory<>("Od"));
		DO.setCellValueFactory(new PropertyValueFactory<>("Do"));
		VISINSKA_RAZLIKA.setCellValueFactory(new PropertyValueFactory<>("visinskaRaz"));
		DUZINA_NIVELMANSKE_STRANE.setCellValueFactory(new PropertyValueFactory<>("duzinaStrane"));
		BROJ_STANICA.setCellValueFactory(new PropertyValueFactory<>("brojStanica"));
		File proba = new File("/Users/kantarion/Desktop/matA.txt");
		try {
			FileReader fr = new FileReader(proba);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] odada = line.split(",");
				String OD = odada[0];
				String DO = odada[1];
				String VR = odada[2];
				String D = odada[3];
				VisinskaRazlika VS = new VisinskaRazlika(OD, DO, VR, D, "");
				data_vr.add(VS);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		tabela_vr.setItems(data_vr);
		tabela_vr.refresh();

		OZNAKA.setCellValueFactory(new PropertyValueFactory<>("oznaka"));
		VISINA.setCellValueFactory(new PropertyValueFactory<>("visina"));
		
		data_v.add(visina = new Visina("1", "100", true));
		data_v.add(visina = new Visina("2", "", false));
		data_v.add(visina = new Visina("3", "", false));
		data_v.add(visina = new Visina("4", "", false));
		data_v.add(visina = new Visina("5", "", false));

		radio_minimalanTrag.setSelected(true);
		txt_s0.setText("0.8");
		tabela_v.setItems(data_v);
		tabela_v.refresh();
	}

	public void popuniTabeluVr(ActionEvent event) {
		System.out.println(data_vr);

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
		if (datum1d.isSelected()) {
			visina = new Visina(txt_oznaka.getText(), txt_visina.getText(), true);
		}
		if (!datum1d.isSelected()) {
			visina = new Visina(txt_oznaka.getText(), txt_visina.getText(), false);
		}
		data_v.add(visina);
		tabela_v.setItems(data_v);
		tabela_v.refresh();
	}

	public void izravnaj(ActionEvent e) {
		MinimalniTrag1D mt = new MinimalniTrag1D(data_v, data_vr, Double.parseDouble(txt_s0.getText()));
		KlasicanNacin1D kn = new KlasicanNacin1D(data_v, data_vr, Double.parseDouble(txt_s0.getText()));
		if (radio_klasicno.isSelected()) {
			kn.napraviIzvjestaj();
		}
		if (radio_minimalanTrag.isSelected()) {
			mt.napraviIzvjestaj();
		}
	}

}
