package application.Tabela;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Desktop;

public class Proba {
	// private static final int ArrayList = 0;

	public static void main(String[] args) throws IOException {
		Integer[] od = { 1, 2, 3, 4, 5 };
		Integer[] doo = { 2, 3, 4, 5, 6 };
		double[] v = { 0.001, 0.001, 0.001, 0.001, 0.001 };
		double[] Qvii = { 0.001, 0.001, 0.001, 0.001, 0.002 };
		double[] loc = { 0.001, 0.001, 0.001, 0.001, 0.003 };
		double[] Qlii = { 0.001, 0.001, 0.001, 0.001, 0.004 };
		double[] rii = { 0.001, 0.001, 0.001, 0.001, 0.005 };
		double[] uv = { 0.001, 0.001, 0.001, 0.001, 0.006 };

		File proba = new File("/Users/kantarion/Desktop/proba.txt");

		ArrayList<String> nemore = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(proba);
			BufferedReader br = new BufferedReader(fr);
			Object[] tableLines = br.lines().toArray();
			for (int i = 0; i < tableLines.length; i++) {
				String line = tableLines[i].toString().trim();
				String[] dataRow = line.split(",");
				// u string prebaci bez tipa podataka
				// System.out.println(Arrays.toString(dataRow));
				nemore.add(Arrays.toString(dataRow));

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<List<String>> da = new ArrayList<>(); // Lista za spremanje redova elemenata

		List<String> ne = new ArrayList<>(); // Privremena lista za svaki red elemenata

		int vel = od.length;

		for (int i = 0; i < vel; i++) {
			String Od = od[i].toString();
			String Do = doo[i].toString();
			String V = String.valueOf(v);
			String Loc = String.valueOf(loc);
			String qvii = String.valueOf(Qvii);
			String qlii = String.valueOf(Qlii);
			String Rii = String.valueOf(rii);
			String UV = String.valueOf(uv);
			ne.add(Od);
			ne.add(Do);
			ne.add(V);
			ne.add(Loc);
			ne.add(qvii);
			ne.add(qlii);
			ne.add(Rii);
			ne.add(UV);

			if (i % 1 == 0) { // Ako je svaki treći element, dodaj red u listu i stvori novi red
				da.add(ne);
				ne = new ArrayList<>();
			}
		}
		System.out.println(da);

		// Dodavanje posljednjeg reda ako je potrebno
		if (!ne.isEmpty()) {
			da.add(ne);
		}

	

		List<String> header = Arrays.asList("OD", "DO", "V", "Qvii", "loc", "Qlii", "rii", "u-v");
		Board board = new Board(100);
		Table table = new Table(board, 20, header, da);
		List<Integer> colWidthsList = Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10);
		table.setColWidthsList(colWidthsList);
		Block tableBlock = table.tableToBlocks();
		board.setInitialBlock(tableBlock);
		board.build();
		String preview1 = board.getPreview();

		System.out.println(preview1);

		File izvjestaj = new File("/Users/kantarion/Desktop/proba.txt");
		FileWriter fw = new FileWriter(izvjestaj);
		fw.write(preview1);
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

}
