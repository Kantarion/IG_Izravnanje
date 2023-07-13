package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import application.Tabela.Block;
import application.Tabela.Board;
import application.Tabela.Table;

import java.nio.file.Files;
import java.io.PrintWriter;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class MinimalniTrag1D {

	private static int DEFEKT = 1;

	private ObservableList<Visina> visine;
	private ObservableList<VisinskaRazlika> visinske_razlike;
	private double s0;
	DecimalFormat df = new DecimalFormat("###.#####");

	private Matrix matrica_A;
	private Matrix matrica_P;
	private Matrix vektor_f;
	private Matrix matrica_N;
	private Matrix vektor_n;
	private Matrix matrica_BT;
	private Matrix matrica_Qx;
	private Matrix vektor_x;
	private Matrix vektor_v;
	private Matrix matrica_Ql;
	private Matrix matrica_Qv;
	private Matrix matrica_r;
	private Matrix popravljena_mjerenja;
	private Matrix ocjenjene_visine;
	private Matrix standardi_visina;

	// Ovo treba da sacuva u fajl
	private double s_ocjenjeno;
	private double niz_v[][]; // mm
	private double niz_loc[][]; // m
	private double niz_Qll[][]; // mm^2
	private double niz_Qvv[][]; // mm^2
	private double niz_rii[][]; // Prikazuje se bez jedinice mjere
	private double niz_x[][]; // mm
	private double niz_ocjenjeneVisine[][]; // m
	private double niz_standardnoOdstupanjeVisina[][]; // mm

	public MinimalniTrag1D(ObservableList<Visina> visine, ObservableList<VisinskaRazlika> visinske_razlike, double s0) {
		this.visine = visine;
		this.visinske_razlike = visinske_razlike;
		this.s0 = s0;
	}

	public void napraviIzvjestaj() {
		izracunajPriblizneVisine();
		formirajMatricuA();
		formirajMatricuP();
		formirajVektorf();
		izracunajMatricuN();
		izracunajVektorn();
		formirajMatricuBT();
		izracunajMatricuQx();
		izracunajVektorx();
		izracunajVektorv();
		izracunajStandardnoOdstupanje();
		izracunajMatricuQl();
		izracunajMatricuQv();
		izracunajMatricur();
		izracunajPopravljenaMjerenja();
		izracunajOcjenjeneVisine();
		izracunajStandardeVisina();

		niz_v = vektor_v.getMatrix();
		niz_loc = popravljena_mjerenja.getMatrix();
		niz_Qll = matrica_Ql.getDiagonal().getMatrix();
		niz_Qvv = matrica_Qv.getDiagonal().getMatrix();
		niz_rii = matrica_r.getDiagonal().getMatrix();
		niz_x = vektor_x.getMatrix();
		niz_ocjenjeneVisine = ocjenjene_visine.getMatrix();
		niz_standardnoOdstupanjeVisina = standardi_visina.getMatrix();

		try {
			proba();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// izvjestaj();

	}


	// ispisivanje u txt u obliku tabele
	public void proba() throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);

		// za provjere 
		// File proba = new File("/Users/kantarion/Desktop/proba.txt");


		// krajnji kod sa prozorom za cuvanje
		File izvjestaj = fileChooser.showSaveDialog(null);


		List<List<String>> lista1 = new ArrayList<>();
		List<String> red1 = new ArrayList<>();
		for (int i = 0; i < niz_v.length; i++) {
			red1.add(visinske_razlike.get(i).getOd());
			red1.add(visinske_razlike.get(i).getDo());
			red1.add(df.format(niz_v[i][0]));
			red1.add(df.format(niz_Qvv[i][0]));
			red1.add(df.format(niz_loc[i][0]));
			red1.add(df.format(niz_Qll[i][0]));
			red1.add(df.format(niz_rii[i][0]));
			if (i % 1 == 0) { // Ako je svaki treći element, dodaj red u listu i stvori novi red
				lista1.add(red1);
				red1 = new ArrayList<>();
			}
		}

		// Dodavanje posljednjeg reda ako je potrebno
		if (!red1.isEmpty()) {
			lista1.add(red1);
		}

		FileWriter fw = new FileWriter(izvjestaj);
		String jed = new String(
				"==================================================================================");

		List<String> header1 = Arrays.asList("OD", "DO", "V[mm]", "Qvii[mm2]", "loc[m]", "Qlii[mm2]", "rii");
		Board board1 = new Board(100);
		Table table1 = new Table(board1, 20, header1, lista1);
		List<Integer> colWidthsList1 = Arrays.asList(10, 10, 10, 10, 10, 10, 10);
		table1.setColWidthsList(colWidthsList1);
		Block tableBlock1 = table1.tableToBlocks();
		board1.setInitialBlock(tableBlock1);
		board1.build();
		String tabela1 = board1.getPreview();

		fw.write("Datum definisu sljedece tacke\n" + "BR. TAC.  H[m]\n" + "Ukupan broj tacaka koje odredjuju datum\n"
				+ "Broj mjerenih velicina je n= " + visinske_razlike.size() + "Broj nepoznatih parametara !"
				+ "sigma apriori =!" + "sigma = " + s0 + "\n" + jed
				+ "\nOcjene dobijene iz izravnanja i kreterijumi kvaliteta i tacnosti\n");
		fw.write(tabela1);
		fw.write("\n" + jed + "\nUsvojeni nivo znacajnosti je: \n" + "Globalni test adekvatnosti modela\n"
				+ "Vrijednosti testa nulta hipoteze\n" + "Dozvoljena vrijednosti jednacine\n"
				+ "Suma rii=\n" + jed + "\nOCJENE NEPOZNATIH PARAMETARA SA OCJENOM TACNOSTI\n");

		List<List<String>> lista2 = new ArrayList<>();
		List<String> red2 = new ArrayList<>();
		for (int i = 0; i < niz_x.length; i++) {
			red2.add(visine.get(i).getOznaka());
			red2.add(df.format(niz_x[i][0]));
			red2.add(df.format(niz_ocjenjeneVisine[i][0]));
			red2.add(df.format(niz_standardnoOdstupanjeVisina[i][0]));
			if (i % 1 == 0) { // Ako je svaki treći element, dodaj red u listu i stvori novi red
				lista2.add(red2);
				red2 = new ArrayList<>();
			}
		}

		// Dodavanje posljednjeg reda ako je potrebno
		if (!red2.isEmpty()) {
			lista2.add(red2);
		}

		List<String> header2 = Arrays.asList("BR.TACKE", "x[mm]", "X[m]", "mx[mm]");
		Board board2 = new Board(100);
		Table table2 = new Table(board2, 20, header2, lista2);
		List<Integer> colWidthsList2 = Arrays.asList(10, 10, 10, 10);
		table2.setColWidthsList(colWidthsList2);
		Block tableBlock2 = table2.tableToBlocks();
		board2.setInitialBlock(tableBlock2);
		board2.build();
		String tabela2 = board2.getPreview();

		fw.write(tabela2);
		fw.close();

		// OTVARANJE DATOTEKE

		// first check if Desktop is supported by Platform or not
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (izvjestaj.exists())
			try {
				desktop.open(izvjestaj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// let's try to open PDF file
		if (izvjestaj.exists())
			try {
				desktop.open(izvjestaj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}


	// ispisivanje u txt klasicno sa namjestanjem piksela
	private void izvjestaj() {
		int prva = niz_v.length;
		int druga = niz_x.length;

		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);

		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			try {
				FileWriter fw = new FileWriter(file);
				String jed = new String(
						"==================================================================================");
				fw.write(jed);
				fw.write("\nДатум дефинишу сљедеће тачке");
				fw.write("\nБР. ТАЧ. H[m]");
				// fw.write("\n"+visine.get(prva).getOznaka()+" "+visine.get(prva).getVisina());
				fw.write("\nУкупан број тачака које одређују датум је  ?");
				fw.write("\nБрој мјерених величина је n=" + visinske_razlike.size());
				fw.write("\nБрој непознатих параметара је ?");
				fw.write("\nсигма априори=1000  ?");
				fw.write("\nсигма = " + s0 + "\n");
				fw.write(jed);
				fw.write("\nОцјене добијене из изравнања и кретеријуми квалитета и тачности\n");
				fw.write(String.format("%1s %10s %10s %12s %10s %12s %10s %10s", "OD", "DO", "V[mm]", "Qvii[mm2]",
						"loc[m]",
						"Qlii[mm2]", "rii", "u-v"));
				for (int i = 0; i < prva; i++) {
					fw.write("\n");
					fw.write(String.format("%1s %10s %10s %12s %10s %12s %10s %12s",
							visinske_razlike.get(i).getOd(),
							visinske_razlike.get(i).getDo(), df.format(niz_v[i][0]),
							df.format(niz_Qvv[i][0]), df.format(niz_loc[i][0]), df.format(niz_Qll[i][0]),
							df.format(niz_rii[i][0]), "0,00000"));
				}
				fw.write("\n" + jed + "\nУсвојени ниво значајности је:0,05\n" + "Глобални тест адекватности модела \n"
						+ "Вриједност теста нулте хипотезе је:1.2341\n" + "Дозвољена вриједност јеч: 3.0798\n"
						+ "Сумаrii=10,0000\n" + jed + " \n" + "ОЦЈЕНЕ НЕПОЗНАТИХ ПАРАМЕТАРА СА ОЦЈЕНОМ ТАЧНОСТИ \n"
						+ "");
				fw.write(String.format("%1s %10s %10s %12s", "Бр.тачке", "x[mm]", "X[m]", "mx[mm]"));
				for (int i = 0; i < druga; i++) {
					fw.write("\n");
					fw.write(String.format("%1s %15s %15s %12s", visine.get(i).getOznaka(), df.format(niz_x[i][0]),
							df.format(niz_ocjenjeneVisine[i][0]), df.format(niz_standardnoOdstupanjeVisina[i][0])));
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("ne ide");
				e.printStackTrace();
			}
		}

		File izvjestaj = new File("/Users/kantarion/Desktop/izvjestaj.txt");
		// try {
		// FileWriter fw = new FileWriter(izvjestaj);
		// String jed = new
		// String("============================================================");
		// fw.write(jed);
		// fw.write("\nДатум дефинишу сљедеће тачке");
		// fw.write("\nБР. ТАЧ. H[m]");
		// // fw.write("\n"+visine.get(prva).getOznaka()+"
		// "+visine.get(prva).getVisina());
		// fw.write("\nУкупан број тачака које одређују датум је ?");
		// fw.write("\nБрој мјерених величина је n=" + visinske_razlike.size());
		// fw.write("\nБрој непознатих параметара је ?");
		// fw.write("\nсигма априори=1000 ?");
		// fw.write("\nсигма = "+s0+"\n");
		// fw.write(jed);
		// fw.write("\nОцјене добијене из изравнања и кретеријуми квалитета и
		// тачности\n");
		// fw.write(String.format("%1s %10s %10s %12s %10s %12s %10s %10s", "OD", "DO",
		// "V[mm]", "Qvii[mm2]", "loc[m]",
		// "Qlii[mm2]", "rii", "u-v"));
		// for (int i = 0; i < prva; i++) {
		// fw.write("\n");
		// fw.write(String.format("%1s %10s %10s %12s %10s %12s %10s",
		// visinske_razlike.get(i).getOd(),
		// visinske_razlike.get(i).getDo(), df.format(niz_v[i][0]),
		// df.format(niz_Qvv[i][0]), df.format(niz_loc[i][0]),df.format(niz_Qll[i][0]),
		// df.format(niz_rii[i][0])));
		// }
		// fw.write("\n" + jed + "\nУсвојени ниво значајности је:0,05\n" + "Глобални
		// тест адекватности модела \n"
		// + "Вриједност теста нулте хипотезе је:1.2341\n" + "Дозвољена вриједност јеч:
		// 3.0798\n"
		// + "Сумаrii=10,0000\n" + jed + " \n" + "ОЦЈЕНЕ НЕПОЗНАТИХ ПАРАМЕТАРА СА
		// ОЦЈЕНОМ ТАЧНОСТИ \n" + "");
		// fw.write(String.format("%1s %10s %10s %12s", "Бр.тачке", "x[mm]", "X[m]",
		// "mx[mm]"));
		// for (int i = 0; i < druga; i++) {
		// fw.write("\n");
		// fw.write(String.format("%1s %10s %10s %12s", visine.get(i).getOznaka(),
		// df.format(niz_x[i][0]),
		// df.format(niz_ocjenjeneVisine[i][0]),
		// df.format(niz_standardnoOdstupanjeVisina[i][0])));
		// }
		// fw.close();
		// } catch (IOException e) {
		// System.out.println("ne ide");
		// e.printStackTrace();
		// }

		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (izvjestaj.exists())
			try {
				desktop.open(izvjestaj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// let's try to open PDF file
		if (izvjestaj.exists())
			try {
				desktop.open(izvjestaj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void izracunajPriblizneVisine() {
		ObservableList<Visina> poznate_visine = FXCollections.observableArrayList();
		for (int i = 0; i < visine.size(); i++) {
			if (!visine.get(i).getVisina().equals("")) {
				poznate_visine.add(visine.get(i));
			}
		}

		for (int i = 0; i < visine.size(); i++) {
			if (visine.get(i).getVisina().equals("")) {
				for (int j = 0; j < visinske_razlike.size(); j++) {
					if (visine.get(i).getOznaka().equals(visinske_razlike.get(j).getDo())) { // Prvi slucaj
						try {
							for (int k = 0; k < poznate_visine.size(); k++) {
								if (visinske_razlike.get(j).getOd().equals(poznate_visine.get(k).getOznaka())) {
									visine.get(i).setVisina(
											Double.toString(Double.parseDouble(poznate_visine.get(k).getVisina())
													+ Double.parseDouble(visinske_razlike.get(j).getVisinskaRaz())));
									break;
								}
							}
						} catch (Exception e) {
							// Uradi nesto?
						}
					} else if (visine.get(i).getOznaka().equals(visinske_razlike.get(j).getOd())) { // Drugi slucaj
						try {
							for (int k = 0; k < poznate_visine.size(); k++) {
								if (visinske_razlike.get(j).getDo().equals(poznate_visine.get(k).getOznaka())) {
									visine.get(i).setVisina(
											Double.toString(Double.parseDouble(poznate_visine.get(k).getVisina())
													- Double.parseDouble(visinske_razlike.get(j).getVisinskaRaz())));
									break;
								}
							}
						} catch (Exception e) {
							// Uradi nesto?
						}
					}
				}
			}
		}
	}

	private void formirajMatricuA() {
		int n = visine.size();
		int u = visinske_razlike.size();
		double niz[][] = new double[u][n];

		for (int i = 0; i < u; i++) {
			for (int j = 0; j < n; j++) {
				String OD = visinske_razlike.get(i).getOd();
				String DO = visinske_razlike.get(i).getDo();
				if (visine.get(j).getOznaka().equals(OD)) {
					niz[i][j] = -1;
				} else if (visine.get(j).getOznaka().equals(DO)) {
					niz[i][j] = 1;
				} else {
					niz[i][j] = 0;
				}
			}
		}

		matrica_A = new Matrix(niz);
	}

	private void formirajMatricuP() {
		int u = visinske_razlike.size();
		double niz[][] = new double[u][u];

		// Ako je dat broj stanica onda je p=1/n
		if (visinske_razlike.get(0).getDuzinaStrane().equals("")) {
			for (int i = 0; i < u; i++) {
				for (int j = 0; j < u; j++) {
					if (i == j) {
						niz[i][j] = 1 / Double.parseDouble(visinske_razlike.get(i).getBrojStanica());
					} else {
						niz[i][j] = 0;
					}
				}
			}
		}

		// Ako je data duzina nivelmanske strane onda je p=1/d[km]
		if (visinske_razlike.get(0).getBrojStanica().equals("")) {
			for (int i = 0; i < u; i++) {
				for (int j = 0; j < u; j++) {
					if (i == j) {
						niz[i][j] = 1 / (Double.parseDouble(visinske_razlike.get(i).getDuzinaStrane()) / 1000);
					} else {
						niz[i][j] = 0;
					}
				}
			}
		}

		matrica_P = new Matrix(niz);
	}

	private void formirajVektorf() {
		int n = visinske_razlike.size();
		double niz[][] = new double[n][1];

		for (int i = 0; i < n; i++) {
			double visinska_razlika = Double.parseDouble(visinske_razlike.get(i).getVisinskaRaz());
			String OD = visinske_razlike.get(i).getOd();
			String DO = visinske_razlike.get(i).getDo();
			niz[i][0] = (nadjiVisinu(DO) - nadjiVisinu(OD) - visinska_razlika) * 1000;
		}

		vektor_f = new Matrix(niz);

	}

	private void izracunajMatricuN() {
		Matrix AT = matrica_A.transpose();
		matrica_N = AT.multiply(matrica_P.multiply(matrica_A));
	}

	private void izracunajVektorn() {
		Matrix AT = matrica_A.transpose();
		vektor_n = AT.multiply(matrica_P.multiply(vektor_f));
	}

	private void formirajMatricuBT() {
		int u = visine.size();
		double niz[][] = new double[1][u];

		for (int i = 0; i < u; i++) {
			niz[0][i] = 1;
		}

		matrica_BT = new Matrix(niz);
	}

	private void izracunajMatricuQx() {
		double niz_N[][] = matrica_N.getMatrix();
		int n = visine.size();
		double niz_Nprosireno[][] = new double[n + 1][n + 1];
		double niz_BT[][] = matrica_BT.getMatrix();
		double niz_B[][] = matrica_BT.transpose().getMatrix();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				niz_Nprosireno[i][j] = niz_N[i][j];
			}
		}
		for (int i = 0; i < n; i++) {
			niz_Nprosireno[n][i] = niz_BT[0][i];
		}
		for (int i = 0; i < n; i++) {
			niz_Nprosireno[i][n] = niz_B[i][0];
		}
		niz_Nprosireno[n][n] = 0;

		Matrix N_prosireno = new Matrix(niz_Nprosireno);
		Matrix Qx_prosireno = N_prosireno.inverse();
		double niz_Qxprosireno[][] = Qx_prosireno.getMatrix();
		double niz_Qx[][] = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				niz_Qx[i][j] = niz_Qxprosireno[i][j];
			}
		}

		matrica_Qx = new Matrix(niz_Qx);
		System.out.println(matrica_Qx.getMatrix());
		System.out.println(Qx_prosireno.getMatrix());

	}

	private void izracunajVektorx() {
		vektor_x = matrica_Qx.multConst(-1).multiply(vektor_n);
	}

	private void izracunajVektorv() {
		vektor_v = (matrica_A.multiply(vektor_x)).add(vektor_f);
	}

	private void izracunajStandardnoOdstupanje() {
		int n = visinske_razlike.size();
		int u = visine.size();
		Matrix vt = vektor_v.transpose();
		Matrix vtp = vt.multiply(matrica_P);
		Matrix vtpv = vtp.multiply(vektor_v);
		s_ocjenjeno = Math.sqrt(vtpv.getMatrix()[0][0] / (n - u + DEFEKT));
	}

	private void izracunajMatricuQl() {
		Matrix AT = matrica_A.transpose();
		Matrix AQx = matrica_A.multiply(matrica_Qx);
		matrica_Ql = AQx.multiply(AT);
	}

	private void izracunajMatricuQv() {
		Matrix P_inverzno = matrica_P.inverse();
		matrica_Qv = matrica_Ql.subtract(P_inverzno);
	}

	private void izracunajMatricur() {
		matrica_r = matrica_P.multiply(matrica_Qv);
	}

	private void izracunajPopravljenaMjerenja() {
		int u = visinske_razlike.size();
		double niz_popravke[][] = (vektor_v.multConst(1 / 1000)).getMatrix();
		double niz_popravljenaMjerenja[][] = new double[u][1];

		for (int i = 0; i < u; i++) {
			niz_popravljenaMjerenja[i][0] = Double.parseDouble(visinske_razlike.get(i).getVisinskaRaz())
					+ niz_popravke[i][0];
		}

		popravljena_mjerenja = new Matrix(niz_popravljenaMjerenja);
	}

	private void izracunajOcjenjeneVisine() {
		int n = visine.size();
		double niz_popravke[][] = (vektor_x.multConst(1 / 1000)).getMatrix();
		double niz_popravljeno[][] = new double[n][1];

		for (int i = 0; i < n; i++) {
			niz_popravljeno[i][0] = Double.parseDouble(visine.get(i).getVisina()) + niz_popravke[i][0];
		}

		ocjenjene_visine = new Matrix(niz_popravljeno);
	}

	private void izracunajStandardeVisina() {
		int n = visine.size();
		double niz_standardi[][] = new double[n][1];
		double niz_Qx[][] = matrica_Qx.getDiagonal().getMatrix();

		for (int i = 0; i < n; i++) {
			niz_standardi[i][0] = s0 * Math.sqrt(niz_Qx[i][0]);
		}

		standardi_visina = new Matrix(niz_standardi);
	}

	private double nadjiVisinu(String oznaka) {
		String v = "";
		for (int i = 0; i < visine.size(); i++) {
			if (visine.get(i).getOznaka().equals(oznaka)) {
				v = visine.get(i).getVisina();
				break;
			}
		}

		double visina = Double.parseDouble(v);

		return visina;
	}
}
